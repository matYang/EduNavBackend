package AdminModule.resources.modelLoader;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.representation.CourseSearchRepresentation;

public class MemcachedBenchMarkResource extends AdminPseudoResource {
	
	@Get
	public Representation makeMemcachedBenchMark() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, PseudoException, InterruptedException{
		EduDaoBasic.clearAllDatabase();
		loadPartners();
		for (int i = 0; i < 50; i++){
			loadCourses();
		}
		for (int i = 0; i < 1000; i++){
			loadIrrelevantCourses();
		}
		
		Map<String, String> kvps= new HashMap<String, String>();
		kvps.put("category", "Chinese");
		kvps.put("useCache", "0");
		
		CourseSearchRepresentation c_sr = new CourseSearchRepresentation();
		c_sr.storeKvps(kvps);
		DebugLog.b_d("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < 5000; i++){
			CourseDaoService.searchCourse(c_sr);
			Thread.sleep(5);
		}
		DebugLog.b_d("middle time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		System.out.println("middle time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		c_sr.setUseCache(1);
		for (int i = 0; i < 5000; i++){
			CourseDaoService.searchCourse(c_sr);
			Thread.sleep(5);
		}
		DebugLog.b_d("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		
		
		
		Representation result = new JsonRepresentation(new JSONObject());
		this.addCORSHeader();
		return result;
	}
	
	
	
	
	
	
	

	private void loadCourses(Connection...connections){
		int p_Id = 1;
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_MONTH, 8);			
		int seatsTotal = 50;
		int seatsLeft = 5;
		int price = 1000;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		String phone = "12344565654";
		AccountStatus status = AccountStatus.activated;		
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		try {
			CourseDao.addCourseToDatabases(course, connections);
		} catch (SQLException e) {	
			e.printStackTrace();
		}

		p_Id = 2;
		startTime = DateUtility.getCurTimeInstance();
		finishTime = DateUtility.getCurTimeInstance();
		startTime.add(Calendar.DAY_OF_MONTH, -8);					
		category = "Chinese";
		subCategory = "sub-Chin";		
		status = AccountStatus.deactivated;		
		Course course2 = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		try {
			CourseDao.addCourseToDatabases(course2, connections);
		} catch (SQLException e) {			
			e.printStackTrace();
		}

		p_Id = 3;
		startTime = DateUtility.getCurTimeInstance();
		finishTime = DateUtility.getCurTimeInstance();
		startTime.add(Calendar.DAY_OF_MONTH, -8);
		finishTime.add(Calendar.DAY_OF_MONTH, -7);
		category = "French";
		subCategory = "sub-French";		
		status = AccountStatus.deleted;		
		Course course3 = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		try {
			CourseDao.addCourseToDatabases(course3, connections);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
	private void loadIrrelevantCourses(Connection...connections){
		int p_Id = 1;
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_MONTH, 8);			
		int seatsTotal = 50;
		int seatsLeft = 5;
		int price = 1000;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		String phone = "12344565654";
		AccountStatus status = AccountStatus.activated;		
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		try {
			CourseDao.addCourseToDatabases(course, connections);
		} catch (SQLException e) {	
			e.printStackTrace();
		}


	}
	
	private void loadPartners(Connection...connections){
		try{
			String name = "XDF";
			String instName = "xiaofeng";
			String licence = "234fdsfsdgergf-dsv,.!@";
			String organizationNum = "1235454361234";
			String reference = "dsf4r";
			String password = "sdf234r";
			String phone = "123545451";
			AccountStatus status = AccountStatus.activated;
			Partner partner = new Partner(name, instName,licence, organizationNum,reference, password, phone,status);
			try {
				PartnerDao.addPartnerToDatabases(partner, connections);
			} catch (SQLException e) {				
				e.printStackTrace();
			}

			String name2 = "HQYS";
			String instName2 = "daofeng";
			String licence2 = "2sdfdsf34545dsfsdgergf-dsv,.!@";
			String organizationNum2 = "12334361234";
			String reference2 = "dsdsfr";
			String password2 = "sdsdf34r";
			String phone2 = "12335451";
			AccountStatus status2 = AccountStatus.deactivated;
			Partner partner2 = new Partner(name2, instName2,licence2, organizationNum2,reference2, password2, phone2,status2);
			try {
				PartnerDao.addPartnerToDatabases(partner2,connections);
			} catch (SQLException e) {				
				e.printStackTrace();
			}

			String name3 = "XDFs";
			String instName3 = "xiaofeng";
			String licence3 = "234fdsfv,.!@";
			String organizationNum3 = "1235454361234";
			String reference3 = "d4r";
			String password3 = "sdf234r";
			String phone3 = "12354";
			AccountStatus status3 = AccountStatus.deleted;
			Partner partner3 = new Partner(name3, instName3,licence3, organizationNum3,reference3, password3, phone3,status3);
			try {
				PartnerDao.addPartnerToDatabases(partner3, connections);
			} catch (SQLException e) {				
				e.printStackTrace();
			}
		}catch(ValidationException e){
			e.printStackTrace();
			DebugLog.d(e);
		}

	}

}
