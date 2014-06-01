package PartnerModule.partnerauthTest;

import static org.junit.Assert.fail;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.service.RedisAuthenticationService;

public class PartnerAuthDaoServiceTest {

	private static final int partnerSession_web_authCodeLength = 15;
	public static final int serviceIdentifier = 5;
	
	@Test
	public void test(){
		EduDaoBasic.clearAllDatabase();
		int partnerId = 1;
		String authCode = RandomStringUtils.randomAlphanumeric(partnerSession_web_authCodeLength);
		long timeStamp = DateUtility.getLongFromTimeStamp(DateUtility.getTimeStamp());
		if(!RedisAuthenticationService.validateWebSession(serviceIdentifier, partnerId, authCode, timeStamp)){
			//Passed;
		}else fail();

		String sessionString = "";
		sessionString = RedisAuthenticationService.openWebSession(serviceIdentifier, partnerId);
		if(sessionString.equals("")){
			fail();
		}
		try {
			authCode = RedisAuthenticationService.getAuthCodeFromSessionString(sessionString);
		} catch (PseudoException e) {			
			e.printStackTrace();
		}
		try {
			timeStamp = RedisAuthenticationService.getTimeStampFromSessionString(sessionString);
		} catch (PseudoException e) {			
			e.printStackTrace();
		}
		if(RedisAuthenticationService.validateWebSession(serviceIdentifier, partnerId, authCode, timeStamp)){
			//Passed;
		}else fail();

		if(RedisAuthenticationService.closeSession(serviceIdentifier, String.valueOf(partnerId))){
			//Passed;
		}else fail();

	}
}
