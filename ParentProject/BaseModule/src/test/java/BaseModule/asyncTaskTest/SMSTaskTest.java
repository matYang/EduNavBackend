package BaseModule.asyncTaskTest;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import BaseModule.asyncRelayExecutor.ExecutorProvider;
import BaseModule.asyncTask.SMSTask;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.SMSEvent;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.ClassPhotoDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.eduDAO.TeacherDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.model.Booking;
import BaseModule.model.ClassPhoto;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.Teacher;
import BaseModule.model.User;
import BaseModule.service.SMSService;

public class SMSTaskTest {

	//@Test
	public void testConnection() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://gbk.sms.webchinese.cn"); 
		
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("Uid", "routea"));
		nvps.add(new BasicNameValuePair("Key", "a221a629eacbddc0720c"));
		nvps.add(new BasicNameValuePair("smsMob", "18662241356"));
		nvps.add(new BasicNameValuePair("smsText", "您的验证码：testConnection"));
		
		post.setEntity(new UrlEncodedFormEntity(nvps, "gbk"));
		
		post.addHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");

		HttpResponse response = client.execute(post);
		
		
		Header[] headers = response.getAllHeaders();
		System.out.println("statusCode:"+response.getStatusLine().getStatusCode());
		for(Header h : headers){
			System.out.println(h.toString());
		}
		String result = new String(response.getEntity().toString().getBytes("gbk")); 
		System.out.println(result);


		post.releaseConnection();
	}
	
	//@Test
	public void testSMSTask(){
		SMSTask smsTask = new SMSTask(SMSEvent.user_cellVerification, "18662241356", "testSMSTask");
		smsTask.execute();
	}
	
	//@Test
	public void testSMSRelay() throws InterruptedException{
		SMSTask smsTask = new SMSTask(SMSEvent.user_changePassword, "18662241356", "testSMSRelay");
		ExecutorProvider.executeRelay(smsTask);
		Thread.sleep(5000);
	}
	
	//@Test
	public void testSMSForgetPassword() throws InterruptedException{
		SMSTask smsTaska = new SMSTask(SMSEvent.user_forgetPassword, "18662241356", "testSMSForgetPassword");
		ExecutorProvider.executeRelay(smsTaska);
		SMSTask smsTaskb = new SMSTask(SMSEvent.user_forgetPassword, "18662241356", "ku79DS3drR");
		ExecutorProvider.executeRelay(smsTaskb);
		Thread.sleep(5000);
	}
	
	@Test
	public void testBookingConfirmed() throws PseudoException, SQLException, InterruptedException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String userphone = "18013955974";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(userphone, password, "", "","1",status);
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
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal, seatsLeft,category,subCategory,phone);
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
		ArrayList<Long> tlist = new ArrayList<Long>();
		ArrayList<Long> clist = new ArrayList<Long>();
		Teacher teacher = new Teacher(p_Id, "teacherImgUrl", "teacherName","teacherIntro");	
		teacher = TeacherDao.addTeacherToDataBases(teacher);
		tlist.add(teacher.getTeacherId());
		ClassPhoto classPhoto = new ClassPhoto(p_Id, "classImgUrl", "classPhoto","classDescription");
		classPhoto = ClassPhotoDao.addClassPhotoToDataBases(classPhoto);
		clist.add(classPhoto.getClassPhotoId());
		course.setClassPhotoIdList(clist);
		course.setTeacherIdList(tlist);
		course.setTeachingMaterialIntro("Hand and Ass");		
		course.setPrice(price);
		course.setOriginalPrice(price+99);
		course.setCourseName("精品Gay速成班");
		course.setLocation("南京市雨花台区宁双路28号汇智大厦，908室，邮编xoxo");
		course.setPhone("40082097155");
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
				user.getName(), "18662241356",
				email,partner.getReference(),BookingStatus.awaiting,cashbackAmount);
		try{
			BookingDao.addBookingToDatabases(booking);			
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		booking = BookingDao.getBookingById(booking.getBookingId());
		
		SMSService.sendBookingConfirmedSMS(booking);
		Thread.sleep(5000);
	}

}
