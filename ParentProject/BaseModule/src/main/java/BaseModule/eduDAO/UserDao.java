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
import BaseModule.encryption.SessionCrypto;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.user.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.User;

public class UserDao {

	public static User addUserToDatabase(User user) throws ValidationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO UserDao (name,password,phone,creationTime,lastLogin,status)" +
				" values (?,?,?,?,?,?);";		
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);			

			stmt.setString(1, user.getName());			
			stmt.setString(2, SessionCrypto.encrypt(user.getPassword()));
			stmt.setString(3, user.getPhone());
			stmt.setString(4, DateUtility.toSQLDateTime(user.getCreationTime()));
			stmt.setString(5, DateUtility.toSQLDateTime(user.getLastLogin()));
			stmt.setInt(6, user.getStatus().code);

			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			user.setUserId(rs.getInt(1));			
		}catch(SQLException e){
			if(e.getMessage().contains("Duplicate")){
				throw new ValidationException("一部分账户内容与其他账户冲突");
			}else{
				e.printStackTrace();
				DebugLog.d(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			DebugLog.d(e);
			throw new ValidationException("创建用户失败，账户信息错误");
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		user.setPassword("");
		return user;
	}

	public static void updateUserInDatabases(User user) throws ValidationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;
		String query = "UPDATE UserDao SET name=?,phone=?,status=?,lastLogin=? where id=?";
		try{
			stmt = conn.prepareStatement(query);

			stmt.setString(1, user.getName());			
			stmt.setString(2, user.getPhone());			
			stmt.setString(3, DateUtility.toSQLDateTime(user.getLastLogin()));
			stmt.setInt(4, user.getStatus().code);
			stmt.setInt(5, user.getUserId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new UserNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		} catch (Exception e) {
			throw new ValidationException("更改用户信息失败，账户信息错误");
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, null,true);
		}
	}

	public static ArrayList<User> getAllUsers(){
		String query = "SELECT * FROM UserDao";
		ArrayList<User> users = new ArrayList<User>();

		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);

			rs = stmt.executeQuery();
			while(rs.next()){
				users.add(createUserByResultSet(rs));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return users;
	}

	public static User getUserById(int id,Connection...connections) throws UserNotFoundException{
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
			}else{
				throw new UserNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 

		return user;
	}

	public static User getUserByPhone(String phone) throws UserNotFoundException{
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
			}else{
				throw new UserNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return user;
	}

	public static void changeUserPassword(int userId, String oldPassword, String newPassword) throws AuthenticationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		boolean validOldPassword = true;
		String query = "SELECT * FROM UserDao where id = ? and password = ? ";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, userId);
			stmt.setString(2, SessionCrypto.encrypt(oldPassword));			
			rs = stmt.executeQuery();						
			if(!rs.next()){
				validOldPassword = false;							
			}		
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}
		catch(Exception e){
			e.printStackTrace();
			DebugLog.d(e);				
		}
		if(validOldPassword){
			query = "UPDATE UserDao set password = ? where id = ?";
			try{
				stmt = conn.prepareStatement(query);
				stmt.setString(1, SessionCrypto.encrypt(newPassword));				
				stmt.setInt(2, userId);
				stmt.executeUpdate();
			}catch(SQLException e){
				e.printStackTrace();
				DebugLog.d(e);
			}catch(Exception e){
				e.printStackTrace();
				DebugLog.d(e);						
			}finally{
				EduDaoBasic.closeResources(conn, stmt, rs, true);				
			}
		}else {
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			throw new AuthenticationException();
		}
		
	}

	public static User authenticateUser(String phone, String password) throws AuthenticationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		User user = null;
		boolean validPassword = true;
		String query = "SELECT * FROM UserDao where phone = ? and password = ? ";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setString(1, phone);
			stmt.setString(2, SessionCrypto.encrypt(password));
			rs = stmt.executeQuery();		
			if(rs.next()){
				user = createUserByResultSet(rs);
			}else{
				validPassword = false;
			}
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}
		catch(Exception e){
			e.printStackTrace();
			DebugLog.d(e);			
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			if(!validPassword){
				throw new AuthenticationException("手机号码或密码输入错误");
			}
		}
		return user;
	}
	private static User createUserByResultSet(ResultSet rs) throws SQLException {		
		return new User(rs.getInt("id"), rs.getString("name"), rs.getString("phone"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")),
				DateUtility.DateToCalendar(rs.getTimestamp("lastLogin")),"", AccountStatus.fromInt(rs.getInt("status")));
	}



}
