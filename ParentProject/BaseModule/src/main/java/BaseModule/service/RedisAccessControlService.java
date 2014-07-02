package BaseModule.service;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.configurations.RedisAccessControlConfig;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.model.dataObj.RedisAccessControlObj;

public class RedisAccessControlService {
	
	
	public static boolean isAbleToLogin(final String moduleId, final String sufix){
		Jedis jedis = null;
		RedisAccessControlObj config = RedisAccessControlConfig.getConfig(moduleId);
		try{
			jedis = EduDaoBasic.getJedis();
			String record = jedis.get(config.keyPrefix + sufix);
			if (RedisUtilityService.isValuedStored(record)){
				String[] values = record.split(RedisAccessControlConfig.redisAccessControlRegex);
				int count = Integer.parseInt(values[0]);
				long timeStamp = DateUtility.getLongFromTimeStamp(values[1]);
				
				if (DateUtility.getCurTime() - timeStamp > config.lockThreshold){
					//lock threshold has passed since the first failed attempt
					return true;
				}
				else{
					//still within release threshold, but failed attempts fewer than lock count
					if (count < config.lockCount){
						return true;
					}
					//still within release threshold, and failed more than lock count
					else{
						return false;
					}
				}
			}
			else{
				//no fail record
				return true;
			}
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
	}
	
	
	//a login fail starts a new access control session or increments failed login count
	public static void fail(final String moduleId, final String sufix){
		Jedis jedis = null;
		RedisAccessControlObj config = RedisAccessControlConfig.getConfig(moduleId);
		try{
			jedis = EduDaoBasic.getJedis();
			String record = jedis.get(config.keyPrefix + sufix);
			if (RedisUtilityService.isValuedStored(record)){
				String[] values = record.split(RedisAccessControlConfig.redisAccessControlRegex);
				int count = Integer.parseInt(values[0]);
				long timeStamp = DateUtility.getLongFromTimeStamp(values[1]);
				
				//more than 1 minute passed since the first failed attempt, restart session
				if (DateUtility.getCurTime() - timeStamp > config.lockThreshold){
					record = 1 + RedisAccessControlConfig.redisAccessControlSeperator + DateUtility.getTimeStamp();
					jedis.set(config.keyPrefix + sufix, record);
				}
				//still within same session
				else{
					count++;
					record = count + RedisAccessControlConfig.redisAccessControlSeperator + values[1];
					jedis.set(config.keyPrefix + sufix, record);
				}
				
			}
			else{
				//first start the failed attemp session
				record = 1 + RedisAccessControlConfig.redisAccessControlSeperator + DateUtility.getTimeStamp();
				jedis.set(config.keyPrefix + sufix, record);
			}
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
	}
	
	
	public static boolean success(final String moduleId, final String sufix){
		Jedis jedis = null;
		boolean result;
		RedisAccessControlObj config = RedisAccessControlConfig.getConfig(moduleId);
		try{
			//a successful login releases previous counts
			jedis = EduDaoBasic.getJedis();
			result = jedis.del(config.keyPrefix + sufix) == 1;
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		return result;
	}
	
	

}
