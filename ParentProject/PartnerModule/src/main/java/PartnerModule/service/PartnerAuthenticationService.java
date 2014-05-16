package PartnerModule.service;

import BaseModule.common.DateUtility;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;


public class PartnerAuthenticationService {

	public static int validateSession(String sessionString) throws PseudoException{
		if (sessionString == null){
			throw new AuthenticationException();
		}
		int partnerId = getPartnerIdFromSessionString(sessionString);
		if (partnerId < 0){
			throw new AuthenticationException();
		}
		else{
			String authCode = getAuthCodeFromSessionString(sessionString);
			long mili = getTimeStampFromSessionString(sessionString);
			boolean login =  PartnerAuthDaoService.validateSession(partnerId, authCode, mili);
			if (!login){
				throw new AuthenticationException();
			}
		}
		return partnerId;
	}
	

	public static String openSession(int id) throws PseudoException{
	   String sessionString = PartnerAuthDaoService.openSession(id);
       return sessionString;
	}
	

	public static boolean closeSession(String sessionString) throws PseudoException{
		boolean logout = PartnerAuthDaoService.closeSession(getPartnerIdFromSessionString(sessionString));
		return logout;
	}
	
	//Session string format: "id+sessionStr+timeStamp"
	public static int getPartnerIdFromSessionString(String sessionString)throws PseudoException{
		String partnerIdStr = sessionString.split(DatabaseConfig.redisSeperatorRegex)[0];
		int partnerId = Integer.parseInt(partnerIdStr);
		return partnerId;
	}
	public static String getAuthCodeFromSessionString(String sessionString)throws PseudoException{
		String authCodeStr = sessionString.split(DatabaseConfig.redisSeperatorRegex)[1];
		return authCodeStr;
	}
	public static long getTimeStampFromSessionString(String sessionString)throws PseudoException{
		String timeStampStr = sessionString.split(DatabaseConfig.redisSeperatorRegex)[2];
		return DateUtility.getLongFromTimeStamp(timeStampStr);
	}

}
