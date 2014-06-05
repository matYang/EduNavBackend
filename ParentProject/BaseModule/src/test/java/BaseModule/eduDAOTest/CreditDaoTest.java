package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import org.junit.Test;

import BaseModule.clean.cleanTasks.CreditCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.credit.CreditNotFoundException;
import BaseModule.exception.user.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Credit;
import BaseModule.model.User;


public class CreditDaoTest {

	@Test
	public void testCreate(){
		EduDaoBasic.clearAllDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 234;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Calendar usableTime = DateUtility.getCurTimeInstance();
		Credit c = new Credit(bookingId,userId,amount,expireTime, CreditStatus.used,usableTime);
		try{
			CreditDao.addCreditToDatabases(c);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGet() throws SQLException{
		EduDaoBasic.clearAllDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 5653;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Calendar usableTime = DateUtility.getCurTimeInstance();
		Credit c = new Credit(bookingId,userId,amount,expireTime, CreditStatus.used,usableTime);
		c = CreditDao.addCreditToDatabases(c);
		Credit test = CreditDao.getCreditByCreditId(c.getCreditId());
		if(test.equals(c)){
			//Passed;
		}else fail();
		
		Credit c2 = new Credit(bookingId+2,userId,amount,expireTime, CreditStatus.used,usableTime);
		c2 = CreditDao.addCreditToDatabases(c2);
		
		ArrayList<Credit> clist = new ArrayList<Credit>();
		clist = CreditDao.getCreditByUserId(userId);
		if(clist.size() == 2 && clist.get(0).equals(c) && clist.get(1).equals(c2)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpdate() throws CreditNotFoundException, SQLException{
		EduDaoBasic.clearAllDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 2;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Calendar usableTime = DateUtility.getCurTimeInstance();
		Credit c = new Credit(bookingId,userId,amount,expireTime, CreditStatus.used,usableTime);
		c = CreditDao.addCreditToDatabases(c);
		c.setStatus(CreditStatus.expired);
		CreditDao.updateCreditInDatabases(c);
		c = CreditDao.getCreditByCreditId(c.getCreditId());
		if(c.getStatus().code == CreditStatus.expired.code){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testClean() throws ValidationException, UserNotFoundException, SQLException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		User user = new User(phone, password,status);
		user.setName(name);
		user.setEmail(email);
		UserDao.addUserToDatabase(user);
		
		int bookingId = 1;
		int userId = user.getUserId();
		int amount = 12;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Calendar usableTime = DateUtility.getCurTimeInstance();
		usableTime.add(Calendar.SECOND, -1);
		expireTime.add(Calendar.SECOND, -1);
		Credit c = new Credit(bookingId,userId,amount,expireTime, CreditStatus.usable,usableTime);
		c = CreditDao.addCreditToDatabases(c);
		UserDao.updateUserBCC(0, c.getAmount(), 0, userId);
		
		Calendar expireTime2 = DateUtility.getCurTimeInstance();
		expireTime2.add(Calendar.SECOND, 1);
		Credit c2 = new Credit(bookingId,userId,amount,expireTime2, CreditStatus.usable,usableTime);
		c2 = CreditDao.addCreditToDatabases(c2);
		UserDao.updateUserBCC(0, c2.getAmount(), 0, userId);
		
		Calendar expireTime3 = DateUtility.getCurTimeInstance();
		expireTime3.add(Calendar.SECOND, -1);
		Credit c3 = new Credit(bookingId,userId,amount,expireTime3, CreditStatus.used,usableTime);
		c3 = CreditDao.addCreditToDatabases(c3);
		UserDao.updateUserBCC(0, c3.getAmount(), 0, userId);
		
		Calendar expireTime4 = DateUtility.getCurTimeInstance();
		expireTime4.add(Calendar.SECOND, 1);
		Credit c4 = new Credit(bookingId,userId,amount,expireTime4, CreditStatus.expired,usableTime);
		c4 = CreditDao.addCreditToDatabases(c4);
		UserDao.updateUserBCC(0, c4.getAmount(), 0, userId);
		
		Calendar expireTime5 = DateUtility.getCurTimeInstance();
		expireTime5.add(Calendar.SECOND, 1);
		Credit c5 = new Credit(bookingId,userId,amount,expireTime5, CreditStatus.awaiting,usableTime);		
		c5 = CreditDao.addCreditToDatabases(c5);
		UserDao.updateUserBCC(0, c5.getAmount(), 0, userId);
		
		CreditCleaner.clean();
		
		ArrayList<Credit> clist = new ArrayList<Credit>();
		clist = CreditDao.getCreditByUserId(userId);
		if(clist.size() == 5 && clist.get(0).getStatus().code == CreditStatus.expired.code &&
				clist.get(1).getStatus().code== CreditStatus.usable.code &&
				clist.get(2).getStatus().code== CreditStatus.used.code &&
				clist.get(3).getStatus().code== CreditStatus.expired.code && 
				clist.get(4).getStatus().code == CreditStatus.usable.code){
			//Passed;
		}else fail();
		
		amount = UserDao.getUserById(userId).getCredit();
		if(amount == 48){
			//Passed
		}else fail();
	}
}
