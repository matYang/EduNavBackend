package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.model.Booking;

public class BookingCleaner extends BookingDao{

	public static void clean(){
		Calendar currentDate = DateUtility.getCurTimeInstance();
		String ct = DateUtility.toSQLDateTime(currentDate);
		String query = "SELECT * from BookingDao where (status = ? and noRefundDate < ?) or (status = ? and cashbackDate < ?) for update";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);

			stmt = conn.prepareStatement(query);
			stmt.setInt(1, BookingStatus.started.code);
			stmt.setString(2,ct);
			stmt.setInt(3,BookingStatus.succeeded.code);
			stmt.setString(4, ct);
			rs = stmt.executeQuery();

			while(rs.next()){
				Booking booking = BookingDao.createBookingByResultSet(rs, conn);
				if(booking.getStatus().code == BookingStatus.started.code){
					booking.setStatus(BookingStatus.succeeded);
				}else{		
					BookingDaoService.consolidateBooking(booking, conn);
					booking.setStatus(BookingStatus.consolidated);
				}
				BookingDao.updateBookingInDatabases(booking, conn);
			}

			conn.commit();
			conn.setAutoCommit(true);
		}catch (Exception e) {
			DebugLog.d(e);
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException sql) {
					DebugLog.d(sql);
				}
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
	}
}
