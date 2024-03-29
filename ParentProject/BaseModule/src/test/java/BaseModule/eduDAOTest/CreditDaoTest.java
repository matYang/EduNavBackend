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
import BaseModule.exception.PseudoException;
import BaseModule.model.Credit;
import BaseModule.model.User;
import BaseModule.model.representation.CreditSearchRepresentation;


public class CreditDaoTest {

	@Test
	public void testCreate(){
		EduDaoBasic.clearAllDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 234;
		Calendar expireTime = DateUtility.getCurTimeInstance();		
		Credit c = new Credit(bookingId,userId,amount);
		c.setExpireTime(expireTime);
		c.setStatus(CreditStatus.used);
		try{
			CreditDao.addCreditToDatabases(c);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGet() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 5653;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Credit c = new Credit(bookingId,userId,amount);
		c.setExpireTime(expireTime);
		c.setStatus(CreditStatus.used);
		c = CreditDao.addCreditToDatabases(c);
		Credit test = CreditDao.getCreditByCreditId(c.getCreditId());
		if(test.equals(c)){
			//Passed;
		}else fail();
		
		Credit c2 = new Credit(bookingId+2,userId,amount);
		c2.setExpireTime(expireTime);
		c2.setStatus(CreditStatus.used);
		c2 = CreditDao.addCreditToDatabases(c2);
		
		ArrayList<Credit> clist = new ArrayList<Credit>();
		clist = CreditDao.getCreditByUserId(userId);
		if(clist.size() == 2 && clist.get(0).equals(c) && clist.get(1).equals(c2)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpdate() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 2;
		Calendar expireTime = DateUtility.getCurTimeInstance();		
		Credit c = new Credit(bookingId,userId,amount);
		c.setExpireTime(expireTime);
		c.setStatus(CreditStatus.used);
		c = CreditDao.addCreditToDatabases(c);
		c.setStatus(CreditStatus.expired);
		CreditDao.updateCreditInDatabases(c);
		c = CreditDao.getCreditByCreditId(c.getCreditId());
		if(c.getStatus().code == CreditStatus.expired.code){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testSearch() throws SQLException{
		EduDaoBasic.clearAllDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 2;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Calendar usableTime = DateUtility.getCurTimeInstance();
		Credit c = new Credit(bookingId,userId,amount);
		c.setExpireTime(expireTime);
		c.setStatus(CreditStatus.used);
		CreditDao.addCreditToDatabases(c);
		
		bookingId = 2;
		userId = 1;
		amount = 500;
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.DAY_OF_YEAR,1);
		usableTime = DateUtility.getCurTimeInstance();
		usableTime.add(Calendar.DAY_OF_YEAR, 30);
		Credit c2 = new Credit(bookingId,userId,amount);
		c2.setExpireTime(expireTime);
		c2.setStatus(CreditStatus.used);
		CreditDao.addCreditToDatabases(c2);
		
		bookingId = 1;
		userId = 2;
		amount = 1500;
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.DAY_OF_YEAR,7);
		usableTime = DateUtility.getCurTimeInstance();
		usableTime.add(Calendar.DAY_OF_YEAR, 10);
		Credit c3 = new Credit(bookingId,userId,amount);
		c3.setExpireTime(expireTime);
		c3.setStatus(CreditStatus.used);
		CreditDao.addCreditToDatabases(c3);
		
		ArrayList<Credit> clist = new ArrayList<Credit>();
		CreditSearchRepresentation csr = new CreditSearchRepresentation();
		
		csr.setBookingId(1);
		csr.setStartAmount(100);
		clist = CreditDao.searchCredit(csr);
		
		if(clist.size()==1&&clist.get(0).equals(c3)){
			//Passed;
		}else fail();
		
		csr.setStartAmount(-1);
		csr.setExpireTime(DateUtility.getCurTimeInstance());
		clist = CreditDao.searchCredit(csr);
		
		if(clist.size()==1&&clist.get(0).equals(c)){
			//Passed;
		}else fail();
		
	}
	
	@Test
	public void testClean() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		String accountNum = "1";
		User user = new User(phone, password, "", "",accountNum,status);
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
		Credit c = new Credit(bookingId,userId,amount);
		c.setExpireTime(expireTime);
		c.setStatus(CreditStatus.usable);
		c = CreditDao.addCreditToDatabases(c);
		UserDao.updateUserBCC(0, c.getAmount(), 0, userId);
		
		Calendar expireTime2 = DateUtility.getCurTimeInstance();
		expireTime2.add(Calendar.SECOND, 1);
		Credit c2 = new Credit(bookingId,userId,amount);
		c2.setExpireTime(expireTime2);
		c2.setStatus(CreditStatus.usable);
		c2 = CreditDao.addCreditToDatabases(c2);
		UserDao.updateUserBCC(0, c2.getAmount(), 0, userId);
		
		Calendar expireTime3 = DateUtility.getCurTimeInstance();
		expireTime3.add(Calendar.SECOND, -1);
		Credit c3 = new Credit(bookingId,userId,amount);
		c3.setExpireTime(expireTime3);
		c3.setStatus(CreditStatus.used);
		c3 = CreditDao.addCreditToDatabases(c3);
		UserDao.updateUserBCC(0, c3.getAmount(), 0, userId);
		
		Calendar expireTime4 = DateUtility.getCurTimeInstance();
		expireTime4.add(Calendar.SECOND, 1);
		Credit c4 = new Credit(bookingId,userId,amount);
		c4.setExpireTime(expireTime4);
		c4.setStatus(CreditStatus.expired);
		c4 = CreditDao.addCreditToDatabases(c4);
		UserDao.updateUserBCC(0, c4.getAmount(), 0, userId);
		
		Calendar expireTime5 = DateUtility.getCurTimeInstance();
		expireTime5.add(Calendar.SECOND, 1);
		Credit c5 = new Credit(bookingId,userId,amount);
		c5.setExpireTime(expireTime5);
		c5.setStatus(CreditStatus.used);
		c5 = CreditDao.addCreditToDatabases(c5);
		UserDao.updateUserBCC(0, c5.getAmount(), 0, userId);
		
		CreditCleaner.clean();
		
		ArrayList<Credit> clist = new ArrayList<Credit>();
		clist = CreditDao.getCreditByUserId(userId);
		if(clist.size() == 5 && clist.get(0).getStatus().code == CreditStatus.expired.code &&
				clist.get(1).getStatus().code== CreditStatus.usable.code &&
				clist.get(2).getStatus().code== CreditStatus.used.code &&
				clist.get(3).getStatus().code== CreditStatus.expired.code && 
				clist.get(4).getStatus().code == CreditStatus.used.code){
			//Passed;
		}else fail();
		
		amount = UserDao.getUserById(userId).getCredit();
		if(amount == 48){
			//Passed
		}else fail();
	}
}
