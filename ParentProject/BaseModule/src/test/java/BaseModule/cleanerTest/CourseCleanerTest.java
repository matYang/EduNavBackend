package BaseModule.cleanerTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.clean.cleanTasks.CourseCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Status;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Course;
import BaseModule.model.Partner;

public class CourseCleanerTest {

	@Test
	public void test() throws ValidationException{
		EduDaoBasic.clearBothDatabase();
		String name = "XDF";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";
		String phone = "123545451";
		Status status = Status.activated;
		Partner partner = new Partner(name, licence, organizationNum,reference, password, phone,status);
		PartnerDao.addPartnerToDatabases(partner);
		int p_Id = partner.getId();
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();		
		finishTime.add(Calendar.DAY_OF_YEAR, 1);
		String instName = partner.getName();
		int seatsTotal = 50;
		int seatsLeft = 5;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		Course course = new Course(p_Id, startTime, finishTime, instName, seatsTotal, seatsLeft, category,subCategory,status);
		CourseDao.addCourseToDatabases(course);
		
		
		Calendar finishTime2 = DateUtility.getCurTimeInstance();		
		finishTime2.add(Calendar.DAY_OF_YEAR, -1);		
		Course course2 = new Course(p_Id, startTime, finishTime2, instName, seatsTotal, seatsLeft, category,subCategory,status);
		CourseDao.addCourseToDatabases(course2);
		
				
		Calendar finishTime3 = DateUtility.getCurTimeInstance();		
		finishTime3.add(Calendar.MINUTE, 1);		
		Course course3 = new Course(p_Id, startTime, finishTime3, instName, seatsTotal, seatsLeft, category,subCategory,status);
		CourseDao.addCourseToDatabases(course3);
		
		CourseCleaner.clean();
		
		ArrayList<Course> clist = new ArrayList<Course>();
		clist = CourseDao.getCoursesFromPartner(p_Id);
		if(clist.size()==3&&clist.get(0).getStatus().code==Status.activated.code&&
				clist.get(1).getStatus().code==Status.deactivated.code&&
				clist.get(2).getStatus().code==Status.activated.code){
			//Passed;
		}else fail();
				
		
	}
}
