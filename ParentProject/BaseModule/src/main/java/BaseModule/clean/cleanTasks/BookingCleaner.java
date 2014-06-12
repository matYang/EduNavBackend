package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.model.Booking;

public class BookingCleaner extends BookingDao{
	
	public static void clean(){
		Calendar currentDate = DateUtility.getCurTimeInstance();
	    String ct=DateUtility.toSQLDateTime(currentDate);
	    //System.out.println("currentTime: "+ct);
	    String query = "SELECT * FROM BookingDao where status = ? and scheduledTime < ? ";
	    Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Booking booking = null;
		try{
			conn = EduDaoBasic.getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, BookingStatus.finished.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();
			while(rs.next()){
				try{
					booking = BookingDao.createBookingByResultSet(rs);
					BookingDao.updateBookingInDatabases(booking,conn);
					//add cash back to user's balance, generate transaction, create credit and incr user's credit balance
				} catch(Exception e){
					DebugLog.d(e);
				}
			}
		}catch (Exception e) {
			DebugLog.d(e);
		} finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
	}

}
