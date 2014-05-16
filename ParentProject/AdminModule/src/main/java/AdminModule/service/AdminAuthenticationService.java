package AdminModule.service;

import BaseModule.common.DateUtility;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;

public class AdminAuthenticationService {

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
			boolean login =  AdminAuthDaoService.validateSession(adminId, authCode, mili);
			if (!login){
				throw new AuthenticationException();
			}
		}
		return adminId;
	}
	

	public static String openSession(int id) throws PseudoException{
	   String sessionString = AdminAuthDaoService.openSession(id);
       return sessionString;
	}
	

	public static boolean closeSession(String sessionString) throws PseudoException{
		boolean logout = AdminAuthDaoService.closeSession(getAdminIdFromSessionString(sessionString));
		return logout;
	}
	
	//Session string format: "adminId+sessionStr+timeStamp"
	public static int getAdminIdFromSessionString(String sessionString)throws PseudoException{
		String adminIdStr = sessionString.split(DatabaseConfig.redisSeperatorRegex)[0];
		int adminId = Integer.parseInt(adminIdStr);
		return adminId;
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
