package BaseModule.configurations;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import BaseModule.common.DebugLog;
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
			try{
				final String value = System.getenv(ENV_VAR_KEY);
				

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
					configurationMap.put("redisUri", "localhost");
					configurationMap.put("memcachedUri", "fdbc1391e96411e3.m.cnhzalicm10pub001.ocs.aliyuncs.com:11211");
					configurationMap.put("sqlPass", "A1E4DDE152B755ECC46248A9D629FDD9");
					configurationMap.put("sqlUser", "7260820C1FAFD1F699249AF73A9D181D7BD6CE549202AD9FE095E1CE635843DB");
					configurationMap.put("sqlMaxConnection","50");
					configurationMap.put("memcachedUser", "91315C17D13585EC7F7A61E3262B203621C258BC16897C8DC1C7C22BEE7E5E5A");
					configurationMap.put("memcachedPass", "BC6BAEC5B287331067E9F864DD9B981B");
					
				}
			} catch (final Exception e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		public static void acDecode(String ac_key, String ac_ivy){
			
			try {
				configurationMap.put("sqlPass", AccessControlCrypto.decrypt(configurationMap.get("sqlPass"), ac_key, ac_ivy));
				configurationMap.put("sqlUser", AccessControlCrypto.decrypt(configurationMap.get("sqlUser"), ac_key, ac_ivy));
				configurationMap.put("memcachedUser", AccessControlCrypto.decrypt(configurationMap.get("memcachedUser"), ac_key, ac_ivy));
				configurationMap.put("memcachedPass", AccessControlCrypto.decrypt(configurationMap.get("memcachedPass"), ac_key, ac_ivy));
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
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
