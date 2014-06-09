package BaseModule.eduDAOTest;

import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.clean.cleanTasks.CouponCleaner;
import BaseModule.clean.cleanTasks.CreditCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.model.Coupon;
import BaseModule.model.Credit;
import BaseModule.model.User;
import BaseModule.model.representation.UserSearchRepresentation;

public class UserDaoTest {

	@Test
	public void testCreate(){
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		User user = new User(phone, password,status);
		user.setName(name);
		user.setEmail(email);

		try{
			UserDao.addUserToDatabase(user);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testGet() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		User user = new User(phone, password,status);
		user.setName(name);
		user.setEmail(email);

		user = UserDao.addUserToDatabase(user);

		User test = UserDao.getUserById(user.getUserId());

		if(user.equals(test)){
			//Passed;
		}else{
			fail();
		}
		

		String name2 = "Matt";
		String phone2 = "1324234234";
		String password2 = "36krl";
		AccountStatus status2 = AccountStatus.activated;		
		User user2 = new User(phone2, password2,status2);
		user2.setName(name2);
		user2.setEmail(email);
		UserDao.addUserToDatabase(user2);

		ArrayList<User> ulist = new ArrayList<User>();
		ulist = UserDao.searchUser(new UserSearchRepresentation());
		if(ulist.size()==2 && ulist.get(0).equals(user) && ulist.get(1).equals(user2)){
			//Passed;
		}else fail();
	}

	@Test
	public void testUpdate() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		User user = new User(phone, password,status);
		user.setName(name);
		user.setEmail(email);

		user = UserDao.addUserToDatabase(user);
		user.setName("Matt");
		user.setPhone("324234324");
		UserDao.updateUserInDatabases(user);
		if(user.getName().equals("Matt")&&user.getPhone().equals("324234324")){
			//Passed;
		}else fail();		
		
		int bookingId = 1;
		int userId = user.getUserId();
		int amount = 234;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.DAY_OF_MONTH, -15);
		Calendar usableTime = DateUtility.getCurTimeInstance();
		usableTime.add(Calendar.DAY_OF_MONTH, -30);
		Credit credit = new Credit(bookingId,userId,amount,expireTime, CreditStatus.usable,usableTime);
		CreditDao.addCreditToDatabases(credit);
		
		CreditCleaner.clean();
		
