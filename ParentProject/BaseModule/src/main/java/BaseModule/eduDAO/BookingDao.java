package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.Status;
import BaseModule.exception.booking.BookingNotFoundException;
import BaseModule.model.Booking;

public class BookingDao {

	public static Booking addBookingToDatabases(Booking booking){
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO BookingDao (name,phone,creationTime,timeStamp,startTime,finishTime,price," +
				"status,u_Id,p_Id,course_Id,reference) values (?,?,?,?,?,?,?,?,?,?,?,?);";
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, booking.getName());
			stmt.setString(2, booking.getPhone());
			stmt.setString(3, DateUtility.toSQLDateTime(booking.getCreationTime()));
			stmt.setString(4, DateUtility.toSQLDateTime(booking.getTimeStamp()));
			stmt.setString(5, DateUtility.toSQLDateTime(booking.getStartTime()));
			stmt.setString(6, DateUtility.toSQLDateTime(booking.getFinishTime()));
			stmt.setInt(7, booking.getPrice());
			stmt.setInt(8, booking.getStatus().code);
			stmt.setInt(9, booking.getUserId());
			stmt.setInt(10, booking.getPartnerId());
			stmt.setInt(11, booking.getCourseId());
			stmt.setString(12, booking.getReference());

			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			booking.setId(rs.getInt(1));
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return booking;
	}

	public static void updateBookingInDatabases(Booking booking) throws BookingNotFoundException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;
		String query = "UPDATE BookingDao SET name=?,phone=?,timeStamp=?,startTime=?,finishTime=?,price=?," +
				"status=?,u_Id=?,p_Id=?,course_Id=?,reference=? where id=?";
		try{
			stmt = conn.prepareStatement(query);

			stmt.setString(1, booking.getName());
			stmt.setString(2, booking.getPhone());			
			stmt.setString(3, DateUtility.toSQLDateTime(booking.getTimeStamp()));
			stmt.setString(4, DateUtility.toSQLDateTime(booking.getStartTime()));
			stmt.setString(5, DateUtility.toSQLDateTime(booking.getFinishTime()));
			stmt.setInt(6, booking.getPrice());
			stmt.setInt(7, booking.getStatus().code);
			stmt.setInt(8, booking.getUserId());
			stmt.setInt(9, booking.getPartnerId());
			stmt.setInt(10, booking.getCourseId());
			stmt.setString(11, booking.getReference());
			stmt.setInt(12, booking.getId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new BookingNotFoundException();
			}
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, null,true);
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
				blist.add(createBookingByResultSet(rs));
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
				booking = createBookingByResultSet(rs);
			}else throw new BookingNotFoundException();
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return booking;
	}

	public static Booking getBookingByReference(String reference) throws BookingNotFoundException{
		String query = "SELECT * FROM BookingDao WHERE reference = ?";
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getSQLConnection();
		ResultSet rs = null;
		Booking booking = null;
		try{
			stmt = conn.prepareStatement(query);

			stmt.setString(1,reference);
			rs = stmt.executeQuery();
			if(rs.next()){
				booking = createBookingByResultSet(rs);
			}else throw new BookingNotFoundException();
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return booking;
	}

	private static Booking createBookingByResultSet(ResultSet rs) throws SQLException {
		return new Booking(rs.getInt("rs"), DateUtility.DateToCalendar(rs.getDate("creationTime")), DateUtility.DateToCalendar(rs.getDate("timeStamp")),
				DateUtility.DateToCalendar(rs.getDate("startTime")),DateUtility.DateToCalendar(rs.getDate("finishTime")), rs.getInt("price"), rs.getInt("u_Id"),
				rs.getInt("p_Id"), rs.getInt("course_Id"), rs.getString("name"), rs.getString("phone"),Status.fromInt(rs.getInt("status")), rs.getString("reference"));
	}









}
