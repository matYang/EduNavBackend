package UserModule.service;

import BaseModule.exception.validation.ValidationException;
import BaseModule.service.RedisAuthenticationService;

public class UserChangePasswordVerificationDaoService {
	
	public static final int serviceIdentifier = 2;
	
	public static boolean valdiateSession(int id, String authCode){
		return RedisAuthenticationService.valdiateCellSession(serviceIdentifier, String.valueOf(id), authCode);
	}
	
	public static String openSession(int id) throws ValidationException{
		return RedisAuthenticationService.openCellSession(serviceIdentifier, String.valueOf(id));
	}
	
	
	public static boolean closeSession(int id){
		return RedisAuthenticationService.closeSession(serviceIdentifier, String.valueOf(id));
	}
	
}
