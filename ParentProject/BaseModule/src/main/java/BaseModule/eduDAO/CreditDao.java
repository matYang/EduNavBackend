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
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		int stmtInt = 1;
		String query = sr.getSearchQuery();
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			
			if(sr.getCreditId() > 0){
				stmt.setLong(stmtInt++, sr.getCreditId());
			}
			if(sr.getBookingId() > 0){
				stmt.setInt(stmtInt++, sr.getBookingId());
			}
			if(sr.getStartAmount() >= 0){
				stmt.setInt(stmtInt++, sr.getStartAmount());
			}
			if(sr.getFinishAmount() >= 0){
				stmt.setInt(stmtInt++, sr.getFinishAmount());
			}			
			if(sr.getUserId() > 0){
				stmt.setInt(stmtInt++, sr.getUserId());
			}
			if(sr.getStartCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getStartCreationTime()));
			}
			if(sr.getFinishCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getFinishCreationTime()));
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
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO Credit (bookingId,userId,creationTime,expireTime,status,amount)" +
				" values (?,?,?,?,?,?);";		

		try{
			conn = EduDaoBasic.getConnection(connections);
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
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;		
		String query = "UPDATE Credit set expireTime=?,status=?,amount=? where creditId = ?";
		try{
			conn = EduDaoBasic.getConnection(connections);		
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
		Connection conn = null;
		ResultSet rs = null;
		ArrayList<Credit> clist = new ArrayList<Credit>();
		String query = "SELECT * from Credit where userId = ?";
		try{		
			conn = EduDaoBasic.getConnection(connections);
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
		Connection conn = null;
		ResultSet rs = null;
		Credit c = null;
		String query = "SELECT * from Credit where creditId = ?";
		try{		
			conn = EduDaoBasic.getConnection(connections);
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
