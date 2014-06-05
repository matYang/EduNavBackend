package BaseModule.concurrentTest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import BaseModule.common.DateUtility;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.booking.BookingNotFoundException;
import BaseModule.exception.coupon.CouponNotFoundException;
import BaseModule.exception.credit.CreditNotFoundException;
import BaseModule.exception.user.UserNotFoundException;
import BaseModule.model.Booking;
import BaseModule.model.Coupon;
import BaseModule.model.Credit;
import BaseModule.model.User;
import BaseModule.service.ModelDataLoaderService;



public class ConcurrentUpdatingTest {

	public class TestThread extends Thread {  
		private CountDownLatch threadsSignal;		
		private Connection conn = EduDaoBasic.getSQLConnection();		
		private ArrayList<?> list;
		private String kind;

		public TestThread(CountDownLatch threadsSignal,ArrayList<?> list,String kind) {  
			this.threadsSignal = threadsSignal;			
			this.kind = kind;
			this.list = list;
		}

		@Override  
		public void run() {
			try{
				//System.out.println("Thread: " + this.index + " starts inner loop");
				int i = 0;
				while(i < list.size()){
					if(kind.equals("Booking")){						
						try {
							BookingDao.updateBookingInDatabases((Booking)list.get(i), conn);
						} catch (BookingNotFoundException | SQLException e) {							
							e.printStackTrace();
						}
					}else if(kind.equals("Credit")){
						try {
							CreditDao.updateCreditInDatabases((Credit)list.get(i), conn);
						} catch (CreditNotFoundException | SQLException e) {							
							e.printStackTrace();
						}
					}else if(kind.equals("Coupon")){
						try {
							CouponDao.updateCouponInDatabases((Coupon)list.get(i), conn);
						} catch (CouponNotFoundException | SQLException e) {							
							e.printStackTrace();
						}

					}else if(kind.equals("User")){
						int count = (int) DateUtility.getCurTime();
						int balance = count % 2 == 0 ? 10 : -10;
						int credit = count % 2 == 0 ? 5 : -5;
						int coupon = count % 2 == 0 ? 20 : -20;
						try {
							//System.out.println("Count: "+ count + " User id: " + ((User)list.get(i)).getUserId() + " add balance: " + balance + " credit: " + credit + " coupon: " + coupon);
							UserDao.updateUserBCC(balance, credit, coupon, ((User)list.get(i)).getUserId(), conn);
						} catch (UserNotFoundException | SQLException e) {
							e.printStackTrace();
						}
					}
					
					i++;
				}
				//System.out.println("Thread: " + this.index + " finishes");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally{
				threadsSignal.countDown();
				EduDaoBasic.closeResources(conn, null, null, true);
			}
		}  
	}


