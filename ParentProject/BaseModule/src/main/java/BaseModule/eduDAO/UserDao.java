package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.encryption.PasswordCrypto;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.model.User;
import BaseModule.model.representation.UserSearchRepresentation;

public class UserDao {

	public static User selectUserForUpdate(int userId,Connection...connections) throws SQLException, UserNotFoundException{
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "select * from UserDao where id =? for update";
		User user = null;
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if(rs.next()){
				user = createUserByResultSet(rs);
			}else{
				throw new UserNotFoundException("用户不存在");
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		return user;
	}
	
	
	
	public static ArrayList<User> searchUser(UserSearchRepresentation sr,Connection...connections) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		ArrayList<User> ulist = new ArrayList<User>();
		String query = sr.getSearchQuery();
		User user = null;
		int stmtInt = 1;		
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);

			if(sr.getUserId() > 0){
				stmt.setInt(stmtInt++, sr.getUserId());
			}
			if(sr.getCreationTime() != null){
				stmt.setString(stmtInt++,DateUtility.toSQLDateTime(sr.getCreationTime()));
			}
			if(sr.getName() != null && sr.getName().length() > 0){
				stmt.setString(stmtInt++, sr.getName());
			}
			if(sr.getPhone() != null && sr.getPhone().length() > 0){
				stmt.setString(stmtInt++, sr.getPhone());
			}						
			if(sr.getStatus() != null){
				stmt.setInt(stmtInt++, sr.getStatus().code);
			}
			if(sr.getEmail() != null){
				stmt.setString(stmtInt++, sr.getEmail());
			}
			if(sr.getInvitationalCode() != null && sr.getInvitationalCode().length() > 0){
				stmt.setString(stmtInt++, sr.getInvitationalCode());
			}
			if(sr.getAppliedInvitationalCode() != null && sr.getAppliedInvitationalCode().length() > 0){
				stmt.setString(stmtInt++, sr.getAppliedInvitationalCode());
			}
			if(sr.getAccountNumber() != null && sr.getAccountNumber().length() > 0){
				stmt.setString(stmtInt++, sr.getAccountNumber());
			}
			if(sr.getStartBalance() != -1){
				stmt.setInt(stmtInt++, sr.getStartBalance());
			}
			if(sr.getFinishBalance() != -1){
				stmt.setInt(stmtInt++, sr.getFinishBalance());
			}
			if(sr.getStartCoupon() != -1){
				stmt.setInt(stmtInt++, sr.getStartCoupon());
			}
			if(sr.getFinishCoupon() != -1){
				stmt.setInt(stmtInt++, sr.getFinishCoupon());
			}
			if(sr.getStartCredit() != -1){
				stmt.setInt(stmtInt++, sr.getStartCredit());
			}
			if(sr.getFinishCredit() != -1){
				stmt.setInt(stmtInt++, sr.getFinishCredit());
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				user = createUserByResultSet(rs);
				user.setCouponList(CouponDao.getCouponByUserId(user.getUserId(),conn));
				user.setCreditList(CreditDao.getCreditByUserId(user.getUserId(),conn));
				user.setTransactionList(TransactionDao.getTransactionByUserId(user.getUserId(),conn));				
				ulist.add(user);
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		return ulist;
	}
	
	public static User getUserById(int id,Connection...connections) throws PseudoException, SQLException{
		String query = "SELECT * FROM UserDao WHERE id = ?";
		User user = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try{		
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()){
				user = createUserByResultSet(rs);
				user.setCouponList(CouponDao.getCouponByUserId(user.getUserId(),conn));
				user.setCreditList(CreditDao.getCreditByUserId(user.getUserId(),conn));
				user.setTransactionList(TransactionDao.getTransactionByUserId(user.getUserId(),conn));
			}else{
				throw new UserNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 

		return user;
	}


	public static User addUserToDatabase(User user,Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO UserDao (name,password,phone,creationTime,lastLogin,status,balance,coupon,credit,email,invitationalCode,appliedInvitationalCode,accountNum)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?);";		
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);			

			stmt.setString(1, user.getName());			
			stmt.setString(2, PasswordCrypto.createHash(user.getPassword()));
			stmt.setString(3, user.getPhone());
			stmt.setString(4, DateUtility.toSQLDateTime(user.getCreationTime()));
			stmt.setString(5, DateUtility.toSQLDateTime(user.getLastLogin()));
			stmt.setInt(6, user.getStatus().code);
			stmt.setInt(7, user.getBalance());
			stmt.setInt(8, user.getCoupon());
			stmt.setInt(9, user.getCredit());
			stmt.setString(10, user.getEmail());
			stmt.setString(11, user.getInvitationalCode());
			stmt.setString(12, user.getAppliedInvitationalCode());
			stmt.setString(13, user.getAccountNumber());
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			user.setUserId(rs.getInt(1));	
			
