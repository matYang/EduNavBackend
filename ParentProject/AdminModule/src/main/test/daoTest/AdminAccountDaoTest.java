package daoTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

import AdminModule.AdminDao.AdminAccountDao;
import AdminModule.exception.AdminAccountNotFoundException;
import AdminModule.model.AdminAccount;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.Status;
import BaseModule.eduDAO.EduDaoBasic;

public class AdminAccountDaoTest {

	@Test
	public void testCreate() {
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "123445676543";
		String reference = "dsfdsf";
		Privilege privilege = Privilege.business;
		Status status = Status.activated;
		AdminAccount account = new AdminAccount(name,phone,reference,privilege,status);
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
		Privilege privilege = Privilege.business;
		Status status = Status.activated;
		AdminAccount account = new AdminAccount(name,phone,reference,privilege,status);
		AdminAccountDao.addAdminAccountToDatabases(account);
		account = AdminAccountDao.getAdminAccountById(account.getId());
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
		Privilege privilege2 = Privilege.economy;
		Status status2 = Status.activated;
		AdminAccount account2 = new AdminAccount(name2,phone2,reference2,privilege2,status2);
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
		String phone = "123445676543";
		String reference = "dsfdsf";
		Privilege privilege = Privilege.business;
		Status status = Status.activated;
		AdminAccount account = new AdminAccount(name,phone,reference,privilege,status);
		AdminAccountDao.addAdminAccountToDatabases(account);
		account = AdminAccountDao.getAdminAccountById(account.getId());
		account.setPhone("234234234");
		account.setStatus(Status.deleted);
		AdminAccountDao.updateAdminAccountInDatabases(account);
		account = AdminAccountDao.getAdminAccountById(account.getId());
		if(account.getName().equals(name)&&account.getPhone().equals("234234234")&&account.getStatus().code==Status.deleted.code){
			//Passed;
		}else fail();
		
	}

}
