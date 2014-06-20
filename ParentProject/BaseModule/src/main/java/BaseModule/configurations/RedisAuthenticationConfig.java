package BaseModule.configurations;

import BaseModule.model.configObj.RedisSubConfig;

public class RedisAuthenticationConfig {

	/* RedisSperator*/
	public static final String redisSeperator = "+";
	public static final String redisSeperatorRegex = "\\+";
	
	
	/* UserModule */
	public static final String userSession_web_keyPrefix = "user-session-web-";
	public static final int userSession_web_authCodeLength = 15;
	public static final long userSession_updateThreshold = 259200000l;		//3 days
	public static final long userSession_expireThreshold = 604800000l;		//7 days
	
	
	public static final String userChangePasswordVerification_keyPrefix = "user-changePasswordVerification-";
	public static final int userChangePasswordVerification_authCodeLength = 6;
	public static final long userChangePasswordVerification_resendThreshold = 60000l;	//1min
	public static final long userChangePasswordVerification_expireThreshold = 600000l;	//10min
	
	
	public static final String userCellVerification_keyPrefix = "user-cellVerification-";
	public static final int userCellVerification_authCodeLength = 6;
	public static final long userCellVerification_resendThreshold = 60000;			//1min
	public static final long userCellVerification_expireThreshold = 600000l;		//10min
	
	
	public static final String userForgotPassword_keyPrefix = "user-forgotPassword-";
	public static final int userForgotPassword_authCodeLength = 8;
	public static final long userForgotPassword_resendThreshold = 60000l;		//1min
	public static final long userForgotPassword_expireThreshold = 600000l;		//10min
	
	
	
	/* PartnerModule */
	public static final String partnerSession_web_keyPrefix = "partner-session-web-";
	public static final int partnerSession_web_authCodeLength = 15;
	public static final long partnerSession_updateThreshold = 7200000l;		//2h
	public static final long partnerSession_expireThreshold = 21600000l;	//6h
	
	
	public static final String partnerChangePasswordVerification_keyPrefix = "partner-changePasswordVerification-";
	public static final int partnerChangePasswordVerification_authCodeLength = 6;
	public static final long partnerChangePasswordVerification_resendThreshold = 60000l;	//1min
	public static final long partnerChangePasswordVerification_expireThreshold = 600000l;	//10min
	
	
	public static final String partnerForgotPassword_keyPrefix = "partner-forgotPassword-";
	public static final int partnerForgotPassword_authCodeLength = 8;
	public static final long partnerForgotPassword_resendThreshould = 60000l;		//1min
	public static final long partnerForgotPassword_expireThreshold = 600000l;		//10min
	
	
	
	/* AdminModule */
	public static final String adminSession_web_keyPrefix = "admin-session-web-";
	public static final int adminSession_web_authCodeLength = 15;
	public static final long adminSession_updateThreshold = 7200000l;		//2h
	public static final long adminSession_expireThreshold = 21600000l;		//6h
	
	
	
	public static RedisSubConfig getConfig(int serviceIdentifier){
		RedisSubConfig config = new RedisSubConfig();
		switch (serviceIdentifier){
			case 1:
				config.keyPrefix = userSession_web_keyPrefix;
				config.authCodeLength = userSession_web_authCodeLength;
				config.activeThreshold = userSession_updateThreshold;
				config.expireThreshold = userSession_expireThreshold;
				config.authCodeUpper = false;
				break;
				
			case 2:
				config.keyPrefix = userChangePasswordVerification_keyPrefix;
				config.authCodeLength = userChangePasswordVerification_authCodeLength;
				config.activeThreshold = userChangePasswordVerification_resendThreshold;
				config.expireThreshold = userChangePasswordVerification_expireThreshold ;
				config.authCodeUpper = true;
				break;
				
			case 3:
				config.keyPrefix = userCellVerification_keyPrefix;
				config.authCodeLength = userCellVerification_authCodeLength;
				config.activeThreshold = userCellVerification_resendThreshold;
				config.expireThreshold = userCellVerification_expireThreshold;
				config.authCodeUpper = true;
				break;
				
			case 4:
				config.keyPrefix = userForgotPassword_keyPrefix;
				config.authCodeLength = userForgotPassword_authCodeLength;
				config.activeThreshold = userForgotPassword_resendThreshold;
				config.expireThreshold = userForgotPassword_expireThreshold;
				config.authCodeUpper = false;
				break;
				
			case 5:
				config.keyPrefix = partnerSession_web_keyPrefix;
				config.authCodeLength = partnerSession_web_authCodeLength;
				config.activeThreshold = partnerSession_updateThreshold;
				config.expireThreshold = partnerSession_expireThreshold;
				config.authCodeUpper = false;
				break;
				
			case 6:
				config.keyPrefix = partnerChangePasswordVerification_keyPrefix;
				config.authCodeLength = partnerChangePasswordVerification_authCodeLength;
				config.activeThreshold = partnerChangePasswordVerification_resendThreshold;
				config.expireThreshold = partnerChangePasswordVerification_expireThreshold;
				config.authCodeUpper = true;
				break;
				
			case 7:
				config.keyPrefix = partnerForgotPassword_keyPrefix;
				config.authCodeLength = partnerForgotPassword_authCodeLength;
				config.activeThreshold = partnerForgotPassword_resendThreshould;
				config.expireThreshold = partnerForgotPassword_expireThreshold;
				config.authCodeUpper = false;
				break;
				
			case 8:
				config.keyPrefix = adminSession_web_keyPrefix;
				config.authCodeLength = adminSession_web_authCodeLength;
				config.activeThreshold = adminSession_updateThreshold;
				config.expireThreshold = adminSession_expireThreshold;
				config.authCodeUpper = false;
				break;
				
			default:
				throw new RuntimeException("RedisPrefixConfig unable to determine server identifer type");
		}
		return config;
	}

	
}
