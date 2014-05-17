package BaseModule.cleanerTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.clean.cleanTasks.BookingCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.course.CourseNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Booking;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.User;

public class BookingCleanerTest {

	@Test
	public void test() throws ValidationException, CourseNotFoundException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String userphone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(name, userphone, password,status);
		UserDao.addUserToDatabase(user);
		
		String pname = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String ppassword = "sdf234r";
		String phone = "123545451";		
		Partner partner = new Partner(pname,instName, licence, organizationNum,reference, ppassword, phone,status);
		PartnerDao.addPartnerToDatabases(partner);
		
		int p_Id = partner.getPartnerId();
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();		
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal = 50;
		int seatsLeft = 5;
		int price = 12000;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		Course course = new Course(p_Id, startTime, finishTime,seatsTotal, seatsLeft, category,subCategory,status);
		CourseDao.addCourseToDatabases(course);		
		course = CourseDao.getCourseById(course.getCourseId());	
		String location = "China";
		String city = "NanJing";
		String district = "JiangNing";
		String reference2 = "testr";
		course.setLocation(location);
		course.setCity(city);
		course.setDistrict(district);
		course.setReference(reference2);
		course.setBackgroundUrl("www.hotmail.com");
		course.setTeacherImgUrl("www.google.ca");
		course.setTeachingMaterial("Hand and Ass");
		course.setTitle("bababa");
		course.setTeacherInfo("sdfkljrghiuoghrer");		
		course.setPrice(price);		
		CourseDao.updateCourseInDatabases(course);		
		course = CourseDao.getCourseById(course.getCourseId());			
		
		int userId = user.getUserId();
		int partnerId = partner.getPartnerId();
		int courseId = course.getCourseId();		
		Calendar timeStamp = DateUtility.getCurTimeInstance();		
		Booking booking = new Booking(timeStamp,course.getStartTime(), course.getFinishTime(), course.getPrice(), userId, partnerId, courseId, user.getName(), partner.getPhone(),partner.getReference(),status);
		BookingDao.addBookingToDatabases(booking);
		
		Calendar finishTime2 = Calendar.getInstance();
		finishTime2.add(Calendar.DAY_OF_YEAR, -1);
		Booking booking2 = new Booking(timeStamp,course.getStartTime(), finishTime2, course.getPrice(), userId, partnerId, courseId, user.getName(), partner.getPhone(),partner.getReference(),status);
		BookingDao.addBookingToDatabases(booking2);
		
		Calendar finishTime3 = Calendar.getInstance();
		finishTime3.add(Calendar.HOUR_OF_DAY, 1);
		Booking booking3 = new Booking(timeStamp,course.getStartTime(), finishTime3, course.getPrice(), userId, partnerId, courseId, user.getName(), partner.getPhone(),partner.getReference(),status);
		BookingDao.addBookingToDatabases(booking3);		
		BookingCleaner.clean();
		
		ArrayList<Booking> list = new ArrayList<Booking>();
		list = BookingDao.getAllBookings();
		if(list.size()==3 && list.get(0).getStatus().code==AccountStatus.activated.code && 
				list.get(1).getStatus().code==AccountStatus.deactivated.code&&list.get(2).getStatus().code==AccountStatus.activated.code){
			//Passed;
		}else{
			fail();
		}
		
	}
}
