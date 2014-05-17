package UserModule.service;

import org.apache.commons.lang3.RandomStringUtils;

import redis.clients.jedis.Jedis;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.eduDAO.EduDaoBasic;

public class UserBookingVerificationDaoService {
	private static final String userBookingVerification_keyPrefix = "user-bookingVerification-";
	private static final int userBookingVerification_authCodeLength = 6;
	private static final long userBookingVerification_expireThreshold = 3600000l;	//1h
	
	public static boolean valdiateSession(int id, String authCode){
		Jedis jedis = EduDaoBasic.getJedis();
		try{
			String redisKey = userBookingVerification_keyPrefix + id;
			String sessionString = jedis.get(redisKey);
			
			if(sessionString == null || sessionString.length() == 0){
				return false;
			}else{
				String redis_authCode = sessionString.split(DatabaseConfig.redisSeperatorRegex)[0];
				long redis_timeStamp = DateUtility.getLongFromTimeStamp(sessionString.split(DatabaseConfig.redisSeperatorRegex)[1]);
				
				if(!redis_authCode.equals(authCode)){
					return false;
				}
				if((DateUtility.getCurTime() - redis_timeStamp) > userBookingVerification_expireThreshold){
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
	
	public static String openSession(int id){
		Jedis jedis = EduDaoBasic.getJedis();
		
		String redisKey = userBookingVerification_keyPrefix + id;
		String sessionString = RandomStringUtils.randomAlphanumeric(userBookingVerification_authCodeLength).toUpperCase() + DatabaseConfig.redisSeperator + DateUtility.getTimeStamp();
		
		jedis.set(redisKey, sessionString);
		EduDaoBasic.returnJedis(jedis);
		return sessionString;
	}
	
	
	public static boolean closeSession(int id){
		Jedis jedis = EduDaoBasic.getJedis();
		boolean result = jedis.del(userBookingVerification_keyPrefix + id) == 1;
		EduDaoBasic.returnJedis(jedis);
		return result;
	}
	
}