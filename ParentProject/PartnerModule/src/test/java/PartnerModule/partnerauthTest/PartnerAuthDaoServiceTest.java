package PartnerModule.partnerauthTest;

import static org.junit.Assert.fail;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import PartnerModule.service.PartnerAuthDaoService;
import PartnerModule.service.PartnerAuthenticationService;

public class PartnerAuthDaoServiceTest {

	private static final int partnerSession_web_authCodeLength = 15;
	
	@Test
	public void test(){
		EduDaoBasic.clearBothDatabase();
		int partnerId = 1;
		String authCode = RandomStringUtils.randomAlphanumeric(partnerSession_web_authCodeLength);
		long timeStamp = DateUtility.getLongFromTimeStamp(DateUtility.getTimeStamp());
		if(!PartnerAuthDaoService.validateSession(partnerId, authCode, timeStamp)){
			//Passed;
		}else fail();

		String sessionString = "";
		sessionString = PartnerAuthDaoService.openSession(partnerId);
		if(sessionString.equals("")){
			fail();
		}
		try {
			authCode = PartnerAuthenticationService.getAuthCodeFromSessionString(sessionString);
		} catch (PseudoException e) {			
			e.printStackTrace();
		}
		try {
			timeStamp = PartnerAuthenticationService.getTimeStampFromSessionString(sessionString);
		} catch (PseudoException e) {			
			e.printStackTrace();
		}
		if(PartnerAuthDaoService.validateSession(partnerId, authCode, timeStamp)){
			//Passed;
		}else fail();

		if(PartnerAuthDaoService.closeSession(partnerId)){
			//Passed;
		}else fail();

	}
}
