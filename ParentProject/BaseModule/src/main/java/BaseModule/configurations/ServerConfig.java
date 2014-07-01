package BaseModule.configurations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import BaseModule.encryption.AccessControlCrypto;


public final class ServerConfig {
	
		private static final String ENV_VAR_KEY = "RA_MAINSERVER_ENV";
		private static final String ENV_TEST = "RA_TEST";
		private static final String ENV_PROD = "RA_PROD";
		
		
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
			String modePrefix = "";
			try{
				final String value = System.getenv(ENV_VAR_KEY);
				

				System.out.println("Server starting under " + value + " envrionment");
				if (value == null || (!value.equals(ENV_TEST) && !value.equals(ENV_PROD))){

					//local env
					configurationMap.put("env", "local");			
					configurationMap.put("jdbcUri", "localhost:3306/db19r3708gdzx5d1?allowMultiQueries=true&&characterSetResults=UTF-8&characterEncoding=UTF-8&useUnicode=yes");
					configurationMap.put("redisUri", "localhost");
					configurationMap.put("redisPort", "6379");
					configurationMap.put("memcachedUri", "localhost:11211");
					configurationMap.put("sqlPass", "");
					configurationMap.put("sqlUser", "root");
					configurationMap.put("sqlMaxConnection","150");
					configurationMap.put("memcachedUser", "");
					configurationMap.put("memcachedPass", "");
				} 
				else if (value.equals(ENV_TEST)){
					//test env
					//TODO
					configurationMap.put("env", "test");
					configurationMap.put("jdbcUri", "badstudent.cunzg2tyzsud.us-west-2.rds.amazonaws.com:3306/test?allowMultiQueries=true&&characterSetResults=UTF-8&characterEncoding=UTF-8&useUnicode=yes");
					configurationMap.put("redisUri", "localhost");
					configurationMap.put("redisPort", "6380");
					configurationMap.put("memcachedUri", "fdbc1391e96411e3.m.cnhzalicm10pub001.ocs.aliyuncs.com:11211");
					configurationMap.put("sqlUser", "082E4185DBC158A01DC2DE32E241AA4C7167BFF2EAD2493A1E95D63496F69CA7");
					configurationMap.put("sqlPass", "9211A28B9094893E29F00C1072940646");
					configurationMap.put("sqlMaxConnection","50");
					configurationMap.put("memcachedUser", "45D65B9EDB590224697482D7525A1612A31A7A0AFE88AE5236E8DA8A85B0145C");
					configurationMap.put("memcachedPass", "B2E36A9EB801CBD8A55A20B70269755F03E3097CCBECC9BE6CEFC26BFBBB3F12");
					
					modePrefix = "test-";
				}
				else{
					//prod env
					configurationMap.put("env", "prod");
					configurationMap.put("jdbcUri", "as4359fdgk.mysql.rds.aliyuncs.com:3306/db19r3708gdzx5d1?allowMultiQueries=true&&characterSetResults=UTF-8&characterEncoding=UTF-8&useUnicode=yes");
					configurationMap.put("redisUri", "localhost");
					configurationMap.put("redisPort", "6379");
					configurationMap.put("memcachedUri", "fdbc1391e96411e3.m.cnhzalicm10pub001.ocs.aliyuncs.com:11211");
					configurationMap.put("sqlUser", "082E4185DBC158A01DC2DE32E241AA4C7167BFF2EAD2493A1E95D63496F69CA7");
					configurationMap.put("sqlPass", "9211A28B9094893E29F00C1072940646");
					configurationMap.put("sqlMaxConnection","50");
					configurationMap.put("memcachedUser", "595859005EA745D60DE860E460DBD057B0D26BEAA841B0DCBD0D4CE4A8F032E0");
					configurationMap.put("memcachedPass", "5B7636EF7D897EDEEF3B1481C6D94017160531FC2020B6E8299E49B218752739");
				}
			
			} catch (final Exception e){
				e.printStackTrace();
				System.exit(1);
			}
			
			userApplicationPrefix = "/"+modePrefix+"api";
			partnerApplicationPrefix = "/"+modePrefix+"p-api";
			adminApplicationPrefix = "/"+modePrefix+"a-api";
			versionPrefix = "/v1.0";
			
			AliyunTeacherImgBucket = modePrefix+"teacherimgbucket";
			AliyunClassroomImgBucket = modePrefix+"classroomimgbucket";		
			AliyunLogoBucket = modePrefix+"logobucket";
		}
		
		public static void acDecode(String ac_key, String ac_ivy){
			try {
				if (!configurationMap.get("env").equals("local")){
					configurationMap.put("sqlPass", AccessControlCrypto.decrypt(configurationMap.get("sqlPass"), ac_key, ac_ivy));
					configurationMap.put("sqlUser", AccessControlCrypto.decrypt(configurationMap.get("sqlUser"), ac_key, ac_ivy));
					configurationMap.put("memcachedUser", AccessControlCrypto.decrypt(configurationMap.get("memcachedUser"), ac_key, ac_ivy));
					configurationMap.put("memcachedPass", AccessControlCrypto.decrypt(configurationMap.get("memcachedPass"), ac_key, ac_ivy));
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		
		public static final String urlSeperator = "+";
		public static final String urlSeperatorRegx = "\\+";

		
		
		/*API level constants*/
		public static final String userApplicationPrefix;
		public static final String partnerApplicationPrefix;
		public static final String adminApplicationPrefix;
		public static final String versionPrefix;
		
		/*Time constants*/
		public static final String timeZoneIdCH = "asia/shanghai";
		

		/* ALIYUN Bucket*/		
		public static final String AliyunAccessKeyID = "UBnwEnaFUdBewFF9";
		public static final String AliyunAccessKeySecrete = "L8hyNuKRXo5bfQ9HWURDq0bprDSDYO";
		public static final String AliyunTeacherImgBucket;
		public static final String AliyunClassroomImgBucket;		
		public static final String AliyunLogoBucket;
		public static final String resourcePrefix = "src/main/resources/";
}
