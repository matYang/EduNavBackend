package PartnerModule.service;

import org.apache.commons.lang3.RandomStringUtils;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.configurations.RedisPrefixConfig;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.validation.ValidationException;
import BaseModule.service.RedisUtilityService;

public class PartnerChangePasswordVerificationDaoService {
	
	private static final String partnerChangePasswordVerification_keyPrefix = RedisPrefixConfig.partnerChangePasswordVerification_keyPrefix;
	private static final int partnerChangePasswordVerification_authCodeLength = 6;
	private static final long partnerChangePasswordVerification_expireThreshold = 3600000l;	//1h
	private static final long partnerChangePasswordVerification_resendThreshold = 60000l;	//1h
	
	public static boolean valdiateSession(int id, String authCode){
		Jedis jedis = EduDaoBasic.getJedis();
		try{
			String redisKey = partnerChangePasswordVerification_keyPrefix + id;
			String sessionString = jedis.get(redisKey);
			
			if(!RedisUtilityService.isValuedStored(sessionString)){
				return false;
			}else{
				String redis_authCode = sessionString.split(DatabaseConfig.redisSeperatorRegex)[0];
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[1]);
				
				if(!redis_authCode.equals(authCode)){
					return false;
				}
				if((DateUtility.getCurTime() - redis_timeStamp) > partnerChangePasswordVerification_expireThreshold){
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
		String authCode;
		
		try{
			String redisKey = partnerChangePasswordVerification_keyPrefix + id;
			String previousRecord = jedis.get(redisKey);
			if (RedisUtilityService.isValuedStored(previousRecord)){
				//check if should resend
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(previousRecord.split(DatabaseConfig.redisSeperatorRegex)[1]);
				if((DateUtility.getCurTime() - redis_timeStamp) <= partnerChangePasswordVerification_resendThreshold){
					throw new ValidationException("连续请求过快");
				}
			}
			
			authCode = RandomStringUtils.randomAlphanumeric(partnerChangePasswordVerification_authCodeLength).toUpperCase();
			String sessionString = authCode + DatabaseConfig.redisSeperator + DateUtility.getTimeStamp();
			
			jedis.set(redisKey, sessionString);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		return authCode;
	}
	
	
	public static boolean closeSession(int id){
		Jedis jedis = EduDaoBasic.getJedis();
		boolean result;
		try{
			result = jedis.del(partnerChangePasswordVerification_keyPrefix + id) == 1;
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		return result;
	}
}
