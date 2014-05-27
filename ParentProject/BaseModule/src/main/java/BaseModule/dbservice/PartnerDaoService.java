package BaseModule.dbservice;

import java.util.ArrayList;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.partner.PartnerNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Partner;
import BaseModule.model.representation.PartnerSearchRepresentation;

import java.sql.Connection;
public class PartnerDaoService {

	public static ArrayList<Partner> getAllPartners(){
		return PartnerDao.getAllPartners();
	}

	public static Partner getPartnerById(int id,Connection...connections) throws PartnerNotFoundException{
		return PartnerDao.getPartnerById(id, connections);
	}

	public static Partner getPartnerByPhone(String phone) throws PartnerNotFoundException{
		return PartnerDao.getPartnerByPhone(phone);
	}

	public static void updatePartner(Partner partner,Connection...connections) throws PartnerNotFoundException{
		PartnerDao.updatePartnerInDatabases(partner,connections);
	}

	public static Partner createPartner(Partner p,Connection...connections) throws ValidationException{
		return PartnerDao.addPartnerToDatabases(p,connections);
	}

	public static boolean isCellPhoneAvailable(String phone){
		try{
			PartnerDao.getPartnerByPhone(phone);
		}catch(PartnerNotFoundException ex){
			return true;
		}
		return false;
	}

	public static void changePassword(int partnerId, String oldPassword, String newPassword) throws AuthenticationException{
		PartnerDao.changePartnerPassword(partnerId, oldPassword, newPassword);
	}

	public static void recoverPassword(String phone, String newPassword) throws AuthenticationException{
		PartnerDao.recoverPartnerPassword(phone, newPassword);
	}

	public static Partner authenticatePartner(String phone,String password) throws AuthenticationException{
		return PartnerDao.authenticatePartner(phone, password);
	}

	public static ArrayList<Partner> searchPartners(PartnerSearchRepresentation sr){
		return PartnerDao.searchPartner(sr);
	}

	public static ArrayList<Partner> getPartnerByReference(String reference){
		PartnerSearchRepresentation sr = new PartnerSearchRepresentation();
		sr.setReference(reference);
		return PartnerDao.searchPartner(sr);

	}
}
