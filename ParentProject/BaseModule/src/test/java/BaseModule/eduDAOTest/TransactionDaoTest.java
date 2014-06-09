package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.TransactionDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.model.Transaction;
import BaseModule.model.User;

public class TransactionDaoTest {

	@Test
	public void testAdd() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		User user = new User(phone, password,status);
		user.setName(name);
		user.setEmail(email);
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
	public void testGet() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
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
		long couponId = 1L;
		Transaction transaction3 = new Transaction(userId3,bookingId3,amount3);
		transaction3.setTransactionType(TransactionType.coupon);
		transaction3.setCouponId(couponId);
		transaction3 = TransactionDao.addTransactionToDatabases(transaction3);		
		couponId = 1;
		transaction3.setCouponId(couponId);
		ArrayList<Transaction> tlist = new ArrayList<Transaction>();
		tlist = TransactionDao.getTransactionByUserId(userId);
		if(tlist.size() == 1 && tlist.get(0).equals(transaction)){
			//Passed;
		}else fail();
		

		Transaction storedTransaction = TransactionDao.getTransactionById(transaction3.getTransactionId());
		if(storedTransaction.equals(transaction3)){
			//Passed;
		}else fail();
		
				
	}
}

