package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Coupon;
import BaseModule.model.User;
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
			if(sr.getOrigin() != null){
				stmt.setInt(stmtInt++, sr.getOrigin().code);
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
		String query = "INSERT INTO CouponDao (bookingId,userId,creationTime,expireTime,status,amount,couponOrigin,originalAmount)" +
				" values (?,?,?,?,?,?,?,?);";		
		int stmtInt = 1;
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(stmtInt++, c.getBookingId());			
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

	public static void updateCouponsInDatabases(ArrayList<Coupon>clist,Connection...connections) 
			throws SQLException, CouponNotFoundException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		Coupon c = null;
		String query0 = "SELECT * From CouponDao where couponId = ? ";		
		String query = "UPDATE CouponDao set expireTime=?,status=?,amount=?,couponOrigin=? where couponId = ?;";
		try{
			if(clist.size() == 0){
				return;
			}

			for(int i=1;i<clist.size();i++){
				query0 += "or couponId = ? ";
			}

			query0 += "for update";

			for(int i=1;i<clist.size();i++){
				query += "UPDATE CouponDao set expireTime=?,status=?,amount=?,couponOrigin=? where couponId = ?;";
			}

			stmt = conn.prepareStatement(query0);
			for(int i=0;i<clist.size();i++){
				stmt.setLong(i+1, clist.get(i).getCouponId());
			}
			rs =stmt.executeQuery();
			int couponNum = 0;
			while(rs.next()){
				couponNum++;				
			}			
			if(couponNum != clist.size()){
				throw new CouponNotFoundException();
			}
			
			stmt = conn.prepareStatement(query);
			int stmtInt = 1;
			for(int i=0;i<clist.size();i++){
				c = clist.get(i);											
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(c.getExpireTime()));
				stmt.setInt(stmtInt++,c.getStatus().code);
				stmt.setInt(stmtInt++, c.getAmount());
				stmt.setInt(stmtInt++, c.getOrigin().code);
				stmt.setLong(stmtInt++, c.getCouponId());					
			}
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new CouponNotFoundException();
			}

		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
	}

	public static void updateCouponInDatabases(Coupon c,Connection...connections) throws CouponNotFoundException, SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query0 = "SELECT * From CouponDao where couponId = ? for update";
		String query = "UPDATE CouponDao set expireTime=?,status=?,amount=?,couponOrigin=? where couponId = ?";
		try{
			stmt = conn.prepareStatement(query0);
			stmt.setLong(1, c.getCouponId());
			rs =stmt.executeQuery();
			if(!rs.next()){
				throw new CouponNotFoundException();
			}
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
		return new Coupon(rs.getLong("couponId"), rs.getInt("bookingId"),rs.getInt("userId"),
				rs.getInt("amount"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")), 
				DateUtility.DateToCalendar(rs.getTimestamp("expireTime")),CouponStatus.fromInt(rs.getInt("status")),
				CouponOrigin.fromInt(rs.getInt("couponOrigin")),rs.getInt("originalAmount"));
	}

	public static String getCouponRecord(int userId,int cashback,Connection...connections) throws ValidationException, 
	SQLException, PseudoException,CouponNotFoundException{
		Connection conn = EduDaoBasic.getConnection(connections);
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		ArrayList<Coupon> vlist = new ArrayList<Coupon>();	
		Coupon c = null;		
		User user = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;		
		int amount = 0;
		int i = 0;
		int totalCoupon = 0;
		String couponRecord = "";
		try{			
			/*    Lock User Table   */
			String userDaoQueryString = "select * from UserDao where id = " + userId + " for update";
			stmt = conn.prepareStatement(userDaoQueryString);
			rs = stmt.executeQuery();
			if(rs.next()){
				user = createUserByResultSet(rs);
			}else{
				throw new ValidationException("用户不存在");
			}
			totalCoupon = user.getCoupon();
			
			/*  Lock Coupon Table  */
			String couponDaoQueryString = "select * from CouponDao where userId = " + userId + " for update";
			stmt = conn.prepareStatement(couponDaoQueryString);
			rs = stmt.executeQuery();
			while(rs.next()){
				clist.add(createCouponByResultSet(rs));
			}
			System.out.println("clist num: " + clist.size());
			if(clist.size()==0){
				return couponRecord;			
			}
			//Sort
			Collections.sort(clist, DateUtility.couponExpireComparator);
			
			while(i < clist.size()){
				c = clist.get(i);
				String ct = DateUtility.toSQLDateTime(DateUtility.getCurTimeInstance());
				String expireTime = DateUtility.toSQLDateTime(c.getExpireTime());
				
				//Check if usable
				if(c.getStatus().code == CouponStatus.usable.code && ct.compareTo(expireTime) < 0){
					amount += c.getAmount();
					System.out.println("adding coupon: " + c.getCouponId() + " By amount: " + c.getAmount());
					vlist.add(c);
				}

				if(amount >= cashback){
					if(amount > totalCoupon){
						//Error
						System.out.println("amount: "  + amount + " totalCoupon: " +totalCoupon);
						throw new ValidationException("账户出错！ 需要管理员处理");
					}else{
						break;
					}					
				}

				i++;
			}

			if(i== clist.size() && amount != totalCoupon){
				//Error
				throw new ValidationException("账户出错！ 需要管理员处理");
			}

			//update coupons
			for(int k=0;k<vlist.size()-1;k++){		
				couponRecord += vlist.get(k).getCouponId() + "_" + vlist.get(k).getAmount() + "-";
				vlist.get(k).setStatus(CouponStatus.used);				
			}
			
			if(amount > cashback){
				//too many coupons: split one
				int newAmount = amount-cashback;
				couponRecord += vlist.get(vlist.size()-1).getCouponId() + "_" + (vlist.get(vlist.size()-1).getAmount()-newAmount) + "-";
				vlist.get(vlist.size()-1).setAmount(newAmount);						
			}else{
				couponRecord += vlist.get(vlist.size()-1).getCouponId() + "_" + vlist.get(vlist.size()-1).getAmount() + "-";
				vlist.get(vlist.size()-1).setStatus(CouponStatus.used);				
			}

			//update coupons in database
			updateCouponsInDatabases(vlist,conn);
			stmt = conn.prepareStatement("commit");
			stmt.execute();
			
			System.out.println("amount: "  + amount + " totalCoupon: " +totalCoupon);
			//update user in database
			if(amount >= cashback){				
				UserDao.updateUserBCC(0, 0, -cashback, userId, conn);
				System.out.println("user account - " + cashback);
			}else{				
				UserDao.updateUserBCC(0, 0, -amount, userId, conn);
				System.out.println("user account - " + amount);
			}
			
			stmt = conn.prepareStatement("commit");
			stmt.execute();
			
		}finally{			
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}

		return couponRecord;
	}
	
	private static User createUserByResultSet(ResultSet rs) throws SQLException {		
		return new User(rs.getInt("id"), rs.getString("name"), rs.getString("phone"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")),
				DateUtility.DateToCalendar(rs.getTimestamp("lastLogin")),"", AccountStatus.fromInt(rs.getInt("status")),rs.getInt("balance"),rs.getInt("coupon"),
				rs.getInt("credit"),rs.getString("email"));
	}

}
