package adminauthTest;

import static org.junit.Assert.fail;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import AdminModule.service.AdminAuthenticationService;
import BaseModule.common.DateUtility;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.service.RedisAuthenticationService;

public class AdminAuthDaoServiceTest {

	private static final int adminSession_web_authCodeLength = 15;
	public static final int serviceIdentifier = 8;
	
	@Test
	public void test(){
		EduDaoBasic.clearBothDatabase();
		int id = 1;
		String authCode = RandomStringUtils.randomAlphanumeric(adminSession_web_authCodeLength);
		long timeStamp = DateUtility.getLongFromTimeStamp(DateUtility.getTimeStamp());
		if(!RedisAuthenticationService.validateWebSession(serviceIdentifier, id, authCode, timeStamp)){
			//Passed;
		}else fail();

		String sessionString = "";
		sessionString = RedisAuthenticationService.openWebSession(serviceIdentifier, id);
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
		if(RedisAuthenticationService.validateWebSession(serviceIdentifier, id, authCode, timeStamp)){
			//Passed;
		}else fail();

		if(RedisAuthenticationService.closeSession(serviceIdentifier, String.valueOf(id))){
			//Passed;
		}else fail();

	}
}
