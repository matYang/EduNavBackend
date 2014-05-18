package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.booking.BookingNotFoundException;
import BaseModule.model.Booking;

public class BookingCleaner extends BookingDao{
	
	public static void clean(){
		Calendar currentDate = DateUtility.getCurTimeInstance();
	    String ct=DateUtility.toSQLDateTime(currentDate);
	    //System.out.println("currentTime: "+ct);
	    String query = "SELECT * FROM BookingDao where status = ? and finishTime < ? ";
	    Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Booking booking = null;
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, AccountStatus.activated.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();
			while(rs.next()){
				booking = BookingDao.createBookingByResultSet(rs);
				booking.setStatus(AccountStatus.deactivated);				
				BookingDao.updateBookingInDatabases(booking,conn);
			}
		}catch (SQLException e) {
			e.printStackTrace();
			DebugLog.d(e);
		} catch (BookingNotFoundException e) {			
			e.printStackTrace();
			DebugLog.d(e);
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
	}

}
