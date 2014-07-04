package BaseModule.validationTest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Booking;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.User;
import BaseModule.service.ValidationService;

public class ValidationTest {

	@Test
	public void testEmailFormat(){
		String myemail = "xiongchuhan@hotmail.com";
		if(ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "lifecentric.o2o@gmail.com";
		if(ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "lifecentri-c.o2o@gmail.com";
		if(ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "sdf";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "lifecentric.o2o@.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "@lifecentric.o2o@gmail.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "lifecentric.o2o@g.com";
		if(ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "lifecentric.o2ogmail.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "asdfsdfsdrewrfdgdfgergtertrewtretertretertertretertreter@hotmail.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "-1!@#$%#$%@hotmail.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "-1!#$%^&*()-+=qw2@hotmail.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "-1sdf..@hotmail.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "-1sdf.3@hotmail.com";
		if(ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "-1sdf.erw-3-3-1@hotmail.com";
		if(ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "-1sdf.erw-3-3-1&@hotmail.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "-1sdf.erw-3-3-1&(@hotmail.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "-1sdf.erw-3-3-1&#2@hotmail.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = ".sdf.erw-3-3-1@hotmail.com";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}
		
		myemail = "uwse@me.com";
		if(ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}	
		
		myemail = "sdfs=-@hot";
		if(!ValidationService.validateEmail(myemail)){
			//Passed;
		}else{
			fail();
		}	
	}
	
	@Test
	public void testPhoneFormat(){
		String myphone = "9892263974";
		if(ValidationService.validatePhone(myphone)){
			//Passed;
		}else{
			fail();
		}

		myphone = "sdf";
		if(!ValidationService.validatePhone(myphone)){
			//Passed;
		}else{
			fail();
		}

		myphone = "1sdf";
		if(!ValidationService.validatePhone(myphone)){
			//Passed;
		}else{
			fail();
		}

		myphone = "1@$sdf";
		if(!ValidationService.validatePhone(myphone)){
			//Passed;
		}else{
			fail();
		}
	}

	@Test
	public void testNameFormat(){
		String name = "熊处寒";
		if(ValidationService.validateName(name)){
			//Passed;
		}else{
			fail();
		}

		name="12345#$45";
		if(!ValidationService.validateName(name)){
			//Passed;
		}else{
			fail();
		}

		name="";
		if(!ValidationService.validateName(name)){
			//Passed;
		}else{
			fail();
		}

		name="1";
		if(!ValidationService.validateName(name)){
			//Passed;
		}else{
			fail();
		}

		name="いっぽん";
		if(!ValidationService.validateName(name)){
			//Passed;
		}else{
			fail();
		}

		name="马修羊isYangChunkai";
		if(ValidationService.validateName(name)){
			//Passed;
		}else{
			fail();
		}

		name="yck马xch熊";
		if(ValidationService.validateName(name)){
			//Passed;
		}else{
			fail();
		}

		name="Harry Xiong";
		if(ValidationService.validateName(name)){
			//Passed;
		}else{
			fail();
		}

		name="Harry X iong";
		if(!ValidationService.validateName(name)){
			//Passed;
		}else{
			fail();
		}			
	}


	@Test
	public void testPasswordFormat(){
		String pw="12345";
		if(!ValidationService.validatePassword(pw)){
			//Passed;
		}else{
			fail();
		}

		pw="1234512345123451234512345123451";
		if(!ValidationService.validatePassword(pw)){
			//Passed;
		}else{
			fail();
		}

		pw="qAD#$1234";
		if(ValidationService.validatePassword(pw)){
			//Passed;
		}else{
			fail();
		}
		pw="ycg1990$";
		if(ValidationService.validatePassword(pw)){
			//Passed;
		}else{
			fail();
		}

		pw="qAD#$123我4";
		if(!ValidationService.validatePassword(pw)){
			//Passed;
		}else{
			fail();
		}

		pw="qAD#$123い4";
		if(!ValidationService.validatePassword(pw)){
			//Passed;
		}else{
			fail();
		}
	}	

	@Test
	public void testUserValidation(){
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		User user = new User(phone, password, "", "","1",status);
		user.setName(name);
		user.setEmail(email);
		try{
			ValidationService.validateUser(user);
		}catch(ValidationException e){
			fail();
		}catch(Exception e){
			fail();
		}
		boolean fail = true;
		user = new User(phone, password, "", "","1",status);
		user.setName(name+"1");
		user.setEmail(email);
		try{
			ValidationService.validateUser(user);
		}catch(ValidationException e){
			//Pass;
			fail = false;
		}catch(Exception e){
			fail();
		}

		if(fail) fail();

		fail = true;
		user = new User("d12", password, "", "","1",status);
		user.setName(name);
		user.setEmail(email);
		try{
			ValidationService.validateUser(user);
		}catch(ValidationException e){
			//Pass;
			fail = false;
		}catch(Exception e){
			fail();
		}		
		if(fail) fail();
		
		fail = true;
		user = new User(phone, password, "", "","1",status);
		user.setName(name);
		user.setEmail("sdfs=-@hot");
		try{
			ValidationService.validateUser(user);
		}catch(ValidationException e){
			//Pass;
			fail = false;
		}catch(Exception e){
			fail();
		}		
		if(fail) fail();
	}

	@Test
	public void testPartnerValidation(){
		EduDaoBasic.clearAllDatabase();
		String name = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";
		String phone = "123545451";
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name, instName,licence, organizationNum,reference, password, phone,status);
		try{
			ValidationService.validatePartner(partner);
		}catch(ValidationException e){
			fail();
		}catch(Exception e){
			fail();
		}

		boolean fail = true;
		partner = new Partner(name,"",licence,organizationNum,reference,password,phone,status);
		try{
			ValidationService.validatePartner(partner);
		}catch(ValidationException e){
			//Pass;
			fail = false;
		}catch(Exception e){
			fail();
		}		
		if(fail) fail();

		fail = true;
		partner = new Partner(name,instName,licence,organizationNum,"",password,phone,status);
		try{
			ValidationService.validatePartner(partner);
		}catch(ValidationException e){
			//Pass;
			fail = false;
		}catch(Exception e){
			fail();
		}		
		if(fail) fail();

		fail = true;
		partner = new Partner(name,instName,licence,"",reference,password,phone,status);
		try{
			ValidationService.validatePartner(partner);
		}catch(ValidationException e){
			//Pass;
			fail = false;
		}catch(Exception e){
			fail();
		}		
		if(fail) fail();

		fail = true;
		partner = new Partner(name,instName,"",organizationNum,reference,password,phone,status);
		try{
			ValidationService.validatePartner(partner);
		}catch(ValidationException e){
			//Pass;
			fail = false;
		}catch(Exception e){
			fail();
		}		
		if(fail) fail();		
	}

	@Test
	public void testCoruseValidation(){
		EduDaoBasic.clearAllDatabase();
		int p_Id = 1;
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int classSize = 50;
		int popularity = 5;
		int price = 1000;	
		String category = "Physics";
		String subCategory = "sub-Phy";			
		String phone = "12344545";
		Course course = new Course(p_Id, startTime, finishTime,price,classSize, popularity,category,subCategory,phone);
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setCourseName("sdlkjoeghjeior");
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(Exception e){
			fail();
		}

		boolean fail = true;
		course = new Course(-1, startTime, finishTime,price,classSize, popularity,category,subCategory,phone);
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		course =new Course(p_Id, startTime, startTime,price,classSize, popularity,category,subCategory,phone);
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		course = new Course(p_Id, startTime, finishTime,price,classSize, classSize+1,category,subCategory,phone);
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		course = new Course(p_Id, startTime, finishTime,price,-1, -2,category,subCategory,phone);
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		course =new Course(p_Id, startTime, finishTime,price,classSize, popularity,"",subCategory,phone);
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		course = new Course(p_Id, startTime, finishTime,-1,classSize, popularity,category,subCategory,phone);
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();		

		fail = true;
		course = new Course(p_Id, startTime, finishTime,price,classSize, popularity,category,"",phone);
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		course = new Course(p_Id, null, finishTime,price,classSize, popularity,category,subCategory,phone);
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		course = new Course(p_Id, startTime, finishTime,price,classSize, popularity,category,subCategory,"dsf124");
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();
		
		fail = true;
		course = new Course(p_Id, startTime, finishTime,price,classSize, popularity,category,subCategory,"dsf124");
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setStartTime2(900);
		course.setFinishTime2(1200);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();		
		
		fail = true;
		course = new Course(p_Id, startTime, finishTime,price,classSize, popularity,category,subCategory,"dsf124");
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setStartTime2(1000);
		course.setFinishTime2(1400);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();
		
		fail = true;
		course = new Course(p_Id, startTime, finishTime,price,classSize, popularity,category,subCategory,"dsf124");
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setStartTime2(1200);
		course.setFinishTime2(1400);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();
		
		fail = true;
		course = new Course(p_Id, startTime, finishTime,price,classSize, popularity,category,subCategory,"dsf124");
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setStartTime2(1205);
		course.setFinishTime2(1205);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();
		
		fail = true;
		course = new Course(p_Id, startTime, finishTime,price,classSize, popularity,category,subCategory,"13585226500");
		course.setCourseName("sdlkjoeghjeior");
		course.setStartTime1(900);
		course.setFinishTime1(1200);
		course.setStartTime2(1205);
		course.setFinishTime2(1305);
		course.setCourseHourLength(1);
		course.setCourseHourNum(2);
		course.setSubSubCategory("xch");
		try{
			ValidationService.validateCourse(course);
			fail = false;
		}catch(Exception e){
			//fail
		}
		if(fail) fail();
	}

	@Test
	public void testBookingValidation() throws SQLException, ValidationException, CouponNotFoundException, PseudoException{
		EduDaoBasic.clearAllDatabase();			
		int price = 12;
		int userId = 1;
		int partnerId = 1;
		int courseId = 1;
		String name = "booking-server-test";
		String phone = "12345612312";
		String reference = "dsf4rdsrfew";
		Calendar adjustTime = DateUtility.getCurTimeInstance();
		Calendar scheduledTime = DateUtility.getCurTimeInstance();
		String email = "xiongchuhanplace@hotmail.com";
		int cashbackAmount = 50;		
		Booking booking = new Booking(scheduledTime,adjustTime, 
				price, userId, partnerId, courseId,name, phone,email,reference,BookingStatus.awaiting,cashbackAmount);
		booking.setTransactionId(1);
		BookingDao.addBookingToDatabases(booking);

		try{
			ValidationService.validateBooking(booking);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		boolean fail = true;
		booking = new Booking(scheduledTime,adjustTime, 
				price, userId, partnerId, courseId,"", 
				phone,email,reference,BookingStatus.awaiting,cashbackAmount);
		booking.setTransactionId(1);
		try{
			ValidationService.validateBooking(booking);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		booking = new Booking(scheduledTime,adjustTime,
				price, userId, partnerId, courseId,name,
				"dsf3323",email,reference,BookingStatus.awaiting,cashbackAmount);
		booking.setTransactionId(1);
		try{
			ValidationService.validateBooking(booking);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		booking = new Booking(null,adjustTime, 
				price, userId, partnerId, courseId,
				name, phone,email,reference,BookingStatus.awaiting,cashbackAmount);
		booking.setTransactionId(1);
		try{
			ValidationService.validateBooking(booking);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();		

		fail = true;
		booking = new Booking(scheduledTime,adjustTime,
				 -1, userId, partnerId, courseId,name, phone,
				email,reference,BookingStatus.awaiting,cashbackAmount);
		booking.setTransactionId(1);
		try{
			ValidationService.validateBooking(booking);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		booking = new Booking(scheduledTime,adjustTime,
				price, -1, partnerId, courseId,name,
				phone,email,reference,BookingStatus.awaiting,cashbackAmount);
		booking.setTransactionId(1);
		try{
			ValidationService.validateBooking(booking);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		booking = new Booking(scheduledTime,adjustTime, 
				price, userId, -1, courseId,name, phone,
				email,reference,BookingStatus.awaiting,cashbackAmount);
		booking.setTransactionId(1);
		try{
			ValidationService.validateBooking(booking);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		booking = new Booking(scheduledTime,adjustTime, 
				price, userId,partnerId, -1,name, phone,
				email,reference,BookingStatus.awaiting,cashbackAmount);
		booking.setTransactionId(1);
		try{
			ValidationService.validateBooking(booking);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();

		fail = true;
		booking = new Booking(scheduledTime,adjustTime, 
				price, userId, partnerId, courseId,name, phone,
				email,"",BookingStatus.awaiting,-1);
		booking.setTransactionId(1);
		try{
			ValidationService.validateBooking(booking);
		}catch(ValidationException e){
			//Passed;
			fail = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();
		
		fail = false;
		booking = new Booking(scheduledTime,adjustTime, 
				price,  userId, partnerId, courseId,name, 
				phone,"sdfj-@hg",reference,BookingStatus.awaiting,cashbackAmount);
		try{
			ValidationService.validateBooking(booking);
		}catch(ValidationException e){			
			fail = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(fail) fail();
	}

}
