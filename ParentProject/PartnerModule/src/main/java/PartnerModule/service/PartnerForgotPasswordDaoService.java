package PartnerModule.service;

import BaseModule.exception.validation.ValidationException;
import BaseModule.service.RedisAuthenticationService;

public class PartnerForgotPasswordDaoService {
	
	public static final int serviceIdentifier = 7;

	public static boolean valdiateSession(String cellNum, String authCode){
		return RedisAuthenticationService.valdiateCellSession(serviceIdentifier, cellNum, authCode);
	}
	
	public static String openSession(String cellNum) throws ValidationException{
		return RedisAuthenticationService.openCellSession(serviceIdentifier, cellNum);
	}
	
	
	public static boolean closeSession(String cellNum){
		return RedisAuthenticationService.closeSession(serviceIdentifier, cellNum);
	}
}
