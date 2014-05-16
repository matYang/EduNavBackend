package UserModule.service;

import BaseModule.common.DateUtility;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;

public class UserAuthenticationService {
	
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
			boolean login =  UserAuthDaoService.validateSession(userId, authCode, mili);
			if (!login){
				throw new AuthenticationException();
			}
		}
		return userId;
	}
	

	public static String openSession(int id) throws PseudoException{
	   String sessionString = UserAuthDaoService.openSession(id);
       return sessionString;
	}
	

	public static boolean closeSession(String sessionString) throws PseudoException{
		boolean logout = UserAuthDaoService.closeSession(getUserIdFromSessionString(sessionString));
		return logout;
	}
	
	//Session string format: "id+sessionStr+timeStamp"
	public static int getUserIdFromSessionString(String sessionString)throws PseudoException{
		String userIdStr = sessionString.split(DatabaseConfig.redisSeperatorRegex)[0];
		int userId = Integer.parseInt(userIdStr);
		return userId;
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
