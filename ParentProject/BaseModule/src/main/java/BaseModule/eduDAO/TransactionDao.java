package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.model.Transaction;

public class TransactionDao {

	public static Transaction addTransactionToDatabases(Transaction transaction,Connection...connections){
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO TransactionDao (userId,bookingId,amount,creationTime)" +
				" values (?,?,?,?);";		
		try{			
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, transaction.getUserId());
			stmt.setInt(2, transaction.getBookingId());
			stmt.setDouble(3, transaction.getTransactionAmount());
			stmt.setString(4, DateUtility.toSQLDateTime(transaction.getCreationTime()));
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			transaction.setTransactionId(rs.getInt(1));				
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return transaction;
	}	

	public static ArrayList<Transaction> getTransactionById(int id,String indicator){
		//User, Transaction, Booking
		ArrayList<Transaction> tlist = new ArrayList<Transaction>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = EduDaoBasic.getSQLConnection();
		String indicatorSelector = "";
		switch(indicator){
		case "user":
			indicatorSelector = "userId";
		case "booking":
			indicatorSelector = "bookingId";
		case "transaction":
			indicatorSelector = "transactionId";
		default:
			indicatorSelector = "transactionId";

		}
		String query = "SELECT * from TransactionDao where " + indicatorSelector + " = ?";
		try{
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while(rs.next()){
				tlist.add(createTransactionByResultSet(rs));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return tlist;
	}

	private static Transaction createTransactionByResultSet(ResultSet rs) throws SQLException {
		System.out.println("amount is: " + rs.getDouble("amount"));
		return new Transaction(rs.getInt("transactionId"),rs.getInt("userId"), rs.getInt("bookingId"),rs.getDouble("amount"),
				DateUtility.DateToCalendar(rs.getTimestamp("creationTime")));
	}



}
