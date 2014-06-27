package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.CourseStatus;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.model.Course;
import BaseModule.model.Partner;

public class CourseCleaner extends CourseDao{

	public static void clean(){
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

}


