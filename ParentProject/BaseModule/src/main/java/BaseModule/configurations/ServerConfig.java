package BaseModule.configurations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import BaseModule.common.DebugLog;
import BaseModule.encryption.AccessControlCrypto;


public class ServerConfig {
	
		private static final String ENV_VAR_KEY = "RA_MAINSERVER_ENV";
		private static final String ENV_TEST = "RA_TEST";
		private static final String ENV_PROD = "RA_PROD";
		
		public static final String ac_key = "du#*(kDJ8jS5-n@d";
		public static final String ac_ivy = "2Hl_39Hk3&l]F3j*";
		
		public static final String MAP_ENV_KEY = "env";
		public static final String MAP_ENV_LOCAL = "local";
		public static final String MAP_ENV_TEST = "test";
		public static final String MAP_ENV_PROD = "prod";
		public static final String MAP_MODULE_KEY = "module";
		public static final String MAP_MODULE_USER = "user";
		public static final String MAP_MODULE_PARTNER = "partner";
		public static final String MAP_MODULE_ADMIN = "admin";
		
		public static final String normalSpliter = "-";
		 
		//use concurrent hashmap to guarantee thread safety
		public static final Map<String, String> configurationMap = new ConcurrentHashMap<String, String>();
		
		
		static{
			try{
				String value = System.getenv(ENV_VAR_KEY);
				

				System.out.println("Server starting under " + value + " envrionment");
				if (value == null || (!value.equals(ENV_TEST) && !value.equals(ENV_PROD))){

					//local env
					configurationMap.put("env", "local");			
					configurationMap.put("jdbcUri", "localhost:3306/db19r3708gdzx5d1?allowMultiQueries=true&&characterSetResults=UTF-8&characterEncoding=UTF-8&useUnicode=yes");
					configurationMap.put("redisUri", "localhost");
					configurationMap.put("memcachedUri", "localhost:11211");
					configurationMap.put("sqlPass", "");
					configurationMap.put("sqlUser", "root");
					configurationMap.put("sqlMaxConnection","150");
					configurationMap.put("memcachedUser", "");
					configurationMap.put("memcachedPass", "");
				} 
				else if (value.equals(ENV_TEST)){
					//test env
					configurationMap.put("env", "test");
					configurationMap.put("jdbcUri", "badstudent.cunzg2tyzsud.us-west-2.rds.amazonaws.com:3306/test?allowMultiQueries=true&&characterSetResults=UTF-8&characterEncoding=UTF-8&useUnicode=yes");
					configurationMap.put("redisUri", "redisserver.ppomgu.0001.usw2.cache.amazonaws.com");
					configurationMap.put("memcachedUri", "localhost:11211");
					configurationMap.put("sqlPass", "badstudent");
					configurationMap.put("sqlUser", "test");
					configurationMap.put("sqlMaxConnection","50");
					configurationMap.put("memcachedUser", "");
					configurationMap.put("memcachedPass", "");
				}
				else{
					//prod env
					configurationMap.put("env", "prod");
					configurationMap.put("jdbcUri", "as4359fdgk.mysql.rds.aliyuncs.com:3306/db19r3708gdzx5d1?allowMultiQueries=true&&characterSetResults=UTF-8&characterEncoding=UTF-8&useUnicode=yes");
					configurationMap.put("redisUri", AccessControlCrypto.decrypt("0E0C572F1FE597594C85ED71A04D006F", ac_key, ac_ivy));
					configurationMap.put("memcachedUri", "fdbc1391e96411e3.m.cnhzalicm10pub001.ocs.aliyuncs.com:11211");
					configurationMap.put("sqlPass", AccessControlCrypto.decrypt("A1E4DDE152B755ECC46248A9D629FDD9", ac_key, ac_ivy));
					configurationMap.put("sqlUser", AccessControlCrypto.decrypt("7260820C1FAFD1F699249AF73A9D181D7BD6CE549202AD9FE095E1CE635843DB", ac_key, ac_ivy));
					configurationMap.put("sqlMaxConnection","50");
					configurationMap.put("memcachedUser", "fdbc1391e96411e3");
					configurationMap.put("memcachedPass", "4RKOZAlh48z1");
				}
			} catch (Exception e){
				DebugLog.d(e);
				e.printStackTrace();
				DebugLog.d("Server init failed, system exit...");
				System.exit(0);
			}
		}
		
		
		public static final String urlSeperator = "+";
		public static final String urlSeperatorRegx = "\\+";

		
		
		/*API level constants*/
		public static final int category_DM = 0;
		public static final String userApplicationPrefix = "/api";
		public static final String partnerApplicationPrefix = "/p-api";
		public static final String adminApplicationPrefix = "/a-api";
		public static final String versionPrefix = "/v1.0";
		
		/*Time constants*/
		public static final String timeZoneIdCH = "asia/shanghai";
		

		/* ALIYUN Bucket*/		
		public static final String AliyunAccessKeyID = "UBnwEnaFUdBewFF9";
		public static final String AliyunAccessKeySecrete = "L8hyNuKRXo5bfQ9HWURDq0bprDSDYO";
		public static final String AliyunTeacherImgBucket = "teacherimgbucket";
		public static final String AliyunClassroomImgBucket = "classroomimgbucket";		
		public static final String AliyunLogoBucket = "logobucket";
		public static final String AliyunProfileBucket = "badstudent-aliyun";		
		public static final String resourcePrefix = "src/main/resources/";
}
