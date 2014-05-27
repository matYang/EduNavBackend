package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.exception.booking.BookingNotFoundException;
import BaseModule.exception.course.CourseNotFoundException;
import BaseModule.factory.QueryFactory;
import BaseModule.model.Booking;
import BaseModule.model.Course;
import BaseModule.model.representation.BookingSearchRepresentation;


public class BookingDao {

	public static ArrayList<Booking> searchBooking(BookingSearchRepresentation sr){
		ArrayList<Booking> blist = new ArrayList<Booking>();
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = QueryFactory.getSearchQuery(sr);		
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
			stmt.setInt(stmtInt++, sr.getStartPrice());
			stmt.setInt(stmtInt++, sr.getFinishPrice());
			stmt.setInt(stmtInt++, AccountStatus.activated.code);			

			if(sr.getReference() !=null && sr.getReference().length() > 0){
				stmt.setString(stmtInt++, sr.getReference());
			}
			if(sr.getCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getCreationTime()));
			}
			if(sr.getStartTime() != null){
				Calendar startTime = (Calendar) sr.getStartTime().clone();
				startTime.set(Calendar.HOUR_OF_DAY,0);
				startTime.set(Calendar.MINUTE, 0);
				startTime.set(Calendar.SECOND, 0);
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(startTime));
			}
			if(sr.getFinishTime() != null){
				Calendar finishTime = (Calendar) sr.getFinishTime().clone();
				finishTime.set(Calendar.HOUR_OF_DAY,23);
				finishTime.set(Calendar.MINUTE, 59);
				finishTime.set(Calendar.SECOND, 59);
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(finishTime));	
			}	
			if(sr.getName() != null && sr.getName().length() > 0){
				stmt.setString(stmtInt,sr.getName());
			}
			if(sr.getPhone() != null && sr.getPhone().length() > 0){
				stmt.setString(stmtInt++, sr.getPhone());
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				blist.add(createBookingByResultSet(rs,conn));
			}
		}catch(SQLException e){
			DebugLog.d(e);
			e.printStackTrace();
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
		return blist;

	}

	public static Booking addBookingToDatabases(Booking booking){
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO BookingDao (name,phone,creationTime,adjustTime,startTime,finishTime,price," +
				"status,u_Id,p_Id,course_Id,reference,transaction_Id,admin_Id,coupon_Id,scheduledTime,email)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, booking.getName());
			stmt.setString(2, booking.getPhone());
			stmt.setString(3, DateUtility.toSQLDateTime(booking.getCreationTime()));
			stmt.setString(4, DateUtility.toSQLDateTime(booking.getAdjustTime()));
			stmt.setString(5, DateUtility.toSQLDateTime(booking.getStartTime()));		
			stmt.setString(6, DateUtility.toSQLDateTime(booking.getFinishTime()));			
			stmt.setInt(7, booking.getPrice());
			stmt.setInt(8, booking.getStatus().code);
			stmt.setInt(9, booking.getUserId());
			stmt.setInt(10, booking.getPartnerId());
			stmt.setInt(11, booking.getCourseId());
			stmt.setString(12, booking.getReference());
			stmt.setInt(13, booking.getTransactionId());
			stmt.setInt(14, booking.getAdminId());
			stmt.setInt(15, booking.getCouponId());
			stmt.setString(16, DateUtility.toSQLDateTime(booking.getScheduledTime()));
			stmt.setString(17, booking.getEmail());
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			booking.setBookingId(rs.getInt(1));
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return booking;
	}

	public static void updateBookingInDatabases(Booking booking,Connection...connections) throws BookingNotFoundException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;
		String query = "UPDATE BookingDao SET name=?,phone=?,adjustTime=?,startTime=?,finishTime=?,price=?," +
				"status=?,u_Id=?,p_Id=?,course_Id=?,reference=?,transaction_Id=?,admin_Id=?,coupon_Id=?,scheduledTime=?,email=? where id=?";
		try{
			stmt = conn.prepareStatement(query);

			stmt.setString(1, booking.getName());
			stmt.setString(2, booking.getPhone());			
			stmt.setString(3, DateUtility.toSQLDateTime(booking.getAdjustTime()));
			stmt.setString(4, DateUtility.toSQLDateTime(booking.getStartTime()));
			stmt.setString(5, DateUtility.toSQLDateTime(booking.getFinishTime()));
			stmt.setInt(6, booking.getPrice());
			stmt.setInt(7, booking.getStatus().code);
			stmt.setInt(8, booking.getUserId());
			stmt.setInt(9, booking.getPartnerId());
			stmt.setInt(10, booking.getCourseId());
			stmt.setString(11, booking.getReference());
			stmt.setInt(12, booking.getTransactionId());
			stmt.setInt(13, booking.getAdminId());
			stmt.setInt(14, booking.getCouponId());
			stmt.setString(15, DateUtility.toSQLDateTime(booking.getScheduledTime()));
			stmt.setString(16, booking.getEmail());
			stmt.setInt(17, booking.getBookingId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new BookingNotFoundException();
			}
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, null,EduDaoBasic.shouldConnectionClose(connections));
		}

	}

	public static ArrayList<Booking> getAllBookings(){
		String query = "SELECT * FROM BookingDao";
		ArrayList<Booking> blist = new ArrayList<Booking>();
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;		
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);

			rs = stmt.executeQuery();
			while(rs.next()){					
				blist.add(createBookingByResultSet(rs,conn));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return blist;
	}

	public static Booking getBookingById(int id) throws BookingNotFoundException{
		String query = "SELECT * FROM BookingDao WHERE id = ?";
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getSQLConnection();
		ResultSet rs = null;
		Booking booking = null;
		try{
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()){
				booking = createBookingByResultSet(rs,conn);
			}else throw new BookingNotFoundException();
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return booking;
	}
	

	protected static Booking createBookingByResultSet(ResultSet rs,Connection...connections) throws SQLException {		
		int courseId = rs.getInt("course_Id");
		Course course = null;
		try {
			course = CourseDao.getCourseById(courseId, connections);
		} catch (CourseNotFoundException e) {			
			e.printStackTrace();
			DebugLog.d(e);
		}
		return new Booking(rs.getInt("id"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")), DateUtility.DateToCalendar(rs.getTimestamp("adjustTime")),
				DateUtility.DateToCalendar(rs.getTimestamp("startTime")),DateUtility.DateToCalendar(rs.getTimestamp("finishTime")), rs.getInt("price"), rs.getInt("u_Id"),
				rs.getInt("p_Id"), courseId, rs.getString("name"), rs.getString("phone"),AccountStatus.fromInt(rs.getInt("status")), rs.getString("reference"),
				rs.getInt("coupon_Id"),rs.getInt("transaction_Id"),rs.getInt("admin_Id"),course,rs.getString("email"),DateUtility.DateToCalendar(rs.getTimestamp("scheduledTime")));
	}
}
