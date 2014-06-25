package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CourseStatus;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.dbservice.UserDaoService;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.TransactionDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.model.Booking;
import BaseModule.model.Course;
import BaseModule.model.Credit;
import BaseModule.model.Partner;
import BaseModule.model.Transaction;
import BaseModule.model.User;
import BaseModule.model.representation.BookingSearchRepresentation;
import BaseModule.model.representation.UserSearchRepresentation;
import BaseModule.service.SMSService;

public class CourseCleaner extends CourseDao{

	public static void cleanCourse(){
		Calendar currentDate = DateUtility.getCurTimeInstance();
		String ct = DateUtility.toSQLDateTime(currentDate);
		String query = "SELECT * FROM CourseDao where status = ? and cutoffDate < ? for update";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;	
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, CourseStatus.openEnroll.code);
			stmt.setString(2, ct);			
			rs = stmt.executeQuery();
			
			//set default course so that CourseDao does not have to pull out partner every single time
			Partner partner = new Partner();
			while(rs.next()){
				//courses are locked, so this can be considered atomic, thus cannot put in a try and commit inside while loop, though cost may be a little higher to lock them all and release at once
				Course course = CourseDao.createCourseByResultSet(rs,partner,conn);
				course.setStatus(CourseStatus.deactivated);
				CourseDao.updateCourseInDatabases(course,conn);
			}
			
			conn.commit();
			conn.setAutoCommit(true);
		}catch (Exception e) {
			DebugLog.d(e);
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					DebugLog.d(e1);
				}
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
	}
	
	
	public static void cleanCourseRelatedBooking(){
		long mili = DateUtility.getCurTime() - Booking.cashbackDelay;
		Calendar targetDate = DateUtility.getTimeFromLong(mili);
	    String ct = DateUtility.toSQLDateTime(targetDate);

	    String query = "SELECT * FROM CourseDao where status = ? and cutoffDate < ? ";
	    Connection conn = null;
	    Connection transientConnection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, CourseStatus.deactivated.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();
			
			//set default course so that CourseDao does not have to pull out partner every single time
			Partner partner = new Partner();
			
			transientConnection = EduDaoBasic.getConnection();
			transientConnection.setAutoCommit(false);
			while(rs.next()){
				try{
					//must do it here, as a guard
					transientConnection.setAutoCommit(false);
					
					//get unconsolidated courses
					Course course = CourseDao.createCourseByResultSet(rs,partner ,transientConnection);
		
					//from unconsolidated courses, get finished but unconsolidated bookings
					BookingSearchRepresentation b_sr = new BookingSearchRepresentation();
					b_sr.setCourseId(course.getCourseId());
					b_sr.setStatus(BookingStatus.succeeded);
					ArrayList<Booking> unconsolidatedBookings = BookingDao.searchBooking(b_sr, transientConnection);
					
					//clear the buffer before entering a more complicated locking phase
					transientConnection.commit();
					
					//for each unconsolidated booking
					boolean allOk = true;
					for (Booking booking : unconsolidatedBookings){
						BookingDaoService.consolidateBooking(booking, transientConnection);
					}

					//only commit the course to be consolidated after all bookings are committed to be consolidated
					if (allOk){
						//probably not a good idea to re-check here, as consolidated course is a very important ending state
						course.setStatus(CourseStatus.consolidated);
						CourseDao.updateCourseInDatabases(course,transientConnection);
						transientConnection.commit();
					}
					
				} catch (Exception e){
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
				} catch (SQLException e1) {
					DebugLog.d(e1);
				}
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			EduDaoBasic.closeResources(transientConnection, null, null, true);
		}
	}

}


