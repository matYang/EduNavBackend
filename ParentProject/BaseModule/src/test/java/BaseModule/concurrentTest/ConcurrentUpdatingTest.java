package BaseModule.concurrentTest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.dbservice.CouponDaoService;
import BaseModule.dbservice.CreditDaoService;
import BaseModule.dbservice.UserDaoService;
import BaseModule.eduDAO.BookingDao;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.BookingNotFoundException;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.exception.notFound.CreditNotFoundException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
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
		private String layer;
		public TestThread(CountDownLatch threadsSignal,ArrayList<?> list,String kind,String layer) {  
			this.threadsSignal = threadsSignal;			
			this.kind = kind;
			this.list = list;
			this.layer = layer;
		}

		@Override  
		public void run() {
			try{
				//System.out.println("Thread: " + this.index + " starts inner loop");
				int i = 0;
				while(i < list.size()){
					if(kind.equals("Booking")){	
						if(layer.equals("Dao")){
							try {
								BookingDao.updateBookingInDatabases((Booking)list.get(i), conn);
							} catch (BookingNotFoundException | SQLException e) {							
								e.printStackTrace();
							}
						}else if(layer.equals("Service")){								
							try {
								BookingDaoService.updateBooking((Booking)list.get(i), ((Booking)list.get(i)).getStatus(), ((Booking)list.get(i)).getAdminId());
							} catch ( PseudoException | SQLException e) {

								e.printStackTrace();
							}
						}else if(layer.equals("DaoBookingRead")){							
							String name,phone,email,reference;
							int price;							
							try {
//								System.out.println("");
//								System.out.println("Booking Reading Started..." + " Time right now is: " + DateUtility.getTimeStamp());
//								System.out.println("");
								Booking booking = BookingDao.getBookingById(((Booking)list.get(i)).getBookingId(), conn);
//								System.out.println("Booking Reading Finished..." + " Time right now is: " + DateUtility.getTimeStamp());
//								System.out.println("");
								name = booking.getName();
								phone = booking.getPhone();
								email = booking.getEmail();
								reference = booking.getReference();
								price = booking.getPrice();									
//								System.out.println("Booking id: " + ((Booking)list.get(i)).getBookingId() + " Name: " + name + " Time right now is: " + DateUtility.getTimeStamp());
//								System.out.println("Booking id: " + ((Booking)list.get(i)).getBookingId() + " Email: " + email + " Time right now is: " + DateUtility.getTimeStamp());
//								System.out.println("Booking id: " + ((Booking)list.get(i)).getBookingId() + " Phone: " + phone + " Time right now is: " + DateUtility.getTimeStamp());
//								System.out.println("Booking id: " + ((Booking)list.get(i)).getBookingId() + " Price: " + price + " Time right now is: " + DateUtility.getTimeStamp());
//								System.out.println("Booking id: " + ((Booking)list.get(i)).getBookingId() + " Reference: " + reference + " Time right now is: " + DateUtility.getTimeStamp());
//								System.out.println("");
							}catch (BookingNotFoundException e) {								
								e.printStackTrace();
							}						
						}else if(layer.equals("DaoBookingUpdate")){
							try {
//								System.out.println("");
//								System.out.println("Booking Updating Started..." + " Time right now is: " + DateUtility.getTimeStamp());
								BookingDao.updateBookingInDatabases((Booking)list.get(i), conn);
//								System.out.println("");
//								System.out.println("Booking Updating Finished..." + " Time right now is: " + DateUtility.getTimeStamp());
//								System.out.println("");
							} catch (BookingNotFoundException | SQLException e) {							
								e.printStackTrace();
							}
						}				 

					}else if(kind.equals("Credit")){
						if(layer.equals("Dao")){
							try {
								CreditDao.updateCreditInDatabases((Credit)list.get(i), conn);
							} catch (CreditNotFoundException | SQLException e) {							
								e.printStackTrace();
							}
						}else if(layer.equals("Service")){
							try {
								CreditDaoService.updateCredit((Credit)list.get(i), conn);
							} catch (SQLException | PseudoException e) {								
								e.printStackTrace();
							}
						}

					}else if(kind.equals("Coupon")){
						if(layer.equals("Dao")){
							try {
								CouponDao.updateCouponInDatabases((Coupon)list.get(i), conn);
							} catch (CouponNotFoundException | SQLException e) {							
								e.printStackTrace();
							}
						}else if(layer.equals("Service")){
							try {
								CouponDaoService.updateCoupon((Coupon)list.get(i),conn);
							} catch (SQLException | PseudoException e) {								
								e.printStackTrace();
							}
						}
					}else if(kind.equals("User")){						
						if(layer.equals("Dao")){
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
						}else if(layer.equals("Service")){
							try {
								UserDaoService.updateUser((User)list.get(i),conn);
							} catch (SQLException | PseudoException e) {								
								e.printStackTrace();
							}
						}

					}

					i++;
				}
				//System.out.println("Thread: " + this.index + " finishes");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally{
				threadsSignal.countDown();
				if(layer.equals("DaoC")){
					System.out.println("thread Num: " + threadsSignal.getCount());
				}				
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
//		System.out.println("Num for Booking Test: " + blist.size());
//		System.out.println("");
//		System.out.println("Dao: Before Updating");
//		for(int i = 0 ; i < blist.size() ; i++){
//			System.out.println("Booking id: " + blist.get(i).getBookingId() + " Name: " + blist.get(i).getName());
//			blist.get(i).setName("Booking " + i);
//		}		
		int threadNum = 1000;
		CountDownLatch threadSignal = new CountDownLatch(threadNum);
//		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
//
//		for (int i = 0; i < threadNum; i++){
//			Thread testRun = new TestThread(threadSignal, blist,"Booking","Dao");
//			testRun.start();
//		}
//		threadSignal.await();
//		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
//		System.out.println("");	
		//////////////////////////////////////////////////		
		System.out.println("Before Updating: ");
		ArrayList<Booking> blist2 = new ArrayList<Booking>();
		for(int i = 0 ; i < blist.size() ; i++){
			blist2.add(blist.get(0));
		}
		for(int i = 0 ; i < blist2.size() ; i++){
			System.out.println("Booking id: " + blist2.get(i).getBookingId() + " Name: " + blist2.get(i).getName());
			if(i==0){
				System.out.println("Booking id: " + blist2.get(i).getBookingId() + " Email: " + blist2.get(i).getEmail());
				blist2.get(i).setEmail("EmailTest@xxx");
			}else if(i==1){
				System.out.println("Booking id: " + blist2.get(i).getBookingId() + " Phone: " + blist2.get(i).getPhone());
				blist2.get(i).setPhone("PhoneTest");
			}else if(i==2){
				System.out.println("Booking id: " + blist2.get(i).getBookingId() + " Price: " + blist2.get(i).getPrice());
				blist2.get(i).setPrice(111);
			}else if(i==3){
				System.out.println("Booking id: " + blist2.get(i).getBookingId() + " Reference: " + blist2.get(i).getReference());
				blist2.get(i).setReference("TOKYO HOT");
			}
		}
		int smallThreadNum = 1000;
		threadSignal = new CountDownLatch(smallThreadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));

		for (int i = 0; i < smallThreadNum; i++){
			Thread testRun = new TestThread(threadSignal, blist2,"Booking","DaoBookingRead");
			Thread testRun2 = new TestThread(threadSignal, blist2,"Booking","DaoBookingUpdate");
			testRun.start();
			testRun2.start();
		}
		
		threadSignal.await();
		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		System.out.println("");	
		System.out.println("After Updating");
		for(int i = 0 ; i < blist2.size() ; i++){
			System.out.println("Booking id: " + blist2.get(i).getBookingId() + " Name: " + blist2.get(i).getName());
			if(i==0){
				System.out.println("Booking id: " + blist2.get(i).getBookingId() + " Email: " + blist2.get(i).getEmail());
			}else if(i==1){
				System.out.println("Booking id: " + blist2.get(i).getBookingId() + " Phone: " + blist2.get(i).getPhone());
			}else if(i==2){
				System.out.println("Booking id: " + blist2.get(i).getBookingId() + " Price: " + blist2.get(i).getPrice());
			}else if(i==3){
				System.out.println("Booking id: " + blist2.get(i).getBookingId() + " Reference: " + blist2.get(i).getReference());
			}
		}
		System.out.println("");
		/////////////////////////////////////////////////
//		System.out.println("Dao: After Updating");
//		blist = BookingDao.getAllBookings();
//		for(int i = 0 ; i < blist.size() ; i++){
//			System.out.println("Booking id: " + blist.get(i).getBookingId() + " Name: " + blist.get(i).getName());			
//		}
//		System.out.println("");
//		System.out.println("Service: Before Updating");
//		for(int i = 0 ; i < blist.size() ; i++){
//			System.out.println("Booking id: " + blist.get(i).getBookingId() + " Email: " + blist.get(i).getEmail());
//			blist.get(i).setEmail(blist.get(i).getName() + "@hotmail.com");
//		}		
//		threadSignal = new CountDownLatch(threadNum);
//		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
//
//		for (int i = 0; i < threadNum; i++){
//			Thread testRun = new TestThread(threadSignal, blist,"Booking","Service");
//			testRun.start();
//		}
//		threadSignal.await();
//		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
//		System.out.println("");		
//		System.out.println("Service: After Updating");
//		blist = BookingDao.getAllBookings();
//		for(int i = 0 ; i < blist.size() ; i++){
//			System.out.println("Booking id: " + blist.get(i).getBookingId() + " Email: " + blist.get(i).getEmail());			
//		}


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
		System.out.println("Dao: Before Updating");
		for(int i = 0 ; i < clist.size() ; i++){
			System.out.println("Credit id: " + clist.get(i).getCreditId() + " amount: " + clist.get(i).getAmount());
			clist.get(i).setAmount(5);
		}		

		threadSignal = new CountDownLatch(threadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < threadNum; i++){
			Thread testRun = new TestThread(threadSignal, clist,"Credit","Dao");
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

		System.out.println("Dao: After Updating");
		for(int i = 0 ; i < clist.size() ; i++){
			System.out.println("Credit id: " + clist.get(i).getCreditId() + " amount: " + clist.get(i).getAmount());
		}
		System.out.println("");
		System.out.println("Service: Before Updating");
		for(int i = 0 ; i < clist.size() ; i++){
			System.out.println("Credit id: " + clist.get(i).getCreditId() + " status: " + clist.get(i).getStatus().toString());
			clist.get(i).setStatus(CreditStatus.expired);
		}		

		threadSignal = new CountDownLatch(threadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < threadNum; i++){
			Thread testRun = new TestThread(threadSignal, clist,"Credit","Service");
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

		System.out.println("Service: After Updating");
		for(int i = 0 ; i < clist.size() ; i++){
			System.out.println("Credit id: " + clist.get(i).getCreditId() + " status: " + clist.get(i).getStatus().toString());
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
		System.out.println("Dao: Before Updating");
		for(int i = 0 ; i < culist.size() ; i++){
			System.out.println("Coupon id: " + culist.get(i).getCouponId() + " amount: " + culist.get(i).getAmount());
			culist.get(i).setAmount(20);
		}		

		threadSignal = new CountDownLatch(threadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < threadNum; i++){
			Thread testRun = new TestThread(threadSignal, culist,"Coupon","Dao");
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

		System.out.println("Dao: After Updating");
		for(int i = 0 ; i < culist.size() ; i++){
			System.out.println("Coupon id: " + culist.get(i).getCouponId() + " amount: " + culist.get(i).getAmount());
		}
		System.out.println("");
		System.out.println("Service: Before Updating");
		for(int i = 0 ; i < culist.size() ; i++){
			System.out.println("Coupon id: " + culist.get(i).getCouponId() + " status: " + culist.get(i).getStatus().toString());
			culist.get(i).setStatus(CouponStatus.used);
		}		

		threadSignal = new CountDownLatch(threadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < threadNum; i++){
			Thread testRun = new TestThread(threadSignal, culist,"Coupon","Service");
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

		System.out.println("Service: After Updating");
		for(int i = 0 ; i < culist.size() ; i++){
			System.out.println("Coupon id: " + culist.get(i).getCouponId() + " status: " + culist.get(i).getStatus().toString());
		}

		System.out.println("");

		System.out.println("Test User");

		ArrayList<User> ulist = new ArrayList<User>();

		ulist = UserDao.getAllUsers();
		System.out.println("Num for User Test: " + ulist.size());
		System.out.println("");
		System.out.println("Dao: Before Updating");
		for(int i = 0 ; i < ulist.size() ; i++){
			System.out.println("User id: " + ulist.get(i).getUserId() + " balance: " + ulist.get(i).getBalance() + " credit: " + ulist.get(i).getCredit() + " coupon: " + ulist.get(i).getCoupon());
			ulist.get(i).setName("User " + i);
		}
		conn = EduDaoBasic.getSQLConnection();
		threadSignal = new CountDownLatch(threadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < threadNum; i++){
			Thread testRun = new TestThread(threadSignal, ulist,"User","Dao");
			testRun.start();
		}
		threadSignal.await();
		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));		
		System.out.println("");

		System.out.println("Dao: After Updating");
		ulist = UserDao.getAllUsers();
		for(int i = 0 ; i < ulist.size() ; i++){
			System.out.println("User id: " + ulist.get(i).getUserId() + " balance: " + ulist.get(i).getBalance() + " credit: " + ulist.get(i).getCredit() + " coupon: " + ulist.get(i).getCoupon());
		}
		System.out.println("");
		System.out.println("Service: Before Updating");
		for(int i = 0 ; i < ulist.size() ; i++){
			System.out.println("User id: " + ulist.get(i).getUserId() + " phone: " + ulist.get(i).getPhone());
			ulist.get(i).setPhone(ulist.get(i).getPhone()+ulist.get(i).getName());
		}
		conn = EduDaoBasic.getSQLConnection();
		threadSignal = new CountDownLatch(threadNum);
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < threadNum; i++){
			Thread testRun = new TestThread(threadSignal, ulist,"User","Service");
			testRun.start();
		}
		threadSignal.await();
		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));		
		System.out.println("");

		System.out.println("Service: After Updating");
		ulist = UserDao.getAllUsers();
		for(int i = 0 ; i < ulist.size() ; i++){
			System.out.println("User id: " + ulist.get(i).getUserId() + " phone: " + ulist.get(i).getPhone());
		}

		System.out.println("");

		System.out.println("All the Test Finished");



	}


}
