package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CreditNotFoundException;
import BaseModule.model.Credit;
import BaseModule.model.representation.CreditSearchRepresentation;


public class CreditDao {

	public static ArrayList<Credit> searchCredit(CreditSearchRepresentation sr, Connection...connections) throws SQLException{
		ArrayList<Credit> clist = new ArrayList<Credit>();
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		int stmtInt = 1;
		String query = sr.getSearchQuery();
		try{
			stmt = conn.prepareStatement(query);
			
			if(sr.getCreditId() > 0){
				stmt.setLong(stmtInt++, sr.getCreditId());
			}
			if(sr.getBookingId() > 0){
				stmt.setInt(stmtInt++, sr.getBookingId());
			}
			if(sr.getStartPrice() >= 0){
				stmt.setInt(stmtInt++, sr.getStartPrice());
			}
			if(sr.getFinishPrice() >= 0){
				stmt.setInt(stmtInt++, sr.getFinishPrice());
			}			
			if(sr.getUserId() > 0){
				stmt.setInt(stmtInt++, sr.getUserId());
			}
			if(sr.getCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getCreationTime()));
			}
			if(sr.getExpireTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getExpireTime()));
			}
			if(sr.getStatus() != null){
				stmt.setInt(stmtInt++, sr.getStatus().code);
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				clist.add(createCreditByResultSet(rs));
			}
			
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		
		return clist;
		
	}
	
	public static Credit addCreditToDatabases(Credit c,Connection...connections) throws SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO CreditDao (bookingId,userId,creationTime,expireTime,status,amount)" +
				" values (?,?,?,?,?,?);";		

		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, c.getBookingId());		
			stmt.setInt(2, c.getUserId());
			stmt.setString(3, DateUtility.toSQLDateTime(c.getCreationTime()));
			stmt.setString(4, DateUtility.toSQLDateTime(c.getExpireTime()));
			stmt.setInt(5,c.getStatus().code);
			stmt.setInt(6, c.getAmount());			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			c.setCreditId(rs.getLong(1));			
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		return c;
	}


	public static void updateCreditInDatabases(Credit c,Connection...connections) throws PseudoException, SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query0 = "SELECT * from CreditDao where creditId = ? for update";
		String query = "UPDATE CreditDao set expireTime=?,status=?,amount=? where creditId = ?";
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
			stmt.setLong(4, c.getCreditId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new CreditNotFoundException();
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}	
	}

	public static ArrayList<Credit> getCreditByUserId(int userId,Connection...connections) throws SQLException{
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
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return clist;
	}

	public static Credit getCreditByCreditId(long creditId, Connection... connections) throws PseudoException, SQLException{
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection(connections);
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
			else{
				throw new CreditNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return c;
	}


	protected static Credit createCreditByResultSet(ResultSet rs) throws SQLException {
		return new Credit(rs.getLong("creditId"), rs.getInt("bookingId"), rs.getInt("userId"),
				rs.getInt("amount"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")), 
				DateUtility.DateToCalendar(rs.getTimestamp("expireTime")),CreditStatus.fromInt(rs.getInt("status")));
	}

}
