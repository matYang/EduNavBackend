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
import BaseModule.exception.course.CourseNotFoundException;
import BaseModule.exception.partner.PartnerNotFoundException;
import BaseModule.model.Course;
import BaseModule.model.Partner;

public class CourseDao {

	public static Course addCourseToDatabases(Course course){
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO CourseDao (p_Id,creationTime,startTime,finishTime,t_Info,t_ImgURL,backgroundURL,price," +
				"seatsTotal,seatsLeft,t_Material,status,instName,category,subCategory,title)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	

			stmt.setInt(1, course.getP_Id());
			stmt.setString(2, DateUtility.toSQLDateTime(course.getCreationTime()));
			stmt.setString(3, DateUtility.toSQLDateTime(course.getStartTime()));
			stmt.setString(4, DateUtility.toSQLDateTime(course.getFinishTime()));
			stmt.setString(5, course.getT_Info());
			stmt.setString(6, course.getT_ImgURL());
			stmt.setString(7, course.getBackgroundURL());
			stmt.setInt(8, course.getPrice());
			stmt.setInt(9, course.getSeatsTotal());
			stmt.setInt(10, course.getSeatsLeft());
			stmt.setString(11, course.getT_Material());
			stmt.setInt(12, course.getStatus().code);
			stmt.setString(13,course.getInstName());
			stmt.setString(14, course.getCategory());
			stmt.setString(15, course.getSubCategory());
			stmt.setString(16, course.getTitle());

			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			course.setCourseId(rs.getInt(1));
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return course;
	}

	public static void updateCourseInDatabases(Course course) throws CourseNotFoundException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;
		String query = "UPDATE CourseDao SET p_Id=?,startTime=?,finishTime=?,t_Info=?,t_ImgURL=?,backgroundURL=?,price=?," +
				"seatsTotal=?,seatsLeft=?,t_Material=?,status=?,instName=?,category=?,subCategory=?,title=? where id=?";
		try{
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, course.getP_Id());			
			stmt.setString(2, DateUtility.toSQLDateTime(course.getStartTime()));
			stmt.setString(3, DateUtility.toSQLDateTime(course.getFinishTime()));
			stmt.setString(4, course.getT_Info());
			stmt.setString(5, course.getT_ImgURL());
			stmt.setString(6, course.getBackgroundURL());
			stmt.setInt(7, course.getPrice());
			stmt.setInt(8, course.getSeatsTotal());
			stmt.setInt(9, course.getSeatsLeft());
			stmt.setString(10, course.getT_Material());
			stmt.setInt(11, course.getStatus().code);
			stmt.setString(12,course.getInstName());
			stmt.setString(13, course.getCategory());
			stmt.setString(14, course.getSubCategory());
			stmt.setString(15, course.getTitle());
			stmt.setInt(16, course.getCourseId());

			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new CourseNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, null,true);
		}
	}
	
	public static Course getCourseById(int courseId) throws CourseNotFoundException{
		String query = "SELECT * FROM CourseDao where id = ?";
		Course course = null;
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getSQLConnection();
		ResultSet rs = null;
		
		try{
			stmt = conn.prepareStatement(query);
			
			stmt.setInt(1, courseId);
			rs = stmt.executeQuery();
			if(rs.next()){
				course = createCourseByResultSet(rs,conn);
			}else throw new CourseNotFoundException();
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return course;
	}

	public static ArrayList<Course> getCoursesFromPartner(int p_Id){
		String query = "SELECT * FROM CourseDao where p_Id = ?";
		ArrayList<Course> clist = new ArrayList<Course>();
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);
			
			stmt.setInt(1, p_Id);
			rs = stmt.executeQuery();
			while(rs.next()){
				clist.add(createCourseByResultSet(rs,conn));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		
		return clist;
	}
	
	private static Course createCourseByResultSet(ResultSet rs,Connection...connections) throws SQLException {
		int p_Id = rs.getInt("p_Id");
		Partner partner = null;
		try {
			partner = PartnerDao.getPartnerById(p_Id, connections);
		} catch (PartnerNotFoundException e) {			
			e.printStackTrace();
			DebugLog.d(e);			
		}
		return new Course(rs.getInt("id"), p_Id, DateUtility.DateToCalendar(rs.getDate("creationTime")),
				DateUtility.DateToCalendar(rs.getDate("startTime")), DateUtility.DateToCalendar(rs.getDate("finishTime")), 
				rs.getString("t_Info"),rs.getString("t_ImgURL"), rs.getString("t_Material"), rs.getString("backgroundURL"),
				rs.getString("instName"), rs.getInt("price"), rs.getInt("seatsTotal"), rs.getInt("seatsLeft"),Status.fromInt(rs.getInt("status")), 
				rs.getString("category"), rs.getString("subCategory"), partner,rs.getString("title"));
	}
}