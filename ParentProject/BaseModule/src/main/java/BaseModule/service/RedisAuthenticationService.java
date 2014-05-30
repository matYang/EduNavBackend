package BaseModule.service;

import org.apache.commons.lang3.RandomStringUtils;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.RedisAuthenticationConfig;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.configObj.RedisSubConfig;

public class RedisAuthenticationService {
	
	/******************************
	 * 
	 * 		Web Session
	 * 
	 ******************************/
	public static boolean validateWebSession(int serviceIdentifier, int id, String authCode, long timeStamp){
		Jedis jedis = EduDaoBasic.getJedis();
		try{
			RedisSubConfig config = RedisAuthenticationConfig.getConfigBean(serviceIdentifier);
			
			String redisKey = config.keyPrefix + id;
			String sessionString = jedis.get(redisKey);
			
			if(!RedisUtilityService.isValuedStored(sessionString)){
				return false;
			}else{
				int redis_id = getIdFromSessionString(sessionString);
				String redis_authCode = getAuthCodeFromSessionString(sessionString);
				long redis_timeStamp = getTimeStampFromSessionString(sessionString);
				
				if(id != redis_id || !redis_authCode.equals(authCode)){
					return false;
				}
				if((DateUtility.getCurTime() - redis_timeStamp) > config.expireThreshold){
					//if expired, clean up and return false
					jedis.del(redisKey);
					return false;
				}
				if ((DateUtility.getCurTime() - redis_timeStamp) > config.activeThreshold){
					//if should update, udpate only the time stamp in the kvp
					jedis.set(redisKey, id + RedisAuthenticationConfig.redisSeperator + authCode + RedisAuthenticationConfig.redisSeperator + DateUtility.getTimeStamp());
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
	
	public static String openWebSession(int serviceIdentifier, int id){
		Jedis jedis = EduDaoBasic.getJedis();
		String sessionString;
		
		try{
			RedisSubConfig config = RedisAuthenticationConfig.getConfigBean(serviceIdentifier);
			
			String redisKey = config.keyPrefix + id;
			sessionString = id + RedisAuthenticationConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(config.authCodeLength) + RedisAuthenticationConfig.redisSeperator + DateUtility.getTimeStamp();
			
			jedis.set(redisKey, sessionString);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		return sessionString;
	}
	
	
	/******************************
	 * 
	 * 		Cell Session
	 * 
	 ******************************/
	public static boolean valdiateCellSession(int serviceIdentifier, String sufix, String authCode){
		Jedis jedis = EduDaoBasic.getJedis();
		try{
			RedisSubConfig config = RedisAuthenticationConfig.getConfigBean(serviceIdentifier);
			
			String redisKey = config.keyPrefix + sufix;
			String sessionString = jedis.get(redisKey);
			
			if(!RedisUtilityService.isValuedStored(sessionString)){
				return false;
			}else{
				String redis_authCode = sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[0];
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[1]);
				
				if(!redis_authCode.equals(authCode)){
					return false;
				}
				if((DateUtility.getCurTime() - redis_timeStamp) > config.expireThreshold){
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
	
	
	public static String openCellSession(int serviceIdentifier, String sufix) throws ValidationException{
		Jedis jedis = EduDaoBasic.getJedis();
		String authCode;
		
		try{
			RedisSubConfig config = RedisAuthenticationConfig.getConfigBean(serviceIdentifier);
			
			String redisKey = config.keyPrefix + sufix;
			String previousRecord = jedis.get(redisKey);
			if (RedisUtilityService.isValuedStored(previousRecord)){
				//check if should resend
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(previousRecord.split(RedisAuthenticationConfig.redisSeperatorRegex)[1]);
				if((DateUtility.getCurTime() - redis_timeStamp) <= config.activeThreshold){
					throw new ValidationException("连续请求过快");
				}
			}
			
			authCode = RandomStringUtils.randomAlphanumeric(config.authCodeLength);
			if (config.authCodeUpper){
				authCode = authCode.toUpperCase();
			}
			String sessionString = authCode + RedisAuthenticationConfig.redisSeperator + DateUtility.getTimeStamp();
			
			jedis.set(redisKey, sessionString);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		return authCode;
	}
	
	
	/******************************
	 * 
	 * 		All Session
	 * 
	 ******************************/
	public static boolean closeSession(int serviceIdentifier, String keySufix){
		Jedis jedis = EduDaoBasic.getJedis();
		boolean result;
		RedisSubConfig config = RedisAuthenticationConfig.getConfigBean(serviceIdentifier);
		try{
			result = jedis.del(config.keyPrefix + keySufix) == 1;
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		return result;
	}
	
	
	
	/******************************
	 * 
	 * 		Session String
	 * 
	 ******************************/
	//Session string format: "id+sessionStr+timeStamp"
	public static int getIdFromSessionString(String sessionString)throws PseudoException{
		String idStr = sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[0];
		int userId = Integer.parseInt(idStr);
		return userId;
	}
	public static String getAuthCodeFromSessionString(String sessionString)throws PseudoException{
		String authCodeStr = sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[1];
		return authCodeStr;
	}
	public static long getTimeStampFromSessionString(String sessionString)throws PseudoException{
		String timeStampStr = sessionString.split(RedisAuthenticationConfig.redisSeperatorRegex)[2];
		return DateUtility.getLongFromTimeStamp(timeStampStr);
	}

}
