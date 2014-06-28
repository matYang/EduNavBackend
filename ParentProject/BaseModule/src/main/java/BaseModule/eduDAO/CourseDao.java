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
import BaseModule.common.Parser;
import BaseModule.configurations.EnumConfig.BookingType;
import BaseModule.configurations.EnumConfig.CourseStatus;
import BaseModule.configurations.EnumConfig.PartnerQualification;
import BaseModule.configurations.ImgConfig;
import BaseModule.configurations.ServerConfig;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CourseNotFoundException;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.representation.CourseSearchRepresentation;

public class CourseDao {


	public static ArrayList<Course> searchCourse(CourseSearchRepresentation sr,Connection...connections) throws PseudoException, SQLException{
		ArrayList<Course> clist = new ArrayList<Course>();
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		HashMap<Integer,Partner> pmap = new HashMap<Integer,Partner>();
		Partner partner = null;		
		String query = sr.getSearchQuery();
		int stmtInt = 1;
		boolean joinQ = false;		

		if(((sr.getPartnerId() > 0) ||(sr.getInstitutionName() != null && sr.getInstitutionName().length() > 0) ||
				(sr.getPartnerReference() != null && sr.getPartnerReference().length() > 0))
				&& !joinQ){
			joinQ = true;		

		}		

		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);	

