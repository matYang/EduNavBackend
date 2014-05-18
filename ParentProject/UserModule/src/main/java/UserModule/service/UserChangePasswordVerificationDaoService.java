package UserModule.service;

import org.apache.commons.lang3.RandomStringUtils;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.configurations.RedisPrefixConfig;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.validation.ValidationException;
import BaseModule.service.RedisUtilityService;

public class UserChangePasswordVerificationDaoService {
	
	private static final String userChangePasswordVerification_keyPrefix = RedisPrefixConfig.userChangePasswordVerification_keyPrefix;
	private static final int userChangePasswordVerification_authCodeLength = 6;
	private static final long userChangePasswordVerification_expireThreshold = 3600000l;	//1h
	private static final long userChangePasswordVerification_resendThreshold = 60000l;	//1h
	
	public static boolean valdiateSession(int id, String authCode){
		Jedis jedis = EduDaoBasic.getJedis();
		try{
			String redisKey = userChangePasswordVerification_keyPrefix + id;
			String sessionString = jedis.get(redisKey);
			
			if(!RedisUtilityService.isValuedStored(sessionString)){
				return false;
			}else{
				String redis_authCode = sessionString.split(DatabaseConfig.redisSeperatorRegex)[0];
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[1]);
				
				if(!redis_authCode.equals(authCode)){
					return false;
				}
				if((DateUtility.getCurTime() - redis_timeStamp) > userChangePasswordVerification_expireThreshold){
					jedis.del(redisKey);
					return false;
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
	
	public static String openSession(int id) throws ValidationException{
		Jedis jedis = EduDaoBasic.getJedis();
		
		String redisKey = userChangePasswordVerification_keyPrefix + id;
		String previousRecord = jedis.get(redisKey);
		if (RedisUtilityService.isValuedStored(previousRecord)){
			//check if should resend
			long redis_timeStamp = DateUtility.getLongFromTimeStamp(previousRecord.split(DatabaseConfig.redisSeperatorRegex)[1]);
			if((DateUtility.getCurTime() - redis_timeStamp) <= userChangePasswordVerification_resendThreshold){
				throw new ValidationException("连续请求过快");
			}
		}
		
		String authCode = RandomStringUtils.randomAlphanumeric(userChangePasswordVerification_authCodeLength).toUpperCase();
		String sessionString = authCode + DatabaseConfig.redisSeperator + DateUtility.getTimeStamp();
		
		jedis.set(redisKey, sessionString);
		EduDaoBasic.returnJedis(jedis);
		return authCode;
	}
	
	
	public static boolean closeSession(int id){
		Jedis jedis = EduDaoBasic.getJedis();
		boolean result = jedis.del(userChangePasswordVerification_keyPrefix + id) == 1;
		EduDaoBasic.returnJedis(jedis);
		return result;
	}
	
}
