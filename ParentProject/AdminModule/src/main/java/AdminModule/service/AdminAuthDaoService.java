package AdminModule.service;

import org.apache.commons.lang3.RandomStringUtils;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.configurations.RedisPrefixConfig;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.service.RedisUtilityService;


public class AdminAuthDaoService {

	public static boolean validateSession(int id, String authCode, long timeStamp){
		Jedis jedis = EduDaoBasic.getJedis();
		try{
			String redisKey = RedisPrefixConfig.adminSession_web_keyPrefix + id;
			String sessionString = jedis.get(redisKey);
			
			if(!RedisUtilityService.isValuedStored(sessionString)){
				return false;
			}else{
				int redis_adminId = AdminAuthenticationService.getAdminIdFromSessionString(sessionString);
				String redis_authCode = AdminAuthenticationService.getAuthCodeFromSessionString(sessionString);
				long redis_timeStamp = AdminAuthenticationService.getTimeStampFromSessionString(sessionString);
				
				if(id != redis_adminId || !redis_authCode.equals(authCode)){
					return false;
				}
				if((DateUtility.getCurTime() - redis_timeStamp) > RedisPrefixConfig.adminSession_expireThreshold){
					//if expired, clean up and return false
					jedis.del(redisKey);
					return false;
				}
				if ((DateUtility.getCurTime() - redis_timeStamp) > RedisPrefixConfig.adminSession_updateThreshold){
					//if should update, udpate only the time stamp in the kvp
					jedis.set(redisKey, id + DatabaseConfig.redisSeperator + authCode + DatabaseConfig.redisSeperator + DateUtility.getTimeStamp());
				}
				return true;
			}
		} catch (Exception e){
			DebugLog.d(e);
			return false;
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
	}

	
	public static String openSession(int id){
		Jedis jedis = EduDaoBasic.getJedis();
		String sessionString;
		
		try{
			String redisKey = RedisPrefixConfig.adminSession_web_keyPrefix + id;
			sessionString = id + DatabaseConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(RedisPrefixConfig.adminSession_web_authCodeLength) + DatabaseConfig.redisSeperator + DateUtility.getTimeStamp();
			
			jedis.set(redisKey, sessionString);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		return sessionString;
	}


	public static boolean closeSession(int id){
		Jedis jedis = EduDaoBasic.getJedis();
		boolean result;
		
		try{
			result = jedis.del(RedisPrefixConfig.adminSession_web_keyPrefix + id) == 1;
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		return result;
	}

}
