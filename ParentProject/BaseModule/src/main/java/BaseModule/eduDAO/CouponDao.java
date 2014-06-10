package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.model.Coupon;
import BaseModule.model.representation.CouponSearchRepresentation;


public class CouponDao {

	public static ArrayList<Coupon> searchCoupon(CouponSearchRepresentation sr, Connection...connections) throws SQLException{
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		int stmtInt = 1;
		String query = sr.getSearchQuery();
		try{
			stmt = conn.prepareStatement(query);
			
			if(sr.getCouponId() > 0){
				stmt.setLong(stmtInt++, sr.getCouponId());
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
			if(sr.getTransactionId() > 0){
				stmt.setInt(stmtInt++, sr.getTransactionId());
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
				clist.add(createCouponByResultSet(rs));
			}
			
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		
		return clist;
		
	}
	
	
	public static Coupon addCouponToDatabases(Coupon c,Connection...connections) throws SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO CouponDao (bookingId,transactionId,userId,creationTime,expireTime,status,amount)" +
				" values (?,?,?,?,?,?,?);";		

		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, c.getBookingId());
			stmt.setInt(2, c.getTransactionId());
			stmt.setInt(3, c.getUserId());
			stmt.setString(4, DateUtility.toSQLDateTime(c.getCreationTime()));
			stmt.setString(5, DateUtility.toSQLDateTime(c.getExpireTime()));
			stmt.setInt(6,c.getStatus().code);
			stmt.setInt(7, c.getAmount());
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
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query0 = "SELECT * From CouponDao where couponId = ? for update";
		String query = "UPDATE CouponDao set transactionId=?,expireTime=?,status=?,amount=? where couponId = ?";
		try{
			stmt = conn.prepareStatement(query0);
			stmt.setLong(1, c.getCouponId());
			rs =stmt.executeQuery();
			if(!rs.next()){
				throw new CouponNotFoundException();
			}
			
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, c.getTransactionId());
			stmt.setString(2, DateUtility.toSQLDateTime(c.getExpireTime()));
			stmt.setInt(3,c.getStatus().code);
			stmt.setInt(4, c.getAmount());
			stmt.setLong(5, c.getCouponId());
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
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		String query = "SELECT * from CouponDao where userId = ?";
		try{		
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
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;
		Coupon c = null;
		String query = "SELECT * from CouponDao where couponId = ?";
		try{		
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
		return new Coupon(rs.getLong("couponId"), rs.getInt("bookingId"), rs.getInt("transactionId"), rs.getInt("userId"),
				rs.getInt("amount"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")), 
				DateUtility.DateToCalendar(rs.getTimestamp("expireTime")),CouponStatus.fromInt(rs.getInt("status")));
	}

}
