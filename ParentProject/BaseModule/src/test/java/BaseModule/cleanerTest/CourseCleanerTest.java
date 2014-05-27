package BaseModule.cleanerTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.clean.cleanTasks.CourseCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
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
		Calendar finishTime = DateUtility.getCurTimeInstance();		
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal = 50;
		int seatsLeft = 5;
		String category = "Physics";
		String subCategory = "sub-Phy";			
		int price = 124;
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		String location = "China";
		String city = "NanJing";
		String district = "JiangNing";
		String reference2 = "testr";
		course.setLocation(location);
		course.setCity(city);
		course.setDistrict(district);
		course.setReference(reference2);
		CourseDao.addCourseToDatabases(course);
		
		
		Calendar finishTime2 = DateUtility.getCurTimeInstance();		
		finishTime2.add(Calendar.DAY_OF_YEAR, -1);			
		Course course2 = new Course(p_Id, startTime, finishTime2,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		course2.setLocation(location);
		course2.setCity(city);
		course2.setDistrict(district);
		course2.setReference(reference2);
		CourseDao.addCourseToDatabases(course2);
		
						
		Calendar finishTime3 = DateUtility.getCurTimeInstance();		
		finishTime3.add(Calendar.MINUTE, 1);
		Course course3 = new Course(p_Id, startTime, finishTime3,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		String location2= "China";
		String city2 = "ChengDu";
		String district2 = "ChengHua";
		String reference3 = "Tse";
		course3.setLocation(location2);
		course3.setCity(city2);
		course3.setDistrict(district2);
		course3.setReference(reference3);
		CourseDao.addCourseToDatabases(course3);		
	
		CourseCleaner.clean();		
		
		ArrayList<Course> clist = new ArrayList<Course>();
		clist = CourseDao.getCoursesFromPartner(p_Id);
		if(clist.size()==3&&clist.get(0).getStatus().code==AccountStatus.activated.code&&
				clist.get(1).getStatus().code==AccountStatus.deactivated.code&&
				clist.get(2).getStatus().code==AccountStatus.activated.code){
			//Passed;
		}else fail();
				
		
	}
}
