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
import BaseModule.model.representation.BookingSearchRepresentation;

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
		Partner partner2 = new Partner(pname2, instName+"2",licence2, organizationNum2,reference2, ppassword2, phone2,status);
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
	
	@Test
	public void testSearch() throws ValidationException, CourseNotFoundException, BookingNotFoundException{
		//User
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String userphone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(name, userphone, password,status);
		UserDao.addUserToDatabase(user);
		
		String name2 = "Fang";
		String userphone2 = "123312";
		String password2 = "36knal";		
		User user2 = new User(name2, userphone2, password2,status);
		UserDao.addUserToDatabase(user2);
		
		//Partner
		String pname = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String ppassword = "sdf234r";
		String phone = "123545451";		
		Partner partner = new Partner(pname, instName,licence, organizationNum,reference, ppassword, phone,status);
		partner = PartnerDao.addPartnerToDatabases(partner);
		
		String pname2 = "XD";
		String licence2 = "234fdsfsd-dsv,.!@";
		String organizationNum2 = "124361234";
		String reference2 = "dsfsdfdsr";
		String ppassword2 = "sdf4r";
		String phone2 = "12351";		
		Partner partner2 = new Partner(pname2, instName+"dsf",licence2, organizationNum2,reference2, ppassword2, phone2,status);
		partner2 = PartnerDao.addPartnerToDatabases(partner2);
		
		//Course
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
		CourseDao.updateCourseInDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());			
		
		Calendar startTime2 = DateUtility.getCurTimeInstance();
		startTime2.add(Calendar.DAY_OF_YEAR, -2);
		Calendar finishTime2 = DateUtility.getCurTimeInstance();
		finishTime2.add(Calendar.DAY_OF_YEAR, 7);	
		String category2 = "English";
		String subCategory2 = "sub-En";			
		int price2 = 20000;				
		Course course2 = new Course(p_Id, startTime2, finishTime2, seatsTotal, seatsLeft, category2,subCategory2,status,price2,title,courseInfo);
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
		CourseDao.updateCourseInDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());
		
		int p_Id2 = partner.getPartnerId();
		Calendar startTime3 = DateUtility.getCurTimeInstance();
		Calendar finishTime3 = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 15);				
		int price3 = 2000;	
		Course course3 = new Course(p_Id2, startTime3, finishTime3,seatsTotal, seatsLeft, category,subCategory,status,price3,title,courseInfo);
		CourseDao.addCourseToDatabases(course3);
		course3 = CourseDao.getCourseById(course3.getCourseId());			
		course3.setLocation(location);
		course3.setCity(city);
		course3.setDistrict(district);
		course3.setReference(reference2);
		course3.setBackgroundUrl("www.hotmail.com");
		course3.setTeacherImgUrl("www.google.ca");
		course3.setTeachingMaterial("Hand and Ass");
		course3.setTitle("bababa");
		course3.setTeacherInfo("sdfkljrghiuoghrer");		
		CourseDao.updateCourseInDatabases(course3);
		course3 = CourseDao.getCourseById(course3.getCourseId());			
		
		Calendar startTime4 = DateUtility.getCurTimeInstance();
		startTime4.add(Calendar.DAY_OF_YEAR, -14);
		Calendar finishTime4 = DateUtility.getCurTimeInstance();
		finishTime2.add(Calendar.DAY_OF_YEAR, 5);			
		int price4 = 10000;				
		Course course4 = new Course(p_Id2, startTime4, finishTime4, seatsTotal, seatsLeft, category2,subCategory2,status,price4,title,courseInfo);
		CourseDao.addCourseToDatabases(course4);
		course4 = CourseDao.getCourseById(course4.getCourseId());				
		course4.setLocation(location);
		course4.setCity(city);
		course4.setDistrict(district);
		course4.setReference(reference2);
		course4.setBackgroundUrl("routea.ca");
		course4.setTeacherImgUrl("baidu.ca");
		course4.setTeachingMaterial("Hands and Feet");
		course4.setTitle("blablabla");
		course4.setTeacherInfo("Teach you how kick your ass!");	
		CourseDao.updateCourseInDatabases(course4);
		course4 = CourseDao.getCourseById(course4.getCourseId());
		
		//Booking
		
		//Booking I: user, partner, course
		int userId = user.getUserId();
		int partnerId = partner.getPartnerId();
		int courseId = course.getCourseId();		
		Calendar timeStamp = DateUtility.getCurTimeInstance();
		Booking booking = new Booking(timeStamp,course.getStartTime(), course.getFinishTime(), 2000, userId, partnerId, courseId, user.getName(), partner.getPhone(),partner.getReference(),status);
		BookingDao.addBookingToDatabases(booking);		
		booking = BookingDao.getBookingById(booking.getBookingId());
		
		//Booking II: user, partner, course2
		int course2Id = course2.getCourseId();
		Booking booking2 = new Booking(timeStamp,course2.getStartTime(), course2.getFinishTime(), 5000, userId, partnerId, course2Id, user.getName(), partner2.getPhone(),partner2.getReference(),status);
		BookingDao.addBookingToDatabases(booking2);
		booking2 = BookingDao.getBookingById(booking2.getBookingId());
		
		//Booking III: user, partner2, course3
		int partner2Id = partner2.getPartnerId();
		int course3Id = course3.getCourseId();
		Booking booking3 = new Booking(timeStamp,course3.getStartTime(), course3.getFinishTime(), 10000, userId, partner2Id, course3Id, user.getName(), partner2.getPhone(),partner2.getReference(),status);
		BookingDao.addBookingToDatabases(booking3);
		booking3 = BookingDao.getBookingById(booking3.getBookingId());
		
		//Booking IV: user2, partner2, course3		
		int user2Id = user2.getUserId();
		Booking booking4 = new Booking(timeStamp,course3.getStartTime(), course3.getFinishTime(), 15000, user2Id, partner2Id, course3Id, user2.getName(), partner2.getPhone(),partner2.getReference(),status);
		BookingDao.addBookingToDatabases(booking4);
		booking4 = BookingDao.getBookingById(booking4.getBookingId());
		
		//Booking V: user2, partner2, course4	
		int course4Id = course4.getCourseId();
		Booking booking5 = new Booking(timeStamp,course4.getStartTime(), course4.getFinishTime(), 20000, user2Id, partner2Id, course4Id, user2.getName(), partner2.getPhone(),partner2.getReference(),status);
		BookingDao.addBookingToDatabases(booking5);
		booking5 = BookingDao.getBookingById(booking5.getBookingId());
		
		ArrayList<Booking> blist = new ArrayList<Booking>();
		Calendar finishTimeTest = DateUtility.getCurTimeInstance();
		finishTimeTest.add(Calendar.DAY_OF_YEAR, 8);
		
		BookingSearchRepresentation sr = new BookingSearchRepresentation();
		sr.setUserId(userId);
		sr.setPartnerId(partnerId);
		sr.setStartPrice(2000);
		sr.setFinishPrice(4000);
		blist = BookingDao.searchBooking(sr);
		if(blist.size()==1 && blist.get(0).equals(booking)){
			//Passed;
		}else fail();
		
		sr.setStartTime(startTime);
		sr.setFinishTime(finishTimeTest);
		blist = BookingDao.searchBooking(sr);
		if(blist.size()==1 && blist.get(0).equals(booking)){
			//Passed;
		}else fail();
		
		BookingSearchRepresentation sr2 = new BookingSearchRepresentation();
		sr2.setUserId(user2Id);
		sr2.setPartnerId(partner2Id);
		sr2.setStartPrice(2000);
		sr2.setFinishPrice(8000);
		blist = BookingDao.searchBooking(sr2);
		if(blist.size()==0){
			//Passed;
		}else fail();
		
		sr2.setStartPrice(15000);
		sr2.setFinishPrice(16000);
		
		blist = BookingDao.searchBooking(sr2);
		if(blist.size()==1 && blist.get(0).equals(booking4)){
			//Passed;
		}else fail();
		
		BookingSearchRepresentation sr3 = new BookingSearchRepresentation();
		sr3.setCourseId(course3Id);
		sr3.setPartnerId(partner2Id);
		sr3.setName(user.getName());
		sr3.setStartPrice(10000);
		sr3.setFinishPrice(13000);
		blist = BookingDao.searchBooking(sr3);
		if(blist.size()==1 && blist.get(0).equals(booking3)){
			//Passed;
		}else fail();
	}
	
}
