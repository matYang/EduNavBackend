package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.clean.cleanTasks.CouponCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.model.Coupon;
import BaseModule.model.User;
import BaseModule.model.representation.CouponSearchRepresentation;

public class CouponDaoTest {

	@Test
	public void testCreate(){
		EduDaoBasic.clearAllDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 234;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Coupon c = new Coupon(userId, amount); 
		c.setBookingId(bookingId);
		c.setExpireTime(expireTime);
		c.setStatus(CouponStatus.usable);
		try{
			CouponDao.addCouponToDatabases(c);
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
		int amount = 45665;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Coupon c = new Coupon(userId, amount);
		c.setBookingId(bookingId);
		c.setExpireTime(expireTime);
		c.setStatus(CouponStatus.usable);
		c = CouponDao.addCouponToDatabases(c);
		Coupon test = CouponDao.getCouponByCouponId(c.getCouponId());
		if(test.equals(c)){
			//Passed;
		}else fail();
		Coupon c2 = new Coupon(userId, amount);
		c2.setBookingId(bookingId+1);
		c2.setExpireTime(expireTime);
		c2.setStatus(CouponStatus.usable);
		c2 = CouponDao.addCouponToDatabases(c2);
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		clist = CouponDao.getCouponByUserId(userId);
		if(clist.size() == 2 && clist.get(0).equals(c) && clist.get(1).equals(c2)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpdate() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 1000034343;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Coupon c = new Coupon(userId, amount);
		c.setBookingId(bookingId);
		c.setExpireTime(expireTime);
		c.setStatus(CouponStatus.usable);
		CouponDao.addCouponToDatabases(c);
		c.setAmount(1);		
		CouponDao.updateCouponInDatabases(c);
		c = CouponDao.getCouponByCouponId(c.getCouponId());
		if(c.getAmount()==1){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testSearch() throws SQLException{
		EduDaoBasic.clearAllDatabase();
		int bookingId = 1;
		int userId = 1;
		int amount = 234;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		Coupon c = new Coupon(userId, amount);
		c.setBookingId(bookingId);
		c.setExpireTime(expireTime);
		c.setStatus(CouponStatus.usable);
		CouponDao.addCouponToDatabases(c);
		
		
		bookingId = 2;
		userId = 1;
		amount = 0;
		expireTime = DateUtility.getCurTimeInstance();
		Coupon c2 = new Coupon(userId, amount);
		c2.setBookingId(bookingId);
		c2.setExpireTime(expireTime);
		c2.setStatus(CouponStatus.expired);
		CouponDao.addCouponToDatabases(c2);
		
		
		bookingId = 2;
		userId = 2;
		amount = 67;
		expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.DAY_OF_YEAR, 1);
		Coupon c3 = new Coupon(userId, amount);
		c3.setBookingId(bookingId);
		c3.setExpireTime(expireTime);
		c3.setStatus(CouponStatus.used);
		CouponDao.addCouponToDatabases(c3);
		
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		CouponSearchRepresentation csr = new CouponSearchRepresentation();
		csr.setBookingId(1);
		csr.setStartPrice(4);
		csr.setFinishPrice(100);
		clist = CouponDao.searchCoupon(csr);
		
		if(clist.size()==0){
			//Passed;
		}else fail();
		
		
		csr.setBookingId(2);
		clist = CouponDao.searchCoupon(csr);
		
		if(clist.size()==1 && clist.get(0).equals(c3)){
			//Passed;
		}else fail();
		
		csr.setBookingId(-1);
		csr.setExpireTime(expireTime);
		clist = CouponDao.searchCoupon(csr);
		
		if(clist.size()==1 && clist.get(0).equals(c3)){
			//Passed;
		}else fail();
		
		csr.setExpireTime(null);		
		csr.setStartPrice(0);
		csr.setFinishPrice(60);
		clist = CouponDao.searchCoupon(csr);
		
		if(clist.size()==1 && clist.get(0).equals(c2)){
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
		int amount = 10;
		Calendar expireTime = DateUtility.getCurTimeInstance();
		expireTime.add(Calendar.SECOND, -1);
		Coupon c = new Coupon(userId, amount);
		c.setBookingId(bookingId);
		c.setExpireTime(expireTime);
		c.setStatus(CouponStatus.usable);
		CouponDao.addCouponToDatabases(c);
		UserDao.updateUserBCC(0, 0, c.getAmount(), userId);
		
		int bookingId2 = 2;			
		expireTime.add(Calendar.HOUR_OF_DAY, 1);
		Coupon c2 = new Coupon(userId, amount);
		c2.setBookingId(bookingId2);
		c2.setExpireTime(expireTime);
		c2.setStatus(CouponStatus.usable);
		CouponDao.addCouponToDatabases(c2);
		UserDao.updateUserBCC(0, 0, c2.getAmount(), userId);
		
		int bookingId3 = 3;			
		expireTime.add(Calendar.HOUR_OF_DAY, -1);
		Coupon c3 = new Coupon(userId, amount);
		c3.setBookingId(bookingId3);
		c3.setExpireTime(expireTime);
		c3.setStatus(CouponStatus.used);
		CouponDao.addCouponToDatabases(c3);
		UserDao.updateUserBCC(0, 0, c3.getAmount(), userId);
		
		int bookingId4 = 4;		
		expireTime.add(Calendar.HOUR_OF_DAY, 2);
		Coupon c4 = new Coupon(userId, amount);
		c4.setBookingId(bookingId4);
		c4.setExpireTime(expireTime);
		c4.setStatus(CouponStatus.used);
		CouponDao.addCouponToDatabases(c4);
		UserDao.updateUserBCC(0, 0, c4.getAmount(), userId);
		
		CouponCleaner.clean();
		
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		clist = CouponDao.getCouponByUserId(userId);
		if(clist.size() == 4 && clist.get(0).getStatus().code == CouponStatus.expired.code &&
				clist.get(1).getStatus().code== CouponStatus.usable.code &&
				clist.get(2).getStatus().code== CouponStatus.used.code &&
				clist.get(3).getStatus().code== CouponStatus.used.code){
			//Passed;
		}else fail();
		
		amount = UserDao.getUserById(userId).getCoupon();
		if(amount == 30){
			//Passed;
		}else fail();
	}
}
