package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.booking.BookingNotFoundException;
import BaseModule.exception.course.CourseNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Booking;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.User;

public class BookingDaoTest {

	@Test
	public void testCreate() throws ValidationException, CourseNotFoundException{
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
		Partner partner = new Partner(pname, instName,licence, organizationNum,reference, ppassword, phone,status);
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
		String title = "New Y";
		String courseInfo = "khopyhk";
		Course course = new Course(p_Id, startTime, finishTime, seatsTotal, seatsLeft, category,subCategory,status,price,title,courseInfo);
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
		try{
			BookingDao.addBookingToDatabases(booking);			
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGet() throws CourseNotFoundException, ValidationException, BookingNotFoundException{
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
		Partner partner = new Partner(pname, instName,licence, organizationNum,reference, ppassword, phone,status);
		PartnerDao.addPartnerToDatabases(partner);
		
		String pname2 = "XD";
		String licence2 = "234fdsfsd-dsv,.!@";
		String organizationNum2 = "124361234";
		String reference2 = "dsfsdfdsr";
		String ppassword2 = "sdf4r";
		String phone2 = "12351";		
		Partner partner2 = new Partner(pname2, instName,licence2, organizationNum2,reference2, ppassword2, phone2,status);
		PartnerDao.addPartnerToDatabases(partner2);
		
		int p_Id = partner.getPartnerId();
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal = 50;
		int seatsLeft = 5;
		int price = 12000;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		String title = "sdfdsfi8";
		String courseInfo = "thkorhk";
		Course course = new Course(p_Id, startTime, finishTime,seatsTotal, seatsLeft, category,subCategory,status,price,title,courseInfo);
		CourseDao.addCourseToDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
		String location = "China";
		String city = "NanJing";
		String district = "JiangNing";		
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
		booking = BookingDao.getBookingById(booking.getBookingId());
		
		String category2 = "English";
		String subCategory2 = "sub-En";	
		int price2 = 20000;				
		Course course2 = new Course(p_Id, startTime, finishTime, seatsTotal, seatsLeft, category2,subCategory2,status,price2,title,courseInfo);
		CourseDao.addCourseToDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());				
		course2.setLocation(location);
		course2.setCity(city);
		course2.setDistrict(district);
		course2.setReference(reference2);
		course2.setBackgroundUrl("routea.ca");
		course2.setTeacherImgUrl("baidu.ca");
		course2.setTeachingMaterial("Hands and Feet");
		course2.setTitle("blablabla");
		course2.setTeacherInfo("Teach you how kick your ass!");
		course2.setPrice(price2);
		CourseDao.updateCourseInDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());	
		int course2Id = course2.getCourseId();
		Booking booking2 = new Booking(timeStamp,course2.getStartTime(), course2.getFinishTime(), course2.getPrice(), userId, partnerId, course2Id, user.getName(), partner2.getPhone(),partner2.getReference(),status);
		BookingDao.addBookingToDatabases(booking2);
		booking2 = BookingDao.getBookingById(booking2.getBookingId());
		
		Booking test = BookingDao.getBookingById(booking.getBookingId());
		if(test.equals(booking)){
			//Passed;
		}else fail();
		
		test = BookingDao.getBookingByReference(booking2.getReference());
		if(test.equals(booking2)){
			//Passed;
		}else fail();
		
		ArrayList<Booking> blist = new ArrayList<Booking>();
		blist = BookingDao.getAllBookings();
		if(blist.size()==2 && blist.get(0).equals(booking)&& blist.get(1).equals(booking2)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpdate() throws CourseNotFoundException, ValidationException, BookingNotFoundException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String userphone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(name, userphone, password,status);
		UserDao.addUserToDatabase(user);
		
		String pname = "XDF";
		String instName = "Tsetingfeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String ppassword = "sdf234r";
		String phone = "123545451";		
		Partner partner = new Partner(pname, instName,licence, organizationNum,reference, ppassword, phone,status);
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
		String title = "dsf";
		String courseInfo = "thgktrjhti";
		Course course = new Course(p_Id, startTime, finishTime, seatsTotal, seatsLeft, category,subCategory,status,price,title,courseInfo);
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
		try{
			BookingDao.addBookingToDatabases(booking);			
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		booking = BookingDao.getBookingById(booking.getBookingId());
		booking.setStatus(AccountStatus.deactivated);
		booking.setPhone("18502877744");
		BookingDao.updateBookingInDatabases(booking);
		Booking test = BookingDao.getBookingById(booking.getBookingId());
		if(test.equals(booking)&&test.getPhone().equals("18502877744")){
			//Passed;
		}else fail();
	}
}
