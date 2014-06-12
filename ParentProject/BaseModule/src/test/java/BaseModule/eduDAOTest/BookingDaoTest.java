package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.model.Booking;
import BaseModule.model.Coupon;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.User;
import BaseModule.model.representation.BookingSearchRepresentation;

public class BookingDaoTest {

	@Test
	public void testCreate() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String userphone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhan@hotmail.com";			
		User user = new User(userphone, password,status);
		user.setName(name);
		user.setEmail(email);		
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
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal, seatsLeft,status,category,subCategory,phone);
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
		course.setClassroomImgUrl("www.hotmail.com");
		course.setTeacherImgUrl("www.google.ca");
		course.setTeachingMethodsIntro("Hand and Ass");
		course.setCourseName("bababa");
		course.setTeacherIntro("sdfkljrghiuoghrer");		
		course.setPrice(price);
		CourseDao.updateCourseInDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());			
		
		int userId = user.getUserId();
		int partnerId = partner.getPartnerId();
		int courseId = course.getCourseId();		
		Calendar timeStamp = DateUtility.getCurTimeInstance();
		String email2 = "xiongchuhanplace@hotmail.com";		
		int cashbackAmount = 50;
		
		//50
		Coupon c = new Coupon(userId,cashbackAmount);	
		Calendar expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.DAY_OF_MONTH, 1);
		c.setExpireTime(expireTime);
		CouponDao.addCouponToDatabases(c);
		
		//70
		Coupon c2 = new Coupon(userId,cashbackAmount + 20);		
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.DAY_OF_MONTH, 15);
		c2.setExpireTime(expireTime);
		CouponDao.addCouponToDatabases(c2);
		
		//100
		Coupon c3 = new Coupon(userId,cashbackAmount + 50);		
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.DAY_OF_MONTH, -15);		
		c3.setExpireTime(expireTime);
		CouponDao.addCouponToDatabases(c3);
		
		//66
		Coupon c4 = new Coupon(userId,cashbackAmount + 11);		
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.MINUTE, -15);
		c4.setExpireTime(expireTime);
		CouponDao.addCouponToDatabases(c4);
		
		//89
		Coupon c5 = new Coupon(userId,cashbackAmount + 39);		
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.MINUTE, 1);
		c5.setExpireTime(expireTime);
		CouponDao.addCouponToDatabases(c5);
		
		//1
		Coupon c6 = new Coupon(userId,cashbackAmount - 49);		
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.DAY_OF_YEAR, 2);
		c6.setExpireTime(expireTime);
		CouponDao.addCouponToDatabases(c6);
		
		//100
		Coupon c7 = new Coupon(userId,cashbackAmount + 50);		
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.DAY_OF_MONTH, -15);
		c7.setExpireTime(expireTime);
		c7.setStatus(CouponStatus.expired);
		CouponDao.addCouponToDatabases(c7);
				
		//66
		Coupon c8 = new Coupon(userId,cashbackAmount + 11);		
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.MINUTE, -15);
		c8.setExpireTime(expireTime);
		c8.setStatus(CouponStatus.used);
		CouponDao.addCouponToDatabases(c8);
				
		//89
		Coupon c9 = new Coupon(userId,cashbackAmount + 39);		
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.MINUTE, 1);
		c9.setExpireTime(expireTime);
		c9.setStatus(CouponStatus.expired);
		CouponDao.addCouponToDatabases(c9);
				
		//2
		Coupon c10 = new Coupon(userId,cashbackAmount - 48);		
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.DAY_OF_YEAR, 2);
		c10.setExpireTime(expireTime);
		c10.setStatus(CouponStatus.usable);
		CouponDao.addCouponToDatabases(c10);
		
		UserDao.updateUserBCC(0, 0, 212, userId);
		
		int backcash = 211;//剩一块
		
		Booking booking = new Booking(timeStamp,timeStamp,course.getPrice(),
				userId, partnerId, courseId, user.getName(), partner.getPhone(),
				email2,partner.getReference(),BookingStatus.confirmed,backcash);
		try{
			BookingDao.addBookingToDatabases(booking);			
		}catch(Exception e){	
			e.printStackTrace();
			fail();
		}
		
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		clist = CouponDao.getCouponByUserId(userId);
		user = UserDao.getUserById(userId);
		booking = BookingDao.getBookingById(booking.getBookingId());
		if(clist.size()==10&&
				clist.get(0).getOriginalAmount()==cashbackAmount&&clist.get(0).getAmount()==cashbackAmount&&clist.get(0).getStatus().code==CouponStatus.used.code&&
				clist.get(1).getOriginalAmount()==cashbackAmount+20&&clist.get(1).getAmount()==1&&clist.get(1).getStatus().code==CouponStatus.usable.code&&
				clist.get(2).getOriginalAmount()==cashbackAmount+50&&clist.get(2).getAmount()==cashbackAmount+50&&clist.get(2).getStatus().code==CouponStatus.usable.code&&
				clist.get(3).getOriginalAmount()==cashbackAmount+11&&clist.get(3).getAmount()==cashbackAmount+11&&clist.get(3).getStatus().code==CouponStatus.usable.code&&
				clist.get(4).getOriginalAmount()==cashbackAmount+39&&clist.get(4).getAmount()==cashbackAmount+39&&clist.get(4).getStatus().code==CouponStatus.used.code&&
				clist.get(5).getOriginalAmount()==1&&clist.get(5).getAmount()==1&&clist.get(5).getStatus().code==CouponStatus.used.code&&
				clist.get(6).getOriginalAmount()==cashbackAmount+50&&clist.get(6).getAmount()==cashbackAmount+50&&clist.get(6).getStatus().code==CouponStatus.expired.code&&
				clist.get(7).getOriginalAmount()==cashbackAmount+11&&clist.get(7).getAmount()==cashbackAmount+11&&clist.get(7).getStatus().code==CouponStatus.used.code&&
				clist.get(8).getOriginalAmount()==cashbackAmount+39&&clist.get(8).getAmount()==cashbackAmount+39&&clist.get(8).getStatus().code==CouponStatus.expired.code&&
				clist.get(9).getOriginalAmount()==2&&clist.get(9).getAmount()==2&&clist.get(9).getStatus().code==CouponStatus.used.code&&
				user.getCoupon()==1&&booking.getCashbackAmount()==backcash){
			//Passed;
		}else fail();
		
		
	}
	
	@Test
	public void testGet() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String userphone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;		
		User user = new User(userphone, password,status);
		user.setName(name);
		user.setEmail("xiongchuhanplace@hotmail.com");
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
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal, seatsLeft,status,category,subCategory,phone);
		CourseDao.addCourseToDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
		String location = "China";
		String city = "NanJing";
		String district = "JiangNing";		
		course.setLocation(location);
		course.setCity(city);
		course.setDistrict(district);
		course.setReference(reference2);
		course.setClassroomImgUrl("www.hotmail.com");
		course.setTeacherImgUrl("www.google.ca");
		course.setTeachingMethodsIntro("Hand and Ass");
		course.setCourseName("bababa");
		course.setTeacherIntro("sdfkljrghiuoghrer");		
		course.setPrice(price);
		CourseDao.updateCourseInDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());	
		
		
		int userId = user.getUserId();
		int partnerId = partner.getPartnerId();
		int courseId = course.getCourseId();		
		Calendar timeStamp = DateUtility.getCurTimeInstance();
		String email = "xiongchuhanplace@hotmail.com";
		int cashbackAmount = 50;		
		Booking booking = new Booking(timeStamp,timeStamp, 
				course.getPrice(), userId, partnerId, courseId,
				user.getName(), partner.getPhone(),
				email,partner.getReference(),BookingStatus.awaiting,cashbackAmount);
		BookingDao.addBookingToDatabases(booking);
		booking = BookingDao.getBookingById(booking.getBookingId());
		
		String category2 = "English";
		String subCategory2 = "sub-En";	
		int price2 = 20000;				
		Course course2 = new Course(p_Id, startTime, finishTime,price2,seatsTotal, seatsLeft,status,category2,subCategory2,phone);
		CourseDao.addCourseToDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());				
		course2.setLocation(location);
		course2.setCity(city);
		course2.setDistrict(district);
		course2.setReference(reference2);
		course2.setClassroomImgUrl("routea.ca");
		course2.setTeacherImgUrl("baidu.ca");
		course2.setTeachingMethodsIntro("Hands and Feet");
		course2.setCourseName("blablabla");
		course2.setTeacherIntro("Teach you how kick your ass!");
		course2.setPrice(price2);
		CourseDao.updateCourseInDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());	
		int course2Id = course2.getCourseId();		
		Booking booking2 = new Booking(timeStamp,timeStamp, 
				course2.getPrice(),userId, partnerId, 
				course2Id, user.getName(), partner2.getPhone(),
				email,partner2.getReference(),BookingStatus.awaiting,cashbackAmount);
		BookingDao.addBookingToDatabases(booking2);
		booking2 = BookingDao.getBookingById(booking2.getBookingId());
		
		Booking test = BookingDao.getBookingById(booking.getBookingId());
		if(test.equals(booking)){
			//Passed;
		}else fail();		
		
		ArrayList<Booking> blist = new ArrayList<Booking>();
		blist = BookingDao.searchBooking(new BookingSearchRepresentation());
		if(blist.size()==2 && blist.get(0).equals(booking)&& blist.get(1).equals(booking2)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpdate() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String userphone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(userphone, password,status);
		user.setName(name);
		user.setEmail("xiongchuhan@uwaterloo.ca");
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
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal, seatsLeft,status,category,subCategory,phone);
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
		course.setClassroomImgUrl("www.hotmail.com");
		course.setTeacherImgUrl("www.google.ca");
		course.setTeachingMethodsIntro("Hand and Ass");
		course.setCourseName("bababa");
		course.setTeacherIntro("sdfkljrghiuoghrer");		
		course.setPrice(price);
		CourseDao.updateCourseInDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());	
		
		
		int userId = user.getUserId();
		int partnerId = partner.getPartnerId();
		int courseId = course.getCourseId();		
		Calendar timeStamp = DateUtility.getCurTimeInstance();
		String email = "xiongchuhanplace@hotmail.com";
		int cashbackAmount = 50;		
		Booking booking = new Booking(timeStamp,timeStamp, 
				course.getPrice(), userId, partnerId, courseId,
				user.getName(), partner.getPhone(),
				email,partner.getReference(),BookingStatus.awaiting,cashbackAmount);
		try{
			BookingDao.addBookingToDatabases(booking);			
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		booking = BookingDao.getBookingById(booking.getBookingId());
		booking.setStatus(BookingStatus.finished);
		booking.setPhone("18502877744");
		BookingDao.updateBookingInDatabases(booking);
		Booking test = BookingDao.getBookingById(booking.getBookingId());
		if(test.equals(booking)&&test.getPhone().equals("18502877744")){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testSearch() throws SQLException, PseudoException{
		//User
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String userphone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(userphone, password,status);
		user.setName(name);
		user.setEmail("349@hotmail.com");
		UserDao.addUserToDatabase(user);
		
		String name2 = "Fang";
		String userphone2 = "123312";
		String password2 = "36knal";		
		User user2 = new User(userphone2, password2,status);
		user2.setName(name2);
		user2.setEmail("kebi@hotmail.com");
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
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal, seatsLeft,status,category,subCategory,phone);
		CourseDao.addCourseToDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
		String location = "China";
		String city = "NanJing";
		String district = "JiangNing";		
		course.setLocation(location);
		course.setCity(city);
		course.setDistrict(district);
		course.setReference(reference2);
		course.setClassroomImgUrl("www.hotmail.com");
		course.setTeacherImgUrl("www.google.ca");
		course.setTeachingMethodsIntro("Hand and Ass");
		course.setCourseName("bababa");
		course.setTeacherIntro("sdfkljrghiuoghrer");				
		CourseDao.updateCourseInDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());			
		
		Calendar startTime2 = DateUtility.getCurTimeInstance();
		startTime2.add(Calendar.DAY_OF_YEAR, -2);
		Calendar finishTime2 = DateUtility.getCurTimeInstance();
		finishTime2.add(Calendar.DAY_OF_YEAR, 7);	
		String category2 = "English";
		String subCategory2 = "sub-En";			
		int price2 = 20000;				
		Course course2 = new Course(p_Id, startTime2, finishTime2,price2,seatsTotal,seatsLeft,status,category2,subCategory2,phone);
		CourseDao.addCourseToDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());				
		course2.setLocation(location);
		course2.setCity(city);
		course2.setDistrict(district);
		course2.setReference(reference2);
		course2.setClassroomImgUrl("routea.ca");
		course2.setTeacherImgUrl("baidu.ca");
		course2.setTeachingMethodsIntro("Hands and Feet");
		course2.setCourseName("blablabla");
		course2.setTeacherIntro("Teach you how kick your ass!");		
		CourseDao.updateCourseInDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());
		
		int p_Id2 = partner.getPartnerId();
		Calendar startTime3 = DateUtility.getCurTimeInstance();
		Calendar finishTime3 = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 15);				
		int price3 = 2000;	
		Course course3 = new Course(p_Id2, startTime3, finishTime3,price3,seatsTotal, seatsLeft,status,category,subCategory,phone);
		CourseDao.addCourseToDatabases(course3);
		course3 = CourseDao.getCourseById(course3.getCourseId());			
		course3.setLocation(location);
		course3.setCity(city);
		course3.setDistrict(district);
		course3.setReference(reference2);
		course3.setClassroomImgUrl("www.hotmail.com");
		course3.setTeacherImgUrl("www.google.ca");
		course3.setTeachingMethodsIntro("Hand and Ass");
		course3.setCourseName("bababa");
		course3.setTeacherIntro("sdfkljrghiuoghrer");		
		CourseDao.updateCourseInDatabases(course3);
		course3 = CourseDao.getCourseById(course3.getCourseId());			
		
		Calendar startTime4 = DateUtility.getCurTimeInstance();
		startTime4.add(Calendar.DAY_OF_YEAR, -14);
		Calendar finishTime4 = DateUtility.getCurTimeInstance();
		finishTime2.add(Calendar.DAY_OF_YEAR, 5);			
		int price4 = 10000;		
		Course course4 = new Course(p_Id2, startTime4, finishTime4,price4,seatsTotal,seatsLeft,status,category2,subCategory2,phone);
		CourseDao.addCourseToDatabases(course4);
		course4 = CourseDao.getCourseById(course4.getCourseId());				
		course4.setLocation(location);
		course4.setCity(city);
		course4.setDistrict(district);
		course4.setReference(reference2);
		course4.setClassroomImgUrl("routea.ca");
		course4.setTeacherImgUrl("baidu.ca");
		course4.setTeachingMethodsIntro("Hands and Feet");
		course4.setCourseName("blablabla");
		course4.setTeacherIntro("Teach you how kick your ass!");	
		CourseDao.updateCourseInDatabases(course4);
		course4 = CourseDao.getCourseById(course4.getCourseId());
		
		//Booking
		
		//Booking I: user, partner, course
		int userId = user.getUserId();
		int partnerId = partner.getPartnerId();
		int courseId = course.getCourseId();		
		Calendar timeStamp0 = DateUtility.getCurTimeInstance();
		timeStamp0.add(Calendar.DAY_OF_MONTH, 15);
		Calendar timeStamp = DateUtility.getCurTimeInstance();
		String email = "xiongchuhanplace@hotmail.com";
		int cashbackAmount = 50;		
		Booking booking = new Booking(timeStamp0,timeStamp, 
				2000, userId, partnerId, courseId, user.getName(), partner.getPhone(),
				email,partner.getReference(),BookingStatus.awaiting,cashbackAmount);
		BookingDao.addBookingToDatabases(booking);		
		booking = BookingDao.getBookingById(booking.getBookingId());
		
		//Booking II: user, partner, course2
		int course2Id = course2.getCourseId();			
		Booking booking2 = new Booking(timeStamp,timeStamp,
				5000, userId, partnerId, course2Id, user.getName(), partner2.getPhone(),
				email,partner2.getReference(),BookingStatus.awaiting,cashbackAmount);
		BookingDao.addBookingToDatabases(booking2);
		booking2 = BookingDao.getBookingById(booking2.getBookingId());
		
		//Booking III: user, partner2, course3
		int partner2Id = partner2.getPartnerId();
		int course3Id = course3.getCourseId();		
		Booking booking3 = new Booking(timeStamp,timeStamp, 
				10000, userId, partner2Id, course3Id, user.getName(), partner2.getPhone(),
				email,partner2.getReference(),BookingStatus.awaiting,cashbackAmount);
		BookingDao.addBookingToDatabases(booking3);
		booking3 = BookingDao.getBookingById(booking3.getBookingId());
		
		//Booking IV: user2, partner2, course3		
		int user2Id = user2.getUserId();			
		Booking booking4 = new Booking(timeStamp,timeStamp,
				15000, user2Id, partner2Id, course3Id, user2.getName(), partner2.getPhone(),
				email,partner2.getReference(),BookingStatus.enter,cashbackAmount);
		BookingDao.addBookingToDatabases(booking4);
		booking4 = BookingDao.getBookingById(booking4.getBookingId());
		
		//Booking V: user2, partner2, course4	
		int course4Id = course4.getCourseId();	
		Booking booking5 = new Booking(timeStamp,timeStamp,
				20000, user2Id, partner2Id, course4Id, user2.getName(), partner2.getPhone(),
				email,partner2.getReference(),BookingStatus.enter,cashbackAmount);
		booking5.setPreStatus(BookingStatus.delivered);
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
		
		sr.setScheduledTime(timeStamp0);
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
		
		BookingSearchRepresentation sr4 = new BookingSearchRepresentation();		
		sr4.setStatus(BookingStatus.enter);
		sr4.setPreStatus(BookingStatus.delivered);
		blist = BookingDao.searchBooking(sr4);
		if(blist.size()==1 && blist.get(0).equals(booking5)){
			//Passed;
		}else fail();
	}
	
}
