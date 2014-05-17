package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import BaseModule.configurations.EnumConfig.Status;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.partner.PartnerNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Partner;

public class PartnerDaoTest {

	@Test
	public void testCreate(){
		EduDaoBasic.clearBothDatabase();
		String name = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";
		String phone = "123545451";
		Status status = Status.activated;
		Partner partner = new Partner(name, instName,licence, organizationNum,reference, password, phone,status);
		try{
			PartnerDao.addPartnerToDatabases(partner);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGet() throws ValidationException, PartnerNotFoundException{
		EduDaoBasic.clearBothDatabase();
		String name = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";
		String phone = "123545451";
		Status status = Status.activated;
		Partner partner = new Partner(name, instName,licence, organizationNum,reference, password, phone,status);
		PartnerDao.addPartnerToDatabases(partner);
		Partner partner2 = PartnerDao.getPartnerById(partner.getPartnerId());
		
		if(partner2.getName().equals(name)&&partner2.getLicence().equals(licence)
				&&partner2.getOrganizationNum().equals(organizationNum)){
			//Passed;
		}else fail();
		
		Partner partner3 = PartnerDao.getPartnerByPhone(partner.getPhone());
		if(partner2.equals(partner3)){
			//Passed;
		}else fail();
		
		String name2 = "HQYS";
		String instName2 = "daofeng";
		String licence2 = "2sdfdsf34545dsfsdgergf-dsv,.!@";
		String organizationNum2 = "12334361234";
		String reference2 = "dsdsfr";
		String password2 = "sdsdf34r";
		String phone2 = "12335451";
		Status status2 = Status.activated;
		Partner test = new Partner(name2, instName2,licence2, organizationNum2,reference2, password2, phone2,status2);
		PartnerDao.addPartnerToDatabases(test);
		
		ArrayList<Partner> plist = new ArrayList<Partner>();
		plist = PartnerDao.getAllPartners();
		if(plist.size()==2&&plist.get(0).equals(partner2)&&plist.get(1).equals(test)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpdate() throws ValidationException, PartnerNotFoundException{
		EduDaoBasic.clearBothDatabase();
		String name = "XDF";
		String instName = "daofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
		String password = "sdf234r";
		String phone = "123545451";
		Status status = Status.activated;
		Partner partner = new Partner(name,instName, licence, organizationNum,reference, password, phone,status);
		partner = PartnerDao.addPartnerToDatabases(partner);
		
		if(!partner.getName().equals(name)){
			fail();
		}
		
		partner.setName("HQYS");
		partner.setPassword("dsfdsf23234");
		
		PartnerDao.updatePartnerInDatabases(partner);
		partner = PartnerDao.getPartnerByPhone(phone);
		if(partner.getName().equals("HQYS")&&partner.getPassword().equals("dsfds23234")){
			
		}
	}
}
