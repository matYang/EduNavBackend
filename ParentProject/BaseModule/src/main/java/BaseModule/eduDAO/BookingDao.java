package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.common.Parser;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.BookingNotFoundException;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Booking;
import BaseModule.model.Course;
import BaseModule.model.representation.BookingSearchRepresentation;


public class BookingDao {

	public static ArrayList<Booking> searchBooking(BookingSearchRepresentation sr,Connection...connections) throws PseudoException, SQLException{
		ArrayList<Booking> blist = new ArrayList<Booking>();
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = sr.getSearchQuery();	
		int stmtInt = 1;
		try{
			stmt = conn.prepareStatement(query);
			if(sr.getBookingId() > 0){
				stmt.setInt(stmtInt,sr.getBookingId());
			}
			if(sr.getCourseId() > 0){
				stmt.setInt(stmtInt++, sr.getCourseId());
			}
			if(sr.getPartnerId() > 0){
				stmt.setInt(stmtInt++,sr.getPartnerId());
			}
			if(sr.getUserId() > 0){
				stmt.setInt(stmtInt++, sr.getUserId());
			}
			if(sr.getStartPrice() >= 0){
				stmt.setInt(stmtInt++, sr.getStartPrice());
			}
			if(sr.getFinishPrice() >= 0){
				stmt.setInt(stmtInt++, sr.getFinishPrice());
			}			
			if(sr.getStatus() != null){
				stmt.setInt(stmtInt++, sr.getStatus().code);
			}
			if(sr.getReference() !=null && sr.getReference().length() > 0){
				stmt.setString(stmtInt++, sr.getReference());
			}
			if(sr.getEmail() != null){
				stmt.setString(stmtInt,sr.getEmail());
			}	
			if(sr.getName() != null && sr.getName().length() > 0){
				stmt.setString(stmtInt,sr.getName());
			}
			if(sr.getPhone() != null && sr.getPhone().length() > 0){
				stmt.setString(stmtInt++, sr.getPhone());
			}		
			if(sr.getPreStatus() != null){
				stmt.setInt(stmtInt++, sr.getPreStatus().code);
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				blist.add(createBookingByResultSet(rs,conn));
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		return blist;

	}

	public static Booking addBookingToDatabases(Booking booking,Connection...connections) throws 
	ValidationException,SQLException, PseudoException,CouponNotFoundException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String couponRecord ="";
		int cashbackAmount = 0;		
		String query = "INSERT INTO BookingDao (name,phone,creationTime,adjustTime,price," +
				"status,u_Id,p_Id,course_Id,reference,transaction_Id,cashbackAmount,note,couponRecord," +
				"scheduledTime,email,actionRecord,preStatus)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";		
		try{	
			stmt = conn.prepareStatement("set autocommit = 0");
			System.out.println("autocommit = 0");
			stmt.execute();
			
			if(booking.getCashbackAmount() > 0){
				couponRecord = CouponDao.getCouponRecord(booking.getUserId(), booking.getCashbackAmount(), conn);
				booking.setCouponRecord(couponRecord);
				cashbackAmount = Parser.getCashBackFromCouponRecord(couponRecord);
			}			
			
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, booking.getName());
			stmt.setString(2, booking.getPhone());
			stmt.setString(3, DateUtility.toSQLDateTime(booking.getCreationTime()));
			stmt.setString(4, DateUtility.toSQLDateTime(booking.getAdjustTime()));					
			stmt.setInt(5, booking.getPrice());
			stmt.setInt(6, booking.getStatus().code);
			stmt.setInt(7, booking.getUserId());
			stmt.setInt(8, booking.getPartnerId());
			stmt.setInt(9, booking.getCourseId());
			stmt.setString(10, booking.getReference());
			stmt.setLong(11, booking.getTransactionId());
			stmt.setInt(12, cashbackAmount);
			stmt.setString(13, booking.getNote());
			stmt.setString(14, booking.getCouponRecord());
			stmt.setString(15, DateUtility.toSQLDateTime(booking.getScheduledTime()));
			stmt.setString(16, booking.getEmail());			
			stmt.setString(17, booking.getActionRecord());
			stmt.setInt(18, booking.getPreStatus().code);
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			booking.setBookingId(rs.getInt(1));			
		
		}finally  {
			stmt = conn.prepareStatement("commit");
			System.out.println("commitr");
			stmt.execute();
			
			stmt = conn.prepareStatement("set autocommit = 1");
			System.out.println("autocommit = 1");
			stmt.execute();
			
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 

		return booking;
	}

	public static void updateBookingInDatabases(Booking booking,Connection...connections) throws BookingNotFoundException, SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int stmtInt = 1;		
		String query0 = "SELECT * FROM BookingDao where id = ? for UPDATE;";
		String query = "UPDATE BookingDao SET name=?,phone=?,adjustTime=?,price=?," +
				"status=?,u_Id=?,p_Id=?,course_Id=?,reference=?,transaction_Id=?,cashbackAmount=?,note=?,couponRecord=?," +
				"scheduledTime=?,email=?,actionRecord=?,preStatus=? where id=?";		
		try{		
			stmt = conn.prepareStatement(query0);
			
			stmt.setInt(1, booking.getBookingId());
			rs = stmt.executeQuery();
			if(!rs.next()){
				throw new BookingNotFoundException();
			}
			
			stmt = conn.prepareStatement(query);
		
			stmt.setString(stmtInt++, booking.getName());
			stmt.setString(stmtInt++, booking.getPhone());			
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(booking.getAdjustTime()));			
			stmt.setInt(stmtInt++, booking.getPrice());
			stmt.setInt(stmtInt++, booking.getStatus().code);
			stmt.setInt(stmtInt++, booking.getUserId());
			stmt.setInt(stmtInt++, booking.getPartnerId());
			stmt.setInt(stmtInt++, booking.getCourseId());
			stmt.setString(stmtInt++, booking.getReference());
			stmt.setLong(stmtInt++, booking.getTransactionId());
			stmt.setInt(stmtInt++, booking.getCashbackAmount());
			stmt.setString(stmtInt++, booking.getNote());
			stmt.setString(stmtInt++, booking.getCouponRecord());
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(booking.getScheduledTime()));
			stmt.setString(stmtInt++, booking.getEmail());			
			stmt.setString(stmtInt++, booking.getActionRecord());
			stmt.setInt(stmtInt++, booking.getPreStatus().code);
			stmt.setInt(stmtInt++, booking.getBookingId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new BookingNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		}

	}


	public static Booking getBookingById(int id,Connection...connections) throws PseudoException, SQLException{
		String query = "SELECT * FROM BookingDao WHERE id = ?";
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;
		Booking booking = null;
		try{
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()){
				booking = createBookingByResultSet(rs,conn);
			}else {
				throw new BookingNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return booking;
	}
	

	protected static Booking createBookingByResultSet(ResultSet rs,Connection...connections) throws SQLException, PseudoException {		
		int courseId = rs.getInt("course_Id");
		Course course = null;
		course = CourseDao.getCourseById(courseId, connections);
		return new Booking(rs.getInt("id"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")), DateUtility.DateToCalendar(rs.getTimestamp("adjustTime")),
				 rs.getInt("price"), rs.getInt("u_Id"),	rs.getInt("p_Id"), courseId, rs.getString("name"), rs.getString("phone"),BookingStatus.fromInt(rs.getInt("status")), rs.getString("reference"),
				rs.getLong("transaction_Id"),rs.getString("email"),DateUtility.DateToCalendar(rs.getTimestamp("scheduledTime")),rs.getString("note"),rs.getInt("cashbackAmount"),rs.getString("couponRecord"),rs.getString("actionRecord"),course,BookingStatus.fromInt(rs.getInt("preStatus")));
	}
}
