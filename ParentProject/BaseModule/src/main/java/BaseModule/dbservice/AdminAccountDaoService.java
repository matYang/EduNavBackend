package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.AdminAccountDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
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
	
	public static void changePassword(final int adminId, String oldPassword, String newPassword) throws PseudoException, SQLException{
		Connection conn = null;
		boolean ok = false;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			//in this case Dao layers takes care of locking
			AdminAccountDao.changeAdminAccountPassword(adminId, oldPassword, newPassword, conn);
			ok = true;
		}finally{
			EduDaoBasic.handleCommitFinally(conn, ok, true);
		}
	}
	
	public static void changeAdminPassword(final int adminId, String password) throws PseudoException, SQLException{
		AdminAccountDao.changeAdminAccountPassword(adminId, password);
	}
	
	public static AdminAccount authenticateAdminAccount(String referece, String password) throws PseudoException, SQLException{
		Connection conn = null;
		boolean ok = false;
		AdminAccount account = null;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			//in this case Dao layers takes care of locking
			account = AdminAccountDao.authenticateAdminAccount(referece, password, conn);
			ok = true;
		}finally{
			EduDaoBasic.handleCommitFinally(conn, ok, true);
		}

		return account;
	}
	
	public static ArrayList<AdminAccount> searchAdminAccount(AdminSearchRepresentation sr) throws SQLException{
		return AdminAccountDao.searchAdminAccount(sr);
	}
	
	public static AdminAccount getAdminAccountByReference(String reference) throws SQLException, PseudoException{
		AdminSearchRepresentation sr = new AdminSearchRepresentation();
		sr.setReference(reference);
		ArrayList<AdminAccount> accounts = AdminAccountDao.searchAdminAccount(sr);
		if (accounts.size() == 0){
			throw new AdminAccountNotFoundException();
		}
		else if (accounts.size() > 1){
			throw new ValidationException("系统错误：编码重复");
		}
		else{
			return accounts.get(0);
		}
	}
	
	public static boolean isReferenceAvailable(String reference) throws SQLException{
		AdminSearchRepresentation sr = new AdminSearchRepresentation();
		sr.setReference(reference);
		return AdminAccountDao.searchAdminAccount(sr).size() == 0;
	}
	
}
