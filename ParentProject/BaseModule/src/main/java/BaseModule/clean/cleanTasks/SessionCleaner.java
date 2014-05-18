package BaseModule.clean.cleanTasks;

import java.util.Set;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.configurations.RedisPrefixConfig;
import BaseModule.eduDAO.EduDaoBasic;


public class SessionCleaner {

	public static void clean() {		
		
		Jedis redis = EduDaoBasic.getJedis();
		Set<String> keys;
		long now = DateUtility.getLongFromTimeStamp(DateUtility.getTimeStamp());
		
		try{
			/* UserModule*/

			//UserAuth
			keys = redis.keys(RedisPrefixConfig.userSession_web_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[2]);			
				if(timeStamp < now){
					redis.del(key);
				}
			}
			//UserBooking
			keys = redis.keys(RedisPrefixConfig.userChangePasswordVerification_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[1]);			
				if(timeStamp < now){
					redis.del(key);
				}
			}
			//UserCell
			keys = redis.keys(RedisPrefixConfig.userCellVerification_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[1]);			
				if(timeStamp < now){
					redis.del(key);
				}
			}
			//UserForgotPassword
			keys = redis.keys(RedisPrefixConfig.userForgotPassword_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[1]);			
				if(timeStamp < now){
					redis.del(key);
				}
			}

			/* PartnerModule */

			//PartnerAuth
			keys = redis.keys(RedisPrefixConfig.partnerSession_web_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[2]);			
				if(timeStamp < now){
					redis.del(key);
				}
			}
			//PartnerForgotPassword
			keys = redis.keys(RedisPrefixConfig.partnerForgotPassword_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[1]);			
				if(timeStamp < now){
					redis.del(key);
				}
			}

			/* AdminModule */

			//AdminAuth
			keys = redis.keys(RedisPrefixConfig.adminSession_web_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[2]);			
				if(timeStamp < now){
					redis.del(key);
				}
			}
		}
		finally{
			EduDaoBasic.returnJedis(redis);
		}
	}
}
