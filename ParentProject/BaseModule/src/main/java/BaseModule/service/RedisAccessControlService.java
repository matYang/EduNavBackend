package BaseModule.service;

import java.util.Calendar;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.configurations.RedisAccessControlConfig;
import BaseModule.configurations.RedisAuthenticationConfig;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.model.configObj.RedisAccessControlObj;

public class RedisAccessControlService {
	
	
	public static boolean isAbleToLogin(final int serviceIdentifier, final int id){
		Jedis jedis = null;
		RedisAccessControlObj config = RedisAccessControlConfig.getConfig(serviceIdentifier);
		try{
			jedis = EduDaoBasic.getJedis();
			String record = jedis.get(config.keyPrefix + id);
			if (RedisUtilityService.isValuedStored(record)){
				String[] values = record.split(RedisAccessControlConfig.redisAccessControlRegex);
				int count = Integer.parseInt(values[0]);
				long timeStamp = DateUtility.getLongFromTimeStamp(values[1]);
				
				if (DateUtility.getCurTime() - timeStamp > config.releaseThreshould){
					//1 minute expired
					return true;
				}
				else{
					if (count < 5){
						return true;
					}
					else{
						return false;
					}
				}
			}
			else{
				//no fail
				return true;
			}
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
	}
	
	
	//a login fail starts a new access control session or increments failed login count
	public static void fail(final int serviceIdentifier, final int id){
		Jedis jedis = null;
		RedisAccessControlObj config = RedisAccessControlConfig.getConfig(serviceIdentifier);
		try{
			jedis = EduDaoBasic.getJedis();
			String record = jedis.get(config.keyPrefix + id);
			if (RedisUtilityService.isValuedStored(record)){
				String[] values = record.split(RedisAccessControlConfig.redisAccessControlRegex);
				int count = Integer.parseInt(values[0]);
				long timeStamp = DateUtility.getLongFromTimeStamp(values[1]);
				
				if (DateUtility.getCurTime() - timeStamp > config.lockThreshold){
					record = 1 + RedisAccessControlConfig.redisAccessControlSeperator + DateUtility.getTimeStamp();
					jedis.set(config.keyPrefix + id, record);
				}
				else{
					count++;
					record = count + RedisAccessControlConfig.redisAccessControlSeperator + values[1];
					jedis.set(config.keyPrefix + id, record);
				}
			}
			else{
				record = 1 + RedisAccessControlConfig.redisAccessControlSeperator + DateUtility.getTimeStamp();
				jedis.set(config.keyPrefix + id, record);
			}
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
	}
	
	//a successful login releases previous counts
	public static boolean success(final int serviceIdentifier, final int id){
		Jedis jedis = null;
		boolean result;
		RedisAccessControlObj config = RedisAccessControlConfig.getConfig(serviceIdentifier);
		try{
			jedis = EduDaoBasic.getJedis();
			result = jedis.del(config.keyPrefix + id) == 1;
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		return result;
	}
	
	

}
