package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.BookingNotFoundException;
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

	public static ArrayList<Booking> getAllBookings() throws PseudoException{
		return BookingDao.getAllBookings();
	}

	public static Booking getBookingById(int id) throws PseudoException{
		return BookingDao.getBookingById(id);
	}


	public static void updateBooking(Booking updatedBooking, BookingStatus previousStatus, int adminId) throws PseudoException, SQLException{
		Connection conn = EduDaoBasic.getSQLConnection();
		try{
			if (updatedBooking.getStatus() == previousStatus){
				updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
				BookingDao.updateBookingInDatabases(updatedBooking);
			}
			else if ((previousStatus == BookingStatus.confirmed || previousStatus == BookingStatus.pending) && updatedBooking.getStatus() == BookingStatus.finished){
				User user = UserDaoService.getUserById(updatedBooking.getUserId());
				if (updatedBooking.getCouponId() > 0){
					Coupon coupon = CouponDaoService.getCouponByCouponId(updatedBooking.getCouponId(),conn);
					if (coupon.getUserId() != updatedBooking.getUserId()){
						throw new ValidationException("Not Your Coupon!");
					}
					Transaction transaction = new Transaction(coupon.getUserId(), updatedBooking.getBookingId(), coupon.getCouponId(), coupon.getAmount(), TransactionType.coupon);
					transaction = TransactionDaoService.createTransaction(transaction,conn);
					coupon.setTransactionId(transaction.getTransactionId());
					CouponDaoService.updateCoupon(coupon,conn);
					user.incBalance(coupon.getAmount());
				}
				Credit credit = new Credit(updatedBooking.getBookingId(), updatedBooking.getPrice(), updatedBooking.getUserId());
				CreditDaoService.createCredit(credit,conn);
				user.incCredit(credit.getAmount());
				UserDaoService.updateUser(user,conn);
				updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
				updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
				BookingDao.updateBookingInDatabases(updatedBooking,conn);
			}
			else if (previousStatus == BookingStatus.awaiting){
				if (updatedBooking.getStatus() == BookingStatus.failed || updatedBooking.getStatus() == BookingStatus.canceled){
					if (updatedBooking.getCouponId() > 0){
						Coupon coupon = CouponDaoService.getCouponByCouponId(updatedBooking.getCouponId(),conn);
						coupon.setStatus(CouponStatus.usable);
						if (DateUtility.getCurTime() > coupon.getExpireTime().getTimeInMillis()){
							coupon.setStatus(CouponStatus.expired);
						}
						CouponDaoService.updateCoupon(coupon,conn);
						User user = UserDaoService.getUserById(coupon.getUserId(),conn);
						user.incCoupon(coupon.getAmount());
						UserDaoService.updateUser(user,conn);
					}
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn); 
					if (updatedBooking.getStatus() == BookingStatus.failed){
						SMSService.sendBookingFailedSMS(updatedBooking);
					}
				}
				else if (updatedBooking.getStatus() == BookingStatus.confirmed){
					updatedBooking.setWasConfirmed(true);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
					SMSService.sendBookingConfirmedSMS(updatedBooking);
				}
			}
			else if (previousStatus == BookingStatus.confirmed){
				if (updatedBooking.getStatus() == BookingStatus.canceled){
					updatedBooking.setWasConfirmed(true);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
			}
			else if (previousStatus == BookingStatus.pending){
				if (updatedBooking.getStatus() == BookingStatus.canceled){
					updatedBooking.setWasConfirmed(true);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
			}
			else if (previousStatus == BookingStatus.canceled){
				if (updatedBooking.getStatus() == BookingStatus.confirmed){
					updatedBooking.setStatus(BookingStatus.canceled);
					updatedBooking.setWasConfirmed(true);
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}
				else if (updatedBooking.getStatus() == BookingStatus.failed){
					return;
				}
			}
			else if (previousStatus == BookingStatus.failed){
				if (updatedBooking.getStatus() == BookingStatus.canceled){
					updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
					updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
					BookingDao.updateBookingInDatabases(updatedBooking,conn);
				}

			}
		} finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}

	}

	public static Booking createBooking(Booking booking) throws PseudoException, SQLException{
		Connection conn = EduDaoBasic.getSQLConnection();
		Coupon coupon = null;
		try{
			//if using coupon, validation coupon
			if (booking.getCouponId() > 0){
				coupon = CouponDaoService.getCouponByCouponId(booking.getCouponId(),conn);
				if (coupon.getUserId() != booking.getUserId()){
					throw new ValidationException("消费券主人并非当前用户！");
				}
				else if (coupon.getStatus() != CouponStatus.usable){
					throw new ValidationException("该消费券当前不可使用！");
				}
			}
			booking = BookingDao.addBookingToDatabases(booking,conn);
			if (booking.getCouponId() > 0){
				//if use coupon, use that coupon
				coupon.setStatus(CouponStatus.used);
				coupon.setBookingId(booking.getBookingId());
				CouponDaoService.updateCoupon(coupon,conn);
				User user = UserDaoService.getUserById(coupon.getUserId(),conn);
				user.decCoupon(coupon.getAmount());
				UserDaoService.updateUser(user,conn);
			}
		}catch (SQLException | CouponNotFoundException 
				| UserNotFoundException | ValidationException e) {			
			throw e;
		}finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}

		return booking;
	}

	public static ArrayList<Booking> searchBooking(BookingSearchRepresentation b_sr) throws PseudoException {
		return BookingDao.searchBooking(b_sr);
	}

	public static ArrayList<Booking> getBookingByReference(String reference) throws PseudoException{
		BookingSearchRepresentation sr = new BookingSearchRepresentation();
		sr.setReference(reference);
		return searchBooking(sr);
	}


	public static boolean isReferenceAvailable(String reference) throws PseudoException{
		return getBookingByReference(reference).size() == 0;
	}
}
