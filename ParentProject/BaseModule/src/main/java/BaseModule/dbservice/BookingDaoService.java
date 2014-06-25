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
import BaseModule.service.ValidStateTransferService;

public class BookingDaoService {

	public static Booking getBookingById(final int id) throws PseudoException, SQLException{
		return BookingDao.getBookingById(id);
	}
	
	public static void updateBookingInfo(Booking updatedBooking, Connection...connections)throws PseudoException, SQLException{
		updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
		BookingDao.updateBookingInDatabases(updatedBooking, connections);
	}

	public static void updateBookingStatuses(Booking updatedBooking, BookingStatusObj statusObj, final int adminId, Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;
		boolean ok = false;

		try{
			conn = EduDaoBasic.getConnection(connections);
			conn.setAutoCommit(false);


			//user can only change state to cancelled, cannot do anything else, adminId <= 0 implies this is a user
			if (updatedBooking.getStatus() != BookingStatus.cancelled && adminId <= 0){
				throw new AuthenticationException("当前用户无权进行此操作");
			}
			if (!ValidStateTransferService.validateBookingStatusTransfer(updatedBooking.getPreStatus(), updatedBooking.getStatus())){
				throw new ValidationException("预订状态操作错误，请刷新页面");
			}

			updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
			updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
			
			if (updatedBooking.getStatus() == BookingStatus.registered && updatedBooking.getServiceFeeStatus() == ServiceFeeStatus.naive){
				updatedBooking.setServiceFeeStatus(ServiceFeeStatus.shouldCharge);
				//TODO set time and adjust record
				//updatedBooking.appendActionRecord(updatedBooking.getServiceFeeStatus(), adminId)
			}
			else if (updatedBooking.getStatus() == BookingStatus.paid && updatedBooking.getCommissionStatus() == CommissionStatus.naive){
				updatedBooking.setCommissionStatus(CommissionStatus.shouldCharge);
				//TODO set time and adjust record
			}
			
			if (updatedBooking.getStatus() != BookingStatus.consolidated){
				BookingDao.updateBookingInDatabases(updatedBooking,conn); 
			}
			else{
				consolidateBooking(updatedBooking, conn);
			}


			ok = true;
		} finally{
			if (EduDaoBasic.handleCommitFinally(conn, ok, EduDaoBasic.shouldConnectionClose(connections))){
				if (updatedBooking != null && updatedBooking.getStatus() != updatedBooking.getPreStatus() && updatedBooking.getStatus() == BookingStatus.confirmed){
					SMSService.sendBookingConfirmedSMS(updatedBooking);
				}
				else if (updatedBooking != null && updatedBooking.getStatus() != updatedBooking.getPreStatus() && updatedBooking.getStatus() == BookingStatus.failed){
					SMSService.sendBookingFailedSMS(updatedBooking);
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
						//create a transaction, indicating a ￥5 invitational deposit
						Transaction transaction = new Transaction(inviter.getUserId(), booking.getBookingId(), 5, TransactionType.invitation);
						TransactionDao.addTransactionToDatabases(transaction, transientConnection);
						//incr inviter's account balance by ￥5
						UserDao.updateUserBCC(5, 0, 0, inviter.getUserId(), transientConnection);
						
					}
				}
			}
			
			
			//update user's money and credit balance
			UserDao.updateUserBCC(booking.getCashbackAmount(), booking.getPrice(), 0, booking.getUserId(), transientConnection);
			
			//change booking status to consolidated
			booking.setStatus(BookingStatus.consolidated);
			booking.setAdjustTime(DateUtility.getCurTimeInstance());
			booking.appendActionRecord(BookingStatus.consolidated, -2);
			BookingDao.updateBookingInDatabases(booking, transientConnection);
			
			//commit after each run to decrease side effect of an error
			transientConnection.commit();
			
			//send notification sms to inviter, if inviter exists and should get the ￥5, left to last as do not notify in case any error occurs
			targetSMSUser = inviter;
			ok = true;
		} finally{
			if (EduDaoBasic.handleCommitFinally(transientConnection, ok, EduDaoBasic.shouldConnectionClose(connections))){
				if (targetSMSUser != null){
					SMSService.sendInviterConsolidationSMS(targetSMSUser.getPhone(), targetSMSUser.getPhone());
				}
			}
		}
	}


	public static Booking createBooking(Booking booking, Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;		
		String couponRecord = "";		
		int cashbackAmount = 0;		
		boolean ok = false;
		
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
			
			ok = true;
		}finally{			
			EduDaoBasic.handleCommitFinally(conn, ok, EduDaoBasic.shouldConnectionClose(connections));
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
