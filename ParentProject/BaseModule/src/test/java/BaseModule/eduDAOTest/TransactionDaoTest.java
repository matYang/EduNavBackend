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
import BaseModule.model.representation.TransactionSearchRepresentation;

public class TransactionDaoTest {

	@Test
	public void testAdd() throws SQLException, PseudoException{
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
		int amount = 20;
		Transaction transaction = new Transaction(user.getUserId(),bookingId,amount,TransactionType.deposit);
		try{
			transaction = TransactionDao.addTransactionToDatabases(transaction);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}		
	}
	
	@Test
	public void testSearch() throws SQLException{
		EduDaoBasic.clearAllDatabase();
		int userId = 1;
		int bookingId = 1;
		int amount = 20;
		Transaction transaction = new Transaction(userId,bookingId,amount,TransactionType.deposit);		
		transaction.setTransactionType(TransactionType.withdraw);
		TransactionDao.addTransactionToDatabases(transaction);
	
		userId = 2;
		bookingId = 1;
		amount = 200;
		Transaction transaction2 = new Transaction(userId,bookingId,amount,TransactionType.deposit);		
		transaction2.setTransactionType(TransactionType.deposit);
		TransactionDao.addTransactionToDatabases(transaction2);
		
		userId = 1;
		bookingId = 1;
		amount = 50;
		Transaction transaction3 = new Transaction(userId,bookingId,amount,TransactionType.deposit);		
		transaction3.setTransactionType(TransactionType.withdraw);
		TransactionDao.addTransactionToDatabases(transaction3);
		
		ArrayList<Transaction> tlist = new ArrayList<Transaction>();
		TransactionSearchRepresentation tsr = new TransactionSearchRepresentation();
		tsr.setUserId(1);
		tsr.setFinishPrice(1999);
		tlist = TransactionDao.searchTransaction(tsr);
		
		if(tlist.size()==2&&tlist.get(0).equals(transaction)&&tlist.get(1).equals(transaction3)){
			//Passed;
		}else fail();
		
		tsr.setStartPrice(50);
		tlist = TransactionDao.searchTransaction(tsr);
		
		if(tlist.size()==1&&tlist.get(0).equals(transaction3)){
			//Passed;
		}else fail();
		
		tsr.setUserId(-1);
		tsr.setStartPrice(-1);		
		tsr.setTransactionType(TransactionType.withdraw);
		tlist = TransactionDao.searchTransaction(tsr);
		if(tlist.size()==2&&tlist.get(0).equals(transaction)&&tlist.get(1).equals(transaction3)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testGet() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		int userId = 1;
		int bookingId = 1;
		int amount = 20;
		Transaction transaction = new Transaction(userId,bookingId,amount,TransactionType.deposit);
		transaction = TransactionDao.addTransactionToDatabases(transaction);
		
		int userId2 = 2;
		int bookingId2 =2;
		int amount2 = -234;
		Transaction transaction2 = new Transaction(userId2,bookingId2,amount2,TransactionType.deposit);
		transaction2 = TransactionDao.addTransactionToDatabases(transaction2);
		
		int userId3 = 3;
		int bookingId3 =3;
		int amount3 = 23454354;
		long couponId = 1L;
		Transaction transaction3 = new Transaction(userId3,bookingId3,amount3,TransactionType.deposit);
		transaction3.setTransactionType(TransactionType.cashback);		
		transaction3 = TransactionDao.addTransactionToDatabases(transaction3);		
		couponId = 1;		
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

