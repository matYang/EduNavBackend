package BaseModule.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.dbservice.CouponDaoService;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.dbservice.CreditDaoService;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.dbservice.UserDaoService;
import BaseModule.eduDAO.CourseDao;

import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.eduDAO.TransactionDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.generator.ReferenceGenerator;
import BaseModule.model.AdminAccount;
import BaseModule.model.Booking;
import BaseModule.model.Coupon;
import BaseModule.model.Course;
import BaseModule.model.Credit;
import BaseModule.model.Partner;
import BaseModule.model.Transaction;
import BaseModule.model.User;
import BaseModule.model.generic.SDTree;
import BaseModule.staticDataService.SDService;
import BaseModule.staticDataService.SystemDataInit;

public final class ModelDataLoaderService {	

	public static void load(){		
		EduDaoBasic.clearAllDatabase();
		SystemDataInit.init();
		Connection conn = EduDaoBasic.getConnection();
		try{
			loadUsers(conn);//20
			loadPartners(conn);//10
			loadAdmins(conn);//10		
			loadCourses(conn);//30
			loadBookings(conn);//20		
			loadTransactions(conn);//20
			loadCredits(conn);//20
			loadCoupons(conn);//20
		} finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}

		DebugLog.d("Models loaded successfully");
	}

	private static void loadCourses(Connection...connections){		
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.HOUR_OF_DAY, 5);
		finishTime.add(Calendar.DAY_OF_MONTH, 8);		
		int price = 100;
		final int courseNum = 20;
		

		String outline = "提纲提纲提纲提纲提纲： \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲  \n  提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲  \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n \n 提纲提纲提纲提纲提纲\n\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n \n 提纲提纲提纲提纲提纲\n\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n \n 提纲提纲提纲提纲提纲\n";
		String goal = "提纲提纲提纲提纲提纲： \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲  \n  提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲  \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n \n 提纲提纲提纲提纲提纲\n\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n \n 提纲提纲提纲提纲提纲\n\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲 \n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n 提纲提纲提纲提纲提纲提纲提纲提纲提纲提纲\n \n 提纲提纲提纲提纲提纲\n";
		ArrayList<String> teacherIntros = new ArrayList<String>();
		teacherIntros.add("教师介绍：避免多于200字。 介绍介绍介绍介绍，介绍介绍介绍介绍介绍介绍介绍，介绍介绍介绍介绍介绍介绍。介绍介绍介绍介绍；介绍介绍介绍介绍介绍，介绍介绍介绍介绍、介绍介绍介绍介绍");
		teacherIntros.add("教师介绍：避免多于200字。 介绍介绍介绍介绍，介绍介绍介绍介绍介绍介绍介绍，介绍介绍介绍介绍；介绍介绍介绍介绍介绍，介绍介绍介绍介绍、介绍介绍介绍介绍");
		teacherIntros.add("教师介绍：避免多于200字。 介绍介绍介绍介绍，介绍介绍介绍介绍介绍介绍介绍，介绍介绍介绍介绍介绍介绍。介绍介绍介介绍介绍介绍介绍。介绍介绍介绍介绍介绍。介绍介绍介绍介绍介绍。介绍绍介绍；介绍介绍介绍介绍介绍，介绍介绍介绍介绍、介绍介绍介绍介绍");
		teacherIntros.add("教师介绍：避免多于200字。 介绍介绍介绍介绍，介绍介绍介绍介绍介绍介绍介绍，介绍介绍介绍介绍介绍介绍。介绍介绍介绍介绍介绍介绍。介绍介绍介绍；介绍介绍介绍介绍介绍，介绍介绍介绍介绍、介绍介绍介绍介绍");
		
		String filler = "--此处为填充字符，在这里的目的是为了展示一下正式内容填充之后的效果。该处字符数为80个字，保持属于同一个段落，内容一致统一，谢谢您的阅读- 填充填充填充填充填充填充填充填充填充填充填充填充填充填充填充填充填充填充-";

		ArrayList<String> teacherNames = new ArrayList<String>();
		teacherNames.add("张老师");
		teacherNames.add("李老师");
		teacherNames.add("谢老师");
		teacherNames.add("陈老师");
		
		ArrayList<String> teacherImgs = new ArrayList<String>();
		teacherImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/teacherImgUrl-3-123124123.jpg");
		teacherImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/teacherImgUrl-3-123124123.jpg");
		teacherImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/teacherImgUrl-3-123124123.jpg");
		teacherImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/teacherImgUrl-3-123124123.jpg");
		
		ArrayList<String> classrommImgs = new ArrayList<String>();
		classrommImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/classroomImgUrl-1-2-3124123412.jpg");
		classrommImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/classroomImgUrl-1-2-3124123412.jpg");
		classrommImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/classroomImgUrl-1-2-3124123412.jpg");
		classrommImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/classroomImgUrl-1-2-3124123412.jpg");
		
		ArrayList<Integer> studyDays = new ArrayList<Integer>();
		studyDays.add(1);
		studyDays.add(3);
		studyDays.add(5);
		studyDays.add(6);
		studyDays.add(7);
		
		
		String location = "上海徐汇万体馆漕溪北路398号";
		
		for(int i=1; i <= courseNum; i++){			
			int classSize = i;
			int popularity = i;
			int p_Id = (i%10)+1;
			price += 100 + i;

			SDTree<String> randomProvinceNode = SDService.getLocationTree().getRandomLeaf();
			SDTree<String> randomCityNode = randomProvinceNode.getRandomLeaf();
			SDTree<String> randomDistrictNode = randomCityNode.getRandomLeaf();
			String province = randomProvinceNode.getHead();
			String city = randomCityNode.getHead();
			String district = randomDistrictNode.getHead();
			
			SDTree<String> randomCatNode = SDService.getCatTree().getRandomLeaf();
			SDTree<String> randomSubCatNode = randomCatNode.getRandomLeaf();
			SDTree<String> randomSubSubCatNode = randomSubCatNode.getRandomLeaf();
			String category = randomCatNode.getHead();
			String subCategory = randomSubCatNode.getHead();
			String subSubCategory = randomSubSubCatNode.getHead();
			
			String phone = "DONOTSEND1234567890" + i;		
			startTime.add(Calendar.MINUTE, i);
			Course course = new Course(p_Id, startTime, finishTime,price,classSize,popularity,category,subCategory,phone);		
			course.setSubSubCategory(subSubCategory);
			course.setProvince(province);
			course.setCity(city);
			course.setDistrict(district);
			course.setLocation(location);
			course.setGoal(goal + i);
			course.setOutline(outline + i);
			course.setTeacherIntros(teacherIntros);
			course.setTeacherImgUrls(teacherImgs);
			course.setCashback(course.getPrice()/10);
			course.setTeacherNames(teacherNames);
			course.setStudyDays(studyDays);
			
			course.setClassImgUrls(classrommImgs);
			course.setClassTeacher(filler);
			course.setQuestionSession(filler);
			course.setPartnerCourseReference("342RY65348FRTYH89RU353FW43");
			course.setTeachingAndExercise(filler);
			course.setTrail("提供试听课一节，40分钟，课后选择是否报名付款");
			course.setLogoUrl("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/1-3-logo.jpg");
			course.setBonusService(filler);
			course.setPrerequest(filler);
			course.setAssignments(filler);
			course.setDownloadMaterials(filler);
			course.setQuestionBank(filler);
			course.setHighScoreReward(filler);
			course.setCourseName("雅思精品冲7保6精品小班");
			course.setPartnerIntro(filler);
			course.setPartnerDistinction(filler);
			course.setSuitableStudent("--此处为填充字符，在这里的目的是为了展示一下正式内容");
			course.setExtracurricular(filler);
			course.setMarking(filler);
			course.setQuiz(filler);
			course.setOpenCourseRequirement(filler);
			course.setTeachingMaterialIntro(filler);
			course.setCourseIntro(filler);
			course.setStudyDaysNote(filler);
			course.setTeachingMaterialFee(filler);
			course.setCertification(filler);
			course.setPassAgreement(filler);
			course.setCourseHourNum(20);
			course.setCourseHourLength(60);
			
			try {
				course.setReference(ReferenceGenerator.generateCourseReference());
				CourseDaoService.createCourse(course);				
			} catch (SQLException | PseudoException e) {	
				DebugLog.d(e);
			} 
		}

	}

	private static void loadBookings(Connection...connections) {

		try{
			int bookingNum = 20;			
			int cashback = 40;
			for(int i=1;i<=bookingNum;i++){
				cashback += i;
				int partnerId = (i)%10 + 1;
				Course course = CourseDao.getCourseById(i, connections);
				User user = UserDao.getUserById(i, connections);
				Partner partner = PartnerDao.getPartnerById(partnerId, connections);
				Booking booking = new Booking(course.getStartDate(),course.getCreationTime(),course.getPrice(), 
						user.getUserId(), partner.getPartnerId(), course.getCourseId(), user.getName(), partner.getPhone(),
						user.getEmail(),ReferenceGenerator.generateBookingReference(),BookingStatus.fromInt(i%9),cashback+i);
				try {
					BookingDaoService.createBooking(booking);
				} catch (SQLException e) {				
					e.printStackTrace();
				}
			}			
		}catch(Exception e){
			DebugLog.d(e);
		}

	}

	private static void loadUsers(Connection...connections){
		try{
			int userNum = 20;
			int i = 1;

			User matt = new User("DONOTSEND18662241356" + i, "111111", "", ReferenceGenerator.generateUserInvitationalCode(), ReferenceGenerator.generateUserAccountNumber(), AccountStatus.activated);
			matt.setName("Matthew");
			matt.setEmail("use@me");
			UserDaoService.createUser(matt);	
			i++;

			User harry = new User("DONOTSEND18502877744" + i, "93dd", "", ReferenceGenerator.generateUserInvitationalCode(), ReferenceGenerator.generateUserAccountNumber(), AccountStatus.activated);
			harry.setName("Harry");
			harry.setEmail("c2xiong@uwaterloo.ca");
			UserDaoService.createUser(harry);	
			i++;

			for(;i<=userNum;i++){
				String name = "用户" + i;
				String phone = "DONOTSEND1234567890" + i;
				String password = "userPassword" + i;
				AccountStatus status = AccountStatus.fromInt(i%3);
				String email = "userEmail" + i + "@example.com";
				String accountNum = ReferenceGenerator.generateUserAccountNumber();				
				String invitationalCode = ReferenceGenerator.generateUserInvitationalCode();
				User user = new User(phone, password, matt.getInvitationalCode(), invitationalCode, accountNum, status);
				user.setName(name);
				user.setEmail(email);
				UserDaoService.createUser(user);			
			}

		}catch(Exception e){
			DebugLog.d(e);
		}

	}

	private static void loadPartners(Connection...connections){
		try{
			int partnerNum = 10;
			for(int i=1;i<=partnerNum;i++){
				String name = "合作商" + i;
				String instName = "教育机构" + i;
				String licence = "证件号" + i;
				String organizationNum = "机构号" + i;
				String reference = "partnerReference" + i;
				String password = "partnerPassword" + i;
				String phone = "DONOTSEND1234567890" + i;
				AccountStatus status = AccountStatus.activated;
				Partner partner = new Partner(name, instName,licence, organizationNum,reference, password, phone,status);
				partner.setLogoUrl("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/1-3-logo.jpg");
				try {
					PartnerDaoService.createPartner(partner, connections);
				} catch (SQLException e) {				
					e.printStackTrace();
				}
			}			
		}catch(Exception e){
			DebugLog.d(e);
		}

	}

	private static void loadAdmins(Connection...connections){
		int adminNum = 10;
		for(int i=1;i<=adminNum;i++){
			String name = "管理员" + i;
			String phone = "DONOTSEND1234567890" + i;
			String reference = null;
			try{
				reference = ReferenceGenerator.generateAdminReference();	
			} catch (Exception e){
				DebugLog.d(e);
			}

			Privilege privilege = Privilege.fromInt(i%3);
			AccountStatus status = AccountStatus.fromInt(i%3);
			String password = "adminPassword" + i;
			AdminAccount account = new AdminAccount(name,phone,reference,privilege,status,password);
			try {
				AdminAccountDaoService.createAdminAccount(account);
			} catch (SQLException | PseudoException e) {		
				e.printStackTrace();
			} 
		}		
	}

	private static void loadCredits(Connection...connections){
		int creditNum = 20;
		int amount = 50;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		for(int i=1;i<=creditNum;i++){
			int bookingId = i;
			int userId = i;
			amount += 50 + i;
			expireTime.add(Calendar.MINUTE, i);		
			Credit c = new Credit(bookingId,userId,amount);
			c.setExpireTime(expireTime);
			c.setStatus(CreditStatus.fromInt(i%3));

			try {
				CreditDaoService.createCredit(c, connections);
				UserDao.updateUserBCC(0, c.getAmount(), 0, c.getUserId(), connections);
			} catch (SQLException | PseudoException e) {			
				e.printStackTrace();
			}
		}		
	}

	private static void loadCoupons(Connection...connections){
		int couponNum = 20;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		for(int i=1;i<=couponNum;i++){			
			int userId = i;
			int amount = 50+i;
			expireTime.add(Calendar.MINUTE, i);
			Coupon c = new Coupon(userId, amount);			
			c.setExpireTime(expireTime);
			c.setStatus(CouponStatus.fromInt(i%4));
			try {
				CouponDaoService.addCouponToUser(c, connections);
			} catch (SQLException | PseudoException e) {			
				e.printStackTrace();
			}

		}
	}

	private static void loadTransactions(Connection...connections){		
		try{

			int transactionNum = 20;
			for(int i=1;i<=transactionNum;i++){
				User user = UserDao.getUserById(i,connections);						
				int amount = 20;
				Transaction transaction = new Transaction(user.getUserId(),i,amount,TransactionType.deposit);
				try {
					TransactionDao.addTransactionToDatabases(transaction, connections);
					UserDao.updateUserBCC(transaction.getTransactionAmount(), 0, 0, transaction.getUserId(), connections);
				} catch (SQLException e) {				
					e.printStackTrace();
				}
			}			
		}catch(Exception e){
			DebugLog.d(e);
		}

	}

}
