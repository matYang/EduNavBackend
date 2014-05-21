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
		int userId = getUserIdFromSessionString(sessionString);
		if (userId < 0){
			throw new AuthenticationException();
		}
		else{
			String authCode = getAuthCodeFromSessionString(sessionString);
			long mili = getTimeStampFromSessionString(sessionString);
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
		return RedisAuthenticationService.closeSession(serviceIdentifier, String.valueOf(getUserIdFromSessionString(sessionString)));
	}
	
	//Session string format: "id+sessionStr+timeStamp"
	public static int getUserIdFromSessionString(String sessionString)throws PseudoException{
		return RedisAuthenticationService.getIdFromSessionString(sessionString);
	}
	public static String getAuthCodeFromSessionString(String sessionString)throws PseudoException{
		return RedisAuthenticationService.getAuthCodeFromSessionString(sessionString);
	}
	public static long getTimeStampFromSessionString(String sessionString)throws PseudoException{
		return RedisAuthenticationService.getTimeStampFromSessionString(sessionString);
	}

}
