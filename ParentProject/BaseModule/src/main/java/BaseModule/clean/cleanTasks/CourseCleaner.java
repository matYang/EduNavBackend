package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.course.CourseNotFoundException;

import BaseModule.model.Course;

public class CourseCleaner extends CourseDao{

	public static void clean(){
		Calendar currentDate = DateUtility.getCurTimeInstance();
		String ct=DateUtility.toSQLDateTime(currentDate);
		String query = "SELECT * FROM CourseDao where status = ? and finishTime < ? ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Course course = null;	
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, AccountStatus.activated.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();
			while(rs.next()){
				course = CourseDao.createCourseByResultSet(rs);
				course.setStatus(AccountStatus.deactivated);
				CourseDao.updateCourseInDatabases(course,conn);
			}
		}catch (SQLException e) {
			e.printStackTrace();
			DebugLog.d(e);
		} catch (CourseNotFoundException e) {			
			e.printStackTrace();
			DebugLog.d(e);
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
	}
}


