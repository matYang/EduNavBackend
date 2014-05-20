package AdminModule.dbservice;

import java.util.ArrayList;

import AdminModule.adminDao.AdminAccountDao;
import AdminModule.exception.AdminAccountNotFoundException;
import AdminModule.model.AdminAccount;
import BaseModule.exception.AuthenticationException;

public class AdminAccountDaoService {

	public static ArrayList<AdminAccount> getAllAdminAccounts(){
		return AdminAccountDao.getAllAdminAccounts();
	}
	
	public static AdminAccount getAdminAccountById(int id) throws AdminAccountNotFoundException{
		return AdminAccountDao.getAdminAccountById(id);
	}
	
	public static AdminAccount getAdminAccountByPhone(String phone) throws AdminAccountNotFoundException{
		return AdminAccountDao.getAdminAccountByPhone(phone);
	}
	
	public static AdminAccount getAdminAccountByName(String name) throws AdminAccountNotFoundException{
		return AdminAccountDao.getAdminAccountByName(name);
	}
	
	public static AdminAccount createAdminAccount(AdminAccount account){
		return AdminAccountDao.addAdminAccountToDatabases(account);
	}
	
	public static void updateAdminAccount(AdminAccount account) throws AdminAccountNotFoundException{
		AdminAccountDao.updateAdminAccountInDatabases(account);
	}
	
	public static void changePassword(int adminId, String oldPassword, String newPassword) throws AuthenticationException{
		AdminAccountDao.changeAdminAccountPassword(adminId, oldPassword, newPassword);
	}
	
	//authenticate use reference
	//TODO using reference instead of phone
	public static AdminAccount authenticateAdminAccount(String phone, String password) throws AuthenticationException{
		return AdminAccountDao.authenticateAdminAccount(phone, password);
	}
	
	//TODO
	public static void forceChangePassword(int targetAdminId, int initiatorAdminId, String newPassword) throws AuthenticationException{
		
	}
}
