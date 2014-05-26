package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.clean.cleanTasks.CouponCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.coupon.CouponNotFoundException;
import BaseModule.model.Coupon;

public class CouponDaoTest {

	@Test
	public void testCreate(){
		EduDaoBasic.clearBothDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 234;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Coupon c = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.usable);
		try{
			CouponDao.addCouponToDatabases(c);
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
		int amount = 45665;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Coupon c = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.usable);
		c = CouponDao.addCouponToDatabases(c);
		Coupon test = CouponDao.getCouponByCouponId(c.getCouponId());
		if(test.equals(c)){
			//Passed;
		}else fail();
		Coupon c2 = new Coupon(bookingId+1,userId, amount, expireTime, CouponStatus.usable);
		c2 = CouponDao.addCouponToDatabases(c2);
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		clist = CouponDao.getCouponByUserId(userId);
		if(clist.size() == 2 && clist.get(0).equals(c) && clist.get(1).equals(c2)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpdate() throws CouponNotFoundException{
		EduDaoBasic.clearBothDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 1000034343;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Coupon c = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.usable);
		CouponDao.addCouponToDatabases(c);
		c.setAmount(1);
		c.setTransactionId(2);
		CouponDao.updateCouponInDatabases(c);
		c = CouponDao.getCouponByCouponId(c.getCouponId());
		if(c.getAmount()==1 && c.getTransactionId() == 2){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testClean(){
		EduDaoBasic.clearBothDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 1000034343;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.SECOND, -1);
		Coupon c = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.usable);
		CouponDao.addCouponToDatabases(c);
		
		int bookingId2 = 2;			
		expireTime.add(Calendar.HOUR_OF_DAY, 1);
		Coupon c2 = new Coupon(bookingId2,userId, amount, expireTime, CouponStatus.usable);
		CouponDao.addCouponToDatabases(c2);
		
		int bookingId3 = 3;			
		expireTime.add(Calendar.HOUR_OF_DAY, -1);
		Coupon c3 = new Coupon(bookingId3,userId, amount, expireTime, CouponStatus.used);
		CouponDao.addCouponToDatabases(c3);
		
		int bookingId4 = 4;		
		expireTime.add(Calendar.HOUR_OF_DAY, 2);
		Coupon c4 = new Coupon(bookingId4,userId, amount, expireTime, CouponStatus.used);
		CouponDao.addCouponToDatabases(c4);
		
		CouponCleaner.clean();
		
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		clist = CouponDao.getCouponByUserId(userId);
		if(clist.size() == 4 && clist.get(0).getStatus().code == CouponStatus.expired.code &&
				clist.get(1).getStatus().code== CouponStatus.usable.code &&
				clist.get(2).getStatus().code== CouponStatus.used.code &&
				clist.get(3).getStatus().code== CouponStatus.used.code){
			//Passed;
		}else fail();
		
	}
}
