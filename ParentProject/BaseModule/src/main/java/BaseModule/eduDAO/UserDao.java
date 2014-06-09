package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.encryption.PasswordCrypto;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.encryptionException.PasswordHashingException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.QueryFactory;
import BaseModule.model.User;
import BaseModule.model.representation.UserSearchRepresentation;

public class UserDao {

	public static ArrayList<User> searchUser(UserSearchRepresentation sr){
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		ArrayList<User> ulist = new ArrayList<User>();
		String query = QueryFactory.getSearchQuery(sr);
		User user = null;
		int stmtInt = 1;		
		try{
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
			if(sr.getBalance() >= 0){
				stmt.setInt(stmtInt++,sr.getBalance());
			}
			if(sr.getCoupon() >= 0){
				stmt.setInt(stmtInt++, sr.getCoupon());
			}
			if(sr.getCredit() >= 0){
				stmt.setInt(stmtInt++,sr.getCredit());
			}
			if(sr.getEmail() != null){
				stmt.setString(stmtInt++, sr.getEmail());
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				user = createUserByResultSet(rs);
				user.setCouponList(CouponDao.getCouponByUserId(user.getUserId(),conn));
				user.setCreditList(CreditDao.getCreditByUserId(user.getUserId(),conn));
				user.setTransactionList(TransactionDao.getTransactionByUserId(user.getUserId(),conn));				
				ulist.add(user);
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
		return ulist;
	}
	
	public static ArrayList<User> getAllUsers(){
		String query = "SELECT * FROM UserDao";
		ArrayList<User> users = new ArrayList<User>();

		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		User user = null;
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);

			rs = stmt.executeQuery();
			while(rs.next()){
				user = createUserByResultSet(rs);
				user.setCouponList(CouponDao.getCouponByUserId(user.getUserId(),conn));
				user.setCreditList(CreditDao.getCreditByUserId(user.getUserId(),conn));
				user.setTransactionList(TransactionDao.getTransactionByUserId(user.getUserId(),conn));
				users.add(user);
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return users;
	}


	public static User addUserToDatabase(User user,Connection...connections) throws ValidationException, PasswordHashingException, SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO UserDao (name,password,phone,creationTime,lastLogin,status,balance,coupon,credit,email)" +
				" values (?,?,?,?,?,?,?,?,?,?);";		
		try{
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

			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			user.setUserId(rs.getInt(1));	
			
			user.setPassword("");
			return user;
		}catch(SQLException e){
			if(e.getMessage().contains("Duplicate")){
				throw new ValidationException("一部分账户内容与其他账户冲突");
			}else{
				throw e;
			}
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
	}

	public static void updateUserInDatabases(User user,Connection...connections) throws ValidationException, SQLException, UserNotFoundException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;		
		String query = "UPDATE UserDao SET name=?,phone=?,lastLogin=?,status=?,balance=?,coupon=?,credit=?,email=? where id=?";
		try{
			stmt = conn.prepareStatement(query);

			stmt.setString(1, user.getName());			
			stmt.setString(2, user.getPhone());			
			stmt.setString(3, DateUtility.toSQLDateTime(user.getLastLogin()));
			stmt.setInt(4, user.getStatus().code);
			stmt.setInt(5, user.getBalance());
			stmt.setInt(6, user.getCoupon());
			stmt.setInt(7, user.getCredit());
			stmt.setString(8, user.getEmail());
			stmt.setInt(9, user.getUserId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new UserNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, null,EduDaoBasic.shouldConnectionClose(connections));
		}
	}


	public static User getUserById(int id,Connection...connections) throws UserNotFoundException, SQLException{
		String query = "SELECT * FROM UserDao WHERE id = ?";
		User user = null;
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;
		try{		
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

	public static void updateUserBCC(int balance,int credit,int coupon,int userId,Connection...connections) throws UserNotFoundException, SQLException{
		String bopr = balance >= 0 ? "+" : "-";
		String cropr = credit >= 0 ? "+" : "-";
		String coopr = coupon >= 0 ? "+" : "-";
		String query0 = "SELECT * From UserDao where id = ? for UPDATE";
		String query = "UPDATE UserDao set balance = balance " + bopr + " ?, " + "credit = credit "  + cropr + " ?, "
				+ "coupon = coupon " + coopr + " ? " + "where id = ?";
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;
		PreparedStatement stmt = null;	
		try{
			stmt = conn.prepareStatement(query0);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if(!rs.next()){
				throw new UserNotFoundException();
			}
			
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
	
	//currently this method is not being used as UserSearchRepresentation does its job
	public static User getUserByPhone(String phone) throws UserNotFoundException, SQLException{
		String query = "SELECT * FROM UserDao WHERE phone = ?";
		User user = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);

			stmt.setString(1, phone);
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
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return user;
	}

	public static void changeUserPassword(int userId, String oldPassword, String newPassword) throws AuthenticationException, SQLException, PasswordHashingException, UserNotFoundException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		boolean validOldPassword = false;
		String query = "SELECT * FROM UserDao WHERE id = ?";		
		try {
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
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}

	}

	public static User authenticateUser(String phone, String password,Connection...connections) throws SQLException, AuthenticationException, PasswordHashingException, UserNotFoundException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		User user = null;
		boolean validPassword = false;
		String query = "SELECT * FROM UserDao where phone = ?  ";
		try{
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

	public static void recoverUserPassword(String phone,String newPassword) throws AuthenticationException, SQLException, PasswordHashingException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;			
		String query = "UPDATE UserDao set password = ? where phone = ?";		
		try{
			stmt = conn.prepareStatement(query);
			stmt.setString(1, PasswordCrypto.createHash(newPassword));				
			stmt.setString(2, phone);
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new AuthenticationException("手机号码输入有误");
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);			
		}
	}

	private static User createUserByResultSet(ResultSet rs) throws SQLException {		
		return new User(rs.getInt("id"), rs.getString("name"), rs.getString("phone"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")),
				DateUtility.DateToCalendar(rs.getTimestamp("lastLogin")),"", AccountStatus.fromInt(rs.getInt("status")),rs.getInt("balance"),rs.getInt("coupon"),
				rs.getInt("credit"),rs.getString("email"));
	}

}
