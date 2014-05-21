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
		int adminId = getAdminIdFromSessionString(sessionString);
		if (adminId < 0){
			throw new AuthenticationException();
		}
		else{
			String authCode = getAuthCodeFromSessionString(sessionString);
			long mili = getTimeStampFromSessionString(sessionString);
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
		return RedisAuthenticationService.closeSession(serviceIdentifier, String.valueOf(getAdminIdFromSessionString(sessionString)));
	}
	
	//Session string format: "adminId+sessionStr+timeStamp"
	public static int getAdminIdFromSessionString(String sessionString)throws PseudoException{
		return RedisAuthenticationService.getIdFromSessionString(sessionString);
	}
	public static String getAuthCodeFromSessionString(String sessionString)throws PseudoException{
		return RedisAuthenticationService.getAuthCodeFromSessionString(sessionString);
	}
	public static long getTimeStampFromSessionString(String sessionString)throws PseudoException{
		return RedisAuthenticationService.getTimeStampFromSessionString(sessionString);
	}

}
