package BaseModule.cleanerTest;

import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import org.junit.Test;
import BaseModule.clean.cleanTasks.BookingCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CourseStatus;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.model.Booking;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.User;
import BaseModule.model.representation.BookingSearchRepresentation;


public class BookingCleanerTest {

	@Test
	public void test() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String userphone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email0 = "xiongchuhan@hotmail.com";
		User user = new User(userphone, password, "", "", "1",status);
		user.setName(name);
		user.setEmail(email0);
		UserDao.addUserToDatabase(user);
		
		String pname = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String ppassword = "sdf234r";		
		Partner partner = new Partner(pname,instName, licence, organizationNum,reference, ppassword,status);
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
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,category,subCategory);
		course.setReference("courseFGgf");
		ArrayList<Long> list = new ArrayList<Long>();	
		list.add(1L);
		course.setOutline("sdfdsf");
		course.setGoal("sdfdsf");
		course.setClassPhotoIdList(list);
		course.setTeacherIdList(list);
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
		course.setTeachingMaterialIntro("Hand and Ass");
		course.setCourseName("bababa");				
		course.setPrice(price);
		course.setStatus(CourseStatus.deactivated);
		course.setStartDate(DateUtility.getTimeFromLong(DateUtility.getCurTime() - Booking.cashbackDelay - 600000l));
		CourseDao.updateCourseInDatabases(course);		
		course = CourseDao.getCourseById(course.getCourseId());			
		
		int userId = user.getUserId();
		int partnerId = partner.getPartnerId();
		int courseId = course.getCourseId();		
		Calendar timeStamp = DateUtility.getCurTimeInstance();	
		timeStamp.add(Calendar.SECOND, -1);
		String email = "xiongchuhanplace@hotmail.com";
		int cashbackAmount = 50;	
		
		//Nothing happen
		Booking booking = new Booking(timeStamp,timeStamp, 
				course.getPrice(), userId, partnerId, courseId, user.getName(), "123456789",
				email,partner.getReference(),BookingStatus.awaiting,cashbackAmount);
		booking.setNoRefundDate(timeStamp);
		booking.setCashbackDate(timeStamp);
		BookingDao.addBookingToDatabases(booking);
		
		//Nothing happen
		Calendar finishTime2 = Calendar.getInstance();
		finishTime2.add(Calendar.DAY_OF_YEAR, -1);			
		Booking booking2 = new Booking(finishTime2,timeStamp,
				course.getPrice(), userId, partnerId, courseId, user.getName(), "123456789",
				email,partner.getReference()+"2",BookingStatus.cancelled,cashbackAmount);
		BookingDao.addBookingToDatabases(booking2);
		
		//succeeded ---> consolidated
		Calendar finishTime3 = Calendar.getInstance();
		finishTime3.add(Calendar.HOUR_OF_DAY, -1);
		Booking booking3 = new Booking(finishTime3,timeStamp, 
				course.getPrice(), userId, partnerId, courseId, user.getName(), "123456789",
				email,partner.getReference()+"3",BookingStatus.succeeded,cashbackAmount);
		booking3.setNoRefundDate(finishTime3);
		booking3.setCashbackDate(finishTime3);
		BookingDao.addBookingToDatabases(booking3);
		
		//started ---> consolidated
		Calendar finishTime4 = Calendar.getInstance();
		finishTime4.add(Calendar.HOUR_OF_DAY, -1);
		Booking booking4 = new Booking(finishTime4,timeStamp, 
				course.getPrice(), userId, partnerId, courseId, user.getName(), "123456789",
				email,partner.getReference()+"4",BookingStatus.started,cashbackAmount);
		booking4.setNoRefundDate(finishTime4);
		booking4.setCashbackDate(finishTime4);
		BookingDao.addBookingToDatabases(booking4);
		
		//Nothing happen
		Calendar finishTime5 = Calendar.getInstance();
		finishTime5.add(Calendar.HOUR_OF_DAY, -1);
		Booking booking5 = new Booking(finishTime4,timeStamp, 
				course.getPrice(), userId, partnerId, courseId, user.getName(), "123456789",
				email,partner.getReference()+"5",BookingStatus.started,cashbackAmount);
		booking5.setCashbackDate(finishTime5);
		BookingDao.addBookingToDatabases(booking5);
		
		//Nothing happen
		Calendar finishTime6 = Calendar.getInstance();
		finishTime6.add(Calendar.HOUR_OF_DAY, -1);
		Booking booking6 = new Booking(finishTime4,timeStamp, 
				course.getPrice(), userId, partnerId, courseId, user.getName(), "123456789",
				email,partner.getReference()+"6",BookingStatus.succeeded,cashbackAmount);
		booking6.setNoRefundDate(finishTime6);
		BookingDao.addBookingToDatabases(booking6);
		
		BookingCleaner.clean();
		
		ArrayList<Booking> blist = new ArrayList<Booking>();
		BookingSearchRepresentation sr = new BookingSearchRepresentation();
		sr.setPartnerId(partnerId);
		blist = BookingDao.searchBooking(sr);
		
		if(blist.get(0).getStatus().code == BookingStatus.awaiting.code &&
				blist.get(1).getStatus().code == BookingStatus.cancelled.code &&
				blist.get(2).getStatus().code == BookingStatus.consolidated.code &&
				blist.get(3).getStatus().code == BookingStatus.consolidated.code &&
				blist.get(4).getStatus().code == BookingStatus.started.code &&
				blist.get(5).getStatus().code == BookingStatus.succeeded.code){
			//Passed;
		}else fail();
		
	}
}
