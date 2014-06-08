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
import BaseModule.common.Parser;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.ClassModel;
import BaseModule.configurations.EnumConfig.PartnerQualification;
import BaseModule.configurations.EnumConfig.TeachingMaterialType;
import BaseModule.exception.notFound.CourseNotFoundException;
import BaseModule.exception.notFound.PartnerNotFoundException;
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
			if(sr.getStartPrice() >= 0){
				stmt.setInt(stmtInt++, sr.getStartPrice());
			}
			if(sr.getFinishPrice() >= 0){
				stmt.setInt(stmtInt++, sr.getFinishPrice());
			}			
			if(sr.getStatus() != null){
				stmt.setInt(stmtInt++, sr.getStatus().code);
			}
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
			if(sr.getClassModel()!=null){
				stmt.setInt(stmtInt++, sr.getClassModel().code);
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
		}catch (PartnerNotFoundException e) {
			DebugLog.d(e);
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return clist;
	}

	public static Course addCourseToDatabases(Course course,Connection...connections) throws SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO CourseDao (p_Id,creationTime,startTime,finishTime,t_Intro,t_ImgUrl,classroomImgUrl,price," +
				"seatsTotal,seatsLeft,status,category,subCategory,location,city,district,reference,courseIntro," +
				"classModel,hasDownloadMaterials,openCourseRequirement,suitableStudent,prerequest,highScoreReward," +
				"quiz,certification,questionBank,extracurricular,courseName,dailyStartTime,dailyFinishTime,studyDaysNote," +
				"courseHourNum,courseHourLength,partnerCourseReference,partnerIntro,classroomIntro,partnerQualification," +
				"t_Methods,t_MaterialType,t_MaterialCost,t_MaterialFree,t_MaterialIntro,t_MethodsIntro,questionBankIntro," +
				"passAgreement,provideAssignments,provideMarking,extracurricularIntro,phone,studyDays,t_MaterialName)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	

			stmt.setInt(1, course.getPartnerId());
			stmt.setString(2, DateUtility.toSQLDateTime(course.getCreationTime()));
			stmt.setString(3, DateUtility.toSQLDateTime(course.getStartTime()));
			stmt.setString(4, DateUtility.toSQLDateTime(course.getFinishTime()));
			stmt.setString(5, course.getTeacherIntro());
			stmt.setString(6, course.getTeacherImgUrl());
			stmt.setString(7, course.getClassroomImgUrl());
			stmt.setInt(8, course.getPrice());
			stmt.setInt(9, course.getSeatsTotal());
			stmt.setInt(10, course.getSeatsLeft());	
			stmt.setInt(11, course.getStatus().code);			
			stmt.setString(12, course.getCategory());
			stmt.setString(13, course.getSubCategory());			
			stmt.setString(14, course.getLocation());
			stmt.setString(15, course.getCity());
			stmt.setString(16, course.getDistrict());
			stmt.setString(17, course.getReference());
			stmt.setString(18, course.getCourseIntro());
			stmt.setInt(19, course.getClassModel().code);			
			stmt.setInt(20, course.isHasDownloadMaterials() ? 1 : 0);			
			stmt.setString(21, course.getOpenCourseRequirement());		
			stmt.setString(22,course.getSuitableStudent());
			stmt.setString(23, course.getPrerequest());			
			stmt.setString(24, course.getHighScoreReward());
			stmt.setString(25, course.getQuiz());
			stmt.setString(26, course.getCertification());
			stmt.setString(27, Parser.listToString(course.getQuestionBank()));
			stmt.setString(28, Parser.listToString(course.getExtracurricular()));
			stmt.setString(29, course.getCourseName());
			stmt.setString(30, course.getDailyStartTime());
			stmt.setString(31, course.getDailyFinishTime());
			stmt.setString(32, course.getStudyDaysNote());
			stmt.setInt(33, course.getCourseHourNum());
			stmt.setInt(34, course.getCourseHourLength());
			stmt.setString(35, course.getPartnerCourseReference());
			stmt.setString(36, course.getPartnerIntro());
			stmt.setString(37, course.getClassroomIntro());
			stmt.setInt(38, course.getPartnerQualification().code);
			stmt.setString(39, Parser.listToString(course.getTeachingMethods()));
			stmt.setInt(40, course.getTeachingMaterialType().code);
			stmt.setInt(41, course.getTeacingMaterialCost());
			stmt.setInt(42, course.isTeachingMaterialFree() ? 1 : 0);
			stmt.setString(43, course.getTeachingMaterialIntro());
			stmt.setString(44, course.getTeachingMethodsIntro());
			stmt.setString(45, course.getQuestionBankIntro());
			stmt.setString(46, course.getPassAgreement());
			stmt.setInt(47, course.isProvideAssignments() ? 1 : 0);
			stmt.setInt(48, course.isProvideMarking() ? 1 : 0);
			stmt.setString(49, course.getExtracurricularIntro());
			stmt.setString(50, course.getPhone());
			stmt.setString(51,Parser.listToString(course.getStudyDays()));
			stmt.setString(52, course.getTeachingMaterialName());
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			course.setCourseId(rs.getInt(1));
		}catch(SQLException e){
			DebugLog.d(e);
			throw new SQLException();
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return course;
	}

	public static void updateCourseInDatabases(Course course,Connection...connections) throws CourseNotFoundException, SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;
		String query = "UPDATE CourseDao SET p_Id=?,startTime=?,finishTime=?,t_Intro=?,t_ImgUrl=?,classroomImgUrl=?,price=?," +
				"seatsTotal=?,seatsLeft=?,status=?,category=?,subCategory=?,location=?,city=?,district=?,reference=?,courseIntro=?," +
				"classModel=?,hasDownloadMaterials=?,openCourseRequirement=?,suitableStudent=?,prerequest=?,highScoreReward=?," +
				"quiz=?,certification=?,questionBank=?,extracurricular=?,courseName=?,dailyStartTime=?,dailyFinishTime=?,studyDaysNote=?," +
				"courseHourNum=?,courseHourLength=?,partnerCourseReference=?,partnerIntro=?,classroomIntro=?,partnerQualification=?," +
				"t_Methods=?,t_MaterialType=?,t_MaterialCost=?,t_MaterialFree=?,t_MaterialIntro=?,t_MethodsIntro=?,questionBankIntro=?," +
				"passAgreement=?,provideAssignments=?,provideMarking=?,extracurricularIntro=?,phone=?,studyDays=?,t_MaterialName=? where id=?";
		try{
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, course.getPartnerId());			
			stmt.setString(2, DateUtility.toSQLDateTime(course.getStartTime()));
			stmt.setString(3, DateUtility.toSQLDateTime(course.getFinishTime()));
			stmt.setString(4, course.getTeacherIntro());
			stmt.setString(5, course.getTeacherImgUrl());
			stmt.setString(6, course.getClassroomImgUrl());
			stmt.setInt(7, course.getPrice());
			stmt.setInt(8, course.getSeatsTotal());
			stmt.setInt(9, course.getSeatsLeft());	
			stmt.setInt(10, course.getStatus().code);			
			stmt.setString(11, course.getCategory());
			stmt.setString(12, course.getSubCategory());			
			stmt.setString(13, course.getLocation());
			stmt.setString(14, course.getCity());
			stmt.setString(15, course.getDistrict());
			stmt.setString(16, course.getReference());
			stmt.setString(17, course.getCourseIntro());
			stmt.setInt(18, course.getClassModel().code);			
			stmt.setInt(19, course.isHasDownloadMaterials() ? 1 : 0);			
			stmt.setString(20, course.getOpenCourseRequirement());		
			stmt.setString(21,course.getSuitableStudent());
			stmt.setString(22, course.getPrerequest());			
			stmt.setString(23, course.getHighScoreReward());
			stmt.setString(24, course.getQuiz());
			stmt.setString(25, course.getCertification());
			stmt.setString(26, Parser.listToString(course.getQuestionBank()));
			stmt.setString(27, Parser.listToString(course.getExtracurricular()));
			stmt.setString(28, course.getCourseName());
			stmt.setString(29, course.getDailyStartTime());
			stmt.setString(30, course.getDailyFinishTime());
			stmt.setString(31, course.getStudyDaysNote());
			stmt.setInt(32, course.getCourseHourNum());
			stmt.setInt(33, course.getCourseHourLength());
			stmt.setString(34, course.getPartnerCourseReference());
			stmt.setString(35, course.getPartnerIntro());
			stmt.setString(36, course.getClassroomIntro());
			stmt.setInt(37, course.getPartnerQualification().code);
			stmt.setString(38, Parser.listToString(course.getTeachingMethods()));
			stmt.setInt(39, course.getTeachingMaterialType().code);
			stmt.setInt(40, course.getTeacingMaterialCost());
			stmt.setInt(41, course.isTeachingMaterialFree() ? 1 : 0);
			stmt.setString(42, course.getTeachingMaterialIntro());
			stmt.setString(43, course.getTeachingMethodsIntro());
			stmt.setString(44, course.getQuestionBankIntro());
			stmt.setString(45, course.getPassAgreement());
			stmt.setInt(46, course.isProvideAssignments() ? 1 : 0);
			stmt.setInt(47, course.isProvideMarking() ? 1 : 0);
			stmt.setString(48, course.getExtracurricularIntro());
			stmt.setString(49, course.getPhone());
			stmt.setString(50,Parser.listToString(course.getStudyDays()));			
			stmt.setString(51, course.getTeachingMaterialName());
			stmt.setInt(52, course.getCourseId());
			
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new CourseNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
			throw new SQLException();
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
	
	public static ArrayList<Course> getCourseByIdList(ArrayList<Integer> idList, Connection...connections){
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;
		int stmtInt = 1;
		ArrayList<Course> clist = null;
		
		try{
			clist = new ArrayList<Course>();
			if(idList == null || idList.size() == 0){
				return clist;
			}
			
			String query = "SELECT * FROM CourseDao where id = ?";
			for(int i = 1 ; i < idList.size() ; i++){
				query += " or id = ? ";
			}
			
			
			stmt = conn.prepareStatement(query);
			for(int i = 0 ; i < idList.size() ; i++){
				stmt.setInt(stmtInt++, idList.get(i));
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				clist.add(createCourseByResultSet(rs,null,conn));
			}
		}catch(SQLException e){
			DebugLog.d(e);			
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		
		return clist;
	}
	
	public static Course getCourseByReference(String reference,Connection...connections) throws CourseNotFoundException{
		String query = "SELECT * FROM CourseDao where reference = ?";
		Course course = null;
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;

		try{
			stmt = conn.prepareStatement(query);

			stmt.setString(1, reference);
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
		} catch(SQLException e){
			DebugLog.d(e);
		} catch (PartnerNotFoundException e) {			
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return clist;
	}

	protected static Course createCourseByResultSet(ResultSet rs,Partner partner,Connection...connections) throws SQLException {
		int p_Id =  rs.getInt("p_Id");
		String logoUrl;
		String instName;
		String wholeName;

		if(partner == null){
			try {
				partner = PartnerDao.getPartnerById(p_Id, connections);				
			} catch (PartnerNotFoundException e) {				
				e.printStackTrace();
				DebugLog.d(e);
			}
		}
		
		logoUrl = partner.getLogoUrl();
		instName = partner.getInstName();
		wholeName = partner.getWholeName();
		
		return new Course(rs.getInt("id"),p_Id,DateUtility.DateToCalendar(rs.getDate("startTime")), 
				DateUtility.DateToCalendar(rs.getDate("finishTime")),rs.getInt("price"),
				rs.getInt("seatsTotal"), rs.getInt("seatsLeft"),
				AccountStatus.fromInt(rs.getInt("status")), 
				rs.getString("category"), rs.getString("subCategory"),
				rs.getString("location"),rs.getString("city"),
				rs.getString("district"),rs.getString("reference"),
				rs.getString("t_Intro"),rs.getString("t_ImgUrl"),
				rs.getString("t_MethodsIntro"), rs.getString("classroomImgUrl"),
				rs.getString("courseIntro"), DateUtility.DateToCalendar(rs.getDate("creationTime")), 
				ClassModel.fromInt(rs.getInt("classModel")),
				rs.getBoolean("hasDownloadMaterials"), rs.getString("quiz"), rs.getString("certification"),
				rs.getString("openCourseRequirement"),
				(ArrayList<String>)Parser.stringToList(rs.getString("questionBank"), new String("")),
				rs.getString("suitableStudent"), rs.getString("prerequest"), rs.getString("highScoreReward"),
				(ArrayList<String>)Parser.stringToList(rs.getString("extracurricular"), new String("")),  
				rs.getString("courseName"),
				rs.getString("dailyStartTime"),rs.getString("dailyFinishTime"),
				(ArrayList<Integer>)Parser.stringToList(rs.getString("studyDays"), new Integer(0)), rs.getString("studyDaysNote"),
				rs.getInt("courseHourNum"), rs.getInt("courseHourLength"),
				rs.getString("partnerCourseReference"), rs.getString("classroomIntro"),
				PartnerQualification.fromInt(rs.getInt("partnerQualification")), rs.getString("partnerIntro"),
				(ArrayList<String>)Parser.stringToList(rs.getString("t_Methods"), new String("")),
				TeachingMaterialType.fromInt(rs.getInt("t_MaterialType")),
				rs.getString("t_MaterialIntro"), rs.getInt("t_MaterialCost"),
				rs.getBoolean("t_MaterialFree"), rs.getString("questionBankIntro"),
				rs.getString("passAgreement"), rs.getBoolean("provideAssignments"),
				rs.getBoolean("provideMarking"), rs.getString("extracurricularIntro"),
				rs.getString("phone"), logoUrl, instName, wholeName,rs.getString("t_MaterialName"));					
	}

	protected static Course createCourseByResultSet(ResultSet rs) throws SQLException{
		return new Course(rs.getInt("id"),-1,DateUtility.DateToCalendar(rs.getDate("startTime")), 
				DateUtility.DateToCalendar(rs.getDate("finishTime")),rs.getInt("price"),
				rs.getInt("seatsTotal"), rs.getInt("seatsLeft"),
				AccountStatus.fromInt(rs.getInt("status")), 
				rs.getString("category"), rs.getString("subCategory"),
				rs.getString("location"),rs.getString("city"),
				rs.getString("district"),rs.getString("reference"),
				rs.getString("t_Intro"),rs.getString("t_ImgUrl"),
				rs.getString("t_MethodsIntro"), rs.getString("classroomImgUrl"),
				rs.getString("courseIntro"), DateUtility.DateToCalendar(rs.getDate("creationTime")), 
				ClassModel.fromInt(rs.getInt("classModel")),
				rs.getBoolean("hasDownloadMaterials"), rs.getString("quiz"), rs.getString("certification"),
				rs.getString("openCourseRequirement"),
				(ArrayList<String>)Parser.stringToList(rs.getString("questionBank"), new String("")),
				rs.getString("suitableStudent"), rs.getString("prerequest"), rs.getString("highScoreReward"),
				(ArrayList<String>)Parser.stringToList(rs.getString("extracurricular"), new String("")),  
				rs.getString("courseName"),
				rs.getString("dailyStartTime"),rs.getString("dailyFinishTime"),
				(ArrayList<Integer>)Parser.stringToList(rs.getString("studyDays"), new Integer(0)), rs.getString("studyDaysNote"),
				rs.getInt("courseHourNum"), rs.getInt("courseHourLength"),
				rs.getString("partnerCourseReference"), rs.getString("classroomIntro"),
				PartnerQualification.fromInt(rs.getInt("partnerQualification")), rs.getString("partnerIntro"),
				(ArrayList<String>)Parser.stringToList(rs.getString("t_Methods"), new String("")),
				TeachingMaterialType.fromInt(rs.getInt("t_MaterialType")),
				rs.getString("t_MaterialIntro"), rs.getInt("t_MaterialCost"),
				rs.getBoolean("t_MaterialFree"), rs.getString("questionBankIntro"),
				rs.getString("passAgreement"), rs.getBoolean("provideAssignments"),
				rs.getBoolean("provideMarking"), rs.getString("extracurricularIntro"),
				rs.getString("phone"), "", "", "",rs.getString("t_MaterialName"));

	}

}