			user.setPassword("");
			return user;
		} finally {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
	}

	public static void updateUserInDatabases(User user,Connection...connections)  throws PseudoException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		
		String query = "UPDATE UserDao SET name=?,phone=?,lastLogin=?,status=?,balance=?,coupon=?,credit=?,email=?,invitationalCode=?,appliedInvitationalCode=?,accountNum=? where id=?";
		try{
			conn = EduDaoBasic.getConnection(connections);
			
			stmt = conn.prepareStatement(query);
			stmt.setString(1, user.getName());			
			stmt.setString(2, user.getPhone());			
			stmt.setString(3, DateUtility.toSQLDateTime(user.getLastLogin()));
			stmt.setInt(4, user.getStatus().code);
			stmt.setInt(5, user.getBalance());
			stmt.setInt(6, user.getCoupon());
			stmt.setInt(7, user.getCredit());
			stmt.setString(8, user.getEmail());
			stmt.setString(9, user.getInvitationalCode());
			stmt.setString(10, user.getAppliedInvitationalCode());
			stmt.setString(11, user.getAccountNumber());
			stmt.setInt(12, user.getUserId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new UserNotFoundException();
			}
		} finally {
			EduDaoBasic.closeResources(conn, stmt, null,EduDaoBasic.shouldConnectionClose(connections));
		}
	}
	

	public static void updateUserBCC(int balance,int credit,int coupon,int userId,Connection...connections)  throws PseudoException, SQLException{
		String bopr = balance >= 0 ? "+" : "-";
		String cropr = credit >= 0 ? "+" : "-";
		String coopr = coupon >= 0 ? "+" : "-";		
		String query = "UPDATE UserDao set balance = balance " + bopr + " ?, " + "credit = credit "  + cropr + " ?, "
				+ "coupon = coupon " + coopr + " ? " + "where id = ?";
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;	
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, Math.abs(balance));
			stmt.setInt(2, Math.abs(credit));
			stmt.setInt(3, Math.abs(coupon));
			stmt.setInt(4, userId);
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new UserNotFoundException();
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
	}
	

	public static void changeUserPassword(int userId, String oldPassword, String newPassword,Connection...connections)  throws PseudoException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		boolean validOldPassword = false;
		String query = "SELECT * FROM UserDao WHERE id = ? for update";		
		try {
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if(rs.next()){
				validOldPassword = PasswordCrypto.validatePassword(oldPassword, rs.getString("password"));
			
				if(validOldPassword){
					query = "UPDATE UserDao set password = ? where id = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, PasswordCrypto.createHash(newPassword));				
					stmt.setInt(2, userId);
					int recordsAffected = stmt.executeUpdate();
					if(recordsAffected==0)
						throw new UserNotFoundException();
				}else {
					throw new AuthenticationException();
				}
			}
			else{
				throw new AuthenticationException();
			}
		} finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}

	}

	public static User authenticateUser(String phone, String password,Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		User user = null;
		boolean validPassword = false;
		String query = "SELECT * FROM UserDao where phone = ? for update";
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setString(1, phone);			
			rs = stmt.executeQuery();		
			if(rs.next()){
				validPassword = PasswordCrypto.validatePassword(password, rs.getString("password"));
				if(validPassword){
					user = createUserByResultSet(rs);
					user.setCouponList(CouponDao.getCouponByUserId(user.getUserId(),conn));
					user.setCreditList(CreditDao.getCreditByUserId(user.getUserId(),conn));
					user.setTransactionList(TransactionDao.getTransactionByUserId(user.getUserId(),conn));
					user.setLastLogin(DateUtility.getCurTimeInstance());
					updateUserInDatabases(user,conn);
				}
				else{
					throw new AuthenticationException("手机号码或密码输入错误");
				}
			}
			else{
				throw new AuthenticationException("手机号码或密码输入错误");
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		return user;
	}

	public static void recoverUserPassword(String phone,String newPassword,Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;		
		ResultSet rs = null;			
		String query = "UPDATE UserDao set password = ? where phone = ?";		
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setString(1, PasswordCrypto.createHash(newPassword));				
			stmt.setString(2, phone);
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new AuthenticationException("手机号码输入有误");
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));			
		}
	}	

	private static User createUserByResultSet(ResultSet rs) throws SQLException {		
		return new User(rs.getInt("id"), rs.getString("name"), rs.getString("phone"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")),
				DateUtility.DateToCalendar(rs.getTimestamp("lastLogin")),"", AccountStatus.fromInt(rs.getInt("status")),rs.getInt("balance"),rs.getInt("coupon"),
				rs.getInt("credit"),rs.getString("email"),rs.getString("invitationalCode"),rs.getString("appliedInvitationalCode"),rs.getString("accountNum"));
	}

}
