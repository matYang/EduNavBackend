package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.TransactionDao;
import BaseModule.exception.PseudoException;
import BaseModule.model.Transaction;
import BaseModule.model.representation.TransactionSearchRepresentation;

public class TransactionDaoService {

	public static Transaction createTransaction(Transaction transaction,Connection...connections) throws SQLException{
		return TransactionDao.addTransactionToDatabases(transaction,connections);
	}
	
	public static Transaction getTransactionByTransactionId(long transactionId,Connection...connections) throws PseudoException, SQLException{
		return TransactionDao.getTransactionById(transactionId, connections);
	}
	
	
	public static ArrayList<Transaction> getTransactionsByUserId(int userId,Connection...connections) throws SQLException{
		TransactionSearchRepresentation t_sr = new TransactionSearchRepresentation();
		t_sr.setUserId(userId);
		return searchTransaction(t_sr, connections);
	}
	
	
	public static ArrayList<Transaction> getTransactionsByBookingId(int bookingId,Connection...connections)throws SQLException{
		TransactionSearchRepresentation t_sr = new TransactionSearchRepresentation();
		t_sr.setBookingId(bookingId);
		return searchTransaction(t_sr, connections);
	}
	
	
	public static ArrayList<Transaction> searchTransaction(TransactionSearchRepresentation t_sr,Connection...connections) throws SQLException{
		return TransactionDao.searchTransaction(t_sr, connections);
	}
	
}
