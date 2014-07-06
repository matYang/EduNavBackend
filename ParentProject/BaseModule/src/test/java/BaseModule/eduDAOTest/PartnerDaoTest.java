package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.model.Partner;
import BaseModule.model.representation.PartnerSearchRepresentation;

public class PartnerDaoTest {

	@Test
	public void testCreate() throws SQLException{
		EduDaoBasic.clearAllDatabase();		
		String name = "XDF";
		String instName = "新东方";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name, instName,licence, organizationNum,reference, password,status);		
		try{
			PartnerDao.addPartnerToDatabases(partner);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGet() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();		
		String name = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";		
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name, instName,licence, organizationNum,reference, password,status);	
		PartnerDao.addPartnerToDatabases(partner);
		Partner partner2 = PartnerDao.getPartnerById(partner.getPartnerId());
		
		if(partner2.getWholeName().equals(name)&&partner2.getLicence().equals(licence)
				&&partner2.getOrganizationNum().equals(organizationNum)){
			//Passed;
		}else fail();
		
		String name2 = "HQYS";
		String instName2 = "daofeng";
		String licence2 = "2sdfdsf34545dsfsdgergf-dsv,.!@";
		String organizationNum2 = "12334361234";
		String reference2 = "dsdsfr";
		String password2 = "sdsdf34r";		
		AccountStatus status2 = AccountStatus.activated;
		Partner test = new Partner(name2, instName2,licence2, organizationNum2,reference2, password2,status2);		
		PartnerDao.addPartnerToDatabases(test);
		
		ArrayList<Partner> plist = new ArrayList<Partner>();
		plist = PartnerDao.searchPartner(new PartnerSearchRepresentation());		
		if(plist.size()==2&&plist.get(0).equals(partner2)&&plist.get(1).equals(test)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpdate() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();		
		String name = "XDF";
		String instName = "daofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";		
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name,instName, licence, organizationNum,reference, password,status);		
		partner = PartnerDao.addPartnerToDatabases(partner);
		
		if(!partner.getWholeName().equals(name)){
			fail();
		}
		
		partner.setWholeName("HQYS");
		PartnerDao.updatePartnerInDatabases(partner);
		partner = PartnerDao.getPartnerById(partner.getPartnerId());
		if(partner.getWholeName().equals("HQYS")){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpdatePartnerPassword() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();
		
		String name = "XDF";
		String instName = "daofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";		
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name,instName, licence, organizationNum,reference, password,status);			
		partner = PartnerDao.addPartnerToDatabases(partner);

		try{
			PartnerDao.changePartnerPassword(partner.getPartnerId(), "sdf234r", "xcf");
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}

		boolean fail = false;

		try{
			PartnerDao.changePartnerPassword(partner.getPartnerId(), "xcf", "3432fdsf");
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
			PartnerDao.changePartnerPassword(partner.getPartnerId(), "xcf", "");
		}catch(AuthenticationException e){
			fail = false;
		}
		catch(Exception e){			
			fail();
		}

		if(fail) fail();

		fail = true;
		try{
			PartnerDao.changePartnerPassword(2, "xcf", "sdf3");
		}catch(AuthenticationException e){
			fail = false;
		}
		catch(Exception e){			
			fail();
		}

		if(fail) fail();

	}

	@Test
	public void testRecoverPassword() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();		
		String name = "XDF";
		String instName = "daofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";		
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name,instName, licence, organizationNum,reference, password,status);		
		partner = PartnerDao.addPartnerToDatabases(partner);
		
		try{
			PartnerDao.recoverPartnerPassword(reference, "newPassword");
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
		
		if(PartnerDao.authenticatePartner(reference, "newPassword").equals(partner)){
			//Passed;
		}else fail();
		
		boolean fail = true;
		try{
			PartnerDao.authenticatePartner(reference, "badPassword");
		}catch(Exception e){
			//Passed;
			fail = false;
		}
		
		if(fail) fail();
	}
	
	@Test
	public void testAuthPartner() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();		
		String name = "XDF";
		String instName = "daofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";		
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name,instName, licence, organizationNum,reference, password,status);		
		partner = PartnerDao.addPartnerToDatabases(partner);
		Partner test = null;
		try {
			test = PartnerDao.authenticatePartner(reference, password);
		} catch (AuthenticationException e) {
			fail();
		}
		if(test==null) fail();
		
		test = null;
		try {
			test = PartnerDao.authenticatePartner("12345612311", password);
		} catch (AuthenticationException e) {
			//Passed;
		}
		
		if(test != null) fail();
		
		test = null;
		try {
			test = PartnerDao.authenticatePartner(reference, "36krfinai");
		} catch (AuthenticationException e) {
			//Passed;
		}
		
		if(test != null) fail();

	}
	
	@Test
	public void testSearch() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();		
		String name = "XDF";
		String instName = "daofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";		
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name,instName, licence, organizationNum,reference, password,status);	
		partner = PartnerDao.addPartnerToDatabases(partner);
		partner = PartnerDao.getPartnerById(partner.getPartnerId());
		
		String name11 = "XDF11";
		String instName11 = "daofeng11";
		String licence11 = "234fdsfsdgergf-d11sv,.!@";
		String organizationNum11 = "123545436111234";
		String reference11 = "ds11f4r";			
		Partner partner11 = new Partner(name11,instName11, licence11, organizationNum11,reference11, password, status);		
		partner11 = PartnerDao.addPartnerToDatabases(partner11);
		partner11 = PartnerDao.getPartnerById(partner11.getPartnerId());
		
		String name2 = "kfdjgklfj";
		String instName2 = "daofeg";
		String licence2 = "234fdsfsdf-dsv,.!@";
		String organizationNum2 = "123541234";
		String reference2 = "dgt4yt4yf4r";		
		
		AccountStatus status2 = AccountStatus.deactivated;
		Partner partner2 = new Partner(name2,instName2, licence2, organizationNum2,reference2, password,status2);		
		partner2 = PartnerDao.addPartnerToDatabases(partner2);
		partner2 = PartnerDao.getPartnerById(partner2.getPartnerId());
		
		String name3 = "name3";
		String instName3 = "instName3";
		String licence3 = "licence3";
		String organizationNum3 = "organizationNum3";
		String reference3= "reference3";		
		
		AccountStatus status3 = AccountStatus.deleted;
		Partner partner3 = new Partner(name3,instName3, licence3, organizationNum3,reference3, password,status3);		
		partner3 = PartnerDao.addPartnerToDatabases(partner3);
		partner3 = PartnerDao.getPartnerById(partner3.getPartnerId());
		
		PartnerSearchRepresentation sr = new PartnerSearchRepresentation();
		ArrayList<Partner> plist = new ArrayList<Partner>();
		sr.setInstName("daofeg");
		plist = PartnerDao.searchPartner(sr);
		if(plist.size()==1 && plist.get(0).equals(partner2)){
			//Passed;
		}else fail();	
		
		sr.setInstName(null);
		sr.setPartnerId(partner.getPartnerId());
		plist = PartnerDao.searchPartner(sr);
		if(plist.size()==1 && plist.get(0).equals(partner)){
			//Passed;
		}else fail();
		
	}
}
