package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.Status;
import BaseModule.exception.partner.PartnerNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Partner;

public class PartnerDao {

	public static Partner addPartnerToDatabases(Partner p) throws ValidationException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO PartnerDao (name,licence,organizationNum,reference,password,phone,creationTime,lastLogin,status)" +
				" values (?,?,?,?,?,?,?,?,?);";
		try{
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, p.getName());
			stmt.setString(2, p.getLicence());
			stmt.setString(3, p.getOrganizationNum());
			stmt.setString(4, p.getReference());
			stmt.setString(5, p.getPassword());
			stmt.setString(6, p.getPhone());
			stmt.setString(7, DateUtility.toSQLDateTime(p.getCreationTime()));
			stmt.setString(8, DateUtility.toSQLDateTime(p.getLastLogin()));
			stmt.setInt(9, p.getStatus().code);

			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			p.setPartnerId(rs.getInt(1));
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,true);
		} 

		return p;
	}

	public static void updatePartnerInDatabases(Partner p) throws PartnerNotFoundException{
		Connection conn = EduDaoBasic.getSQLConnection();
		PreparedStatement stmt = null;
		String query = "UPDATE PartnerDao SET name=?,licence=?,organizationNum=?,reference=?,password=?,phone=?," +
				"lastLogin=?,status=? where id=?";
		try{
			stmt = conn.prepareStatement(query);
			stmt.setString(1, p.getName());
			stmt.setString(2, p.getLicence());
			stmt.setString(3, p.getOrganizationNum());
			stmt.setString(4, p.getReference());
			stmt.setString(5, p.getPassword());
			stmt.setString(6, p.getPhone());			
			stmt.setString(7, DateUtility.toSQLDateTime(p.getLastLogin()));
			stmt.setInt(8, p.getStatus().code);
			stmt.setInt(9, p.getPartnerId());
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new PartnerNotFoundException();
			}
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		} finally  {
			EduDaoBasic.closeResources(conn, stmt, null,true);
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

	private static Partner createPartnerByResultSet(ResultSet rs) throws SQLException {
		return new Partner(rs.getInt("id"), rs.getString("name"), rs.getString("licence"), rs.getString("organizationNum"),
				rs.getString("reference"), rs.getString("password"), DateUtility.DateToCalendar(rs.getTimestamp("creationTime")),
				DateUtility.DateToCalendar(rs.getTimestamp("lastLogin")), rs.getString("phone"), Status.fromInt(rs.getInt("status")));
	}



}
