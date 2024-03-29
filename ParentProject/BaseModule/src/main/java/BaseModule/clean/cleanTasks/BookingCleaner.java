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
		cleanStarted();
		cleanSucceed();
	}
	
	private static void cleanStarted(){
		Calendar currentDate = DateUtility.getCurTimeInstance();
		String ct = DateUtility.toSQLDateTime(currentDate);
		String query = "SELECT * from Booking where status = ? and noRefundDate < ? for update";
		Connection conn = null;		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			
			stmt = conn.prepareStatement(query);			
			stmt.setInt(1,BookingStatus.started.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				Booking booking = BookingDao.createBookingByResultSet(rs, conn);				
				booking.setStatus(BookingStatus.succeeded);				
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
	
	private static void cleanSucceed(){
		Calendar currentDate = DateUtility.getCurTimeInstance();
		String ct = DateUtility.toSQLDateTime(currentDate);
		String query = "SELECT * from Booking where status = ? and cashbackDate < ?";
		Connection conn = null;
		Connection transientConnection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getConnection();
			stmt = conn.prepareStatement(query);			
			stmt.setInt(1,BookingStatus.succeeded.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();

			transientConnection = EduDaoBasic.getConnection();
			transientConnection.setAutoCommit(false);
			while(rs.next()){
				try{
					Booking booking = BookingDao.createBookingByResultSet(rs, transientConnection);
					BookingDaoService.consolidateBooking(booking, transientConnection);
					transientConnection.commit();
				}catch (Exception e){
					transientConnection.rollback();
					DebugLog.d(e);
				}				
			}
			transientConnection.setAutoCommit(true);			
		}catch (Exception e) {
			DebugLog.d(e);
			if (transientConnection != null){
				try {
					transientConnection.rollback();
				} catch (SQLException sql) {
					DebugLog.d(sql);
				}
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			EduDaoBasic.closeResources(transientConnection, null, null, true);
		}
	}

}
