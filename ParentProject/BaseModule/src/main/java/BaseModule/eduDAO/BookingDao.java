package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.BookingNotFoundException;
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
			if(sr.getCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getCreationTime()));
			}
			if(sr.getScheduledTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getScheduledTime()));
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
			if(sr.getAdjustTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getAdjustTime()));
			}
			if(sr.getWasConfirmedIndex() != -1){
				stmt.setInt(stmtInt++,sr.getWasConfirmedIndex());
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

	public static Booking addBookingToDatabases(Booking booking,Connection...connections) throws SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO BookingDao (name,phone,creationTime,adjustTime,price," +
				"status,u_Id,p_Id,course_Id,reference,transaction_Id,admin_Id,coupon_Id,scheduledTime,email,wasConfirmed," +
				"actionRecord)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try{
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
			stmt.setInt(11, booking.getTransactionId());
			stmt.setInt(12, booking.getAdminId());
			stmt.setLong(13, booking.getCouponId());
			stmt.setString(14, DateUtility.toSQLDateTime(booking.getScheduledTime()));
			stmt.setString(15, booking.getEmail());
			stmt.setInt(16, booking.isWasConfirmed() ? 1 : 0);
			stmt.setString(17, booking.getActionRecord());
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			booking.setBookingId(rs.getInt(1));
		}finally  {
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
				"status=?,u_Id=?,p_Id=?,course_Id=?,reference=?,transaction_Id=?,admin_Id=?,coupon_Id=?," +
				"scheduledTime=?,email=?,wasConfirmed=?,actionRecord=? where id=?";		
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
			stmt.setInt(stmtInt++, booking.getTransactionId());
			stmt.setInt(stmtInt++, booking.getAdminId());
			stmt.setLong(stmtInt++, booking.getCouponId());
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(booking.getScheduledTime()));
			stmt.setString(stmtInt++, booking.getEmail());
			stmt.setInt(stmtInt++, booking.isWasConfirmed() ? 1 : 0);
			stmt.setString(stmtInt++, booking.getActionRecord());
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
				rs.getInt("coupon_Id"),rs.getInt("transaction_Id"),rs.getInt("admin_Id"),course,rs.getString("email"),DateUtility.DateToCalendar(rs.getTimestamp("scheduledTime")),rs.getBoolean("wasConfirmed"),
				rs.getString("actionRecord"));
	}
}
