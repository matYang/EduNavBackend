package BaseModule.common;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import BaseModule.configurations.ParamConfig;
import BaseModule.configurations.ServerConfig;
import BaseModule.service.EncodingService;

public class DebugLog {

	private static final Logger systemLogger = Logger.getLogger("system.logger");
	private static final Logger behaviorsLogger = Logger.getLogger("behaviors.logger");
	private static final Logger cleanerLogger = Logger.getLogger("cleaner.logger");

	public static void initializeLogger(){		
		PropertyConfigurator.configure(ServerConfig.resourcePrefix + "log4j.properties");
	}
	
	public static void d(Exception e){
		//using reflection to get caller name, 500x faster than stack trace
		//if not accessible, do:  right click on project -> configure build path -> remove JRE system library -> add Library -> JRE System Library (Default) -> OK, clean & recompile
		String caller = sun.reflect.Reflection.getCallerClass(2).getName();
		systemLogger.info(caller + " got Exception! ",e);
		if (ServerConfig.configurationMap.get(ParamConfig.MAP_ENV_KEY).equals(ParamConfig.MAP_ENV_LOCAL)){
			e.printStackTrace();
		}
	}

	

	public static void d(String message){
		log(message);
	}	

	public static void b_d(String message){
		b_log(message);
	}
	
	public static void b_d(String moduleId, String apiId, String requestId, long initiatorId, String userAgent, String payLoad){
		try {
			b_log("[" + moduleId + "] [" + apiId + "] [" + requestId + "] [" + initiatorId + "] [" + userAgent + "] [" + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()) + "] [" + EncodingService.decodeURI(payLoad) + "]");
		} catch (UnsupportedEncodingException e) {
			b_log("[" + moduleId + "] [" + apiId + "] [" + requestId + "] [" + initiatorId + "] [" + userAgent + "] [" + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()) + "] [Decoding Failed]");
		}
	}
	
	private static void log(String message){
		systemLogger.info(message);
	}
	
	private static void b_log(String message){
		behaviorsLogger.info(message);
	}
	
	public static void c_d(int userId,boolean validTransactionList,boolean validCreditList,boolean validCouponList){
		String bstr = validTransactionList ? "Pass" : "Failed";
		String crestr = validCreditList ? "Pass" : "Failed";
		String coustr = validCouponList ? "Pass" : "Failed";
		
		c_log("user: " + userId + " balance account: " + bstr + " credit account: " + crestr + " coupon account: " + coustr);
	}
	
	private static void c_log(String message){
		cleanerLogger.info(message);
	}
	
}
