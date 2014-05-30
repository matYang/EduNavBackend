package UserModule.service;

import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;
import BaseModule.service.RedisAuthenticationService;

public class UserAuthenticationService {
	
	public static final int serviceIdentifier = 1;
	
	public static int validateSession(String sessionString) throws PseudoException{
		if (sessionString == null){
			throw new AuthenticationException();
		}
		int userId = RedisAuthenticationService.getIdFromSessionString(sessionString);
		if (userId < 0){
			throw new AuthenticationException();
		}
		else{
			String authCode = RedisAuthenticationService.getAuthCodeFromSessionString(sessionString);
			long mili = RedisAuthenticationService.getTimeStampFromSessionString(sessionString);
			boolean login = RedisAuthenticationService.validateWebSession(serviceIdentifier, userId, authCode, mili);
			if (!login){
				throw new AuthenticationException();
			}
		}
		return userId;
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
