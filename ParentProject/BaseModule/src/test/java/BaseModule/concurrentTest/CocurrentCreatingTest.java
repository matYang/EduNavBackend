package BaseModule.concurrentTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.concurrentTest.ConcurrentUpdatingTest.TestThread;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.dbservice.CouponDaoService;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.ReferenceFactory;
import BaseModule.model.Booking;
import BaseModule.model.Coupon;
import BaseModule.model.User;


public class CocurrentCreatingTest {

	private static int counter = 0;	
	public synchronized static void inc(){
		counter++;
	}
	
	private static int coupon = 10;	
	public synchronized static void incc(){
		coupon++;
	}
	
	private static int amount = 0;
	public synchronized static void en(int $){
		amount += $;
	}
	public synchronized static void dn(int $){
		amount -= $;
	}
	

	public class TestThread extends Thread {  
		private CountDownLatch threadsSignal;		
		private ArrayList<Booking> blist;
		private ArrayList<Coupon> clist;		
		public TestThread(CountDownLatch threadsSignal,ArrayList<Booking> blist,ArrayList<Coupon> clist) {  
			this.threadsSignal = threadsSignal;			
			this.blist = blist;
			this.clist = clist;
		}

		@Override  
		public void run() {
			try{
				int i = 0;			
				try {
					Booking booking = blist.get(0).deepCopy();			
					booking.setReference(ReferenceFactory.generateBookingReference());
					BookingDaoService.createBooking(booking);	
				} catch (SQLException | PseudoException | ClassNotFoundException | IOException e) {												
					e.printStackTrace();
				}
				while(i < clist.size()){					
					if(clist.get(i).getStatus().code == CouponStatus.usable.code && DateUtility.toSQLDateTime(DateUtility.getCurTimeInstance()).compareTo(DateUtility.toSQLDateTime(clist.get(i).getExpireTime()))<0){
						try {
							CouponDaoService.addCouponToUser(clist.get(i).deepCopy());
						} catch (SQLException | PseudoException | ClassNotFoundException | IOException e) {							
							e.printStackTrace();
						}
					}						

					i++;
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally{				
				threadsSignal.countDown();
			}
		}  
	}

	@Test
	public void testBenchMark() throws PseudoException, SQLException, InterruptedException{
		EduDaoBasic.clearAllDatabase();				
		String name = "Harry";
		String userphone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhan@hotmail.com";			
		User user = new User(userphone, password, "", "",status);
		user.setName(name);
		user.setEmail(email);		
		UserDao.addUserToDatabase(user);
		int cashbackAmount = 50;
		int userId = 1;
		int partnerId = 1;
		int courseId = 1;
		int price = 5000;
		int bookingNum = 1;
		String reference = "rgjiotrgj";
		String phone = "18542585699";		
		Calendar timeStamp = DateUtility.getCurTimeInstance();
		ArrayList<Booking> blist = new ArrayList<Booking>();
		ArrayList<Coupon> clist = new ArrayList<Coupon>();

		for(int i=0;i<bookingNum;i++){
			//50
			Coupon c = new Coupon(userId,cashbackAmount);	
			Calendar expireTime = DateUtility.getCurTimeInstance();
			expireTime.add(Calendar.DAY_OF_MONTH, 1);
			c.setExpireTime(expireTime);
			CouponDao.addCouponToDatabases(c);
			clist.add(c);

			//70
			Coupon c2 = new Coupon(userId,cashbackAmount + 20);		
			expireTime = DateUtility.getCurTimeInstance();
			expireTime.add(Calendar.DAY_OF_MONTH, 15);
			c2.setExpireTime(expireTime);
			CouponDao.addCouponToDatabases(c2);
			clist.add(c2);

			//100
			Coupon c3 = new Coupon(userId,cashbackAmount + 50);		
			expireTime = DateUtility.getCurTimeInstance();
			expireTime.add(Calendar.DAY_OF_MONTH, -15);		
			c3.setExpireTime(expireTime);
			CouponDao.addCouponToDatabases(c3);
			clist.add(c3);

			//66
			Coupon c4 = new Coupon(userId,cashbackAmount + 11);		
			expireTime = DateUtility.getCurTimeInstance();
			expireTime.add(Calendar.MINUTE, -15);
			c4.setExpireTime(expireTime);
			CouponDao.addCouponToDatabases(c4);
			clist.add(c4);

			//89
			Coupon c5 = new Coupon(userId,cashbackAmount + 39);		
			expireTime = DateUtility.getCurTimeInstance();
			expireTime.add(Calendar.MINUTE, 1);
			c5.setExpireTime(expireTime);
			CouponDao.addCouponToDatabases(c5);
			clist.add(c5);

			//1
			Coupon c6 = new Coupon(userId,cashbackAmount - 49);		
			expireTime = DateUtility.getCurTimeInstance();
			expireTime.add(Calendar.DAY_OF_YEAR, 2);
			c6.setExpireTime(expireTime);
			CouponDao.addCouponToDatabases(c6);
			clist.add(c6);

			//100
			Coupon c7 = new Coupon(userId,cashbackAmount + 50);		
			expireTime = DateUtility.getCurTimeInstance();
			expireTime.add(Calendar.DAY_OF_MONTH, -15);
			c7.setExpireTime(expireTime);				
			c7.setStatus(CouponStatus.expired);
			CouponDao.addCouponToDatabases(c7);
			clist.add(c7);

			//66
			Coupon c8 = new Coupon(userId,cashbackAmount + 11);
			expireTime = DateUtility.getCurTimeInstance();
			expireTime.add(Calendar.MINUTE, -15);
			c8.setExpireTime(expireTime);
			c8.setStatus(CouponStatus.used);
			CouponDao.addCouponToDatabases(c8);
			clist.add(c8);

			//89
			Coupon c9 = new Coupon(userId,cashbackAmount + 39);		
			expireTime = DateUtility.getCurTimeInstance();
			expireTime.add(Calendar.MINUTE, 1);
			c9.setExpireTime(expireTime);
			c9.setStatus(CouponStatus.expired);
			CouponDao.addCouponToDatabases(c9);
			clist.add(c9);

			//2
			Coupon c10 = new Coupon(userId,cashbackAmount - 48);		
			expireTime = DateUtility.getCurTimeInstance();
			expireTime.add(Calendar.DAY_OF_YEAR, 2);
			c10.setExpireTime(expireTime);
			c10.setStatus(CouponStatus.usable);
			CouponDao.addCouponToDatabases(c10);
			clist.add(c10);

			UserDao.updateUserBCC(0, 0, 212, userId);
			en(212);

			int backcash = 211;//剩一块

			Booking booking = new Booking(timeStamp,timeStamp,price,
					userId, partnerId, courseId, user.getName(), phone,
					email,reference,BookingStatus.confirmed,backcash);

			blist.add(booking);
		}		

		int threadNum = 100;
		CountDownLatch threadSignal = new CountDownLatch(threadNum);

		System.out.println("Test Begin");
		user = UserDao.getUserById(userId);
		System.out.println("");	
		System.out.println("user coupon start value: " + user.getCoupon());
		System.out.println("");	

		for(int i=0;i<threadNum;i++){
			Thread bookingThread = new TestThread(threadSignal, blist,clist);	
			bookingThread.start();
		}			
		threadSignal.await();		


		System.out.println("Test End");
		user = UserDao.getUserById(userId);
		System.out.println("user coupon final value: " + user.getCoupon());
		int exptvalue = 212+threadNum+counter*211;
		System.out.println("expected value: " + exptvalue);
		System.out.println("amount: " + amount);
		System.out.println("in total we added : " + coupon + "coupon");
		
		if(exptvalue==user.getCoupon() && user.getCoupon()==amount){
			//Passed;
		}else fail();
	}	
	
}




