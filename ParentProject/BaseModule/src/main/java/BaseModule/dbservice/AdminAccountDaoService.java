package BaseModule.dbservice;

import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.common.DateUtility;
import BaseModule.eduDAO.AdminAccountDao;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.AdminAccountNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.AdminAccount;
import BaseModule.model.representation.AdminSearchRepresentation;

public class AdminAccountDaoService {

	public static ArrayList<AdminAccount> getAllAdminAccounts(){
		return AdminAccountDao.getAllAdminAccounts();
	}
	
	public static AdminAccount getAdminAccountById(int id) throws AdminAccountNotFoundException{
		return AdminAccountDao.getAdminAccountById(id);
	}
	
	public static AdminAccount createAdminAccount(AdminAccount account) throws ValidationException, SQLException{
		return AdminAccountDao.addAdminAccountToDatabases(account);
	}
	
	public static void updateAdminAccount(AdminAccount account) throws AdminAccountNotFoundException, ValidationException, SQLException{
		AdminAccountDao.updateAdminAccountInDatabases(account);
	}
	
	public static void changePassword(int adminId, String oldPassword, String newPassword) throws AuthenticationException{
		AdminAccountDao.changeAdminAccountPassword(adminId, oldPassword, newPassword);
	}
	
	public static void changeAdminPassword(int adminId, String password) throws PseudoException{
		AdminAccountDao.changeAdminAccountPassword(adminId, password);
	}
	
	public static AdminAccount authenticateAdminAccount(String referece, String password) throws AuthenticationException, AdminAccountNotFoundException, ValidationException, SQLException{
		AdminAccount account = AdminAccountDao.authenticateAdminAccount(referece, password);
		account.setLastLogin(DateUtility.getCurTimeInstance());
		updateAdminAccount(account);
		return account;
	}
	
	public static ArrayList<AdminAccount> searchAdminAccount(AdminSearchRepresentation sr){
		return AdminAccountDao.searchAdminAccount(sr);
	}
	
	public static ArrayList<AdminAccount> getAdminAccountByReference(String reference){
		AdminSearchRepresentation sr = new AdminSearchRepresentation();
		sr.setReference(reference);
		return AdminAccountDao.searchAdminAccount(sr);
	}
	
	public static boolean isReferenceAvailable(String reference){
		return getAdminAccountByReference(reference).size() == 0;
	}
	
}
