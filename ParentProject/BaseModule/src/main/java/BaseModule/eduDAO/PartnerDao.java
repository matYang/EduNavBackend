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
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.partner.PartnerNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.QueryFactory;
import BaseModule.model.Partner;
import BaseModule.model.representation.PartnerSearchRepresentation;

public class PartnerDao {

	public static ArrayList<Partner> searchPartner(PartnerSearchRepresentation sr){
		ArrayList<Partner> plist = new ArrayList<Partner>();
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		int stmtInt = 1;
		String query = QueryFactory.getSearchQuery(sr);		
		try{
			stmt = conn.prepareStatement(query);

			if(sr.getPartnerId()>0){
				stmt.setInt(stmtInt++, sr.getPartnerId());
			}
			if(sr.getCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getCreationTime()));
			}
			if(sr.getWholeName() != null && sr.getWholeName().length() > 0){
				stmt.setString(stmtInt++, sr.getWholeName());
			}
			if(sr.getPhone() != null && sr.getPhone().length() > 0){
				stmt.setString(stmtInt++, sr.getPhone());
			}						
			if(sr.getStatus() != null){
				stmt.setInt(stmtInt++, sr.getStatus().code);
			}

			if(sr.getInstName() != null && sr.getInstName().length() > 0){
				stmt.setString(stmtInt++, sr.getInstName());
			}
			if(sr.getLicence() != null && sr.getLicence().length() > 0){
				stmt.setString(stmtInt++, sr.getLicence());
			}
			if(sr.getOrganizationNum() != null && sr.getOrganizationNum().length() > 0){
				stmt.setString(stmtInt++, sr.getOrganizationNum());
			}
			if(sr.getReference() != null && sr.getReference().length() > 0){
				stmt.setString(stmtInt++,sr.getReference());
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				plist.add(createPartnerByResultSet(rs));
			}

		}catch(SQLException e){
			DebugLog.d(e);
			e.printStackTrace();
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
		}
		return plist;
	}

	public static Partner addPartnerToDatabases(Partner p,Connection...connections) throws ValidationException{
		Connection conn = EduDaoBasic.getConnection(connections);
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO PartnerDao (name,licence,organizationNum,reference,password,phone,creationTime,lastLogin,status,instName,logoUrl)" +
				" values (?,?,?,?,?,?,?,?,?,?,?);";
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, p.getWholeName());
			stmt.setString(2, p.getLicence());
			stmt.setString(3, p.getOrganizationNum());
			stmt.setString(4, p.getReference());
			stmt.setString(5, PasswordCrypto.createHash(p.getPassword()));
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
			stmt.setString(1, p.getWholeName());
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

	public static int getPartnerIdByReference(String reference,Connection...connections) throws PartnerNotFoundException{
		String query = "SELECT * FROM PartnerDao WHERE reference = ?";
		int partnerId = -1;
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;
		try{		
			stmt = conn.prepareStatement(query);

			stmt.setString(1, reference);
			rs = stmt.executeQuery();
			if(rs.next()){
				partnerId = rs.getInt("id");
			}else{
				throw new PartnerNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 

		return partnerId;
	}

	public static int getPartnerIdByInstName(String instName,Connection...connections) throws PartnerNotFoundException{
		String query = "SELECT * FROM PartnerDao WHERE instName = ?";
		int partnerId = -1;
		PreparedStatement stmt = null;
		Connection conn = EduDaoBasic.getConnection(connections);
		ResultSet rs = null;
		try{		
			stmt = conn.prepareStatement(query);

			stmt.setString(1, instName);
			rs = stmt.executeQuery();
			if(rs.next()){
				partnerId = rs.getInt("id");
			}else{
				throw new PartnerNotFoundException();
			}
		}catch(SQLException e){
			DebugLog.d(e);
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 

		return partnerId;
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
		boolean validOldPassword = false;
		String query = "SELECT * FROM PartnerDao where id = ? ";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, partnerId);					
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
			query = "UPDATE PartnerDao set password = ? where id = ?";
			try{
				stmt = conn.prepareStatement(query);
				stmt.setString(1, PasswordCrypto.createHash(newPassword));				
				stmt.setInt(2, partnerId);
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

	public static Partner authenticatePartner(String phone, String password) throws AuthenticationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		Partner partner = null;
		boolean validPassword = false;
		String query = "SELECT * FROM PartnerDao where phone = ? ";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setString(1, phone);			
			rs = stmt.executeQuery();		
			if(rs.next()){
				validPassword = PasswordCrypto.validatePassword(password, rs.getString("password"));
				if(validPassword){
					partner = createPartnerByResultSet(rs);
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
			if(!validPassword){
				throw new AuthenticationException("手机号码或密码输入错误");
			}
		}
		return partner;
	}

	public static void recoverPartnerPassword(String phone, String newPassword) throws AuthenticationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		String query = "UPDATE PartnerDao set password = ? where phone = ?";
		boolean success = true;
		try{
			stmt = conn.prepareStatement(query);
			stmt.setString(1, PasswordCrypto.createHash(newPassword));				
			stmt.setString(2, phone);
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

	private static Partner createPartnerByResultSet(ResultSet rs) throws SQLException {
		return new Partner(rs.getInt("id"), rs.getString("name"), rs.getString("licence"), rs.getString("organizationNum"),
				rs.getString("reference"), "", DateUtility.DateToCalendar(rs.getTimestamp("creationTime")),
				DateUtility.DateToCalendar(rs.getTimestamp("lastLogin")), rs.getString("phone"), AccountStatus.fromInt(rs.getInt("status")),
				rs.getString("instName"),rs.getString("logoUrl"));
	}



}
