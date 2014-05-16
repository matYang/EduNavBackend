package UserModule.service;

import org.apache.commons.lang3.RandomStringUtils;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.eduDAO.EduDaoBasic;

public class UserCellVerificationDaoService {
	
	
	private static final String userCellVerification_keyPrefix = "user-cellVerification-";
	private static final int userCellVerification_authCodeLength = 6;
	private static final long userCellVerification_expireThreshold = 21600000l;		//6h
	
	
	
	public static boolean valdiateSession(String cellNum, String authCode){
		Jedis jedis = EduDaoBasic.getJedis();
		try{
			String redisKey = userCellVerification_keyPrefix + cellNum;
			String sessionString = jedis.get(redisKey);
			
			if(sessionString == null || sessionString.length() == 0){
				return false;
			}else{
				String redis_authCode = sessionString.split(DatabaseConfig.redisSeperatorRegex)[0];
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[1]);
				
				if(!redis_authCode.equals(authCode)){
					return false;
				}
				if((DateUtility.getCurTime() - redis_timeStamp) > userCellVerification_expireThreshold){
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
	
	public static String openSession(String cellNum){
		Jedis jedis = EduDaoBasic.getJedis();
		
		String redisKey = userCellVerification_keyPrefix + cellNum;
		String sessionString = RandomStringUtils.randomAlphanumeric(userCellVerification_authCodeLength).toUpperCase() + DatabaseConfig.redisSeperator + DateUtility.getTimeStamp();
		
		jedis.set(redisKey, sessionString);
		EduDaoBasic.returnJedis(jedis);
		return sessionString;
	}
	
	
	public static boolean closeSession(String cellNum){
		Jedis jedis = EduDaoBasic.getJedis();
		boolean result = jedis.del(userCellVerification_keyPrefix + cellNum) == 1;
		EduDaoBasic.returnJedis(jedis);
		return result;
	}
	
}
