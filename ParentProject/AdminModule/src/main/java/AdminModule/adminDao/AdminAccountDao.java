package AdminModule.adminDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import AdminModule.exception.AdminAccountNotFoundException;
import AdminModule.model.AdminAccount;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.encryption.PasswordCrypto;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;



public class AdminAccountDao {

	public static AdminAccount addAdminAccountToDatabases(AdminAccount account){
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO AdminAccountDao (creationTime,lastLogin,status,reference,privilege,name,phone,password)" +
				" values (?,?,?,?,?,?,?,?);";
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, DateUtility.toSQLDateTime(account.getCreationTime()));
			stmt.setString(2, DateUtility.toSQLDateTime(account.getLastLogin()));
			stmt.setInt(3, account.getStatus().code);
			stmt.setString(4, account.getReference());
			stmt.setInt(5, account.getPrivilege().code);
			stmt.setString(6, account.getName());
			stmt.setString(7, account.getPhone());
			stmt.setString(8, PasswordCrypto.createHash(account.getPassword()));
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			account.setAdminId(rs.getInt(1));
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}catch(Exception e){
			e.printStackTrace();
			DebugLog.d(e);
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 		
		return account;
	}

	public static void updateAdminAccountInDatabases(AdminAccount account) throws AdminAccountNotFoundException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;
		String query = "UPDATE AdminAccountDao SET lastLogin=?,status=?,reference=?,privilege=?,name=?,phone=? where id = ?";
		try{
			stmt = conn.prepareStatement(query);

			stmt.setString(1, DateUtility.toSQLDateTime(account.getLastLogin()));
			stmt.setInt(2, account.getStatus().code);
			stmt.setString(3, account.getReference());
			stmt.setInt(4, account.getPrivilege().code);
			stmt.setString(5, account.getName());
			stmt.setString(6, account.getPhone());
			stmt.setInt(7, account.getAdminId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new AdminAccountNotFoundException();
			}
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}catch(Exception e){
			e.printStackTrace();
			DebugLog.d(e);
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, null,true);			
		}
	}

	public static ArrayList<AdminAccount> getAllAdminAccounts(){
		String query = "SELECT * FROM AdminAccountDao";
		ArrayList<AdminAccount> alist = new ArrayList<AdminAccount>();

		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);

			rs = stmt.executeQuery();
			while(rs.next()){
				alist.add(createAdminAccountByResultSet(rs));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return alist;
	}

	public static AdminAccount getAdminAccountById(int id) throws AdminAccountNotFoundException{
		String query = "SELECT * FROM AdminAccountDao where id = ?";
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getSQLConnection();
		ResultSet rs = null;
		AdminAccount account = null;
		try{
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()){
				account = createAdminAccountByResultSet(rs);
			}else{
				throw new AdminAccountNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return account;
	}

	public static AdminAccount getAdminAccountByPhone(String phone) throws AdminAccountNotFoundException{
		String query = "SELECT * FROM AdminAccountDao where phone = ?";
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getSQLConnection();
		ResultSet rs = null;
		AdminAccount account = null;
		try{
			stmt = conn.prepareStatement(query);

			stmt.setString(1,phone);
			rs = stmt.executeQuery();
			if(rs.next()){
				account = createAdminAccountByResultSet(rs);
			}else{
				throw new AdminAccountNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return account;
	}

	public static AdminAccount getAdminAccountByName(String name) throws AdminAccountNotFoundException{
		String query = "SELECT * FROM AdminAccountDao where name = ?";
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getSQLConnection();
		ResultSet rs = null;
		AdminAccount account = null;
		try{
			stmt = conn.prepareStatement(query);

			stmt.setString(1, name);
			rs = stmt.executeQuery();
			if(rs.next()){
				account = createAdminAccountByResultSet(rs);
			}else{
				throw new AdminAccountNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return account;
	}

	public static void changeAdminAccountPassword(int adminId, String oldPassword, String newPassword) throws AuthenticationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		boolean validOldPassword = false;
		String query = "SELECT * FROM AdminAccountDao where id = ? ";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, adminId);						
			rs = stmt.executeQuery();						
			if(rs.next()){
				validOldPassword = PasswordCrypto.validatePassword(oldPassword, rs.getString("password"));
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
			query = "UPDATE AdminAccountDao set password = ? where id = ?";
			try{
				stmt = conn.prepareStatement(query);
				stmt.setString(1, PasswordCrypto.createHash(newPassword));				
				stmt.setInt(2, adminId);
				stmt.executeUpdate();
			}catch(SQLException e){
				e.printStackTrace();
				DebugLog.d(e);
			}catch(Exception e){
				validOldPassword = false;
				e.printStackTrace();
				DebugLog.d(e);						
			}finally{
				EduDaoBasic.closeResources(conn, stmt, rs, true);
				if(!validOldPassword){
					throw new AuthenticationException();
				}
			}
		}else {
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			throw new AuthenticationException();
		}

	}

	public static AdminAccount authenticateAdminAccount(String reference, String password) throws AuthenticationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		AdminAccount account = null;
		boolean validReference = true;
		String query = "SELECT * FROM AdminAccountDao where reference = ? ";
		try{
			stmt = conn.prepareStatement(query);			
			stmt.setString(1, reference);
			rs = stmt.executeQuery();		
			if(rs.next()){
				validReference = PasswordCrypto.validatePassword(password, rs.getString("password"));
				if(validReference){
					account = createAdminAccountByResultSet(rs);
				}				
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
			if(!validReference){
				throw new AuthenticationException("编号或密码输入错误");
			}
		}
		return account;
	}

	public static void changeAdminAccountPassword(int adminId, String password) throws PseudoException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		String query = "UPDATE AdminAccountDao set password = ? where id = ?";
		boolean success = true;
		try{
			stmt = conn.prepareStatement(query);
			stmt.setString(1, PasswordCrypto.createHash(password));				
			stmt.setInt(2, adminId);
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				success = false;
			}
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}catch(Exception e){
			success = false;
			e.printStackTrace();
			DebugLog.d(e);			
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			if(!success){
				throw new AuthenticationException("手机号码或密码输入有误");
			}
		}
	}

	private static AdminAccount createAdminAccountByResultSet(ResultSet rs) throws SQLException {
		return new AdminAccount(rs.getInt("id"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")), DateUtility.DateToCalendar(rs.getTimestamp("lastLogin")),
				rs.getString("reference"), Privilege.fromInt(rs.getInt("privilege")), AccountStatus.fromInt(rs.getInt("status")), rs.getString("name"),rs.getString("phone"));
	}
}
