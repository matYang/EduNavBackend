package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.course.CourseNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Course;
import BaseModule.model.Partner;

public class CourseDaoTest {

	@Test
	public void testCreate(){
		EduDaoBasic.clearBothDatabase();
		int p_Id = 1;
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal = 50;
		int seatsLeft = 5;
		int price = 1000;
		String title = "new X";
		String category = "Physics";
		String subCategory = "sub-Phy";
		AccountStatus status = AccountStatus.activated;
		Course course = new Course(p_Id, startTime, finishTime,seatsTotal, seatsLeft, category,subCategory,status,price,title);
		try{
			CourseDao.addCourseToDatabases(course);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGetAndUpdate() throws ValidationException, CourseNotFoundException{
		EduDaoBasic.clearBothDatabase();
		String name = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";
		String phone = "123545451";	
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name,instName, licence, organizationNum,reference, password, phone,status);
		partner = PartnerDao.addPartnerToDatabases(partner);
		int p_Id = partner.getPartnerId();
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal = 50;
		int seatsLeft = 5;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		int price = 1000;
		String title = "new X";
		Course course = new Course(p_Id, startTime, finishTime, seatsTotal, seatsLeft, category,subCategory,status,price,title);
		String location = "China";
		String city = "NanJing";
		String district = "JiangNing";
		String reference2 = "testr";
		course.setLocation(location);
		course.setCity(city);
		course.setDistrict(district);
		course.setReference(reference2);
		CourseDao.addCourseToDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
		course.setBackgroundUrl("www.hotmail.com");
		course.setTeacherImgUrl("www.google.ca");
		course.setTeachingMaterial("Hand and Ass");
		course.setTitle("bababa");
		course.setTeacherInfo("sdfkljrghiuoghrer");
		course.setPrice(1111);
		CourseDao.updateCourseInDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
		if(partner.equals(course.getPartner())){
			//Passed;
		}else fail();
		
		if(course.getPartner().getInstName().equals(partner.getInstName())&&course.getCategory().equals(category)&&course.getSubCategory().equals(subCategory)){
			//Passed;
		}else fail();
		
		Calendar startTime2 = DateUtility.getCurTimeInstance();
		Calendar finishTime2 = DateUtility.getCurTimeInstance();
		finishTime2.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal2 = 50;
		int seatsLeft2 = 5;
		String category2 = "Chineses";
		String subCategory2 = "sub-Chin";		
		Course course2 = new Course(p_Id, startTime2, finishTime2, seatsTotal2, seatsLeft2, category2,subCategory2,status,price,title);
		CourseDao.addCourseToDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());		
		course2.setLocation(location);
		course2.setCity(city);
		course2.setDistrict(district);
		course2.setReference(reference2);
		course2.setBackgroundUrl("www.hocom");
		course2.setTeacherImgUrl("wwwgle.ca");
		course2.setTeachingMaterial("Hass");
		course2.setTitle("baa");
		course2.setTeacherInfo("sdfklghrer");
		course2.setPrice(2222);
		CourseDao.updateCourseInDatabases(course2);
		
		Course test = CourseDao.getCourseById(course.getCourseId());
		if(test.equals(course)){
			//Passed;
		}else fail();
		
		test = CourseDao.getCourseById(course2.getCourseId());
		if(test.equals(course2)){
			//Passed;
		}else fail();
		
		ArrayList<Course> clist = new ArrayList<Course>();
		clist = CourseDao.getCoursesFromPartner(partner.getPartnerId());
		if(clist.size()==2&&clist.get(0).equals(course)&&clist.get(1).equals(course2)){
			//Passed;
		}else fail();
	}	
	
}