			if(joinQ){	
				if(sr.getInstitutionName() != null && sr.getInstitutionName().length() > 0){
					stmt.setString(stmtInt++, sr.getInstitutionName());
				}
				if(sr.getPartnerReference() != null && sr.getPartnerReference().length() > 0){
					stmt.setString(stmtInt++, sr.getPartnerReference());
				}				
			}
			if(sr.getStartCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getStartCreationTime()));
			}
			if(sr.getFinishCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getFinishCreationTime()));
			}
			if(sr.getStartDate() != null){
				Calendar startTime = (Calendar) sr.getStartDate().clone();
				startTime.set(Calendar.HOUR_OF_DAY,0);
				startTime.set(Calendar.MINUTE, 0);
				startTime.set(Calendar.SECOND, 0);
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(startTime));
			}
			if(sr.getFinishDate() != null){
				Calendar finishTime = (Calendar) sr.getFinishDate().clone();
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
			if(sr.getSubSubCategory()!=null&&sr.getSubSubCategory().length()>0){
				stmt.setString(stmtInt++, sr.getSubSubCategory());		
			}
			if(sr.getProvince() != null && sr.getProvince().length() > 0){
				stmt.setString(stmtInt++, sr.getProvince());
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
			if(sr.getStartClassSize() != -1){
				stmt.setInt(stmtInt++, sr.getStartClassSize());
			}
			if(sr.getFinishClassSize() != -1){
				stmt.setInt(stmtInt++, sr.getFinishClassSize());
			}
			if(sr.getStartCashback() != -1){
				stmt.setInt(stmtInt++, sr.getStartCashback());
			}
			if(sr.getFinishCashback() != -1){
				stmt.setInt(stmtInt++, sr.getFinishCashback());
			}
			if(sr.getStartUponArrival() != -1){
				stmt.setInt(stmtInt++, sr.getStartUponArrival());
			}
			if(sr.getStartCutoffDate() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getStartCutoffDate()));
			}
			if(sr.getFinishCutoffDate() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getFinishCutoffDate()));
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
				}
				//ignore the partner not founds
			}
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 

		return clist;
	}

	public static Course addCourseToDatabases(Course course,Connection...connections) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		int stmtInt = 1;
		String query = "INSERT INTO CourseDao (p_Id,creationTime,startDate,finishDate,t_Intros,t_ImgUrls,classroomImgUrls,price," +
				"status,category,subCategory,subSubCategory,location,province,city,district,reference,courseIntro," +
				"quiz,certification,openCourseRequirement,questionBank,suitableStudent,prerequest,highScoreReward," +
				"extracurricular,courseName,studyDaysNote,courseHourNum,courseHourLength,partnerCourseReference,partnerQualification,partnerIntro," +
				"t_MaterialFree,t_MaterialIntro,passAgreement,phone,studyDays,classSize,cashback,popularity,startTime1,finishTime1,startTime2,finishTime2," +
				"partnerDistinction,outline,goal,classTeacher,teachingAndExercise,questionSession,trail,assignments,marking,bonusService," +
				"downloadMaterials,teacherNames,startUponArrival,cutoffDate,noRefundDate,cashbackDate,bookingType,originalPrice)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	

			stmt.setInt(stmtInt++, course.getPartnerId());
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getCreationTime()));
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getStartDate()));
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getFinishDate()));
			stmt.setString(stmtInt++, Parser.listToString(course.getTeacherIntros(),ServerConfig.normalSpliter));
			stmt.setString(stmtInt++, Parser.listToString(course.getTeacherImgUrls(),ImgConfig.imgSpliter));
			stmt.setString(stmtInt++, Parser.listToString(course.getClassImgUrls(),ImgConfig.imgSpliter));
			stmt.setInt(stmtInt++, course.getPrice());			
			stmt.setInt(stmtInt++, course.getStatus().code);			
			stmt.setString(stmtInt++, course.getCategory());
			stmt.setString(stmtInt++, course.getSubCategory());	
			stmt.setString(stmtInt++, course.getSubSubCategory());		
			stmt.setString(stmtInt++, course.getLocation());
			stmt.setString(stmtInt++, course.getProvince());
			stmt.setString(stmtInt++, course.getCity());
			stmt.setString(stmtInt++, course.getDistrict());
			stmt.setString(stmtInt++, course.getReference());
			stmt.setString(stmtInt++, course.getCourseIntro());
			stmt.setString(stmtInt++, course.getQuiz());
			stmt.setString(stmtInt++, course.getCertification());			
			stmt.setString(stmtInt++, course.getOpenCourseRequirement());	
			stmt.setString(stmtInt++, course.getQuestionBank());			
			stmt.setString(stmtInt++,course.getSuitableStudent());
			stmt.setString(stmtInt++, course.getPrerequest());			
			stmt.setString(stmtInt++, course.getHighScoreReward());
			stmt.setString(stmtInt++, course.getExtracurricular());
			stmt.setString(stmtInt++, course.getCourseName());
			stmt.setString(stmtInt++,Parser.listToString(course.getStudyDays(),ServerConfig.normalSpliter));
			stmt.setInt(stmtInt++, course.getCourseHourNum());
			stmt.setInt(stmtInt++, course.getCourseHourLength());
			stmt.setString(stmtInt++, course.getPartnerCourseReference());
			stmt.setInt(stmtInt++, course.getPartnerQualification().code);
			stmt.setString(stmtInt++, course.getPartnerIntro());
			stmt.setString(stmtInt++, course.getTeachingMaterialFee());
			stmt.setString(stmtInt++, course.getTeachingMaterialIntro());
			stmt.setString(stmtInt++, course.getPassAgreement());
			stmt.setString(stmtInt++, course.getPhone());
			stmt.setString(stmtInt++, course.getStudyDaysNote());
			stmt.setInt(stmtInt++, course.getClassSize());
			stmt.setInt(stmtInt++, course.getCashback());			
			stmt.setInt(stmtInt++, course.getPopularity());
			stmt.setInt(stmtInt++, course.getStartTime1());
			stmt.setInt(stmtInt++, course.getFinishTime1());
			stmt.setInt(stmtInt++, course.getStartTime2());
			stmt.setInt(stmtInt++, course.getFinishTime2());
			stmt.setString(stmtInt++, course.getPartnerDistinction());
			stmt.setString(stmtInt++, course.getOutline());
			stmt.setString(stmtInt++, course.getGoal());
			stmt.setString(stmtInt++, course.getClassTeacher());
			stmt.setString(stmtInt++, course.getTeachingAndExercise());
			stmt.setString(stmtInt++, course.getQuestionSession());
			stmt.setString(stmtInt++, course.getTrail());
			stmt.setString(stmtInt++, course.getAssignments());
			stmt.setString(stmtInt++, course.getMarking());
			stmt.setString(stmtInt++, course.getBonusService());
			stmt.setString(stmtInt++, course.getDownloadMaterials());
			stmt.setString(stmtInt++, Parser.listToString(course.getTeacherNames(),ServerConfig.normalSpliter));		
			stmt.setInt(stmtInt++, course.getStartUponArrival());
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getCutoffDate()));
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getNoRefundDate()));
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getCashbackDate()));
			stmt.setInt(stmtInt++, course.getBookingType().code);
			stmt.setInt(stmtInt++, course.getOriginalPrice());
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			course.setCourseId(rs.getInt(1));
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return course;
	}

	public static void updateCourseInDatabases(Course course,Connection...connections) throws CourseNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		int stmtInt = 1;
		String query = "UPDATE CourseDao SET p_Id=?,startDate=?,finishDate=?,t_Intros=?,t_ImgUrls=?,classroomImgUrls=?,price=?," +
				"status=?,category=?,subCategory=?,subSubCategory=?,location=?,province=?,city=?,district=?,reference=?,courseIntro=?," +
				"quiz=?,certification=?,openCourseRequirement=?,questionBank=?,suitableStudent=?,prerequest=?,highScoreReward=?," +
				"extracurricular=?,courseName=?,studyDaysNote=?,courseHourNum=?,courseHourLength=?,partnerCourseReference=?,partnerQualification=?,partnerIntro=?," +
				"t_MaterialFree=?,t_MaterialIntro=?,passAgreement=?,phone=?,studyDays=?,classSize=?,cashback=?,popularity=?,startTime1=?," +
				"finishTime1=?,startTime2=?,finishTime2=?,partnerDistinction=?,outline=?,goal=?,classTeacher=?,teachingAndExercise=?," +
				"questionSession=?,trail=?,assignments=?,marking=?,bonusService=?,downloadMaterials=?,teacherNames=?,startUponArrival=?," +
				"cutoffDate=?,noRefundDate=?,cashbackDate=?,bookingType=?,originalPrice=? where id=?";
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			
			stmt.setInt(stmtInt++, course.getPartnerId());			
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getStartDate()));
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getFinishDate()));
			stmt.setString(stmtInt++, Parser.listToString(course.getTeacherIntros(),ServerConfig.normalSpliter));
			stmt.setString(stmtInt++, Parser.listToString(course.getTeacherImgUrls(),ImgConfig.imgSpliter));
			stmt.setString(stmtInt++, Parser.listToString(course.getClassImgUrls(),ImgConfig.imgSpliter));
			stmt.setInt(stmtInt++, course.getPrice());			
			stmt.setInt(stmtInt++, course.getStatus().code);			
			stmt.setString(stmtInt++, course.getCategory());
			stmt.setString(stmtInt++, course.getSubCategory());	
			stmt.setString(stmtInt++, course.getSubSubCategory());		
			stmt.setString(stmtInt++, course.getLocation());
			stmt.setString(stmtInt++, course.getProvince());
			stmt.setString(stmtInt++, course.getCity());
			stmt.setString(stmtInt++, course.getDistrict());
			stmt.setString(stmtInt++, course.getReference());
			stmt.setString(stmtInt++, course.getCourseIntro());
			stmt.setString(stmtInt++, course.getQuiz());
			stmt.setString(stmtInt++, course.getCertification());			
			stmt.setString(stmtInt++, course.getOpenCourseRequirement());	
			stmt.setString(stmtInt++, course.getQuestionBank());			
			stmt.setString(stmtInt++,course.getSuitableStudent());
			stmt.setString(stmtInt++, course.getPrerequest());			
			stmt.setString(stmtInt++, course.getHighScoreReward());
			stmt.setString(stmtInt++, course.getExtracurricular());
			stmt.setString(stmtInt++, course.getCourseName());
			stmt.setString(stmtInt++,Parser.listToString(course.getStudyDays(),ServerConfig.normalSpliter));
			stmt.setInt(stmtInt++, course.getCourseHourNum());
			stmt.setInt(stmtInt++, course.getCourseHourLength());
			stmt.setString(stmtInt++, course.getPartnerCourseReference());
			stmt.setInt(stmtInt++, course.getPartnerQualification().code);
			stmt.setString(stmtInt++, course.getPartnerIntro());
			stmt.setString(stmtInt++, course.getTeachingMaterialFee());
			stmt.setString(stmtInt++, course.getTeachingMaterialIntro());
			stmt.setString(stmtInt++, course.getPassAgreement());
			stmt.setString(stmtInt++, course.getPhone());
			stmt.setString(stmtInt++, course.getStudyDaysNote());
			stmt.setInt(stmtInt++, course.getClassSize());
			stmt.setInt(stmtInt++, course.getCashback());			
			stmt.setInt(stmtInt++, course.getPopularity());
			stmt.setInt(stmtInt++, course.getStartTime1());
			stmt.setInt(stmtInt++, course.getFinishTime1());
			stmt.setInt(stmtInt++, course.getStartTime2());
			stmt.setInt(stmtInt++, course.getFinishTime2());
			stmt.setString(stmtInt++, course.getPartnerDistinction());
			stmt.setString(stmtInt++, course.getOutline());
			stmt.setString(stmtInt++, course.getGoal());
			stmt.setString(stmtInt++, course.getClassTeacher());
			stmt.setString(stmtInt++, course.getTeachingAndExercise());
			stmt.setString(stmtInt++, course.getQuestionSession());
			stmt.setString(stmtInt++, course.getTrail());
			stmt.setString(stmtInt++, course.getAssignments());
			stmt.setString(stmtInt++, course.getMarking());
			stmt.setString(stmtInt++, course.getBonusService());
			stmt.setString(stmtInt++, course.getDownloadMaterials());
			stmt.setString(stmtInt++, Parser.listToString(course.getTeacherNames(),ServerConfig.normalSpliter));
			stmt.setInt(stmtInt++, course.getStartUponArrival());
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getCutoffDate()));
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getNoRefundDate()));
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(course.getCashbackDate()));
			stmt.setInt(stmtInt++, course.getBookingType().code);
			stmt.setInt(stmtInt++, course.getOriginalPrice());
			stmt.setInt(stmtInt++, course.getCourseId());
			
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new CourseNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, null,EduDaoBasic.shouldConnectionClose(connections));
		}
	}

	public static Course getCourseById(int courseId,Connection...connections) throws PseudoException, SQLException{
		String query = "SELECT * FROM CourseDao where id = ?";
		Course course = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;

		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, courseId);
			rs = stmt.executeQuery();
			if(rs.next()){
				course = createCourseByResultSet(rs,null,conn);
			}else {
				throw new CourseNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return course;
	}
	
	public static ArrayList<Course> getCourseByIdList(ArrayList<Integer> idList, Connection...connections) throws PseudoException, SQLException{
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int stmtInt = 1;
		ArrayList<Course> clist = null;
		
		try{
			conn = EduDaoBasic.getConnection(connections);
			
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
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		
		return clist;
	}

	

	protected static Course createCourseByResultSet(ResultSet rs,Partner partner,Connection...connections) throws SQLException, PseudoException {
		int p_Id =  rs.getInt("p_Id");
		String logoUrl;
		String instName;
		String wholeName;

		if(partner == null){
			partner = PartnerDao.getPartnerById(p_Id, connections);				
		}
		
		logoUrl = partner.getLogoUrl();
		instName = partner.getInstName();
		wholeName = partner.getWholeName();
		
		return new Course(rs.getInt("id"),p_Id,rs.getInt("price"),rs.getInt("courseHourNum"), 
				rs.getInt("courseHourLength"),rs.getInt("classSize"),rs.getInt("cashback"),rs.getInt("popularity"),
				DateUtility.DateToCalendar(rs.getDate("creationTime")),
				DateUtility.DateToCalendar(rs.getDate("startDate")), 
				DateUtility.DateToCalendar(rs.getDate("finishDate")),
				rs.getInt("startTime1"),rs.getInt("finishTime1"),rs.getInt("startTime2"),rs.getInt("finishTime2"),
				rs.getString("category"), rs.getString("subCategory"),rs.getString("subSubCategory"),
				rs.getString("location"),rs.getString("province"),rs.getString("city"),
				rs.getString("district"),rs.getString("reference"),
				rs.getString("courseIntro"),rs.getString("quiz"),		
				 rs.getString("certification"),	rs.getString("openCourseRequirement"),					
				 rs.getString("suitableStudent"), rs.getString("prerequest"), 
				rs.getString("highScoreReward"),rs.getString("courseName"), 
				rs.getString("studyDaysNote"),rs.getString("partnerCourseReference"),  
				rs.getString("partnerIntro"),rs.getString("t_MaterialIntro"),
				rs.getString("questionBank"),rs.getString("passAgreement"),
				rs.getString("extracurricular"),rs.getString("phone"),
				rs.getString("partnerDistinction"),rs.getString("outline"),rs.getString("goal"),rs.getString("classTeacher"),
				rs.getString("teachingAndExercise"),rs.getString("questionSession"),rs.getString("trail"),
				rs.getString("assignments"),rs.getString("marking"), rs.getString("bonusService"),rs.getString("downloadMaterials"),
				CourseStatus.fromInt(rs.getInt("status")),PartnerQualification.fromInt(rs.getInt("partnerQualification")),
				rs.getString("t_MaterialFree"),	(ArrayList<Integer>)Parser.stringToList(rs.getString("studyDays"),ServerConfig.normalSpliter, new Integer(0)),
				(ArrayList<String>)Parser.stringToList(rs.getString("classroomImgUrls"),ImgConfig.imgSpliterRegex,new String("")),
				(ArrayList<String>)Parser.stringToList(rs.getString("t_Intros"),ServerConfig.normalSpliter,new String("")),
				(ArrayList<String>)Parser.stringToList(rs.getString("t_ImgUrls"),ImgConfig.imgSpliterRegex,new String("")),
				(ArrayList<String>)Parser.stringToList(rs.getString("teacherNames"),ServerConfig.normalSpliter, new String("")),
				logoUrl, instName, wholeName,rs.getInt("startUponArrival"),DateUtility.DateToCalendar(rs.getDate("cutoffDate")),DateUtility.DateToCalendar(rs.getTimestamp("noRefundDate")),
				DateUtility.DateToCalendar(rs.getTimestamp("cashbackDate")),BookingType.fromInt(rs.getInt("bookingType")),rs.getInt("originalPrice"));					
	}


}