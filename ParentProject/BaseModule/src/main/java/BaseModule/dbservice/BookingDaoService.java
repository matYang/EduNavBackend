package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.common.Parser;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Booking;
import BaseModule.model.Coupon;
import BaseModule.model.Credit;
import BaseModule.model.Transaction;
import BaseModule.model.User;
import BaseModule.model.representation.BookingSearchRepresentation;
import BaseModule.service.SMSService;

public class BookingDaoService {

	public static Booking getBookingById(int id) throws PseudoException, SQLException{
		return BookingDao.getBookingById(id);
	}

	public static void updateBooking(Booking updatedBooking, BookingStatus previousStatus, int adminId) throws PseudoException, SQLException{
		Connection conn = null;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);

			if (updatedBooking.getStatus() == previousStatus){
				updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
				BookingDao.updateBookingInDatabases(updatedBooking);
				return;
			}
			//user can only change state to cancelled, cannot do anything else, adminId <= 0 implies this is a user
			if (updatedBooking.getStatus() != BookingStatus.canceled && adminId <= 0){
				throw new AuthenticationException("当前用户无权进行此操作");
			}
			if (previousStatus == BookingStatus.awaiting){
				if (updatedBooking.getStatus() == BookingStatus.canceled){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn); 
				}
				else if (updatedBooking.getStatus() == BookingStatus.failed){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn); 
					SMSService.sendBookingFailedSMS(updatedBooking);
				}
				else if (updatedBooking.getStatus() == BookingStatus.confirmed){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
					SMSService.sendBookingConfirmedSMS(updatedBooking);
				}

			}
			else if (previousStatus == BookingStatus.confirmed){
				if (updatedBooking.getStatus() == BookingStatus.delivered){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
				else if (updatedBooking.getStatus() == BookingStatus.canceled){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
				else if (updatedBooking.getStatus() == BookingStatus.failed){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
					SMSService.sendBookingFailedSMS(updatedBooking);
				}
			}
			else if (previousStatus == BookingStatus.delivered){
				if (updatedBooking.getStatus() == BookingStatus.canceled){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
				else if (updatedBooking.getStatus() == BookingStatus.enter){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
			}
			else if (previousStatus == BookingStatus.enter){
				if (updatedBooking.getStatus() == BookingStatus.finished){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
				else if (updatedBooking.getStatus() == BookingStatus.quit){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
			}
			else if (previousStatus == BookingStatus.failed){
				if (updatedBooking.getStatus() == BookingStatus.canceled){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
			}
			else if (previousStatus == BookingStatus.canceled){
				if (updatedBooking.getStatus() == BookingStatus.failed){
					//not in error state, and do nothing, stay cancelled
				}
				else if (updatedBooking.getStatus() == BookingStatus.confirmed){
					updatedBooking.setStatus(BookingStatus.canceled);
					updatedBooking.setPreStatus(BookingStatus.confirmed);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
				else if (updatedBooking.getStatus() == BookingStatus.delivered){
					updatedBooking.setStatus(BookingStatus.canceled);
					updatedBooking.setPreStatus(BookingStatus.delivered);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
				else if (updatedBooking.getStatus() == BookingStatus.enter){
					updatedBooking.setPreStatus(previousStatus);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
			}
			else{
				throw new ValidationException("预订状态操作错误，请刷新页面");
			}
		} finally{
			if (conn != null){
				conn.commit();
				conn.setAutoCommit(true);
				EduDaoBasic.closeResources(conn, null, null, true);
			}

		}
	}


	public static Booking createBooking(Booking booking,Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;		
		String couponRecord = "";
		int cashbackAmount = 0;
		try{
			conn = EduDaoBasic.getConnection(connections);
			conn.setAutoCommit(false);
			if(booking.getCashbackAmount() > 0){
				couponRecord = CouponDaoService.getCouponRecord(booking.getUserId(), booking.getCashbackAmount(), conn);
				booking.setCouponRecord(couponRecord);
				cashbackAmount = Parser.getCashBackFromCouponRecord(couponRecord);
				booking.setCashbackAmount(cashbackAmount);
				booking = BookingDao.addBookingToDatabases(booking, conn);
			}	
		}finally{			
			if (conn != null){
				if(!conn.getAutoCommit()){
					conn.commit();
					conn.setAutoCommit(true);
				}				
				EduDaoBasic.closeResources(conn, null, null, EduDaoBasic.shouldConnectionClose(connections));
			}
		}
		return booking;
	}

	public static ArrayList<Booking> searchBooking(BookingSearchRepresentation b_sr) throws PseudoException, SQLException {
		return BookingDao.searchBooking(b_sr);
	}

	public static ArrayList<Booking> getBookingByReference(String reference) throws PseudoException, SQLException{
		BookingSearchRepresentation sr = new BookingSearchRepresentation();
		sr.setReference(reference);
		return searchBooking(sr);
	}


	public static boolean isReferenceAvailable(String reference) throws PseudoException, SQLException{
		return getBookingByReference(reference).size() == 0;
	}
}
