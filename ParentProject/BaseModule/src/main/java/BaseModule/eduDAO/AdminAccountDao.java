package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.encryption.PasswordCrypto;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.notFound.AdminAccountNotFoundException;
import BaseModule.model.AdminAccount;
import BaseModule.model.representation.AdminSearchRepresentation;

public class AdminAccountDao {

	public static ArrayList<AdminAccount> searchAdminAccount(AdminSearchRepresentation sr) throws SQLException{
		Connection conn = EduDaoBasic.getConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		ArrayList<AdminAccount> alist = new ArrayList<AdminAccount>();
		String query = sr.getSearchQuery();
		int stmtInt = 1;
		try{
			stmt = conn.prepareStatement(query);
			
			if(sr.getAdminId() > 0){
				stmt.setInt(stmtInt++, sr.getAdminId());
			}
			if(sr.getName() != null && sr.getName().length() > 0){
				stmt.setString(stmtInt++, sr.getName());
			}
			if(sr.getPhone() != null && sr.getPhone().length() > 0){
				stmt.setString(stmtInt++, sr.getPhone());
			}
			if(sr.getReference() != null && sr.getReference().length() > 0){
				stmt.setString(stmtInt++, sr.getReference());
			}
			if(sr.getPrivilege() != null){
				stmt.setInt(stmtInt++, sr.getPrivilege().code);
			}
			if(sr.getStatus() != null){
				stmt.setInt(stmtInt++, sr.getStatus().code);
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				alist.add(createAdminAccountByResultSet(rs));
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
		return alist;
	}
	
	public static AdminAccount addAdminAccountToDatabases(AdminAccount account,Connection...connections) throws PseudoException, SQLException{
		Connection conn = EduDaoBasic.getConnection(connections);
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
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 		
		return account;
	}

	public static void updateAdminAccountInDatabases(AdminAccount account) throws PseudoException, SQLException{
		Connection conn = EduDaoBasic.getConnection();
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
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, null,true);			
		}
	}


	public static AdminAccount getAdminAccountById(int id) throws PseudoException, SQLException{
		String query = "SELECT * FROM AdminAccountDao where id = ?";
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection();
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
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return account;
	}



	public static void changeAdminAccountPassword(int adminId, String oldPassword, String newPassword) throws PseudoException, SQLException{
		Connection conn = EduDaoBasic.getConnection();
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
				if(validOldPassword){
					query = "UPDATE AdminAccountDao set password = ? where id = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, PasswordCrypto.createHash(newPassword));				
					stmt.setInt(2, adminId);
					stmt.executeUpdate();
				}else {
					throw new AuthenticationException("密码错误");
				}
			}
			else{
				throw new AuthenticationException();
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
	}

	public static AdminAccount authenticateAdminAccount(String reference, String password) throws PseudoException, SQLException{
		Connection conn = EduDaoBasic.getConnection();
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
				else{
					throw new AuthenticationException("编号或密码输入错误");
				}
			}
			else{
				throw new AuthenticationException("编号或密码输入错误");
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
		return account;
	}

	public static void changeAdminAccountPassword(int adminId, String password) throws PseudoException, SQLException{
		Connection conn = EduDaoBasic.getConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		String query = "UPDATE AdminAccountDao set password = ? where id = ?";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setString(1, PasswordCrypto.createHash(password));				
			stmt.setInt(2, adminId);
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new AuthenticationException("手机号码或密码输入有误");
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
	}

	private static AdminAccount createAdminAccountByResultSet(ResultSet rs) throws SQLException {
		return new AdminAccount(rs.getInt("id"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")), DateUtility.DateToCalendar(rs.getTimestamp("lastLogin")),
				rs.getString("reference"), Privilege.fromInt(rs.getInt("privilege")), AccountStatus.fromInt(rs.getInt("status")), rs.getString("name"),rs.getString("phone"));
	}
}
