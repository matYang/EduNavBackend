package BaseModule.configurations;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import BaseModule.common.DateUtility;

public class ServerConfig {

	
		private static final String ENV_VAR_KEY = "RA_MAINSERVER_ENV";
		private static final String ENV_TEST = "RA_TEST";
		private static final String ENV_PROD = "RA_PROD";
		
		//use concurrent hashmap to guarantee thread safety
		public static final Map<String, String> configurationMap = new ConcurrentHashMap<String, String>();
		
		
		static{
			String value = System.getenv(ENV_VAR_KEY);

			System.out.println("Server starting under " + value + " envrionment");
			if (value == null || (!value.equals(ENV_TEST) && !value.equals(ENV_PROD))){

				//local env
				configurationMap.put("env", "local");			
				configurationMap.put("jdbcUri", "localhost:3306/EduModel?allowMultiQueries=true&&characterSetResults=UTF-8&characterEncoding=UTF-8&useUnicode=yes");
				configurationMap.put("redisUri", "localhost");
				configurationMap.put("domainName", "localhost:8015");
				configurationMap.put("redisSearchHistoryUpbound", "6");
				configurationMap.put("sqlPass", "");
				configurationMap.put("sqlUser", "root");

			} 
			else if (value.equals(ENV_TEST)){
				//test env
				configurationMap.put("env", "test");
				configurationMap.put("jdbcUri", "badstudent.cunzg2tyzsud.us-west-2.rds.amazonaws.com:3306/test?allowMultiQueries=true&&characterSetResults=UTF-8&characterEncoding=UTF-8&useUnicode=yes");
				configurationMap.put("redisUri", "redisserver.ppomgu.0001.usw2.cache.amazonaws.com");
				configurationMap.put("domainName", "www.routea.ca");
				configurationMap.put("redisSearchHistoryUpbound", "50");
				configurationMap.put("sqlPass", "badstudent");
				configurationMap.put("sqlUser", "test");
			}
			else{
				//prod env
				configurationMap.put("env", "prod");
				configurationMap.put("jdbcUri", "badstudent.mysql.rds.aliyuncs.com:3306/db19r3708gdzx5d1?allowMultiQueries=true&&characterSetResults=UTF-8&characterEncoding=UTF-8&useUnicode=yes");
				configurationMap.put("redisUri", "localhost");
				configurationMap.put("domainName", "www.routea.ca");
				configurationMap.put("redisSearchHistoryUpbound", "50");
				configurationMap.put("sqlPass", "LIFECENTRICo2o");
				configurationMap.put("sqlUser", "db19r3708gdzx5d1");
			}
			
		}
		
		public static final String domainName = configurationMap.get("domainName");
		public static final boolean cookieEnabled = false;
		public static final String cookie_userSession = "userSessionCookie";
		public static final int cookie_maxAge = 5184000; //2 month
		
		
		public static final String urlSeperator = "+";
		public static final String urlSeperatorRegx = "\\+";

		
		
		/*API level constants*/
		public static final int category_DM = 0;
		public static final String applicationPrefix = "/api";
		public static final String versionPrefix = "/v1.0";
		
		/*Time constants*/
		public static final String timeZoneIdCH = "asia/shanghai";
		

		/* ALIYUN Bucket*/
		public static final String AliyunGetImgPrefix = "http://badstudent-aliyun.oss-cn-hangzhou.aliyuncs.com/";
		public static final String AliyunAccessKeyID = "UBnwEnaFUdBewFF9";
		public static final String AliyunAccessKeySecrete = "L8hyNuKRXo5bfQ9HWURDq0bprDSDYO";
		public static final String AliyunTeacherImgBucket = "teacherimgbucket";
		public static final String AliyunProfileBucket = "badstudent-aliyun";
		public static final String AliyunDriverVerificationBucket = "driververification";
		public static final String AliyunPassengerVerificationBucket = "passengerverification";
		
		public static final int AdminRefLength = 6;
		public static final String resourcePrefix = "src/main/resources/";
}
