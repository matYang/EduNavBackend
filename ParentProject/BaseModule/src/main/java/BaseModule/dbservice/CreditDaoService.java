package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.CreditDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CreditNotFoundException;
import BaseModule.model.Credit;
import BaseModule.model.representation.CreditSearchRepresentation;

public class CreditDaoService {

	public static Credit createCredit(Credit c,Connection...connections) throws SQLException{
		return CreditDao.addCreditToDatabases(c,connections);
	}
	
	public static void updateCredit(Credit c,Connection...connections) throws PseudoException, SQLException{
		CreditDao.updateCreditInDatabases(c,connections);
	}
	
	public static Credit getCreditByCreditId(long creditId,Connection...connections) throws PseudoException, SQLException{
		return CreditDao.getCreditByCreditId(creditId, connections);
	}
	
	public static ArrayList<Credit> getCreditByUserId(int userId,Connection...connections){
		CreditSearchRepresentation cr_sr = new CreditSearchRepresentation();
		cr_sr.setUserId(userId);
		return searchCredit(cr_sr);
	}
	
	
	public static ArrayList<Credit> searchCredit(CreditSearchRepresentation cr_sr,Connection...connections){
		return null;
	}
}
