package BaseModule.cleanerTest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.clean.cleanTasks.CourseCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.CourseStatus;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.PseudoException;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.representation.CourseSearchRepresentation;

public class CourseCleanerTest {

	@Test
	public void test() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "XDF";
		String instName ="TseTingFeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";		
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name, instName,licence, organizationNum,reference, password,status);
		PartnerDao.addPartnerToDatabases(partner);
		int p_Id = partner.getPartnerId();
		Calendar startTime = DateUtility.getCurTimeInstance();
		startTime.add(Calendar.SECOND, -1);
		Calendar finishTime = DateUtility.getCurTimeInstance();				
		int classSize = 50;
		int popularity = 5;
		String category = "Physics";
		String subCategory = "sub-Phy";			
		int price = 124;
		Course course = new Course(p_Id, startTime, finishTime,price,classSize,popularity,category,subCategory);
		String location = "China";
		String city = "NanJing";
		String district = "JiangNing";
		String reference2 = "testr";
		course.setLocation(location);
		course.setCity(city);
		course.setDistrict(district);
		course.setReference(reference2);
		course.setCutoffDate(startTime);
		ArrayList<Long> list = new ArrayList<Long>();
		list.add(1L);
		course.setOutline("sdfdsf");
		course.setGoal("sdfdsf");
		course.setClassPhotoIdList(list);
		course.setTeacherIdList(list);
		CourseDao.addCourseToDatabases(course);
		
		
		Calendar startTime2 = DateUtility.getCurTimeInstance();		
		startTime2.add(Calendar.DAY_OF_YEAR, -1);			
		Course course2 = new Course(p_Id, startTime2, finishTime,price,classSize,popularity,category,subCategory);
		course2.setLocation(location);
		course2.setCity(city);
		course2.setDistrict(district);
		course2.setReference(reference2+"sdf");
		course2.setStatus(CourseStatus.consolidated);
		course2.setCutoffDate(startTime2);		
		course2.setClassPhotoIdList(list);
		course2.setTeacherIdList(list);
		course2.setOutline("sdfdsf");
		course2.setGoal("sdfdsf");
		CourseDao.addCourseToDatabases(course2);
		
						
		Calendar startTime3 = DateUtility.getCurTimeInstance();		
		startTime3.add(Calendar.MINUTE, 1);
		Course course3 = new Course(p_Id, startTime3, finishTime,price,classSize,popularity,category,subCategory);
		String location2= "China";
		String city2 = "ChengDu";
		String district2 = "ChengHua";
		String reference3 = "Tse";
		course3.setLocation(location2);
		course3.setCity(city2);
		course3.setDistrict(district2);
		course3.setReference(reference3);
		course3.setStatus(CourseStatus.openEnroll);
		course3.setCutoffDate(startTime3);
		course3.setClassPhotoIdList(list);
		course3.setTeacherIdList(list);
		course3.setOutline("sdfdsf");
		course3.setGoal("sdfdsf");
		CourseDao.addCourseToDatabases(course3);
		
		Calendar startTime4 = DateUtility.getCurTimeInstance();		
		startTime4.add(Calendar.MINUTE, 1);
		Course course4 = new Course(p_Id, startTime4, finishTime,price,classSize,popularity,category,subCategory);	
		String reference4 = "Tsesdf";
		course4.setLocation(location2);
		course4.setCity(city2);
		course4.setDistrict(district2);
		course4.setReference(reference4);
		course4.setStatus(CourseStatus.deactivated);
		course4.setCutoffDate(startTime4);
		course4.setClassPhotoIdList(list);
		course4.setTeacherIdList(list);
		course4.setOutline("sdfdsf");
		course4.setGoal("sdfdsf");
		CourseDao.addCourseToDatabases(course4);	
		
		Calendar startTime5 = DateUtility.getCurTimeInstance();		
		startTime5.add(Calendar.MINUTE, -1);
		Course course5 = new Course(p_Id, startTime5, finishTime,price,classSize,popularity,category,subCategory);	
		String reference5 = "Tsesdtyuf";
		course5.setLocation(location2);
		course5.setCity(city2);
		course5.setDistrict(district2);
		course5.setReference(reference5);
		course5.setStatus(CourseStatus.consolidated);
		course5.setCutoffDate(startTime5);
		course5.setClassPhotoIdList(list);
		course5.setTeacherIdList(list);
		course5.setOutline("sdfdsf");
		course5.setGoal("sdfdsf");
		CourseDao.addCourseToDatabases(course5);
		
		Calendar startTime6 = DateUtility.getCurTimeInstance();		
		startTime6.add(Calendar.MINUTE, -1);
		Course course6 = new Course(p_Id, startTime6, finishTime,price,classSize,popularity,category,subCategory);	
		String reference6 = "Tsesdty58uf";
		course6.setLocation(location2);
		course6.setCity(city2);
		course6.setDistrict(district2);
		course6.setReference(reference6);
		course6.setStatus(CourseStatus.openEnroll);
		course6.setCutoffDate(startTime6);
		course6.setClassPhotoIdList(list);
		course6.setTeacherIdList(list);
		course6.setOutline("sdfdsf");
		course6.setGoal("sdfdsf");
		CourseDao.addCourseToDatabases(course6);
	
		CourseCleaner.clean();		
		
		ArrayList<Course> clist = new ArrayList<Course>();
		CourseSearchRepresentation c_sr = new CourseSearchRepresentation();
		c_sr.setPartnerId(p_Id);
		clist = CourseDao.searchCourse(c_sr);
		if(clist.size()==6&&clist.get(0).getStatus().code==CourseStatus.deactivated.code &&
				clist.get(1).getStatus().code==CourseStatus.consolidated.code &&
				clist.get(2).getStatus().code==CourseStatus.openEnroll.code &&
				clist.get(3).getStatus().code==CourseStatus.deactivated.code &&
				clist.get(4).getStatus().code==CourseStatus.consolidated.code &&
				clist.get(5).getStatus().code==CourseStatus.deactivated.code){
			//Passed;
		}else fail();
				
		
	}
}
