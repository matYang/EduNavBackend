package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.common.Parser;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.TransactionDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.notFound.BookingNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Booking;
import BaseModule.model.Credit;
import BaseModule.model.Transaction;
import BaseModule.model.User;
import BaseModule.model.dataObj.BookingStatusObj;
import BaseModule.model.representation.BookingSearchRepresentation;
import BaseModule.model.representation.UserSearchRepresentation;
import BaseModule.service.SMSService;
import BaseModule.service.StateTransferService;

public class BookingDaoService {
	public static final int consolidationAmount = 5;

	public static Booking getBookingById(final int id) throws PseudoException, SQLException{
		return BookingDao.getBookingById(id);
	}
	
	public static void updateBookingInfo(Booking updatedBooking, Connection...connections)throws PseudoException, SQLException{
		updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
		BookingDao.updateBookingInDatabases(updatedBooking, connections);
	}

	public static void updateBookingStatuses(Booking curBooking, BookingStatusObj statusObj, final int adminId, boolean supervisor, Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;
		boolean ok = false;

		try{
			conn = EduDaoBasic.getConnection(connections);
			conn.setAutoCommit(false);
			
			
			
			if (statusObj.bookingStatus != null && statusObj.bookingStatus != curBooking.getStatus()){
				//user can only change state to cancelled, cannot do anything else, adminId <= 0 implies this is a user
				if (statusObj.bookingStatus != BookingStatus.cancelled && adminId <= 0){
					throw new AuthenticationException("当前用户无权进行此操作");
				}
				if (!supervisor && !StateTransferService.validateBookingStatusTransfer(curBooking.getStatus(), statusObj.bookingStatus)){
					throw new ValidationException("预订状态操作错误，请联系管理员或者尝试刷新页面");
				}
				
				curBooking.setPreStatus(curBooking.getStatus());
				curBooking.setStatus(statusObj.bookingStatus);
				curBooking.setBookingStatusAdjustTime(DateUtility.getCurTimeInstance());
				curBooking.appendActionRecord(statusObj.bookingStatus, adminId);
				
				if (curBooking.getStatus() == BookingStatus.registered && curBooking.getServiceFeeStatus() == ServiceFeeStatus.naive){
					curBooking.setPreServiceFeeStatus(curBooking.getServiceFeeStatus());
					curBooking.setServiceFeeStatus(ServiceFeeStatus.shouldCharge);
					curBooking.setServiceFeeStatusAdjustTime(DateUtility.getCurTimeInstance());
					curBooking.appendServiceFeeActionRecord(ServiceFeeStatus.shouldCharge, adminId);
				}
				else if (curBooking.getStatus() == BookingStatus.paid && curBooking.getCommissionStatus() == CommissionStatus.naive){
					curBooking.setPreCommissionStatus(curBooking.getCommissionStatus());
					curBooking.setCommissionStatus(CommissionStatus.shouldCharge);
					curBooking.setCommissionStatusAdjustTime(DateUtility.getCurTimeInstance());
					curBooking.appendCommissionActionRecord(CommissionStatus.shouldCharge, adminId);
				}
			}
			if (statusObj.serviceFeeStatus != null && statusObj.serviceFeeStatus != curBooking.getServiceFeeStatus()){
				if (!supervisor && !StateTransferService.validateServiceFeeStatusTransfer(curBooking.getServiceFeeStatus(), statusObj.serviceFeeStatus)){
					throw new ValidationException("预订服务费状态操作错误，请联系管理员或者尝试刷新页面");
				}
				curBooking.setPreServiceFeeStatus(curBooking.getServiceFeeStatus());
				curBooking.setServiceFeeStatus(statusObj.serviceFeeStatus);
				curBooking.setServiceFeeStatusAdjustTime(DateUtility.getCurTimeInstance());
				curBooking.appendServiceFeeActionRecord(statusObj.serviceFeeStatus, adminId);
			}
			if (statusObj.commissionStatus != null && statusObj.commissionStatus != curBooking.getCommissionStatus()){
				if (!supervisor && !StateTransferService.validateCommissionStatusTransfer(curBooking.getCommissionStatus(), statusObj.commissionStatus)){
					throw new ValidationException("预订佣金状态操作错误，请联系管理员或者尝试刷新页面");
				}
				curBooking.setPreCommissionStatus(curBooking.getCommissionStatus());
				curBooking.setCommissionStatus(statusObj.commissionStatus);
				curBooking.setCommissionStatusAdjustTime(DateUtility.getCurTimeInstance());
				curBooking.appendCommissionActionRecord(statusObj.commissionStatus, adminId);
			}
			
			
			//new to be consolidated
			if (curBooking.getStatus() == BookingStatus.consolidated && curBooking.getPreStatus() != BookingStatus.consolidated){
				consolidateBooking(curBooking, conn);
			}
			else{
				BookingDao.updateBookingInDatabases(curBooking,conn); 
			}


			ok = true;
		} finally{
			if (EduDaoBasic.handleCommitFinally(conn, ok, EduDaoBasic.shouldConnectionClose(connections))){
				if (curBooking != null && curBooking.getStatus() != curBooking.getPreStatus() && curBooking.getStatus() == BookingStatus.confirmed){
					SMSService.sendBookingConfirmedSMS(curBooking);
				}
				else if (curBooking != null && curBooking.getStatus() != curBooking.getPreStatus() && curBooking.getStatus() == BookingStatus.failed){
					SMSService.sendBookingFailedSMS(curBooking);
				}
			}
		}
	}
	
