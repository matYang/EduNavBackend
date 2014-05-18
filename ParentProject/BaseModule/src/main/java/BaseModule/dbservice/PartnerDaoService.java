package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.partner.PartnerNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Partner;
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
	
	public static void updatePartner(Partner partner) throws PartnerNotFoundException{
		PartnerDao.updatePartnerInDatabases(partner);
	}
	
	public static Partner createPartner(Partner p) throws ValidationException{
		return PartnerDao.addPartnerToDatabases(p);
	}
	
	public static void changePassword(int partnerId, String oldPassword, String newPassword) throws AuthenticationException{
		PartnerDao.changePartnerPassword(partnerId, oldPassword, newPassword);
	}
	
	public static Partner authenticatePartner(String phone,String password) throws AuthenticationException{
		return PartnerDao.authenticatePartner(phone, password);
	}
}
