package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.eduDAO.CreditDao;
import BaseModule.exception.credit.CreditNotFoundException;
import BaseModule.model.Credit;

public class CreditDaoService {

	public static Credit createCredit(Credit c){
		return CreditDao.addCreditToDatabases(c);
	}
	
	public static void updateCredit(Credit c) throws CreditNotFoundException{
		CreditDao.updateCreditInDatabases(c);
	}
	
	public static ArrayList<Credit> getCreditByUserId(int userId){
		return CreditDao.getCreditByUserId(userId);
	}
	
	public static Credit getCreditByCreditId(long creditId){
		return CreditDao.getCreditByCreditId(creditId);
	}
}
