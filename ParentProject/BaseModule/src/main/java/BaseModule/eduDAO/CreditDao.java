package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.exception.notFound.CreditNotFoundException;
import BaseModule.model.Credit;




public class CreditDao {

	public static Credit addCreditToDatabases(Credit c,Connection...connections) throws SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO CreditDao (bookingId,userId,creationTime,expireTime,status,amount,usableTime)" +
				" values (?,?,?,?,?,?,?);";		

		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, c.getBookingId());		
			stmt.setInt(2, c.getUserId());
			stmt.setString(3, DateUtility.toSQLDateTime(c.getCreationTime()));
			stmt.setString(4, DateUtility.toSQLDateTime(c.getExpireTime()));
			stmt.setInt(5,c.getStatus().code);
			stmt.setInt(6, c.getAmount());
			stmt.setString(7,DateUtility.toSQLDateTime(c.getUsableTime()));
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			c.setCreditId(rs.getLong(1));			

		}catch(SQLException e){
			DebugLog.d(e);
			throw new SQLException();
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		return c;
	}


	public static void updateCreditInDatabases(Credit c,Connection...connections) throws CreditNotFoundException, SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query0 = "SELECT * from CreditDao where creditId = ? for update";
		String query = "UPDATE CreditDao set expireTime=?,status=?,amount=?, usableTime=? where creditId = ?";
		try{
			stmt = conn.prepareStatement(query0);
			stmt.setLong(1, c.getCreditId());
			rs = stmt.executeQuery();
			if(!rs.next()){
				throw new CreditNotFoundException();
			}
			
			stmt = conn.prepareStatement(query);			
			stmt.setString(1, DateUtility.toSQLDateTime(c.getExpireTime()));
			stmt.setInt(2,c.getStatus().code);
			stmt.setInt(3, c.getAmount());				
			stmt.setString(4,DateUtility.toSQLDateTime(c.getUsableTime()));
			stmt.setLong(5, c.getCreditId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new CreditNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
			throw new SQLException();
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}	
	}

	public static ArrayList<Credit> getCreditByUserId(int userId,Connection...connections){
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;
		ArrayList<Credit> clist = new ArrayList<Credit>();
		String query = "SELECT * from CreditDao where userId = ?";
		try{		
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			while(rs.next()){
				clist.add(createCreditByResultSet(rs));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return clist;
	}

	public static Credit getCreditByCreditId(long creditId){
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getSQLConnection();
		ResultSet rs = null;
		Credit c = null;
		String query = "SELECT * from CreditDao where creditId = ?";
		try{		
			stmt = conn.prepareStatement(query);

			stmt.setLong(1, creditId);
			rs = stmt.executeQuery();
			if(rs.next()){
				c = createCreditByResultSet(rs);
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return c;
	}


	protected static Credit createCreditByResultSet(ResultSet rs) throws SQLException {
		return new Credit(rs.getLong("creditId"), rs.getInt("bookingId"), rs.getInt("userId"),
				rs.getInt("amount"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")), 
				DateUtility.DateToCalendar(rs.getTimestamp("expireTime")),CreditStatus.fromInt(rs.getInt("status")),
				DateUtility.DateToCalendar(rs.getTimestamp("usableTime")));
	}

}
