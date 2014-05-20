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
import BaseModule.exception.partner.PartnerNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Partner;

public class PartnerDao {

	public static Partner addPartnerToDatabases(Partner p,Connection...connections) throws ValidationException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO PartnerDao (name,licence,organizationNum,reference,password,phone,creationTime,lastLogin,status,instName,logoUrl)" +
				" values (?,?,?,?,?,?,?,?,?,?,?);";
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, p.getName());
			stmt.setString(2, p.getLicence());
			stmt.setString(3, p.getOrganizationNum());
			stmt.setString(4, p.getReference());
			stmt.setString(5, SessionCrypto.encrypt(p.getPassword()));
			stmt.setString(6, p.getPhone());
			stmt.setString(7, DateUtility.toSQLDateTime(p.getCreationTime()));
			stmt.setString(8, DateUtility.toSQLDateTime(p.getLastLogin()));
			stmt.setInt(9, p.getStatus().code);
			stmt.setString(10, p.getInstName());
			stmt.setString(11, p.getLogoUrl());
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			p.setPartnerId(rs.getInt(1));
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		} catch (Exception e) {			
			e.printStackTrace();
			DebugLog.d(e);
		}  finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		p.setPassword("");
		return p;
	}

	public static void updatePartnerInDatabases(Partner p,Connection...connections) throws PartnerNotFoundException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;
		String query = "UPDATE PartnerDao SET name=?,licence=?,organizationNum=?,reference=?,phone=?," +
				"lastLogin=?,status=?, instName=?, logoUrl=? where id=?";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setString(1, p.getName());
			stmt.setString(2, p.getLicence());
			stmt.setString(3, p.getOrganizationNum());
			stmt.setString(4, p.getReference());			
			stmt.setString(5, p.getPhone());			
			stmt.setString(6, DateUtility.toSQLDateTime(p.getLastLogin()));
			stmt.setInt(7, p.getStatus().code);
			stmt.setString(8, p.getInstName());
			stmt.setString(9, p.getLogoUrl());
			stmt.setInt(10, p.getPartnerId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new PartnerNotFoundException();
			}
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, null,EduDaoBasic.shouldConnectionClose(connections));
		}
	}

	public static ArrayList<Partner> getAllPartners(){
		String query = "SELECT * FROM PartnerDao";
		ArrayList<Partner> partners = new ArrayList<Partner>();

		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);

			rs = stmt.executeQuery();
			while(rs.next()){
				partners.add(createPartnerByResultSet(rs));
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 
		return partners;
	}

	public static Partner getPartnerById(int id,Connection...connections) throws PartnerNotFoundException{
		String query = "SELECT * FROM PartnerDao WHERE id = ?";
		Partner partner = null;
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;
		try{		
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()){
				partner = createPartnerByResultSet(rs);
			}else{
				throw new PartnerNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 

		return partner;
	}

	public static Partner getPartnerByPhone(String phone) throws PartnerNotFoundException{
		String query = "SELECT * FROM PartnerDao WHERE phone = ?";
		Partner partner = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try{
			conn = EduDaoBasic.getSQLConnection();
			stmt = conn.prepareStatement(query);

			stmt.setString(1, phone);
			rs = stmt.executeQuery();
			if(rs.next()){
				partner = createPartnerByResultSet(rs);
			}else{
				throw new PartnerNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return partner;
	}
	
	public static void changePartnerPassword(int partnerId, String oldPassword, String newPassword) throws AuthenticationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		boolean validOldPassword = true;
		String query = "SELECT * FROM PartnerDao where id = ? and password = ? ";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, partnerId);
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
			query = "UPDATE PartnerDao set password = ? where id = ?";
			try{
				stmt = conn.prepareStatement(query);
				stmt.setString(1, SessionCrypto.encrypt(newPassword));				
				stmt.setInt(2, partnerId);
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
	
	public static Partner authenticatePartner(String phone, String password) throws AuthenticationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		Partner partner = null;
		boolean validPassword = true;
		String query = "SELECT * FROM PartnerDao where phone = ? and password = ? ";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setString(1, phone);
			stmt.setString(2, SessionCrypto.encrypt(password));
			rs = stmt.executeQuery();		
			if(rs.next()){
				partner = createPartnerByResultSet(rs);
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
		return partner;
	}

	private static Partner createPartnerByResultSet(ResultSet rs) throws SQLException {
		return new Partner(rs.getInt("id"), rs.getString("name"), rs.getString("licence"), rs.getString("organizationNum"),
				rs.getString("reference"), "", DateUtility.DateToCalendar(rs.getTimestamp("creationTime")),
				DateUtility.DateToCalendar(rs.getTimestamp("lastLogin")), rs.getString("phone"), AccountStatus.fromInt(rs.getInt("status")),
				rs.getString("instName"),rs.getString("logoUrl"));
	}



}
