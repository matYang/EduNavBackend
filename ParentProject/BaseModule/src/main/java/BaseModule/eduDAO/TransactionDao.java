package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.TransactionNotFoundException;
import BaseModule.model.Transaction;
import BaseModule.model.representation.TransactionSearchRepresentation;

public class TransactionDao {
	
	public static ArrayList<Transaction> searchTransaction(TransactionSearchRepresentation sr,Connection...connections) throws SQLException{
		ArrayList<Transaction> tlist = new ArrayList<Transaction>();
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		int stmtInt = 1;
		String query = sr.getSearchQuery();
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			
			if(sr.getTransactionId() > 0){
				stmt.setLong(stmtInt++, sr.getTransactionId());
			}
			if(sr.getBookingId() > 0){
				stmt.setInt(stmtInt++, sr.getBookingId());
			}			
			if(sr.getUserId() > 0){
				stmt.setInt(stmtInt++, sr.getUserId());
			}
			if(sr.getStartAmount() >= 0){
				stmt.setInt(stmtInt++, sr.getStartAmount());
			}
			if(sr.getFinishAmount() >= 0){
				stmt.setInt(stmtInt++, sr.getFinishAmount());
			}
			if(sr.getStartCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getStartCreationTime()));
			}
			if(sr.getFinishCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getFinishCreationTime()));
			}
			if(sr.getTransactionType() != null){
				stmt.setInt(stmtInt++, sr.getTransactionType().code);
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				tlist.add(createTransactionByResultSet(rs));
			}
			
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		
		return tlist;
	}
	

	public static Transaction addTransactionToDatabases(Transaction transaction,Connection...connections) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO Transaction (userId,bookingId,amount,creationTime,transactionType)" +
				" values (?,?,?,?,?);";		
		try{			
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, transaction.getUserId());
			stmt.setInt(2, transaction.getBookingId());
			stmt.setInt(3, transaction.getTransactionAmount());
			stmt.setString(4, DateUtility.toSQLDateTime(transaction.getCreationTime()));			
			stmt.setInt(5, transaction.getTransactionType().code);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			transaction.setTransactionId(rs.getLong(1));				
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return transaction;
	}	

	public static Transaction getTransactionById(long id, Connection...connections) throws PseudoException, SQLException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		Transaction transaction = null;
		
		String query = "SELECT * from Transaction where transactionId = ?";
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			if (rs.next()){
				transaction = createTransactionByResultSet(rs);
			}
			else{
				throw new TransactionNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		
		return transaction;
	}
	
	public static ArrayList<Transaction> getTransactionByUserId(int id, Connection...connections) throws SQLException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		
		String query = "SELECT * from Transaction where userId = ?";
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while (rs.next()){
				transactions.add(createTransactionByResultSet(rs));
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		
		return transactions;
	}
	


	private static Transaction createTransactionByResultSet(ResultSet rs) throws SQLException {		
		return new Transaction(rs.getLong("transactionId"),rs.getInt("userId"), rs.getInt("bookingId"),
				rs.getInt("amount"),TransactionType.fromInt(rs.getInt("transactionType")),DateUtility.DateToCalendar(rs.getTimestamp("creationTime")));
	}



}
