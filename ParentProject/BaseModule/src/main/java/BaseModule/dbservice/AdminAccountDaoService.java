package BaseModule.dbservice;

import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.common.DateUtility;
import BaseModule.eduDAO.AdminAccountDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.notFound.AdminAccountNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.AdminAccount;
import BaseModule.model.representation.AdminSearchRepresentation;

public class AdminAccountDaoService {

	public static AdminAccount getAdminAccountById(int id) throws PseudoException, SQLException{
		return AdminAccountDao.getAdminAccountById(id);
	}
	
	public static AdminAccount createAdminAccount(AdminAccount account) throws PseudoException, SQLException{
		return AdminAccountDao.addAdminAccountToDatabases(account);
	}
	
	public static void updateAdminAccount(AdminAccount account) throws PseudoException, SQLException{
		AdminAccountDao.updateAdminAccountInDatabases(account);
	}
	
	public static void changePassword(int adminId, String oldPassword, String newPassword) throws PseudoException, SQLException{
		AdminAccountDao.changeAdminAccountPassword(adminId, oldPassword, newPassword);
	}
	
	public static void changeAdminPassword(int adminId, String password) throws PseudoException, SQLException{
		AdminAccountDao.changeAdminAccountPassword(adminId, password);
	}
	
	public static AdminAccount authenticateAdminAccount(String referece, String password) throws PseudoException, SQLException{
		AdminAccount account = AdminAccountDao.authenticateAdminAccount(referece, password);
		account.setLastLogin(DateUtility.getCurTimeInstance());
		updateAdminAccount(account);
		return account;
	}
	
	public static ArrayList<AdminAccount> searchAdminAccount(AdminSearchRepresentation sr) throws SQLException{
		return AdminAccountDao.searchAdminAccount(sr);
	}
	
	public static ArrayList<AdminAccount> getAdminAccountByReference(String reference) throws SQLException{
		AdminSearchRepresentation sr = new AdminSearchRepresentation();
		sr.setReference(reference);
		return AdminAccountDao.searchAdminAccount(sr);
	}
	
	public static boolean isReferenceAvailable(String reference) throws SQLException{
		return getAdminAccountByReference(reference).size() == 0;
	}
	
}
