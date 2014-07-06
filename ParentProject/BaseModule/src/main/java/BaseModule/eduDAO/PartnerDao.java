package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.common.Parser;
import BaseModule.configurations.ServerConfig;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.PartnerQualification;
import BaseModule.encryption.PasswordCrypto;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.notFound.PartnerNotFoundException;
import BaseModule.model.ClassPhoto;
import BaseModule.model.Partner;
import BaseModule.model.Teacher;
import BaseModule.model.representation.PartnerSearchRepresentation;

public class PartnerDao {

	public static ArrayList<Partner> searchPartner(PartnerSearchRepresentation sr,Connection...connections) throws SQLException{
		ArrayList<Partner> plist = new ArrayList<Partner>();
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		int stmtInt = 1;
		String query = sr.getSearchQuery();
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);

			if(sr.getPartnerId()>0){
				stmt.setInt(stmtInt++, sr.getPartnerId());
			}
			if(sr.getStartCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getStartCreationTime()));
			}
			if(sr.getFinishCreationTime() != null){
				stmt.setString(stmtInt++, DateUtility.toSQLDateTime(sr.getFinishCreationTime()));
			}
			if(sr.getWholeName() != null && sr.getWholeName().length() > 0){
				stmt.setString(stmtInt++, sr.getWholeName());
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
				plist.add(createPartnerByResultSet(rs,conn));
			}

		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));
		}
		return plist;
	}

	public static Partner addPartnerToDatabases(Partner p,Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String query = "INSERT INTO Partner (name,licence,organizationNum,reference,password,creationTime,lastLogin," +
				"status,instName,logoUrl,partnerIntro,partnerDistinction,licenceImgUrl,taxRegistrationImgUrl,eduQualificationImgUrl," +
				"hqLocation,subLocations,uniformRegistraLocation,hqContact,hqContactPhone,hqContactSecOpt,courseContact,courseContactPhone," +
				"studentInqueryPhone,registraContact,registraContactPhone,registraContactFax,defaultCutOffDay,defaultCutOffTime," +
				"partnerQualification) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		int stmtInt = 1;
		
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(stmtInt++, p.getWholeName());
			stmt.setString(stmtInt++, p.getLicence());
			stmt.setString(stmtInt++, p.getOrganizationNum());
			stmt.setString(stmtInt++, p.getReference());
			stmt.setString(stmtInt++, PasswordCrypto.createHash(p.getPassword()));			
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(p.getCreationTime()));
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(p.getLastLogin()));
			stmt.setInt(stmtInt++, p.getStatus().code);
			stmt.setString(stmtInt++, p.getInstName());
			stmt.setString(stmtInt++, p.getLogoUrl());	
			stmt.setString(stmtInt++, p.getPartnerIntro());
			stmt.setString(stmtInt++, p.getPartnerDistinction());
			stmt.setString(stmtInt++, p.getLiscenceImgUrl());
			stmt.setString(stmtInt++, p.getTaxRegistrationImgUrl());
			stmt.setString(stmtInt++, p.getEduQualificationImgUrl());
			stmt.setString(stmtInt++, p.getHqLocation());
			stmt.setString(stmtInt++, Parser.listToString(p.getSubLocations(), ServerConfig.normalSpliter));
			stmt.setInt(stmtInt++, p.isUniformRegistraLocation() ? 1 : 0);
			stmt.setString(stmtInt++, p.getHqContact());
			stmt.setString(stmtInt++, p.getHqContactPhone());
			stmt.setString(stmtInt++, p.getHqContactSecOpt());
			stmt.setString(stmtInt++, p.getCourseContact());
			stmt.setString(stmtInt++, p.getCourseContactPhone());
			stmt.setString(stmtInt++, p.getStudentInqueryPhone());
			stmt.setString(stmtInt++, p.getRegistraContact());
			stmt.setString(stmtInt++, p.getRegistraContactPhone());
			stmt.setString(stmtInt++, p.getRegistraContactFax());
			stmt.setInt(stmtInt++, p.getDefaultCutoffDay());
			stmt.setInt(stmtInt++, p.getDefaultCutoffTime());
			stmt.setInt(stmtInt++, p.getPartnerQualification().code);

			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			p.setPartnerId(rs.getInt(1));
			p.setPassword("");

		} finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return p;
	}

	public static void updatePartnerInDatabases(Partner p,Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String query = "UPDATE Partner SET name=?,licence=?,organizationNum=?,reference=?,password=?,lastLogin=?," +
				"status=?,instName=?,logoUrl=?,partnerIntro=?,partnerDistinction=?,licenceImgUrl=?,taxRegistrationImgUrl=?,eduQualificationImgUrl=?," +
				"hqLocation=?,subLocations=?,uniformRegistraLocation=?,hqContact=?,hqContactPhone=?,hqContactSecOpt=?,courseContact=?,courseContactPhone=?," +
				"studentInqueryPhone=?,registraContact=?,registraContactPhone=?,registraContactFax=?,defaultCutOffDay=?,defaultCutOffTime=?," +
				"partnerQualification=? where id=?";
		int stmtInt = 1;
		
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setString(stmtInt++, p.getWholeName());
			stmt.setString(stmtInt++, p.getLicence());
			stmt.setString(stmtInt++, p.getOrganizationNum());
			stmt.setString(stmtInt++, p.getReference());
			stmt.setString(stmtInt++, PasswordCrypto.createHash(p.getPassword()));						
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(p.getLastLogin()));
			stmt.setInt(stmtInt++, p.getStatus().code);
			stmt.setString(stmtInt++, p.getInstName());
			stmt.setString(stmtInt++, p.getLogoUrl());	
			stmt.setString(stmtInt++, p.getPartnerIntro());
			stmt.setString(stmtInt++, p.getPartnerDistinction());
			stmt.setString(stmtInt++, p.getLiscenceImgUrl());
			stmt.setString(stmtInt++, p.getTaxRegistrationImgUrl());
			stmt.setString(stmtInt++, p.getEduQualificationImgUrl());
			stmt.setString(stmtInt++, p.getHqLocation());
			stmt.setString(stmtInt++, Parser.listToString(p.getSubLocations(), ServerConfig.normalSpliter));
			stmt.setInt(stmtInt++, p.isUniformRegistraLocation() ? 1 : 0);
			stmt.setString(stmtInt++, p.getHqContact());
			stmt.setString(stmtInt++, p.getHqContactPhone());
			stmt.setString(stmtInt++, p.getHqContactSecOpt());
			stmt.setString(stmtInt++, p.getCourseContact());
			stmt.setString(stmtInt++, p.getCourseContactPhone());
			stmt.setString(stmtInt++, p.getStudentInqueryPhone());
			stmt.setString(stmtInt++, p.getRegistraContact());
			stmt.setString(stmtInt++, p.getRegistraContactPhone());
			stmt.setString(stmtInt++, p.getRegistraContactFax());
			stmt.setInt(stmtInt++, p.getDefaultCutoffDay());
			stmt.setInt(stmtInt++, p.getDefaultCutoffTime());
			stmt.setInt(stmtInt++, p.getPartnerQualification().code);
			stmt.setInt(stmtInt++, p.getPartnerId());
			
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new PartnerNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, null,EduDaoBasic.shouldConnectionClose(connections));
		}
	}


	public static Partner getPartnerById(int id,Connection...connections) throws PseudoException, SQLException{
		String query = "SELECT * FROM Partner WHERE id = ?";
		Partner partner = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try{		
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);

			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()){
				partner = createPartnerByResultSet(rs,conn);
			}else{
				throw new PartnerNotFoundException();
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 

		return partner;
	}


	public static void changePartnerPassword(int partnerId, String oldPassword, String newPassword,Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		boolean validOldPassword = false;
		String query = "SELECT * FROM Partner where id = ? for update";
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, partnerId);					
			rs = stmt.executeQuery();						
			if(rs.next()){
				validOldPassword = PasswordCrypto.validatePassword(oldPassword, rs.getString("password"));
				if(validOldPassword){
					query = "UPDATE Partner set password = ? where id = ?";

					stmt = conn.prepareStatement(query);
					stmt.setString(1, PasswordCrypto.createHash(newPassword));				
					stmt.setInt(2, partnerId);
					int recordsAffected = stmt.executeUpdate();
					if(recordsAffected==0){
						throw new PartnerNotFoundException();
					}
				}
				else {
					throw new AuthenticationException("更改密码失败");
				}		
			}
			else{
				throw new AuthenticationException();
			}
		}
		finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));				
		}

	}

	public static Partner authenticatePartner(String reference, String password,Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		Partner partner = null;
		boolean validPassword = false;
		String query = "SELECT * FROM Partner where reference = ? for update";
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setString(1, reference);			
			rs = stmt.executeQuery();		
			if(rs.next()){
				validPassword = PasswordCrypto.validatePassword(password, rs.getString("password"));
				if(validPassword){
					partner = createPartnerByResultSet(rs,conn);
					partner.setLastLogin(DateUtility.getCurTimeInstance());
					updatePartnerInDatabases(partner, conn);
				}else{
					throw new AuthenticationException("商家号或密码输入错误");
				}				
			}
			else{
				throw new AuthenticationException();
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));			
		}
		return partner;
	}

	public static void recoverPartnerPassword(String reference, String newPassword,Connection...connections) throws PseudoException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;		
		ResultSet rs = null;
		String query = "UPDATE Partner set password = ? where reference = ?";		
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setString(1, PasswordCrypto.createHash(newPassword));				
			stmt.setString(2, reference);
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new AuthenticationException("商家号或密码输入有误");
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, EduDaoBasic.shouldConnectionClose(connections));			
		}
	}

	private static Partner createPartnerByResultSet(ResultSet rs, Connection...connections) throws SQLException {

		ArrayList<ClassPhoto> classPhotoList = new ArrayList<ClassPhoto>();		
		ArrayList<Teacher> teacherList = new ArrayList<Teacher>();

		classPhotoList = ClassPhotoDao.getPartnerClassPhotos(rs.getInt("id"), connections);	
		teacherList = TeacherDao.getPartnerTeachers(rs.getInt("id"), connections);

		return new Partner(rs.getInt("id"), rs.getString("name"), rs.getString("instName"),rs.getString("licence"), rs.getString("organizationNum"),
				rs.getString("logoUrl"),classPhotoList,teacherList,rs.getString("reference"),rs.getString("password"),  AccountStatus.fromInt(rs.getInt("status")),
				DateUtility.DateToCalendar(rs.getTimestamp("creationTime")),DateUtility.DateToCalendar(rs.getTimestamp("lastLogin")),rs.getString("partnerIntro"),
				rs.getString("partnerDistinction"),rs.getString("licenceImgUrl"),rs.getString("taxRegistrationImgUrl"),rs.getString("eduQualificationImgUrl"),
				rs.getString("hqLocation"),Parser.stringToList(rs.getString("subLocations"), ServerConfig.normalSpliter, String.class),
				rs.getBoolean("uniformRegistraLocation"),rs.getString("hqContact"),rs.getString("hqContactPhone"),rs.getString("hqContactSecOpt"),
				rs.getString("courseContact"),rs.getString("courseContactPhone"),rs.getString("studentInqueryPhone"),rs.getString("registraContact"),
				rs.getString("registraContactPhone"),rs.getString("registraContactFax"),rs.getInt("defaultCutOffDay"),rs.getInt("defaultCutOffTime"),
				PartnerQualification.fromInt(rs.getInt("partnerQualification")));
	}

}
