package BaseModule.clean.cleanTasks;

import java.util.Set;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.RedisAuthenticationConfig;
import BaseModule.eduDAO.EduDaoBasic;


public class SessionCleaner {

	public static void clean() {		
		
		Jedis redis = EduDaoBasic.getJedis();
		Set<String> keys;
		long now = DateUtility.getLongFromTimeStamp(DateUtility.getTimeStamp());
		
		try{
			/* UserModule*/

			//UserAuth
			keys = redis.keys(RedisAuthenticationConfig.userSession_web_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[2]);			
				if(timeStamp + RedisAuthenticationConfig.userSession_expireThreshold < now){
					redis.del(key);
				}
			}
			//UserChangePassword
			keys = redis.keys(RedisAuthenticationConfig.userChangePasswordVerification_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[1]);			
				if(timeStamp + RedisAuthenticationConfig.userChangePasswordVerification_expireThreshold < now){
					redis.del(key);
				}
			}
			//UserCell
			keys = redis.keys(RedisAuthenticationConfig.userCellVerification_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[1]);			
				if(timeStamp + RedisAuthenticationConfig.userCellVerification_expireThreshold < now){
					redis.del(key);
				}
			}
			//UserForgotPassword
			keys = redis.keys(RedisAuthenticationConfig.userForgotPassword_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[1]);			
				if(timeStamp + RedisAuthenticationConfig.userForgotPassword_expireThreshold < now){
					redis.del(key);
				}
			}

			/* PartnerModule */

			//PartnerAuth
			keys = redis.keys(RedisAuthenticationConfig.partnerSession_web_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[2]);			
				if(timeStamp + RedisAuthenticationConfig.partnerSession_expireThreshold < now){
					redis.del(key);
				}
			}
			//PartnerForgotPassword
			keys = redis.keys(RedisAuthenticationConfig.partnerForgotPassword_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[1]);			
				if(timeStamp + RedisAuthenticationConfig.partnerForgotPassword_expireThreshold < now){
					redis.del(key);
				}
			}
			//PartnerChangePassword
			keys = redis.keys(RedisAuthenticationConfig.partnerChangePasswordVerification_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[1]);			
				if(timeStamp + RedisAuthenticationConfig.partnerChangePasswordVerification_expireThreshold < now){
					redis.del(key);
				}
			}

			/* AdminModule */

			//AdminAuth
			keys = redis.keys(RedisAuthenticationConfig.adminSession_web_keyPrefix+"*");		
			for(String key : keys){
				String sessionString = redis.get(key);
				long timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[2]);			
				if(timeStamp + RedisAuthenticationConfig.adminSession_expireThreshold < now){
					redis.del(key);
				}
			}
		}
		catch (Exception e){
			DebugLog.d(e);
		}
		finally{
			EduDaoBasic.returnJedis(redis);
		}
	}
}
