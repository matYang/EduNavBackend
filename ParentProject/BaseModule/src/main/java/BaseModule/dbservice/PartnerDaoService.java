package BaseModule.dbservice;

import java.util.ArrayList;


import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.PartnerNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Partner;
import BaseModule.model.representation.PartnerSearchRepresentation;

import java.sql.Connection;
import java.sql.SQLException;
public class PartnerDaoService {

	public static Partner getPartnerById(int id,Connection...connections) throws PseudoException, SQLException{
		return PartnerDao.getPartnerById(id, connections);
	}

	
	public static void updatePartner(Partner partner,Connection...connections) throws PseudoException, SQLException{
		PartnerDao.updatePartnerInDatabases(partner,connections);
	}

	public static Partner createPartner(Partner p,Connection...connections) throws PseudoException, SQLException{
		return PartnerDao.addPartnerToDatabases(p,connections);
	}	
	
	public static Partner authenticatePartner(String phone,String password) throws PseudoException, SQLException{
		Connection conn = null;
		boolean ok = false;
		Partner partner = null;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			//in this case Dao layers takes care of locking
			partner = PartnerDao.authenticatePartner(phone, password, conn);
			ok = true;
		}finally{
			EduDaoBasic.handleCommitFinally(conn, ok, true);
		}

		return partner;
	}

	public static void changePassword(int partnerId, String oldPassword, String newPassword) throws PseudoException, SQLException{
		Connection conn = null;
		boolean ok = false;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			//in this case Dao layers takes care of locking
			PartnerDao.changePartnerPassword(partnerId, oldPassword, newPassword, conn);
			ok = true;
		}finally{
			EduDaoBasic.handleCommitFinally(conn, ok, true);
		}
		
	}

	public static void recoverPassword(String phone, String newPassword) throws PseudoException, SQLException{
		PartnerDao.recoverPartnerPassword(phone, newPassword);
	}

	public static ArrayList<Partner> searchPartner(PartnerSearchRepresentation sr) throws SQLException{
		return PartnerDao.searchPartner(sr);
	}

	public static Partner getPartnerByReference(String reference) throws SQLException, PseudoException{
		PartnerSearchRepresentation sr = new PartnerSearchRepresentation();
		sr.setReference(reference);
		ArrayList<Partner> partners = PartnerDao.searchPartner(sr);
		if (partners.size() == 0){
			throw new PartnerNotFoundException();
		}
		else if (partners.size() > 1){
			throw new ValidationException("系统错误：编码重复");
		}
		else{
			return partners.get(0);
		}

	}
	
	public static boolean isReferenceAvailable(String reference) throws SQLException{
		PartnerSearchRepresentation sr = new PartnerSearchRepresentation();
		sr.setReference(reference);
		return PartnerDao.searchPartner(sr).size() == 0;
	}

}
