package BaseModule.eduDAOTest;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Calendar;
import org.junit.Test;

import BaseModule.clean.cleanTasks.CreditCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.credit.CreditNotFoundException;
import BaseModule.model.Credit;


public class CreditDaoTest {

	@Test
	public void testCreate(){
		EduDaoBasic.clearBothDatabase();
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
	public void testGet(){
		EduDaoBasic.clearBothDatabase();
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
	public void testUpdate() throws CreditNotFoundException{
		EduDaoBasic.clearBothDatabase();
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
	public void testClean(){
		EduDaoBasic.clearBothDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 2;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Calendar usableTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.SECOND, -1);
		Credit c = new Credit(bookingId,userId,amount,expireTime, CreditStatus.usable,usableTime);
		c = CreditDao.addCreditToDatabases(c);
		
		Calendar expireTime2 = DateUtility.getCurTimeInstance();
		expireTime2.add(Calendar.SECOND, 1);
		Credit c2 = new Credit(bookingId,userId,amount,expireTime2, CreditStatus.usable,usableTime);
		c2 = CreditDao.addCreditToDatabases(c2);
		
		Calendar expireTime3 = DateUtility.getCurTimeInstance();
		expireTime3.add(Calendar.SECOND, -1);
		Credit c3 = new Credit(bookingId,userId,amount,expireTime3, CreditStatus.used,usableTime);
		c3 = CreditDao.addCreditToDatabases(c3);
		
		Calendar expireTime4 = DateUtility.getCurTimeInstance();
		expireTime4.add(Calendar.SECOND, 1);
		Credit c4 = new Credit(bookingId,userId,amount,expireTime4, CreditStatus.expired,usableTime);
		c4 = CreditDao.addCreditToDatabases(c4);
		
		CreditCleaner.clean();
		
		ArrayList<Credit> clist = new ArrayList<Credit>();
		clist = CreditDao.getCreditByUserId(userId);
		if(clist.size() == 4 && clist.get(0).getStatus().code == CreditStatus.expired.code &&
				clist.get(1).getStatus().code== CreditStatus.usable.code &&
				clist.get(2).getStatus().code== CreditStatus.used.code &&
				clist.get(3).getStatus().code== CreditStatus.expired.code){
			//Passed;
		}else fail();
	}
}
