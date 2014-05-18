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

public class UserForgotPasswordDaoService {
	
	private static final String userForgotPassword_keyPrefix =  RedisPrefixConfig.userForgotPassword_keyPrefix;
	private static final int userForgotPassword_authCodeLength = 8;
	private static final long userForgotPassword_expireThreshold = 21600000l;		//6h
	private static final long userForgotPassword_resendThreshould = 60000l;			//1min
	
	public static boolean valdiateSession(String cellNum, String authCode){
		Jedis jedis = EduDaoBasic.getJedis();
		try{
			String redisKey = userForgotPassword_keyPrefix + cellNum;
			String sessionString = jedis.get(redisKey);
			
			if(!RedisUtilityService.isValuedStored(sessionString)){
				return false;
			}else{
				String redis_authCode = sessionString.split(DatabaseConfig.redisSeperatorRegex)[0];
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[1]);
				
				if(!redis_authCode.equals(authCode)){
					return false;
				}
				if((DateUtility.getCurTime() - redis_timeStamp) > userForgotPassword_expireThreshold){
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
	
	public static String openSession(String cellNum) throws ValidationException{
		Jedis jedis = EduDaoBasic.getJedis();
		String authCode;
		
		try{
			String redisKey = userForgotPassword_keyPrefix + cellNum;
			String previousRecord = jedis.get(redisKey);
			if (RedisUtilityService.isValuedStored(previousRecord)){
				//check if should resend
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(previousRecord.split(DatabaseConfig.redisSeperatorRegex)[1]);
				if((DateUtility.getCurTime() - redis_timeStamp) <= userForgotPassword_resendThreshould){
					throw new ValidationException("连续请求过快");
				}
			}
			
			authCode = RandomStringUtils.randomAlphanumeric(userForgotPassword_authCodeLength);
			String sessionString = authCode + DatabaseConfig.redisSeperator + DateUtility.getTimeStamp();
			
			jedis.set(redisKey, sessionString);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		return authCode;
	}
	
	
	public static boolean closeSession(String cellNum){
		Jedis jedis = EduDaoBasic.getJedis();
		boolean result;
		
		try{
			result = jedis.del(userForgotPassword_keyPrefix + cellNum) == 1;
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		return result;
	}
	
}
