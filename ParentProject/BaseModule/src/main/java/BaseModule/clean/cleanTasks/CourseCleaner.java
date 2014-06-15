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
		String query = "SELECT * FROM CourseDao where status = ? and startDate < ? for update";
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

	    String query = "SELECT * FROM CourseDao where status = ? and startDate < ? ";
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
					//get unconsolidated courses
					Course course = CourseDao.createCourseByResultSet(rs,partner ,transientConnection);
		
					//from unconsolidated courses, get finished but unconsolidated bookings
					BookingSearchRepresentation b_sr = new BookingSearchRepresentation();
					b_sr.setCourseId(course.getCourseId());
					b_sr.setStatus(BookingStatus.finished);
					ArrayList<Booking> unconsolidatedBookings = BookingDao.searchBooking(b_sr, transientConnection);
					
					//clear the buffer before entering a more complicated locking phase
					transientConnection.commit();
					
					//for each unconsolidated booking
					boolean allOk = true;
					for (Booking booking : unconsolidatedBookings){
						try{
							//lock user; now user, booking, coupon, credit, transaction are all safe to proceed
							User user = UserDao.selectUserForUpdate(booking.getUserId(), transientConnection);
							User inviter = null;
							
							//create credit
							Credit credit = new Credit(booking.getBookingId(), booking.getPrice(), booking.getUserId());
							CreditDao.addCreditToDatabases(credit, transientConnection);
							
							//create transaction if cash back
							if (booking.getCashbackAmount() > 0){
								Transaction transaction = new Transaction(booking.getUserId(), booking.getBookingId(), booking.getCashbackAmount(), TransactionType.cashback);
								TransactionDao.addTransactionToDatabases(transaction, transientConnection);
							}
							
							//check if the current user is invited by another user
							String appliedInvitationalCode = user.getAppliedInvitationalCode();
							if (appliedInvitationalCode != null && appliedInvitationalCode.length() != 0){
								//check if this is the current user's first consolidated booking 
								BookingSearchRepresentation invitee_sr = new BookingSearchRepresentation();
								invitee_sr.setUserId(booking.getUserId());
								invitee_sr.setStatus(BookingStatus.consolidated);
								ArrayList<Booking> consolidatedBookings = BookingDao.searchBooking(invitee_sr, transientConnection);
								//if this is the current user's first consolidated booking
								if (consolidatedBookings.size() == 0){
									UserSearchRepresentation u_sr = new UserSearchRepresentation();
									u_sr.setAppliedInvitationalCode(appliedInvitationalCode);
									ArrayList<User> inviters = UserDao.searchUser(u_sr, transientConnection);
									//find inviter (ignore error, if error too bad..)
									if (inviters.size() == 1){
										inviter = inviters.get(0);
										//lock inviter
										UserDao.selectUserForUpdate(inviter.getUserId(), transientConnection);
										//create a transaction, indicating a ￥5 invitational deposit
										Transaction transaction = new Transaction(inviter.getUserId(), booking.getBookingId(), 5, TransactionType.invitation);
										TransactionDao.addTransactionToDatabases(transaction, transientConnection);
										//incr inviter's account balance by ￥5
										UserDao.updateUserBCC(5, 0, 0, inviter.getUserId(), transientConnection);
										
									}
								}
							}
							
							
							//update user's money and credit balance
							UserDao.updateUserBCC(booking.getCashbackAmount(), booking.getPrice(), 0, booking.getUserId(), transientConnection);
							
							//change booking status to consolidated
							booking.setStatus(BookingStatus.consolidated);
							booking.setAdjustTime(DateUtility.getCurTimeInstance());
							booking.appendActionRecord(BookingStatus.consolidated, -2);
							BookingDao.updateBookingInDatabases(booking, transientConnection);
							
							//commit after each run to decrease side effect of an error
							transientConnection.commit();
							
							//send notification sms to inviter, if inviter exists and should get the ￥5, left to last as do not notify in case any error occurs
							if (inviter != null){
								SMSService.sendInviterConsolidationSMS(inviter.getPhone(), user.getPhone());
							}
							
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


