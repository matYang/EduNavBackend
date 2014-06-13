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
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.model.Booking;
import BaseModule.model.Course;
import BaseModule.model.Credit;
import BaseModule.model.representation.BookingSearchRepresentation;

public class CourseCleaner extends CourseDao{

	public static void cleanCourse(){
		Calendar currentDate = DateUtility.getCurTimeInstance();
		String ct=DateUtility.toSQLDateTime(currentDate);
		String query = "SELECT * FROM CourseDao where status = ? and startTime < ? ";
		Connection conn = null;
		Connection transientConnection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;	
		try{
			conn = EduDaoBasic.getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, AccountStatus.activated.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();
			
			transientConnection = EduDaoBasic.getConnection();
			transientConnection.setAutoCommit(false);
			while(rs.next()){
				try{
					Course course = CourseDao.createCourseByResultSet(rs,null,transientConnection);
					course.setStatus(AccountStatus.deactivated);
					CourseDao.updateCourseInDatabases(course,transientConnection);
					
					transientConnection.commit();
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
					e1.printStackTrace();
				}
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			EduDaoBasic.closeResources(transientConnection, null, null, true);
		}
	}
	
	
	public static void cleanCourseRelatedBooking(){
		long mili = DateUtility.getCurTime() - Booking.cashbackDelay;
		Calendar targetDate = DateUtility.getTimeFromLong(mili);
	    String ct = DateUtility.toSQLDateTime(targetDate);

	    String query = "SELECT * FROM CourseDao where status = ? and startTime < ? ";
	    Connection conn = null;
	    Connection transientConnection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, AccountStatus.deactivated.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();
			
			transientConnection = EduDaoBasic.getConnection();
			transientConnection.setAutoCommit(false);
			while(rs.next()){
				try{
					//get unconsolidated courses
					Course course = CourseDao.createCourseByResultSet(rs,null,transientConnection);
					course.setStatus(AccountStatus.consolidated);
					int courseId = course.getCourseId();
					
					//from unconsolidated courses, get finished but unconsolidated bookings
					BookingSearchRepresentation b_sr = new BookingSearchRepresentation();
					b_sr.setCourseId(courseId);
					b_sr.setStatus(BookingStatus.finished);
					ArrayList<Booking> unconsolidatedBookings = BookingDao.searchBooking(b_sr, transientConnection);
					
					//for each unconsolidated booking
					boolean allOk = true;
					for (Booking booking : unconsolidatedBookings){
						try{
							//create credit
							Credit credit = new Credit(booking.getBookingId(), booking.getPrice(), booking.getUserId());
							CreditDao.addCreditToDatabases(credit, transientConnection);
							
							//update user's money and credit balance
							UserDao.updateUserBCC(booking.getCashbackAmount(), booking.getPrice(), 0, booking.getUserId(), transientConnection);
							
							//change booking status to consolidated
							booking.setStatus(BookingStatus.consolidated);
							booking.setAdjustTime(DateUtility.getCurTimeInstance());
							booking.appendActionRecord(BookingStatus.consolidated, -2);
							BookingDao.updateBookingInDatabases(booking, transientConnection);
							
							//commit after each run to decrease side effect of an error
							transientConnection.commit();
							
							System.out.println("cleaned booking: " + booking.getBookingId());
						} catch (Exception e){
							transientConnection.rollback();
							DebugLog.d(e);
							//if a single booking failed to update, do not update course to be consolidated, cleaner will retry in the next clean run
							allOk = false;
						}
						
					}

					//only commit the course to be consolidated after all bookings are committed to be consolidated
					if (allOk){
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
					e1.printStackTrace();
				}
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			EduDaoBasic.closeResources(transientConnection, null, null, true);
		}
	}

}


