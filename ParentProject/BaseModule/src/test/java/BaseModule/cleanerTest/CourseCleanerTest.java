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
		String phone = "123545451";
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name, instName,licence, organizationNum,reference, password, phone,status);
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
		Course course = new Course(p_Id, startTime, finishTime,price,classSize,popularity,category,subCategory,phone);
		String location = "China";
		String city = "NanJing";
		String district = "JiangNing";
		String reference2 = "testr";
		course.setLocation(location);
		course.setCity(city);
		course.setDistrict(district);
		course.setReference(reference2);
		CourseDao.addCourseToDatabases(course);
		
		
		Calendar startTime2 = DateUtility.getCurTimeInstance();		
		startTime2.add(Calendar.DAY_OF_YEAR, -1);			
		Course course2 = new Course(p_Id, startTime2, finishTime,price,classSize,popularity,category,subCategory,phone);
		course2.setLocation(location);
		course2.setCity(city);
		course2.setDistrict(district);
		course2.setReference(reference2);
		course2.setStatus(CourseStatus.consolidated);
		CourseDao.addCourseToDatabases(course2);
		
						
		Calendar startTime3 = DateUtility.getCurTimeInstance();		
		startTime3.add(Calendar.MINUTE, 1);
		Course course3 = new Course(p_Id, startTime3, finishTime,price,classSize,popularity,category,subCategory,phone);
		String location2= "China";
		String city2 = "ChengDu";
		String district2 = "ChengHua";
		String reference3 = "Tse";
		course3.setLocation(location2);
		course3.setCity(city2);
		course3.setDistrict(district2);
		course3.setReference(reference3);
		course3.setStatus(CourseStatus.openEnroll);
		CourseDao.addCourseToDatabases(course3);		
	
		CourseCleaner.cleanCourse();		
		
		ArrayList<Course> clist = new ArrayList<Course>();
		CourseSearchRepresentation c_sr = new CourseSearchRepresentation();
		c_sr.setPartnerId(p_Id);
		clist = CourseDao.searchCourse(c_sr);
		if(clist.size()==3&&clist.get(0).getStatus().code==CourseStatus.deactivated.code &&
				clist.get(1).getStatus().code==CourseStatus.consolidated.code &&
				clist.get(2).getStatus().code==CourseStatus.openEnroll.code){
			//Passed;
		}else fail();
				
		
	}
}