		Coupon coupon = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.usable);
		CouponDao.addCouponToDatabases(coupon);
		
		CouponCleaner.clean();
		
		user = UserDao.getUserById(userId);
		if(user.getBalance()==0 && user.getCoupon()==-amount && user.getCredit()==-amount){
			//Passed;
		}else fail();
	}

	@Test
	public void testUpdateUserPassword() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		User user = new User(phone, password,status);
		user.setName(name);
		user.setEmail(email);
		user.setName("Matt");		
		user = UserDao.addUserToDatabase(user);
		
		try{
			UserDao.changeUserPassword(user.getUserId(), "36krfinal", "xcf");
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}

		boolean fail = false;

		try{
			UserDao.changeUserPassword(user.getUserId(), "xcf", "3432fdsf");
		}catch(AuthenticationException e){
			fail = true;
		}
		catch(Exception e){
			e.printStackTrace();
			fail();
		}

		if(fail) fail();

		fail = true;
		try{
			UserDao.changeUserPassword(user.getUserId(), "xcf", "");
		}catch(AuthenticationException e){
			fail = false;
		}
		catch(Exception e){			
			fail();
		}

		if(fail) fail();

		fail = true;
		try{
			UserDao.changeUserPassword(2, "xcf", "sdf3");
		}catch(AuthenticationException e){
			fail = false;
		}
		catch(Exception e){			
			fail();
		}

		if(fail) fail();
	
	}

	@Test
	public void testRecoverPassword() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		User user = new User(phone, password,status);
		user.setName(name);
		user.setEmail(email);
		user.setName("Matt");		
		user = UserDao.addUserToDatabase(user);
		
		try{
			UserDao.recoverUserPassword(phone, "xch1234");
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		
		boolean fail = true;		
		String badPhone = "12345612315";
	
		try{
			UserDao.recoverUserPassword(badPhone, "fdgfg");
		}catch(AuthenticationException e){
			fail =false;
		}
		
		if(fail) fail();
		
	}
	
	@Test
	public void testAuthUser() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		User user = new User(phone, password,status);
		user.setName(name);
		user.setEmail(email);
		user.setName("Matt");		
		user = UserDao.addUserToDatabase(user);
		User test = null;
		try {
			test = UserDao.authenticateUser(phone, password);
		} catch (AuthenticationException e) {
			fail();
		}
		if(test==null) fail();
		
		test = null;
		try {
			test = UserDao.authenticateUser("12345612311", password);
		} catch (AuthenticationException e) {
			//Passed;
		}
		
		if(test != null) fail();
		
		test = null;
		try {
			test = UserDao.authenticateUser(phone, "36krfinai");
		} catch (AuthenticationException e) {
			//Passed;
		}
		
		if(test != null) fail();

	}
	
	@Test
	public void testSearch() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "name";
		String phone = "phone";
		String password = "password";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		User user = new User(phone, password,status);
		user.setName(name);
		user.setEmail(email+"test");
		user = UserDao.addUserToDatabase(user);
		user = UserDao.getUserById(user.getUserId());
		
		String name11 = "name11";
		String phone11 = "phone1";
		String password11 = "password11";		
		User user11 = new User(phone11, password11, status);
		user11.setName(name11);
		user11.setEmail(email);
		user11 = UserDao.addUserToDatabase(user11);
		user11 = UserDao.getUserById(user11.getUserId());
		
		String name2 = "name2";
		String phone2 = "phone2";
		String password2 = "password2";		
		User user2 = new User(phone2, password2, status);
		user2.setName(name2);
		user2.setEmail(email);
		user2 = UserDao.addUserToDatabase(user2);
		user2 = UserDao.getUserById(user2.getUserId());
		
		String name22 = "name22";
		String phone22 = "phone3";
		String password22 = "password22";		
		User user22 = new User(phone22, password22,status);
		user22.setName(name22);
		user22.setEmail(email);
		user22 = UserDao.addUserToDatabase(user22);
		user22 = UserDao.getUserById(user22.getUserId());
		
		String name3 = "name3";
		String phone3 = "phone4";
		String password3 = "password3";		
		User user3 = new User(phone3, password3,status);
		user3.setName(name3);
		user3.setEmail(email);
		user3 = UserDao.addUserToDatabase(user3);
		user3 = UserDao.getUserById(user3.getUserId());
		
		ArrayList<User> ulist = new ArrayList<User>();
		UserSearchRepresentation sr = new UserSearchRepresentation();
		sr.setName("name");
		ulist = UserDao.searchUser(sr);
		if(ulist.size()==1 && ulist.get(0).equals(user)){
			//Passed;
		}else fail();
		
		sr.setName(null);
		sr.setPhone("phone");
		ulist = UserDao.searchUser(sr);
		if(ulist.size()==1 && ulist.get(0).equals(user)){
			//Passed;
		}else fail();
		
		sr.setPhone(null);
		sr.setUserId(user3.getUserId());
		ulist = UserDao.searchUser(sr);
		if(ulist.size()==1 && ulist.get(0).equals(user3)){				
			//Passed;
		}else fail();
		
		sr.setUserId(-1);
		sr.setPhone("phone2");
		ulist = UserDao.searchUser(sr);
		if(ulist.size()==1 && ulist.get(0).equals(user2)){				
			//Passed;
		}else fail();
		
		sr.setName(null);
		sr.setPhone(null);
		sr.setEmail(email+"test");
		ulist = UserDao.searchUser(sr);
		if(ulist.size()==1 && ulist.get(0).equals(user)){				
			//Passed;
		}else fail();
		
	}

}
