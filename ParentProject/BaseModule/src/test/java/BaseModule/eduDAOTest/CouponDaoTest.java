package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

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
		double amount = 454.545;
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
		double amount = 454.545;
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
		double amount = 454.545;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Coupon c = new Coupon(bookingId,userId, amount, expireTime, CouponStatus.usable);
		CouponDao.addCouponToDatabases(c);
		c.setAmount(0.77);
		c.setTransactionId(2);
		CouponDao.updateCouponInDatabases(c);
		c = CouponDao.getCouponByCouponId(c.getCouponId());
		if(c.getAmount()==0.77 && c.getTransactionId() == 2){
			//Passed;
		}else fail();
	}
}
