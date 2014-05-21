package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.exception.course.CourseNotFoundException;
import BaseModule.exception.partner.PartnerNotFoundException;
import BaseModule.factory.QueryFactory;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.representation.CourseSearchRepresentation;

public class CourseDao {


	public static ArrayList<Course> searchCourse(CourseSearchRepresentation sr){
		ArrayList<Course> clist = new ArrayList<Course>();
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		HashMap<Integer,Partner> pmap = new HashMap<Integer,Partner>();
		Partner partner = null;		
		String query = QueryFactory.getSearchQuery(sr);
		int stmtInt = 1;
		boolean joinQ = false;		

		if(((sr.getPartnerId() > 0) ||(sr.getInstitutionName() != null && sr.getInstitutionName().length() > 0) ||
				(sr.getPartnerReference() != null && sr.getPartnerReference().length() > 0))
				&& !joinQ){
			joinQ = true;		

		}		

		try{
			stmt = conn.prepareStatement(query);	

			if(joinQ){	
				if(sr.getInstitutionName() != null && sr.getInstitutionName().length() > 0){
					stmt.setString(stmtInt++, sr.getInstitutionName());
				}
				if(sr.getPartnerReference() != null && sr.getPartnerReference().length() > 0){
					stmt.setString(stmtInt++, sr.getPartnerReference());
				}				
			}
			if(sr.getCreationTime() != null){				
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getCreationTime()));
			}
			if(sr.getStartTime() != null){
				Calendar startTime = (Calendar) sr.getStartTime().clone();
				startTime.set(Calendar.HOUR_OF_DAY,0);
				startTime.set(Calendar.MINUTE, 0);
				startTime.set(Calendar.SECOND, 0);
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(startTime));
			}
			if(sr.getFinishTime() != null){
				Calendar finishTime = (Calendar) sr.getFinishTime().clone();
				finishTime.set(Calendar.HOUR_OF_DAY,23);
				finishTime.set(Calendar.MINUTE, 59);
				finishTime.set(Calendar.SECOND, 59);
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(finishTime));	
			}

			stmt.setInt(stmtInt++, sr.getStartPrice());
			stmt.setInt(stmtInt++, sr.getFinishPrice());
			stmt.setInt(stmtInt++, AccountStatus.activated.code);

			if(sr.getCategory()!=null&&sr.getCategory().length()>0){
				stmt.setString(stmtInt++, sr.getCategory());
			}
			if(sr.getSubCategory()!=null&&sr.getSubCategory().length()>0){
				stmt.setString(stmtInt++, sr.getSubCategory());		
			}
			if(sr.getCity()!=null&&sr.getCity().length()>0){
				stmt.setString(stmtInt++, sr.getCity());
			}
			if(sr.getDistrict()!=null&&sr.getDistrict().length()>0){
				stmt.setString(stmtInt++, sr.getDistrict());
			}
			if(sr.getCourseReference()!=null&&sr.getCourseReference().length()>0){
				stmt.setString(stmtInt++, sr.getCourseReference());
			}
			if(sr.getCourseId()>0){
				stmt.setInt(stmtInt++, sr.getCourseId());		
			}	

			rs = stmt.executeQuery();
			while(rs.next()){
				int p_Id = rs.getInt("p_Id");
				if(p_Id > 0){
					partner = pmap.get(p_Id);
					if(partner==null){						
						partner = PartnerDao.getPartnerById(p_Id, conn);						
						pmap.put(p_Id, partner);
					}
					clist.add(createCourseByResultSet(rs,partner,conn));
				}else{
					clist.add(createCourseByResultSet(rs));
				}
			}
		}catch(SQLException e){
			DebugLog.d(e);
			e.printStackTrace();
		}catch (PartnerNotFoundException e) {
			DebugLog.d(e);
			e.printStackTrace();
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return clist;
	}

	public static Course addCourseToDatabases(Course course,Connection...connections){
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO CourseDao (p_Id,creationTime,startTime,finishTime,t_Info,t_ImgURL,backgroundURL,price," +
				"seatsTotal,seatsLeft,t_Material,status,category,subCategory,title,location,city,district,reference,courseInfo)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	

			stmt.setInt(1, course.getPartnerId());
			stmt.setString(2, DateUtility.toSQLDateTime(course.getCreationTime()));
			stmt.setString(3, DateUtility.toSQLDateTime(course.getStartTime()));
			stmt.setString(4, DateUtility.toSQLDateTime(course.getFinishTime()));
			stmt.setString(5, course.getTeacherInfo());
			stmt.setString(6, course.getTeacherImgUrl());
			stmt.setString(7, course.getBackgroundUrl());
			stmt.setInt(8, course.getPrice());
			stmt.setInt(9, course.getSeatsTotal());
			stmt.setInt(10, course.getSeatsLeft());
			stmt.setString(11, course.getTeachingMaterial());
			stmt.setInt(12, course.getStatus().code);			
			stmt.setString(13, course.getCategory());
			stmt.setString(14, course.getSubCategory());
			stmt.setString(15, course.getTitle());
			stmt.setString(16, course.getLocation());
			stmt.setString(17, course.getCity());
			stmt.setString(18, course.getDistrict());
			stmt.setString(19, course.getReference());
			stmt.setString(20, course.getCourseInfo());

			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			course.setCourseId(rs.getInt(1));
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return course;
	}

	public static void updateCourseInDatabases(Course course,Connection...connections) throws CourseNotFoundException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;
		String query = "UPDATE CourseDao SET p_Id=?,startTime=?,finishTime=?,t_Info=?,t_ImgURL=?,backgroundURL=?,price=?," +
				"seatsTotal=?,seatsLeft=?,t_Material=?,status=?,category=?,subCategory=?,title=?,location=?," +
				"city=?,district=?,reference=?,courseInfo=? where id=?";
		try{
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, course.getPartnerId());			
			stmt.setString(2, DateUtility.toSQLDateTime(course.getStartTime()));
			stmt.setString(3, DateUtility.toSQLDateTime(course.getFinishTime()));
			stmt.setString(4, course.getTeacherInfo());
			stmt.setString(5, course.getTeacherImgUrl());
			stmt.setString(6, course.getBackgroundUrl());
			stmt.setInt(7, course.getPrice());
			stmt.setInt(8, course.getSeatsTotal());
			stmt.setInt(9, course.getSeatsLeft());
			stmt.setString(10, course.getTeachingMaterial());
			stmt.setInt(11, course.getStatus().code);			
			stmt.setString(12, course.getCategory());
			stmt.setString(13, course.getSubCategory());
			stmt.setString(14, course.getTitle());
			stmt.setString(15, course.getLocation());
			stmt.setString(16, course.getCity());
			stmt.setString(17, course.getDistrict());
			stmt.setString(18, course.getReference());
			stmt.setString(19, course.getCourseInfo());
			stmt.setInt(20, course.getCourseId());

			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new CourseNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, null,EduDaoBasic.shouldConnectionClose(connections));
		}
	}

	public static Course getCourseById(int courseId,Connection...connections) throws CourseNotFoundException{
		String query = "SELECT * FROM CourseDao where id = ?";
		Course course = null;
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;

		try{
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, courseId);
			rs = stmt.executeQuery();
			if(rs.next()){
				course = createCourseByResultSet(rs,null,conn);				
			}else throw new CourseNotFoundException();
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return course;
	}

	public static ArrayList<Course> getCoursesFromPartner(int p_Id){
		String query = "SELECT * FROM CourseDao where p_Id = ? ";
		ArrayList<Course> clist = new ArrayList<Course>();
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		HashMap<Integer,Partner> pmap = new HashMap<Integer,Partner>();
		Partner partner = null;		
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, p_Id);
			rs = stmt.executeQuery();
			while(rs.next()){
				partner = pmap.get(p_Id);
				if(partner==null){
					partner = PartnerDao.getPartnerById(p_Id, conn);
					pmap.put(p_Id, partner);
				}
				clist.add(createCourseByResultSet(rs,partner,conn));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		} catch (PartnerNotFoundException e) {			
			e.printStackTrace();
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return clist;
	}

	protected static Course createCourseByResultSet(ResultSet rs,Partner partner,Connection...connections) throws SQLException {
		int p_Id =  rs.getInt("p_Id");
		if(partner == null){
			try {
				partner = PartnerDao.getPartnerById(p_Id, connections);
			} catch (PartnerNotFoundException e) {				
				e.printStackTrace();
				DebugLog.d(e);
			}
		}
		return new Course(rs.getInt("id"), p_Id, DateUtility.DateToCalendar(rs.getTimestamp("creationTime")),
				DateUtility.DateToCalendar(rs.getTimestamp("startTime")), DateUtility.DateToCalendar(rs.getTimestamp("finishTime")), 
				rs.getString("t_Info"),rs.getString("t_ImgURL"), rs.getString("t_Material"), rs.getString("backgroundURL"),
				rs.getInt("price"), rs.getInt("seatsTotal"), rs.getInt("seatsLeft"),AccountStatus.fromInt(rs.getInt("status")), 
				rs.getString("category"), rs.getString("subCategory"), partner,rs.getString("title"),rs.getString("location"),
				rs.getString("city"),rs.getString("district"),rs.getString("reference"),rs.getString("courseInfo"));
	}

	protected static Course createCourseByResultSet(ResultSet rs) throws SQLException {
		return new Course(rs.getInt("id"), rs.getInt("p_Id"), DateUtility.DateToCalendar(rs.getDate("creationTime")),
				DateUtility.DateToCalendar(rs.getDate("startTime")), DateUtility.DateToCalendar(rs.getDate("finishTime")), 
				rs.getString("t_Info"),rs.getString("t_ImgURL"), rs.getString("t_Material"), rs.getString("backgroundURL"),
				rs.getInt("price"), rs.getInt("seatsTotal"), rs.getInt("seatsLeft"),AccountStatus.fromInt(rs.getInt("status")), 
				rs.getString("category"), rs.getString("subCategory"), null,rs.getString("title"),rs.getString("location"),
				rs.getString("city"),rs.getString("district"),rs.getString("reference"),rs.getString("courseInfo"));
	}	
}