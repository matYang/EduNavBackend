package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.eduDAO.BookingDao;
import BaseModule.exception.booking.BookingNotFoundException;
import BaseModule.model.Booking;

public class BookingDaoService {

	public ArrayList<Booking> getAllBookings(){
		return BookingDao.getAllBookings();
	}
	
	public Booking getBookingById(int id) throws BookingNotFoundException{
		return BookingDao.getBookingById(id);
	}
	
	public Booking getBookingByReference(String reference) throws BookingNotFoundException{
		return BookingDao.getBookingByReference(reference);
	}
	
	public static void updateBooking(Booking booking) throws BookingNotFoundException{
		BookingDao.updateBookingInDatabases(booking);
	}
	
	public static Booking createBooking(Booking booking){
		return BookingDao.addBookingToDatabases(booking);
	}
	
}
