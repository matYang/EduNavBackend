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
				"红色太阳帽，我颤抖着提起脚。是的，我的心中滋生出登上山顶的冲动，但面对这炙热的阳光，我又收回了脚。老爸离山顶越来越近了，我似乎已经听到了他登上顶峰的欢呼声。我再也按捺不住了，“不，我要出发！”终于踏出了第一步，凭着一股子劲，" +
				"我冲上了半山腰。天气闷热得要命，一丝风也没有，稠乎乎的空气好像凝住了。汗像断了线的珠子，顺着脸颊往下直流，我喉咙发痒，口渴难耐，拼命地灌下了半瓶水。" +
				"脚步渐渐慢了下来，脚像灌了铅似的，怎么也迈不开步，我没有力气了。休息，休息，我要休息。一屁股坐在了石阶上，我呼呼地喘着大气，豆大的汗珠浸湿了衣衫，" +
				"一束束滚烫如开水的阳光刺过我的皮肤，疼痛不已，我感觉自己要虚脱了。路边的杂草抵不住太阳的爆晒，叶子都卷成个细条了。抬头看着密密匝匝的台阶，一股恐惧的暗流在逐渐侵蚀着我的心。“停止吧！”我的心在呐喊，心里紧绷的弦在慢慢松下来。" +
				"“儿子，既然选择了出发，目标就是终点。加油啊！”突然，耳边传来了老爸的助威声。我的心一颤，弦又一次绷紧了。一阵微风拂过，给我带来了丝丝凉爽。我欣喜地站起来想要更多的风。眼前的一幕令我怔住了：四周树木繁茂，翠竹成阴；不知名的野花虽然热得耷拉着脑袋，却散发着" +
				"特有的清香。深深吸一口，疲倦似乎缓解了不少。向下一看，竟然很多人都在尽力往上登，有些已经是将近花甲的老人了。心中不屈的火苗仿佛瞬间被点燃了，我握紧拳头，" +
				"为自己加油说：“我可以的，我一定能到达山顶！”我又一次选择了出发。这一次，我仿佛全身充满了能量，三步并作两步，“噌噌噌”地往上“飞”。“儿子，好样的！”老爸的赞许声" +
				"清晰地响起。我惊喜地发现我离山顶只有一小段距离了。冲啊，我像运动员般地冲刺，10米，9米，8米……坚韧的脚步在延伸着顽强，执着的信念在迎接艰难的洗礼。最终，" +
				"我到达了山顶，我胜利了。站在山顶上，太阳还是那么毒辣，心中却是无比喜悦与满足。放眼望去，满山蓊郁荫翳的树木，湛蓝辽阔的天空" +
				"，缥缈的几缕云恰好构成了一幅雅趣盎然的淡墨山水画。而我此时就身置其中，临风而立，“会当凌绝顶，一览众山小”的豪情便油然而生。我很幸运，因为我选择了出发，才看到了最美的风景；我很自豪，因为我选择了出发，才品尝到胜利的喜悦。";
		String goal = "当上总经理，迎娶白富美，走上人生巅峰，想想还有点小激动呢？（哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈）";
		ArrayList<String> teacherIntros = new ArrayList<String>();
		teacherIntros.add("尊敬的各位考官、各位评委老师： 通过考试，今天，我以本岗位笔试第一的成绩进入了面试。对我来说，这次机会显得尤为珍贵。我叫陈日安，今年21岁。浙江工业职业技术学院计算机系毕业。我于今年5月在新华小学实习。回想起那一个月时光真是既甜蜜又美好，" +
				"非师范类学校毕业的我虽然遇到了种种困难，但听着学生们围在身旁“老师”、“老师”的叫个不停，看着那一双双充满信任的眼睛，那一张张稚气的小脸，种种困难带来的不快都顿时烟消云散了。    非师范院校毕业的我虽然在教学理论，理念上相对匮乏。但是我丰富全面的专业知识" +
				"也能为学校带来他们所不能给予的帮助。现在的社会的发展要求现代教师不仅要有高尚的情操，成熟的教法，更要有非常专业理论的知识。我能运用photo shop处理图片，利用flash制作课件，waveCN进行声音效果的处理。能够独立完成网站的制作和论坛的架设。" +
				"我精通计算机硬件，曾在学校担任E网社的维修部部长，为全校学生维护维修电脑两百人次以上。我熟悉整个数码市场的行情，能在学校采购数码产品的时候提出有参考价值的意见，节省学校在该方面的开支。 我自觉遵纪守法，遵守社会公德，从不吸烟喝酒，没有不良嗜好和行为。" +
				"我想这些都是一名教育工作者应该具备的最起码的素养。 假如，我通过了面试，成为教师队伍中的成员，我将不断的努力学习，努力工作，为家乡的教育事业贡献自己的力量，决不辜负“人类灵魂的工程师”这个光荣的称号。");
		teacherIntros.add("XXX，女，1980年7月出生。1999年7月参加教育工作。小学一级教师。1999年至2001年6月在仓埠街丛林小学任教。在两年任教期间，本人吃苦耐劳，勇于挑重担。先后在校，街道，讲公开课，研讨课，示范课，送教下乡课。并多次荣获校，街道先进个人。" +
				"2001年7月年至2004年 6月在邾城街第四小学任教。期间2002年至2003年到邾城街钟杨村钟杨小学支教扶贫。三年来本人深钻教材，虚心求教。虽然三年都任教五年级的语文教学，但本人积极主动参加各类教研活动。曾讲授“科学实验课”，“活动课”“班会课”分别得到市，区领导的好评；" +
				"多次指导学生参加写字比赛，作文比赛，语基比赛分别获市，区级奖。2004年7月至今在邾城街中心小学任教。几年来克服家庭的重重困难，坚守岗位，默默耕耘。2004年，2006年撰写教案，2007年撰写案例，2008年撰写论文，分别荣获市，区级奖；2008年撰写教案《占好格，写好字》刊登" +
				"《教育教学研究》第三期，2008年所带班级荣获区先进班级体；并多次参加各级各类教育活动，多次到市区虚心学习；所带班级教学成绩连续几年居前三名。我热爱教育事业，在平凡的岗位上，我会用一颗火热的心去点燃学生的希望之光，使他（她）们在快乐成长的天地里能拥有一个多彩人生的起点！");
		teacherIntros.add("李开柱，男，1967年1月出生，中共党员，毕业于四川师范大学外语系，英语副教授,基础部副主任，公共英语教研室主任。长期从事英语教学，先后承担了《大学英语》、《财经英语》、《英语听说》、大学英语二三级、专升本考前辅导等英语教学工作。积极探索教学教法，并参与多项科研活动，工作认真负责，教学效果好。");
		teacherIntros.add("钟文斌，副教授，四川财经职业学院基础部语文教研室主任。主研《应用文写作》和《商务秘书学》，现主要讲授《应用文写作》《公共关系学》和《商务秘书学》。主要科研成果：1．" +
				"2005年参加高等教育出版社“中等职业学校文化基础课”《语文》第一册第六单元的编写。" + "2．2005年至2006年任五年制高职语文系列教材副主编，第一分册教材、教学参考书、多媒体课件" +
				"文字脚本、同步练习册主编；同时，参编第二册第五单元教材、教学参考书、多媒体课件文字脚本、同步练习册；第三册第六单元教学参考书、第六册《财经应用文》" +	"诉讼文书及多媒体课件文字脚本。3. 2007年" +
				"出版《财经应用文写作》（北京工业大学出版社），任主编；4. 2007——2008年，编写三年制中职语文系列教材。5．论文《高职语文教" +
				"育亟待改革》获2006年全国财经职业教育语文研究会论文评比一等奖。6．论文《培养学生人文素养  深化高" +
				"职语文教学改革》获2007年全国财经院校语文研究会第24届学术年会论文评比二等奖。7．论文《改革职业教育语文教学评价方式——构建发展性学生评价体系》获2008年全国财经职业教育语文研究会论文评比一等奖。");
		
		ArrayList<String> teacherImgs = new ArrayList<String>();
		teacherImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUy");
		teacherImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUy");
		teacherImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUyNTJGcG9ydGFsQnVja2V0JTI1MkZ2aWV3Lmh0bWwlMjUzRnNwbSUyNTNEMC4wLjAuMC5VOUNOSGklMjUyNmJ1Y2tldE5hbWUlMjUzRGNs");
		String location = "汇智大厦写字楼出租信息，上海徐汇万体馆漕溪北路398号，汇智大厦写字楼出租，找更多上海汇智大厦写字楼信息就到上海写字楼-搜房网。";
		
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
			
			String phone = "1234567890" + i;		
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
				String phone = "1234567890" + i;
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
