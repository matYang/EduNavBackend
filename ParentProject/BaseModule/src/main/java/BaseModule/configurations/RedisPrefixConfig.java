package BaseModule.configurations;

public class RedisPrefixConfig {
	/* UserModule */
	public static final String userSession_web_keyPrefix = "user-session-web-";
	public static final String userChangePasswordVerification_keyPrefix = "user-changePasswordVerification-";
	public static final String userCellVerification_keyPrefix = "user-cellVerification-";
	public static final String userForgotPassword_keyPrefix = "user-forgotPassword-";
	public static final long userSession_expireThreshold = 604800000l;		//7 days
	public static final long userCellVerification_expireThreshold = 21600000l;		//6h
	public static final long userChangePasswordVerification_expireThreshold = 3600000l;	//1h
	public static final long userForgotPassword_expireThreshold = 21600000l;// 6h
	/* PartnerModule */
	public static final String partnerSession_web_keyPrefix = "partner-session-web-";
	public static final String partnerForgotPassword_keyPrefix = "partner-forgotPassword-";
	public static final long partnerSession_expireThreshold = 604800000l;		//7 days
	public static final long partnerForgotPassword_expireThreshold = 21600000l;		//6h
	
	/* AdminModule */
	public static final String adminSession_web_keyPrefix = "admin-session-web-";
	public static final long adminSession_expireThreshold = 604800000l;		//7 days
}
