package BaseModule.concurrentTest;

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
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Booking;
import BaseModule.model.Coupon;
import BaseModule.model.User;


public class CocurrentCreatingTest {

	public class TestThread extends Thread {  
		private CountDownLatch threadsSignal;		
		private Connection conn = null;	
		private ArrayList<?> list;
		private String kind;
		private String layer;
		public TestThread(CountDownLatch threadsSignal,ArrayList<?> list,String kind,String layer,Connection connections) {  
			this.threadsSignal = threadsSignal;			
			this.kind = kind;
			this.list = list;
			this.layer = layer;
			this.conn = EduDaoBasic.getConnection(connections);
		}

		@Override  
		public void run() {
			try{
				int i = 0;
				while(i < list.size()){
					if(kind.equals("Booking")){	
						if(layer.equals("Dao")){							
							try {
								BookingDao.addBookingToDatabases((Booking)list.get(i), conn);
							} catch (SQLException | PseudoException e) {									
								e.printStackTrace();
							}

						}
					}else if(kind.equals("Coupon")){
						if(layer.equals("Dao")){
							try{								
								CouponDao.addCouponToDatabases((Coupon)list.get(i), conn);
								if(((Coupon)list.get(i)).getStatus().code==CouponStatus.usable.code &&
										DateUtility.toSQLDateTime(((Coupon)list.get(i)).getExpireTime()).compareTo(
										DateUtility.toSQLDateTime(DateUtility.getCurTimeInstance()))>0){
									UserDao.updateUserBCC(0, 0, ((Coupon)list.get(i)).getAmount(), ((Coupon)list.get(i)).getUserId(), conn);
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}

					i++;
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally{
				threadsSignal.countDown();
				EduDaoBasic.closeResources(conn, null, null, false);
			}
		}  
	}

	@Test
	public void testBenchMark() throws PseudoException, SQLException, InterruptedException{
		EduDaoBasic.clearAllDatabase();
		Connection conn = null;
		try{
			conn = EduDaoBasic.getConnection();
			String name = "Harry";
			String userphone = "12345612312";
			String password = "36krfinal";
			AccountStatus status = AccountStatus.activated;
			String email = "xiongchuhan@hotmail.com";			
			User user = new User(userphone, password,status);
			user.setName(name);
			user.setEmail(email);		
			UserDao.addUserToDatabase(user,conn);
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
				CouponDao.addCouponToDatabases(c, conn);
				clist.add(c);

				//70
				Coupon c2 = new Coupon(userId,cashbackAmount + 20);		
				expireTime = DateUtility.getCurTimeInstance();
				expireTime.add(Calendar.DAY_OF_MONTH, 15);
				c2.setExpireTime(expireTime);
				CouponDao.addCouponToDatabases(c2, conn);
				clist.add(c2);

				//100
				Coupon c3 = new Coupon(userId,cashbackAmount + 50);		
				expireTime = DateUtility.getCurTimeInstance();
				expireTime.add(Calendar.DAY_OF_MONTH, -15);		
				c3.setExpireTime(expireTime);
				CouponDao.addCouponToDatabases(c3, conn);
				clist.add(c3);

				//66
				Coupon c4 = new Coupon(userId,cashbackAmount + 11);		
				expireTime = DateUtility.getCurTimeInstance();
				expireTime.add(Calendar.MINUTE, -15);
				c4.setExpireTime(expireTime);
				CouponDao.addCouponToDatabases(c4, conn);
				clist.add(c4);

				//89
				Coupon c5 = new Coupon(userId,cashbackAmount + 39);		
				expireTime = DateUtility.getCurTimeInstance();
				expireTime.add(Calendar.MINUTE, 1);
				c5.setExpireTime(expireTime);
				CouponDao.addCouponToDatabases(c5, conn);
				clist.add(c5);

				//1
				Coupon c6 = new Coupon(userId,cashbackAmount - 49);		
				expireTime = DateUtility.getCurTimeInstance();
				expireTime.add(Calendar.DAY_OF_YEAR, 2);
				c6.setExpireTime(expireTime);
				CouponDao.addCouponToDatabases(c6, conn);
				clist.add(c6);

				//100
				Coupon c7 = new Coupon(userId,cashbackAmount + 50);		
				expireTime = DateUtility.getCurTimeInstance();
				expireTime.add(Calendar.DAY_OF_MONTH, -15);
				c7.setExpireTime(expireTime);				
				c7.setStatus(CouponStatus.expired);
				CouponDao.addCouponToDatabases(c7, conn);
				clist.add(c7);

				//66
				Coupon c8 = new Coupon(userId,cashbackAmount + 11);		
				expireTime = DateUtility.getCurTimeInstance();
				expireTime.add(Calendar.MINUTE, -15);
				c8.setExpireTime(expireTime);
				c8.setStatus(CouponStatus.used);
				CouponDao.addCouponToDatabases(c8, conn);
				clist.add(c8);

				//89
				Coupon c9 = new Coupon(userId,cashbackAmount + 39);		
				expireTime = DateUtility.getCurTimeInstance();
				expireTime.add(Calendar.MINUTE, 1);
				c9.setExpireTime(expireTime);
				c9.setStatus(CouponStatus.expired);
				CouponDao.addCouponToDatabases(c9, conn);
				clist.add(c9);

				//2
				Coupon c10 = new Coupon(userId,cashbackAmount - 48);		
				expireTime = DateUtility.getCurTimeInstance();
				expireTime.add(Calendar.DAY_OF_YEAR, 2);
				c10.setExpireTime(expireTime);
				c10.setStatus(CouponStatus.usable);
				CouponDao.addCouponToDatabases(c10, conn);
				clist.add(c10);

				UserDao.updateUserBCC(0, 0, 212, userId);

				int backcash = 211;//剩一块

				Booking booking = new Booking(timeStamp,timeStamp,price,
						userId, partnerId, courseId, user.getName(), phone,
						email,reference,BookingStatus.confirmed,backcash);

				blist.add(booking);
			}		

			int threadNum = 1;
			CountDownLatch threadSignal = new CountDownLatch(threadNum);

			System.out.println("Test Begin");
			user = UserDao.getUserById(userId, conn);
			System.out.println("user coupon start value: " + user.getCoupon());
			System.out.println("");
			System.out.println("Start a booking for user 1");

			Thread bookingThread = new TestThread(threadSignal, blist,"Booking","Dao",conn);	

			/////////////////////
			bookingThread.start();
			////////////////////

			System.out.println("Start adding coupons to user 1");
			System.out.println("");

			Thread couponThread = new TestThread(threadSignal, clist,"Coupon","Dao",conn);	

			////////////////////
			couponThread.start();
			///////////////////

			threadSignal.await();
			System.out.println("Test End");
			user = UserDao.getUserById(userId, conn);
			System.out.println("user coupon final value: " + user.getCoupon());
		}finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}

	}

}
