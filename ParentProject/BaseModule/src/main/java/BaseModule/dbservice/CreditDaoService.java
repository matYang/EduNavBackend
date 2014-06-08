package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.CreditDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CreditNotFoundException;
import BaseModule.model.Credit;

public class CreditDaoService {

	public static Credit createCredit(Credit c,Connection...connections) throws SQLException{
		return CreditDao.addCreditToDatabases(c,connections);
	}
	
	public static void updateCredit(Credit c,Connection...connections) throws PseudoException, SQLException{
		CreditDao.updateCreditInDatabases(c,connections);
	}
	
	public static ArrayList<Credit> getCreditByUserId(int userId){
		return CreditDao.getCreditByUserId(userId);
	}
	
	public static Credit getCreditByCreditId(long creditId){
		return CreditDao.getCreditByCreditId(creditId);
	}
}
