package UserModule.userauthTest;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import UserModule.service.UserAuthDaoService;
import UserModule.service.UserAuthenticationService;
import BaseModule.common.DateUtility;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;

public class UserAuthDaoServiceTest {

	private static final int userSession_web_authCodeLength = 15;

	@Test
	public void test(){
		EduDaoBasic.clearBothDatabase();
		int userId = 1;
		String authCode = RandomStringUtils.randomAlphanumeric(userSession_web_authCodeLength);
		long timeStamp = DateUtility.getLongFromTimeStamp(DateUtility.getTimeStamp());
		if(!UserAuthDaoService.validateSession(userId, authCode, timeStamp)){
			//Passed;
		}else fail();

		String sessionString = "";
		sessionString = UserAuthDaoService.openSession(userId);
		if(sessionString.equals("")){
			fail();
		}
		try {
			authCode = UserAuthenticationService.getAuthCodeFromSessionString(sessionString);
		} catch (PseudoException e) {			
			e.printStackTrace();
		}
		try {
			timeStamp = UserAuthenticationService.getTimeStampFromSessionString(sessionString);
		} catch (PseudoException e) {			
			e.printStackTrace();
		}
		if(UserAuthDaoService.validateSession(userId, authCode, timeStamp)){
			//Passed;
		}else fail();

		if(UserAuthDaoService.closeSession(userId)){
			//Passed;
		}else fail();

	}

}
