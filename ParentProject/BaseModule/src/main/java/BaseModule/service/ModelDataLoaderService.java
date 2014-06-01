package BaseModule.service;

import java.sql.Connection;
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
import BaseModule.exception.booking.BookingNotFoundException;
import BaseModule.exception.course.CourseNotFoundException;
import BaseModule.exception.partner.PartnerNotFoundException;
import BaseModule.exception.user.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.AdminAccount;
import BaseModule.model.Booking;
import BaseModule.model.Coupon;
import BaseModule.model.Course;
import BaseModule.model.Credit;
import BaseModule.model.Partner;
import BaseModule.model.Transaction;
import BaseModule.model.User;

public class ModelDataLoaderService {	

	public static void load(){		
		EduDaoBasic.clearAllDatabase();
		Connection conn = EduDaoBasic.getSQLConnection();

		loadUsers(conn);//4
		loadPartners(conn);//3
		loadAdmins(conn);//3		
		loadCourses(conn);//3
		loadBookings(conn);//6		
		loadTransactions(conn);//4
		loadCredits(conn);//5
		loadCoupons(conn);//5

		EduDaoBasic.closeResources(conn, null, null, true);
		DebugLog.d("Models loaded successfully");
		
	}

	private static void loadCourses(Connection...connections){
		int p_Id = 1;
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_MONTH, 8);			
		int seatsTotal = 50;
		int seatsLeft = 5;
		int price = 1000;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		String phone = "12344565654";
		AccountStatus status = AccountStatus.activated;		
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		CourseDao.addCourseToDatabases(course, connections);

		p_Id = 2;
		startTime = DateUtility.getCurTimeInstance();
		finishTime = DateUtility.getCurTimeInstance();
		startTime.add(Calendar.DAY_OF_MONTH, -8);					
		category = "Chinese";
		subCategory = "sub-Chin";		
		status = AccountStatus.deactivated;		
		Course course2 = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		CourseDao.addCourseToDatabases(course2, connections);

