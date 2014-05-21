package BaseModule.configurations;

public class RedisPrefixConfig {
	
	/* UserModule */
	public static final String userSession_web_keyPrefix = "user-session-web-";
	public static final int userSession_web_authCodeLength = 15;
	public static final long userSession_updateThreshold = 259200000l;		//3 days
	public static final long userSession_expireThreshold = 604800000l;		//7 days
	
	
	public static final String userChangePasswordVerification_keyPrefix = "user-changePasswordVerification-";
	public static final int userChangePasswordVerification_authCodeLength = 6;
	public static final long userChangePasswordVerification_resendThreshold = 60000l;	//1h
	public static final long userChangePasswordVerification_expireThreshold = 3600000l;	//1h
	
	
	public static final String userCellVerification_keyPrefix = "user-cellVerification-";
	public static final int userCellVerification_authCodeLength = 6;
	public static final long userCellVerification_resendThreshold = 60000;			//1min
	public static final long userCellVerification_expireThreshold = 21600000l;		//6h
	
	
	public static final String userForgotPassword_keyPrefix = "user-forgotPassword-";
	public static final int userForgotPassword_authCodeLength = 8;
	public static final long userForgotPassword_resendThreshould = 60000l;			//1min
	public static final long userForgotPassword_expireThreshold = 21600000l;// 6h
	
	
	
	/* PartnerModule */
	public static final String partnerSession_web_keyPrefix = "partner-session-web-";
	public static final int partnerSession_web_authCodeLength = 15;
	public static final long partnerSession_updateThreshold = 259200000l;		//3 days
	public static final long partnerSession_expireThreshold = 604800000l;		//7 days
	
	public static final String partnerForgotPassword_keyPrefix = "partner-forgotPassword-";
	public static final int partnerChangePasswordVerification_authCodeLength = 6;
	public static final long partnerChangePasswordVerification_resendThreshold = 60000l;	//1h
	public static final long partnerForgotPassword_expireThreshold = 21600000l;		//6h
	
	public static final String partnerChangePasswordVerification_keyPrefix= "partner-changePasswordVerification-";
	public static final int partnerForgotPassword_authCodeLength = 8;
	public static final long partnerForgotPassword_resendThreshould = 60000l;			//1min
	public static final long partnerChangePasswordVerification_expireThreshold = 3600000l;	//1h
	
	
	
	/* AdminModule */
	public static final String adminSession_web_keyPrefix = "admin-session-web-";
	public static final int adminSession_web_authCodeLength = 15;
	public static final long adminSession_updateThreshold = 259200000l;		//3 days
	public static final long adminSession_expireThreshold = 604800000l;		//7 days
	
	
}
