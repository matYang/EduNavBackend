package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.eduDAO.BookingDao;
import BaseModule.exception.booking.BookingNotFoundException;
import BaseModule.exception.coupon.CouponNotFoundException;
import BaseModule.exception.user.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;
import BaseModule.model.Coupon;
import BaseModule.model.Credit;
import BaseModule.model.Transaction;
import BaseModule.model.User;
import BaseModule.model.representation.BookingSearchRepresentation;

public class BookingDaoService {

	public static ArrayList<Booking> getAllBookings(){
		return BookingDao.getAllBookings();
	}
	
	public static Booking getBookingById(int id) throws BookingNotFoundException{
		return BookingDao.getBookingById(id);
	}
	
	
	public static void updateBooking(Booking updatedBooking, BookingStatus previousStatus, int adminId) throws BookingNotFoundException, ValidationException, CouponNotFoundException, UserNotFoundException{
		if (updatedBooking.getStatus() == previousStatus){
			updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
			BookingDao.updateBookingInDatabases(updatedBooking);
		}
		else if ((previousStatus == BookingStatus.confirmed || previousStatus == BookingStatus.pending) && updatedBooking.getStatus() == BookingStatus.finished){
			User user = UserDaoService.getUserById(updatedBooking.getUserId());
			if (updatedBooking.getCouponId() > 0){
				Coupon coupon = CouponDaoService.getCouponByCouponId(updatedBooking.getCouponId());
				if (coupon.getUserId() != updatedBooking.getUserId()){
					throw new ValidationException("Not Your Coupon!");
				}
				Transaction transaction = new Transaction(coupon.getUserId(), updatedBooking.getBookingId(), coupon.getCouponId(), coupon.getAmount(), TransactionType.coupon);
				transaction = TransactionDaoService.createTransaction(transaction);
				coupon.setTransactionId(transaction.getTransactionId());
				CouponDaoService.updateCoupon(coupon);
				user.incBalance(coupon.getAmount());
			}
			Credit credit = new Credit(updatedBooking.getBookingId(), updatedBooking.getPrice(), updatedBooking.getUserId());
			CreditDaoService.createCredit(credit);
			user.incCredit(credit.getAmount());
			UserDaoService.updateUser(user);
			updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
			updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
			BookingDao.updateBookingInDatabases(updatedBooking);
		}
		else if (previousStatus == BookingStatus.awaiting){
			if (updatedBooking.getStatus() == BookingStatus.failed || updatedBooking.getStatus() == BookingStatus.canceled){
				if (updatedBooking.getCouponId() > 0){
					Coupon coupon = CouponDaoService.getCouponByCouponId(updatedBooking.getCouponId());
					coupon.setStatus(CouponStatus.usable);
					if (DateUtility.getCurTime() > coupon.getExpireTime().getTimeInMillis()){
						coupon.setStatus(CouponStatus.expired);
					}
					CouponDaoService.updateCoupon(coupon);
					User user = UserDaoService.getUserById(coupon.getUserId());
					user.incCoupon(coupon.getAmount());
					UserDaoService.updateUser(user);
				}
				updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
				updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
				BookingDao.updateBookingInDatabases(updatedBooking);
			}
			else if (updatedBooking.getStatus() == BookingStatus.confirmed){
				updatedBooking.setWasConfirmed(true);
				updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
				updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
				BookingDao.updateBookingInDatabases(updatedBooking);
			}
		}
		else if (previousStatus == BookingStatus.confirmed){
			if (updatedBooking.getStatus() == BookingStatus.canceled){
				updatedBooking.setWasConfirmed(true);
				updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
				updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
				BookingDao.updateBookingInDatabases(updatedBooking);
			}
		}
		else if (previousStatus == BookingStatus.pending){
			if (updatedBooking.getStatus() == BookingStatus.canceled){
				updatedBooking.setWasConfirmed(true);
				updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
				updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
				BookingDao.updateBookingInDatabases(updatedBooking);
			}
		}
		else if (previousStatus == BookingStatus.canceled){
			if (updatedBooking.getStatus() == BookingStatus.confirmed){
				updatedBooking.setStatus(BookingStatus.canceled);
				updatedBooking.setWasConfirmed(true);
				updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
				updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
				BookingDao.updateBookingInDatabases(updatedBooking);
			}
			else if (updatedBooking.getStatus() == BookingStatus.failed){
				return;
			}
		}
		else if (previousStatus == BookingStatus.failed){
			if (updatedBooking.getStatus() == BookingStatus.canceled){
				updatedBooking.setAdjustTime(DateUtility.getCurTimeInstance());
				updatedBooking.appendActionRecord(updatedBooking.getStatus(), adminId);
				BookingDao.updateBookingInDatabases(updatedBooking);
			}
		}
		
	}
	
	public static Booking createBooking(Booking booking) throws ValidationException, CouponNotFoundException, UserNotFoundException{
		Coupon coupon = null;
		//if using coupon, validation coupon
		if (booking.getCouponId() > 0){
			coupon = CouponDaoService.getCouponByCouponId(booking.getCouponId());
			if (coupon.getUserId() != booking.getUserId()){
				throw new ValidationException("消费券主人并非当前用户！");
			}
			else if (coupon.getStatus() != CouponStatus.usable){
				throw new ValidationException("该消费券当前不可使用！");
			}
		}
		booking = BookingDao.addBookingToDatabases(booking);
		if (booking.getCouponId() > 0){
			//if use coupon, use that coupon
			coupon.setStatus(CouponStatus.used);
			coupon.setBookingId(booking.getBookingId());
			CouponDaoService.updateCoupon(coupon);
			User user = UserDaoService.getUserById(coupon.getUserId());
			user.decCoupon(coupon.getAmount());
			UserDaoService.updateUser(user);
		}
		
		return booking;
	}

	public static ArrayList<Booking> searchBooking(BookingSearchRepresentation b_sr) {
		return BookingDao.searchBooking(b_sr);
	}
	
	public static ArrayList<Booking> getBookingByReference(String reference){
		BookingSearchRepresentation sr = new BookingSearchRepresentation();
		sr.setReference(reference);
		return searchBooking(sr);
	}

	
	public static boolean isReferenceAvailable(String reference){
		return getBookingByReference(reference).size() == 0;
	}
}
