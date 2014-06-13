package BaseModule.dbservice;

import java.util.ArrayList;


import BaseModule.common.DateUtility;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CourseNotFoundException;
import BaseModule.exception.notFound.PartnerNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.representation.PartnerSearchRepresentation;

import java.sql.Connection;
import java.sql.SQLException;
public class PartnerDaoService {

	public static Partner getPartnerById(int id,Connection...connections) throws PseudoException, SQLException{
		return PartnerDao.getPartnerById(id, connections);
	}

	public static Partner getPartnerByPhone(String phone) throws PartnerNotFoundException, SQLException{
		PartnerSearchRepresentation p_sr = new PartnerSearchRepresentation();
		p_sr.setPhone(phone);
		ArrayList<Partner> partners = searchPartner(p_sr);
		if (partners.size() == 0){
			throw new PartnerNotFoundException();
		}
		return partners.get(0);
	}

	public static void updatePartner(Partner partner,Connection...connections) throws PseudoException, SQLException{
		PartnerDao.updatePartnerInDatabases(partner,connections);
	}

	public static Partner createPartner(Partner p,Connection...connections) throws PseudoException, SQLException{
		return PartnerDao.addPartnerToDatabases(p,connections);
	}

	public static boolean isCellPhoneAvailable(String phone) throws SQLException{
		PartnerSearchRepresentation p_sr = new PartnerSearchRepresentation();
		p_sr.setPhone(phone);
		ArrayList<Partner> partners = searchPartner(p_sr);
		return partners.size() == 0;
	}

	public static void changePassword(int partnerId, String oldPassword, String newPassword) throws PseudoException, SQLException{
		PartnerDao.changePartnerPassword(partnerId, oldPassword, newPassword);
	}

	public static void recoverPassword(String phone, String newPassword) throws PseudoException, SQLException{
		PartnerDao.recoverPartnerPassword(phone, newPassword);
	}

	public static Partner authenticatePartner(String phone,String password) throws PseudoException, SQLException{
		Partner partner =  PartnerDao.authenticatePartner(phone, password);
		partner.setLastLogin(DateUtility.getCurTimeInstance());
		updatePartner(partner);
		return partner;
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