		p_Id = 3;
		startTime = DateUtility.getCurTimeInstance();
		finishTime = DateUtility.getCurTimeInstance();
		startTime.add(Calendar.DAY_OF_MONTH, -8);
		finishTime.add(Calendar.DAY_OF_MONTH, -7);
		category = "French";
		subCategory = "sub-French";		
		status = AccountStatus.deleted;		
		Course course3 = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		CourseDao.addCourseToDatabases(course3, connections);


	}

	private static void loadBookings(Connection...connections) {
		try{
			Course course = CourseDao.getCourseById(1, connections);
			User user = UserDao.getUserById(1, connections);
			Partner partner = PartnerDao.getPartnerById(1, connections);
			Booking booking = new Booking(course.getStartTime(),course.getCreationTime(),course.getPrice(), 1, 
					user.getUserId(), partner.getPartnerId(), course.getCourseId(), user.getName(), partner.getPhone(),
					user.getEmail(),"4trg45rt",BookingStatus.awaiting);
			BookingDao.addBookingToDatabases(booking,connections);

			course = CourseDao.getCourseById(2, connections);
			user = UserDao.getUserById(1, connections);
			partner = PartnerDao.getPartnerById(2, connections);
			booking = new Booking(course.getStartTime(),course.getCreationTime(),course.getPrice(), 2, 
					user.getUserId(), partner.getPartnerId(), course.getCourseId(), user.getName(), partner.getPhone(),
					user.getEmail(),"iuyiohy89",BookingStatus.confirmed);
			BookingDao.addBookingToDatabases(booking,connections);

			course = CourseDao.getCourseById(3, connections);
			user = UserDao.getUserById(2, connections);
			partner = PartnerDao.getPartnerById(3, connections);
			booking = new Booking(course.getStartTime(),course.getCreationTime(),course.getPrice(), 2, 
					user.getUserId(), partner.getPartnerId(), course.getCourseId(), user.getName(), partner.getPhone(),
					user.getEmail(),"ihjgijrth",BookingStatus.pending);
			BookingDao.addBookingToDatabases(booking,connections);

			course = CourseDao.getCourseById(2, connections);
			user = UserDao.getUserById(3, connections);
			partner = PartnerDao.getPartnerById(2, connections);
			booking = new Booking(course.getStartTime(),course.getCreationTime(),course.getPrice(), -1, 
					user.getUserId(), partner.getPartnerId(), course.getCourseId(), user.getName(), partner.getPhone(),
					user.getEmail(),"regtret",BookingStatus.finished);
			BookingDao.addBookingToDatabases(booking,connections);

			course = CourseDao.getCourseById(1, connections);
			user = UserDao.getUserById(4, connections);
			partner = PartnerDao.getPartnerById(1, connections);
			booking = new Booking(course.getStartTime(),course.getCreationTime(),course.getPrice(), -1, 
					user.getUserId(), partner.getPartnerId(), course.getCourseId(), user.getName(), partner.getPhone(),
					user.getEmail(),"klpupkl",BookingStatus.canceled);
			BookingDao.addBookingToDatabases(booking,connections);

			course = CourseDao.getCourseById(2, connections);
			user = UserDao.getUserById(2, connections);
			partner = PartnerDao.getPartnerById(2, connections);
			booking = new Booking(course.getStartTime(),course.getCreationTime(),course.getPrice(), 4, 
					user.getUserId(), partner.getPartnerId(), course.getCourseId(), user.getName(), partner.getPhone(),
					user.getEmail(),"fgjuifug",BookingStatus.failed);
			BookingDao.addBookingToDatabases(booking,connections);
		}catch(CourseNotFoundException e){
			e.printStackTrace();
			DebugLog.d(e);
		}catch(UserNotFoundException e){
			e.printStackTrace();
			DebugLog.d(e);
		}catch(PartnerNotFoundException e){
			e.printStackTrace();
			DebugLog.d(e);
		}


	}

	private static void loadUsers(Connection...connections){
		try{
			String name = "Harry";
			String phone = "18502877744";
			String password = "36krfinal";
			AccountStatus status = AccountStatus.activated;
			String email = "xiongchuhanplace@hotmail.com";
			User user = new User(name, phone, password,status,email);
			UserDao.addUserToDatabase(user,connections);

			String name2 = "Matt";
			String phone2 = "1324234234";
			String password2 = "36krlrethjhgu";
			String email2 = "uwse@me";
			AccountStatus status2 = AccountStatus.activated;		
			User user2 = new User(name2, phone2, password2,status2,email2);
			UserDao.addUserToDatabase(user2,connections);

			String name3 = "Fan";
			String phone3 = "1345452342";
			String password3 = "36krlfgfdgdf";
			String email3 = "uwse@me";
			AccountStatus status3 = AccountStatus.deactivated;		
			User user3 = new User(name3, phone3, password3,status3,email3);
			UserDao.addUserToDatabase(user3,connections);

			String name4 = "Jiang";
			String phone4 = "1344354355452";
			String password4 = "36kr53ty4ylfgfdgdf";
			String email4 = "45r4235@me";
			AccountStatus status4 = AccountStatus.deleted;		
			User user4 = new User(name4, phone4, password4,status4,email4);
			UserDao.addUserToDatabase(user4,connections);
		}catch(ValidationException e){
			e.printStackTrace();
			DebugLog.d(e);
		}

	}

	private static void loadPartners(Connection...connections){
		try{
			String name = "XDF";
			String instName = "xiaofeng";
			String licence = "234fdsfsdgergf-dsv,.!@";
			String organizationNum = "1235454361234";
			String reference = "dsf4r";
			String password = "sdf234r";
			String phone = "123545451";
			AccountStatus status = AccountStatus.activated;
			Partner partner = new Partner(name, instName,licence, organizationNum,reference, password, phone,status);
			PartnerDao.addPartnerToDatabases(partner, connections);

			String name2 = "HQYS";
			String instName2 = "daofeng";
			String licence2 = "2sdfdsf34545dsfsdgergf-dsv,.!@";
			String organizationNum2 = "12334361234";
			String reference2 = "dsdsfr";
			String password2 = "sdsdf34r";
			String phone2 = "12335451";
			AccountStatus status2 = AccountStatus.deactivated;
			Partner partner2 = new Partner(name2, instName2,licence2, organizationNum2,reference2, password2, phone2,status2);
			PartnerDao.addPartnerToDatabases(partner2,connections);

			String name3 = "XDFs";
			String instName3 = "xiaofeng";
			String licence3 = "234fdsfv,.!@";
			String organizationNum3 = "1235454361234";
			String reference3 = "d4r";
			String password3 = "sdf234r";
			String phone3 = "12354";
			AccountStatus status3 = AccountStatus.deleted;
			Partner partner3 = new Partner(name3, instName3,licence3, organizationNum3,reference3, password3, phone3,status3);
			PartnerDao.addPartnerToDatabases(partner3, connections);
		}catch(ValidationException e){
			e.printStackTrace();
			DebugLog.d(e);
		}

	}

	private static void loadAdmins(Connection...connections){
		String name = "Harry";
		String phone = "123445676543";
		String reference = "dsfdsf";
		Privilege privilege = Privilege.mamagement;
		AccountStatus status = AccountStatus.activated;
		String password = "hgfudifhg3489";
		AdminAccount account = new AdminAccount(name,phone,reference,privilege,status,password);
		AdminAccountDao.addAdminAccountToDatabases(account,connections);

		String name2 = "Mattan";
		String phone2 = "12344";
		String reference2 = "dsfsersf";
		Privilege privilege2 = Privilege.routine;
		AccountStatus status2 = AccountStatus.activated;		
		AdminAccount account2 = new AdminAccount(name2,phone2,reference2,privilege2,status2,password);
		AdminAccountDao.addAdminAccountToDatabases(account2);

		String name3 = "Fan";
		String phone3 = "2323";
		String reference3 = "dsfs5656ersf";
		Privilege privilege3 = Privilege.root;
		AccountStatus status3 = AccountStatus.activated;		
		AdminAccount account3 = new AdminAccount(name3,phone3,reference3,privilege3,status3,password);
		AdminAccountDao.addAdminAccountToDatabases(account3);
	}

	private static void loadCredits(Connection...connections){
		int bookingId = 1;
		int userId = 1;
		int amount = 50;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Calendar usableTime = DateUtility.getCurTimeInstance();
		Credit c = new Credit(bookingId,userId,amount,expireTime, CreditStatus.used,usableTime);
		CreditDao.addCreditToDatabases(c,connections);

		bookingId = 3;
		userId = 2;		
		c = new Credit(bookingId,userId,amount,expireTime, CreditStatus.awaiting,usableTime);
		CreditDao.addCreditToDatabases(c,connections);

		bookingId = 4;
		userId = 3;		
		c = new Credit(bookingId,userId,amount,expireTime, CreditStatus.expired,usableTime);
		CreditDao.addCreditToDatabases(c,connections);

		bookingId = 5;
		userId = 4;		
		c = new Credit(bookingId,userId,amount,expireTime, CreditStatus.used,usableTime);
		CreditDao.addCreditToDatabases(c,connections);

		bookingId = 2;
		userId = 1;		
		c = new Credit(bookingId,userId,amount,expireTime, CreditStatus.awaiting,usableTime);
		CreditDao.addCreditToDatabases(c,connections);
	}

	private static void loadCoupons(Connection...connections){
		int bookingId = 1;
		int userId = 1;
		int amount = 50;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Coupon c = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.usable);
		CouponDao.addCouponToDatabases(c,connections);

		bookingId = 3;
		userId = 2;				
		c = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.expired);
		CouponDao.addCouponToDatabases(c,connections);

		bookingId = 4;
		userId = 3;				
		c = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.used);
		CouponDao.addCouponToDatabases(c,connections);

		bookingId = 5;
		userId = 4;				
		c = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.usable);
		CouponDao.addCouponToDatabases(c,connections);

		bookingId = 2;
		userId = 1;				
		c = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.usable);
		CouponDao.addCouponToDatabases(c,connections);

	}

	private static void loadTransactions(Connection...connections){
		try{
			User user = UserDao.getUserById(1,connections);
			Booking booking = BookingDao.getBookingById(1,connections);		
			int amount = 2000;
			Transaction transaction = new Transaction(user.getUserId(),booking.getBookingId(),booking.getCouponId(),amount,TransactionType.coupon);
			TransactionDao.addTransactionToDatabases(transaction, connections);

			user = UserDao.getUserById(2,connections);
			booking = BookingDao.getBookingById(3,connections);		
			amount = 20;
			transaction = new Transaction(user.getUserId(),booking.getBookingId(),booking.getCouponId(),amount,TransactionType.deposit);
			TransactionDao.addTransactionToDatabases(transaction, connections);

			user = UserDao.getUserById(3,connections);
			booking = BookingDao.getBookingById(4,connections);		
			amount = 2300000;
			transaction = new Transaction(user.getUserId(),booking.getBookingId(),booking.getCouponId(),amount,TransactionType.withdraw);
			TransactionDao.addTransactionToDatabases(transaction, connections);	

			user = UserDao.getUserById(4,connections);
			booking = BookingDao.getBookingById(5,connections);		
			amount = 998;
			transaction = new Transaction(user.getUserId(),booking.getBookingId(),booking.getCouponId(),amount,TransactionType.withdraw);
			TransactionDao.addTransactionToDatabases(transaction, connections);	
		}catch(UserNotFoundException e){
			e.printStackTrace();
			DebugLog.d(e);
		}catch(BookingNotFoundException e){
			e.printStackTrace();
			DebugLog.d(e);
		}

	}

}
