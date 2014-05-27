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
	
	public static ArrayList<Booking> getBookingByReference(String reference) throws BookingNotFoundException{
		BookingSearchRepresentation sr = new BookingSearchRepresentation();
		sr.setReference(reference);
		return BookingDao.searchBooking(sr);
	}
	
	public static void updateBooking(Booking booking) throws BookingNotFoundException{
		BookingDao.updateBookingInDatabases(booking);
	}
	
	public static Booking createBooking(Booking booking){
		return BookingDao.addBookingToDatabases(booking);
	}

	public static ArrayList<Booking> searchBooking(BookingSearchRepresentation sr) {
		return BookingDao.searchBooking(sr);
	}
	
}
