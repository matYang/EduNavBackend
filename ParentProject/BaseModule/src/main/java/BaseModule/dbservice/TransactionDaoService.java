package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.TransactionDao;
import BaseModule.exception.notFound.TransactionNotFoundException;
import BaseModule.model.Transaction;
import BaseModule.model.representation.TransactionSearchRepresentation;

public class TransactionDaoService {

	public static Transaction createTransaction(Transaction transaction,Connection...connections) throws SQLException{
		return TransactionDao.addTransactionToDatabases(transaction,connections);
	}
	
	public static Transaction getTransactionByTransactionId(int transactionId) throws TransactionNotFoundException{
		return TransactionDao.getTransactionById(transactionId);
	}
	
	
	public static ArrayList<Transaction> getTransactionsByUserId(int userId,Connection...connections){
		TransactionSearchRepresentation t_sr = new TransactionSearchRepresentation();
		t_sr.setUserId(userId);
		return searchTransaction(t_sr, connections);
	}
	
	
	public static ArrayList<Transaction> getTransactionsByBookingId(int bookingId,Connection...connections){
		TransactionSearchRepresentation t_sr = new TransactionSearchRepresentation();
		t_sr.setBookingId(bookingId);
		return searchTransaction(t_sr, connections);
	}
	
	public static ArrayList<Transaction> getTransactionByCouponId(long couponId,Connection...connections){
		TransactionSearchRepresentation t_sr = new TransactionSearchRepresentation();
		t_sr.setCouponId(couponId);
		return searchTransaction(t_sr, connections);
	}
	
	public static ArrayList<Transaction> searchTransaction(TransactionSearchRepresentation t_sr,Connection...connections){
		return null;
	}
	
}
