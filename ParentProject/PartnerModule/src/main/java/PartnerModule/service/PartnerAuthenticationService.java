package PartnerModule.service;

import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.service.RedisAuthenticationService;


public class PartnerAuthenticationService {
	
	public static final int serviceIdentifier = 5;

	public static int validateSession(String sessionString) throws PseudoException{
		if (sessionString == null){
			throw new AuthenticationException();
		}
		int partnerId = RedisAuthenticationService.getIdFromSessionString(sessionString);
		if (partnerId < 0){
			throw new AuthenticationException();
		}
		else{
			String authCode = RedisAuthenticationService.getAuthCodeFromSessionString(sessionString);
			long mili = RedisAuthenticationService.getTimeStampFromSessionString(sessionString);
			boolean login =  RedisAuthenticationService.validateWebSession(serviceIdentifier, partnerId, authCode, mili);
			if (!login){
				throw new AuthenticationException();
			}
		}
		return partnerId;
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
