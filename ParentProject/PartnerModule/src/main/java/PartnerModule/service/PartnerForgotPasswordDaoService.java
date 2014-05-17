package PartnerModule.service;

import org.apache.commons.lang3.RandomStringUtils;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.validation.ValidationException;
import BaseModule.service.RedisUtilityService;

public class PartnerForgotPasswordDaoService {

	private static final String partnerForgotPassword_keyPrefix = "partner-forgotPassword-";
	private static final int partnerForgotPassword_authCodeLength = 8;
	private static final long partnerForgotPassword_expireThreshold = 21600000l;		//6h
	private static final long partnerForgotPassword_resendThreshould = 60000l;			//1min
	
	public static boolean valdiateSession(String cellNum, String authCode){
		Jedis jedis = EduDaoBasic.getJedis();
		try{
			String redisKey = partnerForgotPassword_keyPrefix + cellNum;
			String sessionString = jedis.get(redisKey);
			
			if(!RedisUtilityService.isValuedStored(sessionString)){
				return false;
			}else{
				String redis_authCode = sessionString.split(DatabaseConfig.redisSeperatorRegex)[0];
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[1]);
				
				if(!redis_authCode.equals(authCode)){
					return false;
				}
				if((DateUtility.getCurTime() - redis_timeStamp) > partnerForgotPassword_expireThreshold){
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
		
		String redisKey = partnerForgotPassword_keyPrefix + cellNum;
		String previousRecord = jedis.get(redisKey);
		if (RedisUtilityService.isValuedStored(previousRecord)){
			//check if should resend
			long redis_timeStamp = DateUtility.getLongFromTimeStamp(previousRecord.split(DatabaseConfig.redisSeperatorRegex)[1]);
			if((DateUtility.getCurTime() - redis_timeStamp) <= partnerForgotPassword_resendThreshould){
				throw new ValidationException("连续请求过快");
			}
		}
		
		String authCode = RandomStringUtils.randomAlphanumeric(partnerForgotPassword_authCodeLength);
		String sessionString = authCode  + DatabaseConfig.redisSeperator + DateUtility.getTimeStamp();
		
		jedis.set(redisKey, sessionString);
		EduDaoBasic.returnJedis(jedis);
		return authCode ;
	}
	
	
	public static boolean closeSession(String cellNum){
		Jedis jedis = EduDaoBasic.getJedis();
		boolean result = jedis.del(partnerForgotPassword_keyPrefix + cellNum) == 1;
		EduDaoBasic.returnJedis(jedis);
		return result;
	}
}
