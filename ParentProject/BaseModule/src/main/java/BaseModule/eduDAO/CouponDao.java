package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.exception.coupon.CouponNotFoundException;
import BaseModule.model.Coupon;


public class CouponDao {

	public static Coupon addCouponToDatabases(Coupon c,Connection...connections) throws SQLException{
		Connection conn = EduDaoBasic.getSQLConnection();
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
			stmt.setDouble(7, c.getAmount());
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			c.setCouponId(rs.getLong(1));			

		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
			throw new SQLException();
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
			stmt.setDouble(4, c.getAmount());
			stmt.setLong(5, c.getCouponId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new CouponNotFoundException();
			}
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
			throw new SQLException();
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}	
	}	

	public static ArrayList<Coupon> getCouponByUserId(int userId,Connection...connections){
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
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return clist;
	}
	
	public static Coupon getCouponByCouponId(long couponId){
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getSQLConnection();
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
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return c;
	}


	protected static Coupon createCouponByResultSet(ResultSet rs) throws SQLException {
		return new Coupon(rs.getLong("couponId"), rs.getInt("bookingId"), rs.getInt("transactionId"), rs.getInt("userId"),
				rs.getInt("amount"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")), 
				DateUtility.DateToCalendar(rs.getTimestamp("expireTime")),CouponStatus.fromInt(rs.getInt("status")));
	}

}
