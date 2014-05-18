package BaseModule.configurations;

public class RedisPrefixConfig {
	/* UserModule */
	public static final String userSession_web_keyPrefix = "user-session-web-";
	public static final String userChangePasswordVerification_keyPrefix = "user-changePasswordVerification-";
	public static final String userCellVerification_keyPrefix = "user-cellVerification-";
	public static final String userForgotPassword_keyPrefix = "user-forgotPassword-";
	
	/* PartnerModule */
	public static final String partnerSession_web_keyPrefix = "partner-session-web-";
	public static final String partnerForgotPassword_keyPrefix = "partner-forgotPassword-";
	
	/* AdminModule */
	public static final String adminSession_web_keyPrefix = "admin-session-web-";
}
