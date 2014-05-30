package AdminModule.service;

import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;
import BaseModule.service.RedisAuthenticationService;

public class AdminAuthenticationService {
	
	public static final int serviceIdentifier = 8;

	public static int validateSession(String sessionString) throws PseudoException{
		if (sessionString == null){
			throw new AuthenticationException();
		}
		int adminId = RedisAuthenticationService.getIdFromSessionString(sessionString);
		if (adminId < 0){
			throw new AuthenticationException();
		}
		else{
			String authCode = RedisAuthenticationService.getAuthCodeFromSessionString(sessionString);
			long mili = RedisAuthenticationService.getTimeStampFromSessionString(sessionString);
			boolean login =  RedisAuthenticationService.validateWebSession(serviceIdentifier, adminId, authCode, mili);
			if (!login){
				throw new AuthenticationException();
			}
		}
		return adminId;
	}
	

	public static String openSession(int id) throws PseudoException{
	   return RedisAuthenticationService.openWebSession(serviceIdentifier, id);
	}
	

	public static boolean closeSession(String sessionString) throws PseudoException{
		if (sessionString == null){
			return true;
		}
		return RedisAuthenticationService.closeSession(serviceIdentifier, String.valueOf(RedisAuthenticationService.getIdFromSessionString(sessionString)));
	}
	


}
