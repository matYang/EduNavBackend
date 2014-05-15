package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Status;
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
		String instName = "XDF";
		int seatsTotal = 50;
		int seatsLeft = 5;
		String category = "Physics";
		String subCategory = "sub-Phy";
		Status status = Status.activated;
		Course course = new Course(p_Id, startTime, finishTime, instName, seatsTotal, seatsLeft, category,subCategory,status);
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
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";
		String phone = "123545451";	
		Status status = Status.activated;
		Partner partner = new Partner(name, licence, organizationNum,reference, password, phone,status);
		partner = PartnerDao.addPartnerToDatabases(partner);
		int p_Id = partner.getId();
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 1);
		String instName = "XDF";
		int seatsTotal = 50;
		int seatsLeft = 5;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		Course course = new Course(p_Id, startTime, finishTime, instName, seatsTotal, seatsLeft, category,subCategory,status);
		CourseDao.addCourseToDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
		course.setBackgroundURL("www.hotmail.com");
		course.setT_ImgURL("www.google.ca");
		course.setT_Material("Hand and Ass");
		course.setTitle("bababa");
		course.setT_Info("sdfkljrghiuoghrer");
		course.setPrice(1111);
		CourseDao.updateCourseInDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
		if(partner.equals(course.getPartner())){
			//Passed;
		}else fail();
		
		if(course.getInstName().equals(partner.getName())&&course.getCategory().equals(category)&&course.getSubCategory().equals(subCategory)){
			//Passed;
		}else fail();
		
		Calendar startTime2 = DateUtility.getCurTimeInstance();
		Calendar finishTime2 = DateUtility.getCurTimeInstance();
		finishTime2.add(Calendar.DAY_OF_YEAR, 1);
		String instName2 = "XDF";
		int seatsTotal2 = 50;
		int seatsLeft2 = 5;
		String category2 = "Chineses";
		String subCategory2 = "sub-Chin";		
		Course course2 = new Course(p_Id, startTime2, finishTime2, instName2, seatsTotal2, seatsLeft2, category2,subCategory2,status);
		CourseDao.addCourseToDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());
		course2.setBackgroundURL("www.hotmail.com");
		course2.setT_ImgURL("www.google.ca");
		course2.setT_Material("Hand and Ass");
		course2.setTitle("bababa");
		course2.setT_Info("sdfeorigjkerogjeiorg");
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
		clist = CourseDao.getCoursesFromPartner(partner.getId());
		if(clist.size()==2&&clist.get(0).equals(course)&&clist.get(1).equals(course2)){
			//Passed;
		}else fail();
	}	
	
}