	@Test
	public void testBenchMark() throws InterruptedException{	
		System.out.println("Loading model...");
		ModelDataLoaderService.load();
		
		System.out.println("");
		System.out.println("Test Booking");
		
		ArrayList<Booking> blist = BookingDao.getAllBookings();
		System.out.println("Num for Booking Test: " + blist.size());
		System.out.println("");
		System.out.println("Before Updating");
		for(int i = 0 ; i < blist.size() ; i++){
			System.out.println("Booking id: " + blist.get(i).getBookingId() + " Name: " + blist.get(i).getName());
			blist.get(i).setName("Booking " + i);
		}
		
		int threadNum = 1000;
		CountDownLatch threadSignal = new CountDownLatch(threadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		
		for (int i = 0; i < threadNum; i++){
			Thread testRun = new TestThread(threadSignal, blist,"Booking");
			testRun.start();
		}
		threadSignal.await();
		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		System.out.println("");		
		System.out.println("After Updating");
		blist = BookingDao.getAllBookings();
		for(int i = 0 ; i < blist.size() ; i++){
			System.out.println("Booking id: " + blist.get(i).getBookingId() + " Name: " + blist.get(i).getName());			
		}
		
		System.out.println("");
		
		System.out.println("Test Credit");		
		ArrayList<Credit> clist = new ArrayList<Credit>();
		ArrayList<Credit> clistTest = new ArrayList<Credit>();
		Connection conn = EduDaoBasic.getSQLConnection();
		clistTest = CreditDao.getCreditByUserId(1, conn);
		clist.addAll(clistTest);
		
		clistTest = CreditDao.getCreditByUserId(2, conn);
		clist.addAll(clistTest);
		
		clistTest = CreditDao.getCreditByUserId(3, conn);
		clist.addAll(clistTest);
		
		clistTest = CreditDao.getCreditByUserId(4, conn);
		clist.addAll(clistTest);
		EduDaoBasic.closeResources(conn, null, null, true);
		
		System.out.println("Num for Credit Test: " + clist.size());
		System.out.println("");
		System.out.println("Before Updating");
		for(int i = 0 ; i < clist.size() ; i++){
			System.out.println("Credit id: " + clist.get(i).getCreditId() + " amount: " + clist.get(i).getAmount());
			clist.get(i).setAmount(5);
		}		
		
		threadSignal = new CountDownLatch(threadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < threadNum; i++){
			Thread testRun = new TestThread(threadSignal, clist,"Credit");
			testRun.start();
		}
		threadSignal.await();
		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		
		System.out.println("");
		
		clist = new ArrayList<Credit>();
		conn = EduDaoBasic.getSQLConnection();
		clistTest = CreditDao.getCreditByUserId(1, conn);
		clist.addAll(clistTest);
		
		clistTest = CreditDao.getCreditByUserId(2, conn);
		clist.addAll(clistTest);
		
		clistTest = CreditDao.getCreditByUserId(3, conn);
		clist.addAll(clistTest);
		
		clistTest = CreditDao.getCreditByUserId(4, conn);
		clist.addAll(clistTest);
		
		System.out.println("After Updating");
		for(int i = 0 ; i < clist.size() ; i++){
			System.out.println("Credit id: " + clist.get(i).getCreditId() + " amount: " + clist.get(i).getAmount());
		}
		
		System.out.println("");
		
		System.out.println("Test Coupon");
		
		ArrayList<Coupon> culist = new ArrayList<Coupon>();
		ArrayList<Coupon> culistTest = null;
		
		culistTest = CouponDao.getCouponByUserId(1, conn);
		culist.addAll(culistTest);
		
		culistTest = CouponDao.getCouponByUserId(2, conn);
		culist.addAll(culistTest);
		
		culistTest = CouponDao.getCouponByUserId(3, conn);
		culist.addAll(culistTest);
		
		culistTest = CouponDao.getCouponByUserId(4, conn);
		culist.addAll(culistTest);
		EduDaoBasic.closeResources(conn, null, null, true);
		System.out.println("NUM for Coupon Test: " + culist.size());
		System.out.println("");
		System.out.println("Before Updating");
		for(int i = 0 ; i < culist.size() ; i++){
			System.out.println("Coupon id: " + culist.get(i).getCouponId() + " amount: " + culist.get(i).getAmount());
			culist.get(i).setAmount(20);
		}		
		
		threadSignal = new CountDownLatch(threadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < threadNum; i++){
			Thread testRun = new TestThread(threadSignal, culist,"Coupon");
			testRun.start();
		}
		threadSignal.await();
		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		
		System.out.println("");		
		
		culist = new ArrayList<Coupon>();		
		conn = EduDaoBasic.getSQLConnection();
		culistTest = CouponDao.getCouponByUserId(1, conn);
		culist.addAll(culistTest);
		
		culistTest = CouponDao.getCouponByUserId(2, conn);
		culist.addAll(culistTest);
		
		culistTest = CouponDao.getCouponByUserId(3, conn);
		culist.addAll(culistTest);
		
		culistTest = CouponDao.getCouponByUserId(4, conn);
		culist.addAll(culistTest);
		
		EduDaoBasic.closeResources(conn, null, null, true);
		
		System.out.println("After Updating");
		for(int i = 0 ; i < culist.size() ; i++){
			System.out.println("Coupon id: " + culist.get(i).getCouponId() + " amount: " + culist.get(i).getAmount());
		}
		
		System.out.println("");
		
		System.out.println("Test User");
		
		ArrayList<User> ulist = new ArrayList<User>();
		
		ulist = UserDao.getAllUsers();
		System.out.println("Num for User Test: " + ulist.size());
		System.out.println("");
		System.out.println("Before Updating");
		for(int i = 0 ; i < ulist.size() ; i++){
			System.out.println("User id: " + ulist.get(i).getUserId() + " balance: " + ulist.get(i).getBalance() + " credit: " + ulist.get(i).getCredit() + " coupon: " + ulist.get(i).getCoupon());
			ulist.get(i).setName("User " + i);
		}
		conn = EduDaoBasic.getSQLConnection();
		threadSignal = new CountDownLatch(threadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < threadNum; i++){
			Thread testRun = new TestThread(threadSignal, ulist,"User");
			testRun.start();
		}
		threadSignal.await();
		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));		
		System.out.println("");
		
		System.out.println("After Updating");
		ulist = UserDao.getAllUsers();
		for(int i = 0 ; i < ulist.size() ; i++){
			System.out.println("User id: " + ulist.get(i).getUserId() + " balance: " + ulist.get(i).getBalance() + " credit: " + ulist.get(i).getCredit() + " coupon: " + ulist.get(i).getCoupon());
		}
		
		System.out.println("");
		
		System.out.println("All the Test Finished");
		
		
		
	}
	
	
}
