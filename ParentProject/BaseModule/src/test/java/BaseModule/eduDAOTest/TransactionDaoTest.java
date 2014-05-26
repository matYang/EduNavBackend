package BaseModule.eduDAOTest;

import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.Test;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.TransactionDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Transaction;
import BaseModule.model.User;

public class TransactionDaoTest {

	@Test
	public void testAdd() throws ValidationException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(name, phone, password,status);
		UserDao.addUserToDatabase(user);
		int bookingId = 1;
		int amount = 20;
		Transaction transaction = new Transaction(user.getUserId(),bookingId,amount);
		try{
			transaction = TransactionDao.addTransactionToDatabases(transaction);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}		
	}
	
	@Test
	public void testGet(){
		EduDaoBasic.clearBothDatabase();
		int userId = 1;
		int bookingId = 1;
		int amount = 20;
		Transaction transaction = new Transaction(userId,bookingId,amount);
		transaction = TransactionDao.addTransactionToDatabases(transaction);
		
		int userId2 = 2;
		int bookingId2 =2;
		int amount2 = -234;
		Transaction transaction2 = new Transaction(userId2,bookingId2,amount2);
		transaction2 = TransactionDao.addTransactionToDatabases(transaction2);
		
		int userId3 = 3;
		int bookingId3 =3;
		int amount3 = 23454354;
		long couponId = 1;
		Transaction transaction3 = new Transaction(userId3,bookingId3,amount3);
		transaction3.setTransactionType(TransactionType.coupon);
		transaction3.setCouponId(couponId);
		transaction3 = TransactionDao.addTransactionToDatabases(transaction3);		
		
		ArrayList<Transaction> tlist = new ArrayList<Transaction>();
		tlist = TransactionDao.getTransactionById(userId, "user");
		if(tlist.size() == 1 && tlist.get(0).equals(transaction)){
			//Passed;
		}else fail();
		
		tlist = TransactionDao.getTransactionById(bookingId2, "booking");
		if(tlist.size() == 1 && tlist.get(0).equals(transaction2)){
			//Passed;
		}else fail();
		
		tlist = TransactionDao.getTransactionById(transaction3.getTransactionId(), "transaction");
		if(tlist.size() == 1 && tlist.get(0).equals(transaction3)){
			//Passed;
		}else fail();
				
	}
}

