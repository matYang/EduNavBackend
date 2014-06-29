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
			loadCourses(conn);//100
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
		final int courseNum = 100;
		

		String outline = "选择出发夏天的太阳像个大火炉，把大地烤得发烫，就连空气也是热的，人一动就浑身冒汗。我躲在山下的遮阳伞下，考虑着是否要出发。“出发了吗？儿子。”老爸洪亮的声音传来。抬头，愕然，老爸竟然已快半山腰了。看着他手中挥动的" +
				"红色太阳帽，我颤抖着提起脚。是的，我的心中滋生出登上山顶热的阳光，我又收回了脚。老李开柱，男，1967年1月出生，中共党员，毕业于四川师范大学外语系，英语副爸离山顶越来越近了，我似乎已经听到了他登上顶峰的欢呼声。我再也按捺不住了，“不，我要出发！”终于踏出了第一步，凭着一股子劲，" +
				"我冲上了半山腰。天气闷热得要命，一丝风也没有，稠乎乎的空气好像凝住了。汗像断了线的我擦氪风格好大啊珠子，顺着脸颊往下直流，我喉咙发痒，口渴难耐，拼命地灌下了半瓶水。李开柱，男，1967年1月出生，中共党员，毕业于四川师范大学外语系，英语副教授,基础部副主任，公共英语教研室主任。长期从事英语教学，先后承担了《大学英语》李开柱";
		String goal = "当上总经理，迎娶白富美，走上人生巅峰，想想还有点小激动呢？（哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈）有，稠乎乎的空气好像凝住了。汗像断了线的珠子，顺着脸颊往下直流，我喉咙发痒，口渴难耐，拼命地灌下了半瓶水。" +
				"脚步渐渐慢了下来，脚像灌了铅似的，怎么也迈不开步，我没有我乐隔休息。一屁股坐在了石阶上，我呼呼地李开柱，男，1967年1月出生，中共党员，毕业于四川师范大学外语系，英语副喘着大气，豆大的汗珠浸湿了衣衫，" +
				"一束束滚烫如开水的阳光刺过我的皮肤，疼痛不已，我感觉自己要虚脱了。路边的杂草抵不住太阳的爆晒，叶子都卷成个细条了。抬头看着密密匝匝的台阶，一股恐惧的暗流在逐渐侵蚀着我的心。“停止吧！”我的心在呐喊，心里紧绷的弦在慢慢松下来。李开柱，男，1967年1月出生，中共党员，毕业于四川师范大学外语系，英语副教授,基础部副主任，公共英语教研室主任。长期从事英语教学，先后承担了《大学英语》李开柱";
		ArrayList<String> teacherIntros = new ArrayList<String>();
		teacherIntros.add("尊敬的各位考官、各位评委老师： 通过考试，今天，我以本岗位笔试第一的成大神大神绩进入DSA了面试。对我来说，这次机会显得尤为珍贵。我叫陈日安，今年21岁。浙江工业职业技术");
		teacherIntros.add("XXX，女，1980年7月出生。1999年7月参加教育工作。小学一级教师。1999年至20大神01盛大 年6月在仓埠街丛林小学任教。在两年任教期间，本人吃苦耐劳，勇于挑重担。先后在");
		teacherIntros.add("李开柱，男，1967年1月出生，中共党员，毕业于四川师范大学外语系，英语副教授,基础的撒打算部副主任，公共英语教研室主任。长期从事英语教学，先后承担了《大学英语》、《财经");
		teacherIntros.add("李开柱，男，1967年1月出生，中共党员，毕业于四川师范大学外语系，英语副大神大神教授,基础部副主任，公共英语教研室主任。长期从事英语教学，先后承担了《大学英语》李开柱");
		
		String filler = "李开柱，男，1967年1月出生，中共党员，毕业于我乐哥去！川师范大学外语系，英语副教地方授,基础DSA部副主任，公共英语教研室主任。长期从事英语教学，先后承担了《大学英语》";

		ArrayList<String> teacherNames = new ArrayList<String>();
		teacherNames.add("苍老师");
		teacherNames.add("陈老师");
		teacherNames.add("谢老师");
		
		ArrayList<String> teacherImgs = new ArrayList<String>();
		teacherImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUy");
		teacherImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUy");
		teacherImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUyNT");
		String location = "汇智大厦写字楼 出租信息，上海徐汇万体馆漕溪北路398号，汇智大厦写字楼出租";
		
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
			course.setCashback(i);
			course.setTeacherNames(teacherNames);
			
			course.setClassImgUrls(teacherImgs);
			course.setClassTeacher(filler);
			course.setQuestionSession(filler);
			course.setPartnerCourseReference("342RY65348FRTYH89RU353FW43");
			course.setTeachingAndExercise(filler);
			course.setTrail("李开柱，男，1967年1月出生，中共党员，毕业于四川师范大学外语系，英语副教授,基础部副主任，公共英语教研室主任。长期从事英语教学，先后承担了《大学英语》李开柱，男");
			course.setLogoUrl("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUyNTJGcG9ydGFsQnVja2V0JTI1MkZ2aWV3Lmh0bWwlMjUzRnNwbSUyNTNEMC4wLjAuMC5VOUNOSGklMjUyNmJ1Y2tldE5hbWUlMjUzRGNs");
			course.setBonusService(filler);
			course.setPrerequest(filler);
			course.setAssignments(filler);
			course.setDownloadMaterials(filler);
			course.setQuestionBank(filler);
			course.setHighScoreReward(filler);
			course.setCourseName("雅思精品冲8保7.5 - 30人超级小班 原价 8999元 现只要 4999元 便宜3000哦亲快来抢");
			course.setPartnerIntro(filler);
			course.setPartnerDistinction(filler);
			course.setSuitableStudent(filler);
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

			User harry = new User("DONOTSEND1234567890" + i, "222222", "", ReferenceGenerator.generateUserInvitationalCode(), ReferenceGenerator.generateUserAccountNumber(), AccountStatus.activated);
			harry.setName("Harry");
			harry.setEmail("c2xiong@uwaterloo.ca");
			UserDaoService.createUser(harry);	
			i++;

			for(;i<=userNum;i++){
				String name = "userName " + i;
				String phone = "DONOTSEND1234567890" + i;
				String password = "userPassword " + i;
				AccountStatus status = AccountStatus.fromInt(i%3);
				String email = "userEmail " + i;
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
				String name = "partnerName " + i;
				String instName = "instName " + i;
				String licence = "licence " + i;
				String organizationNum = "organizationNum " + i;
				String reference = "partnerReference " + i;
				String password = "partnerPassword " + i;
				String phone = "DONOTSEND1234567890" + i;
				AccountStatus status = AccountStatus.activated;
				Partner partner = new Partner(name, instName,licence, organizationNum,reference, password, phone,status);
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
			String name = "adminName " + i;
			String phone = "1234567890" + i;
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
			int amount = 50;
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
