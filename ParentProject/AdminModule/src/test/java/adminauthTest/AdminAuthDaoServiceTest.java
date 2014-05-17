package adminauthTest;

import static org.junit.Assert.fail;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import AdminModule.service.AdminAuthDaoService;
import AdminModule.service.AdminAuthenticationService;
import BaseModule.common.DateUtility;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;

public class AdminAuthDaoServiceTest {

private static final int adminSession_web_authCodeLength = 15;
	
	@Test
	public void test(){
		EduDaoBasic.clearBothDatabase();
		int partnerId = 1;
		String authCode = RandomStringUtils.randomAlphanumeric(adminSession_web_authCodeLength);
		long timeStamp = DateUtility.getLongFromTimeStamp(DateUtility.getTimeStamp());
		if(!AdminAuthDaoService.validateSession(partnerId, authCode, timeStamp)){
			//Passed;
		}else fail();

		String sessionString = "";
		sessionString = AdminAuthDaoService.openSession(partnerId);
		if(sessionString.equals("")){
			fail();
		}
		try {
			authCode = AdminAuthenticationService.getAuthCodeFromSessionString(sessionString);
		} catch (PseudoException e) {			
			e.printStackTrace();
		}
		try {
			timeStamp = AdminAuthenticationService.getTimeStampFromSessionString(sessionString);
		} catch (PseudoException e) {			
			e.printStackTrace();
		}
		if(AdminAuthDaoService.validateSession(partnerId, authCode, timeStamp)){
			//Passed;
		}else fail();

		if(AdminAuthDaoService.closeSession(partnerId)){
			//Passed;
		}else fail();

	}
}