	public static void consolidateBooking(Booking booking, Connection...connections) throws PseudoException, SQLException{
		Connection transientConnection = null;
		boolean ok = false;
		User targetSMSUser = null;
		try{
			transientConnection = EduDaoBasic.getConnection(connections);
			transientConnection.setAutoCommit(false);
			
			//lock user; now user, booking, coupon, credit, transaction are all safe to proceed
			User user = UserDao.getAndLock(booking.getUserId(), transientConnection);
			User inviter = null;
			
			//create credit
			Credit credit = new Credit(booking.getBookingId(), booking.getPrice(), booking.getUserId());
			CreditDao.addCreditToDatabases(credit, transientConnection);
			
			//create transaction if cash back
			if (booking.getCashbackAmount() > 0){
				Transaction transaction = new Transaction(booking.getUserId(), booking.getBookingId(), booking.getCashbackAmount(), TransactionType.cashback);
				TransactionDao.addTransactionToDatabases(transaction, transientConnection);
			}
			
			//check if the current user is invited by another user
			String appliedInvitationalCode = user.getAppliedInvitationalCode();
			if (appliedInvitationalCode != null && appliedInvitationalCode.length() != 0){
				//check if this is the current user's first consolidated booking 
				BookingSearchRepresentation invitee_sr = new BookingSearchRepresentation();
				invitee_sr.setUserId(booking.getUserId());
				invitee_sr.setStatus(BookingStatus.consolidated);
				ArrayList<Booking> consolidatedBookings = BookingDao.searchBooking(invitee_sr, transientConnection);
				//if this is the current user's first consolidated booking
				if (consolidatedBookings.size() == 0){
					UserSearchRepresentation u_sr = new UserSearchRepresentation();
					u_sr.setAppliedInvitationalCode(appliedInvitationalCode);
					ArrayList<User> inviters = UserDao.searchUser(u_sr, transientConnection);
					//find inviter (ignore error, if error too bad..)
					if (inviters.size() == 1){
						inviter = inviters.get(0);
						//lock inviter
						UserDao.getAndLock(inviter.getUserId(), transientConnection);
						//create a transaction, indicating a ￥consolidationAmount invitational deposit
						Transaction transaction = new Transaction(inviter.getUserId(), booking.getBookingId(), consolidationAmount, TransactionType.invitation);
						TransactionDao.addTransactionToDatabases(transaction, transientConnection);
						//incr inviter's account balance by ￥consolidationAmount
						UserDao.updateUserBCC(consolidationAmount, 0, 0, inviter.getUserId(), transientConnection);
						
					}
				}
			}
			
			//update user's money and credit balance
			UserDao.updateUserBCC(booking.getCashbackAmount(), booking.getPrice(), 0, booking.getUserId(), transientConnection);
			
			//change booking status to consolidated, if not been done already
			if (booking.getStatus() != BookingStatus.consolidated){
				booking.setPreStatus(booking.getStatus());
				booking.setStatus(BookingStatus.consolidated);
				booking.setAdjustTime(DateUtility.getCurTimeInstance());
				booking.appendActionRecord(BookingStatus.consolidated, -2);
			}
			BookingDao.updateBookingInDatabases(booking, transientConnection);
			
			//commit after each run to decrease side effect of an error
			transientConnection.commit();
			
			//send notification sms to inviter, if inviter exists and should get the ￥consolidationAmount, left to last as do not notify in case any error occurs
			targetSMSUser = inviter;
			ok = true;
		} finally{
			if (EduDaoBasic.handleCommitFinally(transientConnection, ok, EduDaoBasic.shouldConnectionClose(connections))){
				if (targetSMSUser != null){
					SMSService.sendInviterConsolidationSMS(targetSMSUser.getPhone(), consolidationAmount);
				}
			}
		}
	}


	public static Booking createBooking(Booking booking, Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;		
		String couponRecord = "";		
		int cashbackAmount = 0;		
		boolean ok = false;
		Booking targetSMSBooking = booking;
		
		try{
			conn = EduDaoBasic.getConnection(connections);				
			conn.setAutoCommit(false);		
			
			UserDaoService.getAndLock(booking.getUserId(), conn);
			
			if(booking.getCashbackAmount() > 0){
				couponRecord = CouponDaoService.getCouponRecord(booking.getUserId(), booking.getCashbackAmount(),conn);
				booking.setCouponRecord(couponRecord);
				cashbackAmount = Parser.getCashBackFromCouponRecord(couponRecord);
				booking.setCashbackAmount(cashbackAmount);
				booking = BookingDao.addBookingToDatabases(booking,conn);
			}
			if (booking.getCourse() == null){
				booking.setCourse(CourseDao.getCourseById(booking.getCourseId(), conn));
			}
			
			ok = true;
		}finally{			
			if (EduDaoBasic.handleCommitFinally(conn, ok, EduDaoBasic.shouldConnectionClose(connections))){
				if (targetSMSBooking != null){
					SMSService.sendBookingAwaitingSMS(targetSMSBooking);
				}
			}
		}
		return booking;
	}

	public static ArrayList<Booking> searchBooking(BookingSearchRepresentation b_sr) throws PseudoException, SQLException {
		return BookingDao.searchBooking(b_sr);
	}

	public static Booking getBookingByReference(String reference) throws PseudoException, SQLException{
		BookingSearchRepresentation sr = new BookingSearchRepresentation();
		sr.setReference(reference);
		ArrayList<Booking> bookings = BookingDao.searchBooking(sr);
		if (bookings.size() == 0){
			throw new BookingNotFoundException();
		}
		else if (bookings.size() > 1){
			throw new ValidationException("系统错误：编码重复");
		}
		else{
			return bookings.get(0);
		}
	}


	public static boolean isReferenceAvailable(String reference) throws PseudoException, SQLException{
		BookingSearchRepresentation sr = new BookingSearchRepresentation();
		sr.setReference(reference);
		return BookingDao.searchBooking(sr).size() == 0;
	}
}
