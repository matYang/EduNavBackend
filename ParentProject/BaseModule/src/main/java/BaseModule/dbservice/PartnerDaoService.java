package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.eduDAO.PartnerDao;
import BaseModule.model.Partner;

public class PartnerDaoService {

	public static ArrayList<Partner> getAllPartners(){
		return PartnerDao.getAllPartners();
	}
	
	public static Partner getPartnerById(int id){
		return PartnerDao.
	}
}
