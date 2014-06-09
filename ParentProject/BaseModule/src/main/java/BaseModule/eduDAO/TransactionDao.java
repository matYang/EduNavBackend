package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.exception.notFound.TransactionNotFoundException;
import BaseModule.model.Transaction;

public class TransactionDao {

	public static Transaction addTransactionToDatabases(Transaction transaction,Connection...connections) throws SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO TransactionDao (userId,bookingId,amount,creationTime,couponId,transactionType)" +
				" values (?,?,?,?,?,?);";		
		try{			
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, transaction.getUserId());
			stmt.setInt(2, transaction.getBookingId());
			stmt.setDouble(3, transaction.getTransactionAmount());
			stmt.setString(4, DateUtility.toSQLDateTime(transaction.getCreationTime()));
			stmt.setLong(5, transaction.getCouponId());
			stmt.setInt(6, transaction.getTransactionType().code);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			transaction.setTransactionId(rs.getInt(1));				
		}catch(SQLException e){
			DebugLog.d(e);
			throw new SQLException();
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return transaction;
	}	

	public static Transaction getTransactionById(int id, Connection...connections) throws TransactionNotFoundException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		Transaction transaction = null;
		
		String query = "SELECT * from TransactionDao where transactionId = ?";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()){
				transaction = createTransactionByResultSet(rs);
			}
			else{
				throw new TransactionNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		
		return transaction;
	}
	
	public static ArrayList<Transaction> getTransactionByUserId(int id, Connection...connections){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		
		String query = "SELECT * from TransactionDao where userId = ?";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while (rs.next()){
				transactions.add(createTransactionByResultSet(rs));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		
		return transactions;
	}
	
	public static ArrayList<Transaction> getTransactionByCouponId(long id, Connection...connections){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		
		String query = "SELECT * from TransactionDao where couponId = ?";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			while (rs.next()){
				transactions.add(createTransactionByResultSet(rs));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		
		return transactions;
	}
	
	public static ArrayList<Transaction> getTransactionByBookingId(int id, Connection...connections){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		
		String query = "SELECT * from TransactionDao where bookingId = ?";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while (rs.next()){
				transactions.add(createTransactionByResultSet(rs));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		
		return transactions;
	}
	


	private static Transaction createTransactionByResultSet(ResultSet rs) throws SQLException {		
		return new Transaction(rs.getInt("transactionId"),rs.getInt("userId"), rs.getInt("bookingId"),rs.getLong("couponId"),
				rs.getInt("amount"),TransactionType.fromInt(rs.getInt("transactionType")),DateUtility.DateToCalendar(rs.getTimestamp("creationTime")));
	}



}
