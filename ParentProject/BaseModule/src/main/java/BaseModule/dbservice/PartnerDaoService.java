package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.common.DateUtility;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.notFound.PartnerNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Partner;
import BaseModule.model.representation.PartnerSearchRepresentation;

import java.sql.Connection;
import java.sql.SQLException;
public class PartnerDaoService {

	public static ArrayList<Partner> getAllPartners(){
		return PartnerDao.getAllPartners();
	}

	public static Partner getPartnerById(int id,Connection...connections) throws PartnerNotFoundException{
		return PartnerDao.getPartnerById(id, connections);
	}

	public static Partner getPartnerByPhone(String phone) throws PartnerNotFoundException{
		PartnerSearchRepresentation p_sr = new PartnerSearchRepresentation();
		p_sr.setPhone(phone);
		ArrayList<Partner> partners = searchPartner(p_sr);
		if (partners.size() == 0){
			throw new PartnerNotFoundException();
		}
		return partners.get(0);
	}

	public static void updatePartner(Partner partner,Connection...connections) throws PartnerNotFoundException, SQLException{
		PartnerDao.updatePartnerInDatabases(partner,connections);
	}

	public static Partner createPartner(Partner p,Connection...connections) throws ValidationException, SQLException{
		return PartnerDao.addPartnerToDatabases(p,connections);
	}

	public static boolean isCellPhoneAvailable(String phone){
		PartnerSearchRepresentation p_sr = new PartnerSearchRepresentation();
		p_sr.setPhone(phone);
		ArrayList<Partner> partners = searchPartner(p_sr);
		return partners.size() == 0;
	}

	public static void changePassword(int partnerId, String oldPassword, String newPassword) throws AuthenticationException, SQLException{
		PartnerDao.changePartnerPassword(partnerId, oldPassword, newPassword);
	}

	public static void recoverPassword(String phone, String newPassword) throws AuthenticationException, SQLException{
		PartnerDao.recoverPartnerPassword(phone, newPassword);
	}

	public static Partner authenticatePartner(String phone,String password) throws AuthenticationException, SQLException, PartnerNotFoundException{
		Partner partner =  PartnerDao.authenticatePartner(phone, password);
		partner.setLastLogin(DateUtility.getCurTimeInstance());
		updatePartner(partner);
		return partner;
	}

	public static ArrayList<Partner> searchPartner(PartnerSearchRepresentation sr){
		return PartnerDao.searchPartner(sr);
	}

	public static ArrayList<Partner> getPartnerByReference(String reference){
		PartnerSearchRepresentation sr = new PartnerSearchRepresentation();
		sr.setReference(reference);
		return searchPartner(sr);

	}
	
	public static boolean isReferenceAvailable(String reference){
		return getPartnerByReference(reference).size() == 0;
	}
}
