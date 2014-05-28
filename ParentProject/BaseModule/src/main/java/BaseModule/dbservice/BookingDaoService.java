package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.eduDAO.BookingDao;
import BaseModule.exception.booking.BookingNotFoundException;
import BaseModule.model.Booking;
import BaseModule.model.representation.BookingSearchRepresentation;

public class BookingDaoService {

	public static ArrayList<Booking> getAllBookings(){
		return BookingDao.getAllBookings();
	}
	
	public static Booking getBookingById(int id) throws BookingNotFoundException{
		return BookingDao.getBookingById(id);
	}
	
	
	public static void updateBooking(Booking booking) throws BookingNotFoundException{
		BookingDao.updateBookingInDatabases(booking);
	}
	
	public static Booking createBooking(Booking booking){
		return BookingDao.addBookingToDatabases(booking);
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
