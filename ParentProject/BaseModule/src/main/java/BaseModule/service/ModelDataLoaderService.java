package BaseModule.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.eduDAO.AdminAccountDao;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.eduDAO.TransactionDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.factory.ReferenceFactory;
import BaseModule.model.AdminAccount;
import BaseModule.model.Booking;
import BaseModule.model.Coupon;
import BaseModule.model.Course;
import BaseModule.model.Credit;
import BaseModule.model.Partner;
import BaseModule.model.Transaction;
import BaseModule.model.User;
import BaseModule.staticDataService.SystemDataInit;

public class ModelDataLoaderService {	

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
		int courseNum = 100;
		for(int i=1;i<=courseNum;i++){			
			int classSize = i;
			int popularity = i;
			int p_Id = (i+1)/2;
			price += 100 + i;
			String province = "province" + i;
			String location = "location" + i;
			String city = "city" + i;
			String category = "category" + i;			
			String subCategory = "subCategory" + i;
			String subSubCategory = "subSubCategory" + i;
			String phone = "1234567890" + i;		
			startTime.add(Calendar.MINUTE, i);
			Course course = new Course(p_Id, startTime, finishTime,price,classSize,popularity,category,subCategory,phone);
			course.setSubSubCategory(subSubCategory);
			course.setProvince(province);
			course.setLocation(location);
			course.setCity(city); 
			try {
				course.setReference(ReferenceFactory.generateCourseReference());
			} catch (SQLException | PseudoException e1) {				
				e1.printStackTrace();
			}
			try {
				CourseDao.addCourseToDatabases(course, connections);
			} catch (SQLException e) {	
				e.printStackTrace();
			}
		}

	}

	private static void loadBookings(Connection...connections) {

		try{
			int bookingNum = 20;			
			int cashback = 50;
			for(int i=1;i<=bookingNum;i++){
				int partnerId = (i+1)/2;
				Course course = CourseDao.getCourseById(i, connections);
				User user = UserDao.getUserById(i, connections);
				Partner partner = PartnerDao.getPartnerById(partnerId, connections);
				Booking booking = new Booking(course.getStartDate(),course.getCreationTime(),course.getPrice(), 
						user.getUserId(), partner.getPartnerId(), course.getCourseId(), user.getName(), partner.getPhone(),
						user.getEmail(),ReferenceFactory.generateBookingReference(),BookingStatus.fromInt(i%9),cashback+i);
				try {
					BookingDao.addBookingToDatabases(booking, connections);
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
			int balance = 50;		
			int i = 1;
			
			User matt = new User("1234567890" + i, "111111", "Emma", ReferenceFactory.generateUserInvitationalCode(), ReferenceFactory.generateUserAccountNumber(), AccountStatus.activated);
			matt.setName("Matthew");
			matt.setEmail("use@me");
			matt.incBalance(100000000);
			matt.incCoupon(50);
			matt.incCredit(50);
			UserDao.addUserToDatabase(matt,connections);	
			i++;
			
			User harry = new User("1234567890" + i, "222222", "None", ReferenceFactory.generateUserInvitationalCode(), ReferenceFactory.generateUserAccountNumber(), AccountStatus.activated);
			harry.setName("Harry");
			harry.setEmail("c2xiong@uwaterloo.ca");
			harry.incBalance(200000000);
			harry.incCoupon(50);
			harry.incCredit(50);
			UserDao.addUserToDatabase(harry,connections);	
			i++;
			
			for(;i<=userNum;i++){
				balance += 1;
				int credit = balance += i;
				int coupon = credit += i;
				String name = "userName " + i;
				String phone = "1234567890" + i;
				String password = "userPassword " + i;
				AccountStatus status = AccountStatus.fromInt(i%3);
				String email = "userEmail " + i;
				String accountNum = ReferenceFactory.generateUserAccountNumber();				
				String invitationalCode = ReferenceFactory.generateUserInvitationalCode();
				User user = new User(phone, password, "", invitationalCode, accountNum, status);
				user.setName(name);
				user.setEmail(email);
				user.incBalance(balance);
				user.incCoupon(coupon);
				user.incCredit(credit);
				UserDao.addUserToDatabase(user,connections);			
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
					PartnerDao.addPartnerToDatabases(partner, connections);
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
				reference = ReferenceFactory.generateAdminReference();	
			} catch (Exception e){
				DebugLog.d(e);
			}
			
			Privilege privilege = Privilege.fromInt(i%3);
			AccountStatus status = AccountStatus.fromInt(i%3);
			String password = "adminPassword " + i;
			AdminAccount account = new AdminAccount(name,phone,reference,privilege,status,password);
			try {
				AdminAccountDao.addAdminAccountToDatabases(account,connections);
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
				CreditDao.addCreditToDatabases(c,connections);
			} catch (SQLException e) {			
				e.printStackTrace();
			}
		}		
	}

	private static void loadCoupons(Connection...connections){
		int couponNum = 20;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		for(int i=1;i<=couponNum;i++){
			int bookingId = i;
			int userId = i;
			int amount = 50;
			expireTime.add(Calendar.MINUTE, i);
			Coupon c = new Coupon(userId, amount);
			c.setBookingId(bookingId);
			c.setExpireTime(expireTime);
			c.setStatus(CouponStatus.fromInt(i%4));
			try {
				CouponDao.addCouponToDatabases(c,connections);
			} catch (SQLException e) {			
				e.printStackTrace();
			}

		}
	}

	private static void loadTransactions(Connection...connections){		
		try{

			int transactionNum = 20;
			for(int i=1;i<=transactionNum;i++){
				User user = UserDao.getUserById(i,connections);						
				int amount = 2000;
				Transaction transaction = new Transaction(user.getUserId(),i,amount,TransactionType.fromInt(i%4));
				try {
					TransactionDao.addTransactionToDatabases(transaction, connections);
				} catch (SQLException e) {				
					e.printStackTrace();
				}
			}			
		}catch(Exception e){
			DebugLog.d(e);
		}

	}

}
