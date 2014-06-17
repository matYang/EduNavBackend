package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.model.Coupon;
import BaseModule.model.representation.CouponSearchRepresentation;


public class CouponDao {

	public static ArrayList<Coupon> searchCoupon(CouponSearchRepresentation sr, Connection...connections) throws SQLException{
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		int stmtInt = 1;
		String query = sr.getSearchQuery();
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);

			if(sr.getCouponId() > 0){
				stmt.setLong(stmtInt++, sr.getCouponId());
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
			if(sr.getOrigin() != null){
				stmt.setInt(stmtInt++, sr.getOrigin().code);
			}
			if(sr.getStartOriginalAmount() != -1){
				stmt.setInt(stmtInt++, sr.getStartOriginalAmount());
			}
			if(sr.getFinishOriginalAmount() != -1){
				stmt.setInt(stmtInt++, sr.getFinishOriginalAmount());
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				clist.add(createCouponByResultSet(rs));
			}

		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}

		return clist;

	}


	public static Coupon addCouponToDatabases(Coupon c,Connection...connections) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO CouponDao (userId,creationTime,expireTime,status,amount,couponOrigin,originalAmount)" +
				" values (?,?,?,?,?,?,?);";		
		int stmtInt = 1;
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);						
			stmt.setInt(stmtInt++, c.getUserId());
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(c.getCreationTime()));
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(c.getExpireTime()));
			stmt.setInt(stmtInt++,c.getStatus().code);
			stmt.setInt(stmtInt++, c.getAmount());
			stmt.setInt(stmtInt++, c.getOrigin().code);
			stmt.setInt(stmtInt++, c.getOriginalAmount());
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			c.setCouponId(rs.getLong(1));		

		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		return c;
	}

	public static void updateCouponInDatabases(Coupon c,Connection...connections) throws CouponNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		
		String query = "UPDATE CouponDao set expireTime=?,status=?,amount=?,couponOrigin=? where couponId = ?";
		try{
			conn = EduDaoBasic.getConnection(connections);
			
			int stmtInt = 1;
			stmt = conn.prepareStatement(query);			
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(c.getExpireTime()));
			stmt.setInt(stmtInt++,c.getStatus().code);
			stmt.setInt(stmtInt++, c.getAmount());
			stmt.setInt(stmtInt++, c.getOrigin().code);
			stmt.setLong(stmtInt++, c.getCouponId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new CouponNotFoundException();
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}	
	}	

	public static ArrayList<Coupon> getCouponByUserId(int userId,Connection...connections) throws SQLException{
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		String query = "SELECT * from CouponDao where userId = ?";
		try{		
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			while(rs.next()){
				clist.add(createCouponByResultSet(rs));
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return clist;
	}
	
	public static Coupon getCouponByCouponId(long couponId,Connection...connections) throws PseudoException, SQLException{
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		Coupon c = null;
		String query = "SELECT * from CouponDao where couponId = ?";
		try{		
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);

			stmt.setLong(1, couponId);
			rs = stmt.executeQuery();
			if(rs.next()){
				c = createCouponByResultSet(rs);
			}
			else{
				throw new CouponNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return c;
	}


	protected static Coupon createCouponByResultSet(ResultSet rs) throws SQLException {
		return new Coupon(rs.getLong("couponId"), rs.getInt("userId"),
				rs.getInt("amount"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")), 
				DateUtility.DateToCalendar(rs.getTimestamp("expireTime")),CouponStatus.fromInt(rs.getInt("status")),
				CouponOrigin.fromInt(rs.getInt("couponOrigin")),rs.getInt("originalAmount"));
				
	}
	

}
