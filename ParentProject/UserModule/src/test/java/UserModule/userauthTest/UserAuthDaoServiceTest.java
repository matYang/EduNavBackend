package UserModule.userauthTest;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import UserModule.service.UserAuthenticationService;
import BaseModule.common.DateUtility;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.service.RedisAuthenticationService;

public class UserAuthDaoServiceTest {

	private static final int userSession_web_authCodeLength = 15;
	public static final int serviceIdentifier = 1;

	@Test
	public void test(){
		EduDaoBasic.clearBothDatabase();
		int userId = 1;
		String authCode = RandomStringUtils.randomAlphanumeric(userSession_web_authCodeLength);
		long timeStamp = DateUtility.getLongFromTimeStamp(DateUtility.getTimeStamp());
		if(!RedisAuthenticationService.validateWebSession(serviceIdentifier, userId, authCode, timeStamp)){
			//Passed;
		}else fail();

		String sessionString = "";
		sessionString = RedisAuthenticationService.openWebSession(serviceIdentifier, userId);
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
		if(RedisAuthenticationService.validateWebSession(serviceIdentifier, userId, authCode, timeStamp)){
			//Passed;
		}else fail();

		if(RedisAuthenticationService.closeSession(serviceIdentifier, String.valueOf(userId))){
			//Passed;
		}else fail();

	}

}
