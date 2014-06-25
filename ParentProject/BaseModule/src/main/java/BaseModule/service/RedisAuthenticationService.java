package BaseModule.service;

import org.apache.commons.lang3.RandomStringUtils;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.RedisAuthenticationConfig;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.dataObj.RedisAuthenticationObj;

public final class RedisAuthenticationService {
	
	/******************************
	 * 
	 * 		Web Session
	 * 
	 ******************************/
	public static boolean validateWebSession(final int serviceIdentifier, final int id, final String authCode, final long timeStamp){
		Jedis jedis = null;
		try{
			jedis = EduDaoBasic.getJedis();
			
			RedisAuthenticationObj config = RedisAuthenticationConfig.getConfig(serviceIdentifier);
			
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
					jedis.set(redisKey, id + RedisAuthenticationConfig.redisAuthenticationSeperator + authCode + RedisAuthenticationConfig.redisAuthenticationSeperator + DateUtility.getTimeStamp());
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
	
	public static String openWebSession(final int serviceIdentifier, final int id){
		Jedis jedis = null;
		String sessionString;
		
		try{
			jedis = EduDaoBasic.getJedis();
			
			RedisAuthenticationObj config = RedisAuthenticationConfig.getConfig(serviceIdentifier);
			
			String redisKey = config.keyPrefix + id;
			sessionString = id + RedisAuthenticationConfig.redisAuthenticationSeperator + RandomStringUtils.randomAlphanumeric(config.authCodeLength) + RedisAuthenticationConfig.redisAuthenticationSeperator + DateUtility.getTimeStamp();
			
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
	public static boolean valdiateCellSession(final int serviceIdentifier, final String sufix, final String authCode){
		Jedis jedis = null;
		try{
			jedis = EduDaoBasic.getJedis();
			
			RedisAuthenticationObj config = RedisAuthenticationConfig.getConfig(serviceIdentifier);
			
			String redisKey = config.keyPrefix + sufix;
			String sessionString = jedis.get(redisKey);
			
			if(!RedisUtilityService.isValuedStored(sessionString)){
				return false;
			}else{
				String redis_authCode = sessionString.split(RedisAuthenticationConfig.redisAuthenticationSeperatorRegex)[0];
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(RedisAuthenticationConfig.redisAuthenticationSeperatorRegex)[1]);
				
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
	
	
	public static String openCellSession(final int serviceIdentifier, final String sufix) throws ValidationException{
		Jedis jedis = null;
		String authCode;
		
		try{
			jedis = EduDaoBasic.getJedis();
			
			RedisAuthenticationObj config = RedisAuthenticationConfig.getConfig(serviceIdentifier);
			
			String redisKey = config.keyPrefix + sufix;
			String previousRecord = jedis.get(redisKey);
			if (RedisUtilityService.isValuedStored(previousRecord)){
				//check if should resend
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(previousRecord.split(RedisAuthenticationConfig.redisAuthenticationSeperatorRegex)[1]);
				if((DateUtility.getCurTime() - redis_timeStamp) <= config.activeThreshold){
					throw new ValidationException("连续请求过快");
				}
			}
			
			authCode = RandomStringUtils.randomAlphanumeric(config.authCodeLength);
			if (config.authCodeUpper){
				authCode = authCode.toUpperCase();
			}
			String sessionString = authCode + RedisAuthenticationConfig.redisAuthenticationSeperator + DateUtility.getTimeStamp();
			
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
	public static boolean closeSession(final int serviceIdentifier, final String keySufix){
		Jedis jedis = null;
		boolean result;
		RedisAuthenticationObj config = RedisAuthenticationConfig.getConfig(serviceIdentifier);
		try{
			jedis = EduDaoBasic.getJedis();
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
	public static int getIdFromSessionString(final String sessionString)throws PseudoException{
		String idStr = sessionString.split(RedisAuthenticationConfig.redisAuthenticationSeperatorRegex)[0];
		int userId = Integer.parseInt(idStr);
		return userId;
	}
	public static String getAuthCodeFromSessionString(final String sessionString)throws PseudoException{
		String authCodeStr = sessionString.split(RedisAuthenticationConfig.redisAuthenticationSeperatorRegex)[1];
		return authCodeStr;
	}
	public static long getTimeStampFromSessionString(final String sessionString)throws PseudoException{
		String timeStampStr = sessionString.split(RedisAuthenticationConfig.redisAuthenticationSeperatorRegex)[2];
		return DateUtility.getLongFromTimeStamp(timeStampStr);
	}

}
