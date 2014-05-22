package daoTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;
import AdminModule.adminDao.AdminAccountDao;
import AdminModule.exception.AdminAccountNotFoundException;
import AdminModule.model.AdminAccount;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.validation.ValidationException;



public class AdminAccountDaoTest {

	@Test
	public void testCreate() {
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "123445676543";
		String reference = "dsfdsf";
		Privilege privilege = Privilege.mamagement;
		AccountStatus status = AccountStatus.activated;
		String password = "hgfudifhg3489";
		AdminAccount account = new AdminAccount(name,phone,reference,privilege,status,password);
		try{
			AdminAccountDao.addAdminAccountToDatabases(account);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}	
	}
	
	@Test
	public void testGet() throws AdminAccountNotFoundException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "123445676543";
		String reference = "dsfdsf";
		Privilege privilege = Privilege.mamagement;
		AccountStatus status = AccountStatus.activated;
		String password = "hgfudifhg3489";
		AdminAccount account = new AdminAccount(name,phone,reference,privilege,status,password);
		AdminAccountDao.addAdminAccountToDatabases(account);
		account = AdminAccountDao.getAdminAccountById(account.getAdminId());
		AdminAccount test = AdminAccountDao.getAdminAccountByName(account.getName());
		if(account.equals(test)&&test.getPhone().equals(phone)){
			//Passed;
		}else fail();
		
		test = AdminAccountDao.getAdminAccountByPhone(account.getPhone());
		if(account.equals(test)&&test.getReference().equals(reference)){
			//Passed;
		}else fail();
		
		String name2 = "Mattan";
		String phone2 = "12344";
		String reference2 = "dsfsersf";
		Privilege privilege2 = Privilege.routine;
		AccountStatus status2 = AccountStatus.activated;		
		AdminAccount account2 = new AdminAccount(name2,phone2,reference2,privilege2,status2,password);
		AdminAccountDao.addAdminAccountToDatabases(account2);
		account2 = AdminAccountDao.getAdminAccountByPhone(phone2);
		
		ArrayList<AdminAccount> alist = new ArrayList<AdminAccount>();
		alist = AdminAccountDao.getAllAdminAccounts();
		if(alist.size()==2&&alist.get(0).equals(account)&&alist.get(1).equals(account2)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpdate() throws AdminAccountNotFoundException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "1";
		String reference = "dsfdsf";
		Privilege privilege = Privilege.mamagement;
		AccountStatus status = AccountStatus.activated;
		String password = "hgfudifhg3489";
		AdminAccount account = new AdminAccount(name,phone,reference,privilege,status,password);
		AdminAccountDao.addAdminAccountToDatabases(account);
		account = AdminAccountDao.getAdminAccountById(account.getAdminId());
		account.setPhone("2");
		account.setStatus(AccountStatus.deleted);
		AdminAccountDao.updateAdminAccountInDatabases(account);
		account = AdminAccountDao.getAdminAccountById(account.getAdminId());
		if(account.getName().equals(name)&&account.getPhone().equals("2")&&account.getStatus().code==AccountStatus.deleted.code){
			//Passed;
		}else fail();
		
	}
	
	@Test
	public void testUpdateAdminAccountPassword() throws ValidationException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "123445676543";
		String reference = "dsfdsf";
		Privilege privilege = Privilege.mamagement;
		AccountStatus status = AccountStatus.activated;
		String password = "hgfudifhg3489";
		AdminAccount account = new AdminAccount(name,phone,reference,privilege,status,password);
		AdminAccountDao.addAdminAccountToDatabases(account);
		try{
			AdminAccountDao.changeAdminAccountPassword(account.getAdminId(), "hgfudifhg3489", "sdfe3r");
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}

		boolean fail = false;

		try{
			AdminAccountDao.changeAdminAccountPassword(account.getAdminId(), "sdfe3r", "s123213");
		}catch(AuthenticationException e){
			fail = true;
		}
		catch(Exception e){
			e.printStackTrace();
			fail();
		}

		if(fail) fail();

		fail = true;
		try{
			AdminAccountDao.changeAdminAccountPassword(account.getAdminId(), "sdfe3r", "");
		}catch(AuthenticationException e){
			fail = false;
		}
		catch(Exception e){			
			fail();
		}

		if(fail) fail();

		fail = true;
		try{
			AdminAccountDao.changeAdminAccountPassword(account.getAdminId(), "sdfe3r", "s123");
		}catch(AuthenticationException e){
			fail = false;
		}
		catch(Exception e){			
			fail();
		}

		if(fail) fail();

	}

	@Test
	public void testRecoverPassword() throws ValidationException, AuthenticationException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "123445676543";
		String reference = "dsfdsf";
		Privilege privilege = Privilege.mamagement;
		AccountStatus status = AccountStatus.activated;
		String password = "hgfudifhg3489";
		AdminAccount account = new AdminAccount(name,phone,reference,privilege,status,password);
		account = AdminAccountDao.addAdminAccountToDatabases(account);
		
		try{
			AdminAccountDao.changeAdminAccountPassword(account.getAdminId(), "newPassword");
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		account.setPhone("12455233");
		if(AdminAccountDao.addAdminAccountToDatabases(account).equals(account)){
			//Passed;
		}else fail();
		
		boolean fail = true;
		try{
			AdminAccountDao.authenticateAdminAccount(reference, "badPassword");
		}catch(Exception e){
			//Passed;
			fail = false;
		}
		
		if(fail) fail();
	}
	
	@Test
	public void testAuthAdmin() throws ValidationException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "123445676543";
		String reference = "dsfdsf";
		Privilege privilege = Privilege.mamagement;
		AccountStatus status = AccountStatus.activated;		
		String password = "dsfdsftewr";
		AdminAccount account = new AdminAccount(name,phone,reference,privilege,status,password);
		AdminAccountDao.addAdminAccountToDatabases(account);
		AdminAccount test = null;
		try {
			test = AdminAccountDao.authenticateAdminAccount(reference, password);
		} catch (AuthenticationException e) {
			fail();
		}
		if(test==null) fail();
		
		test = null;
		try {
			test = AdminAccountDao.authenticateAdminAccount("12345612311", password);
		} catch (AuthenticationException e) {
			//Passed;
		}
		
		if(test != null) fail();
		
		test = null;
		try {
			test = AdminAccountDao.authenticateAdminAccount(phone, "36krfinai");
		} catch (AuthenticationException e) {
			//Passed;
		}
		
		if(test != null) fail();

	}

}
