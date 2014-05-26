package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.eduDAO.TransactionDao;
import BaseModule.model.Transaction;

public class TransactionDaoService {

	public static Transaction createTransaction(Transaction transaction){
		return TransactionDao.addTransactionToDatabases(transaction);
	}
	
	public static ArrayList<Transaction> getTransactionsByUserId(int userId){
		return TransactionDao.getTransactionById(userId, "user");
	}
	
	public static ArrayList<Transaction> getTransactionsByBookingId(int bookingId){
		return TransactionDao.getTransactionById(bookingId, "booking");
	}
	
	public static ArrayList<Transaction> getTransactionByTransactionId(int transactionId){
		return TransactionDao.getTransactionById(transactionId, "transaction");
	}
	
	public static ArrayList<Transaction> getTransactionByCouponId(long couponId){
		return TransactionDao.getTransactionByCouponId(couponId);
	}
	
}
