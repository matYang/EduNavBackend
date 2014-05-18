package BaseModule.cleanerTest;

import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import BaseModule.clean.cleanTasks.SessionCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.DatabaseConfig;
import BaseModule.configurations.RedisPrefixConfig;
import BaseModule.eduDAO.EduDaoBasic;

public class SessionCleanerTest {

	private static final int userSession_web_authCodeLength = 15;
	private static final int partnerSession_web_authCodeLength = 15;
	private static final int adminSession_web_authCodeLength = 15;
	private static final int userBookingVerification_authCodeLength = 6;
	private static final int userCellVerification_authCodeLength = 6;
	private static final int userForgotPassword_authCodeLength = 8;
	private static final int partnerForgotPassword_authCodeLength = 8;
	@Test
	public void test(){
		EduDaoBasic.clearBothDatabase();
		Jedis redis = EduDaoBasic.getJedis();
		Calendar badTime;
		long badTimeStamp;
		String nowTimeStamp = DateUtility.getTimeStamp();
		//UserAuthOpenSession
		int userId = 1;		
		String redisKey = RedisPrefixConfig.userSession_web_keyPrefix + userId;
		String sessionString0 = userId + DatabaseConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(userSession_web_authCodeLength) + DatabaseConfig.redisSeperator + nowTimeStamp;		
		redis.set(redisKey, sessionString0);

		userId++;		
		redisKey = RedisPrefixConfig.userSession_web_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.DAY_OF_YEAR, -1);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString1 = userId + DatabaseConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(userSession_web_authCodeLength) + DatabaseConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString1);

		userId++;		
		redisKey = RedisPrefixConfig.userSession_web_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 5);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString2 = userId + DatabaseConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(userSession_web_authCodeLength) + DatabaseConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString2);

		//UserBookingOpenSession
		userId++;			
		redisKey = RedisPrefixConfig.userBookingVerification_keyPrefix + userId;
		String sessionString3 = RandomStringUtils.randomAlphanumeric(userBookingVerification_authCodeLength).toUpperCase() + DatabaseConfig.redisSeperator + nowTimeStamp;
		redis.set(redisKey, sessionString3);

		userId++;		
		redisKey = RedisPrefixConfig.userBookingVerification_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, -5);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString4 = RandomStringUtils.randomAlphanumeric(userBookingVerification_authCodeLength).toUpperCase() + DatabaseConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString4);

		//UserCellOpenSession
		userId++;			
		redisKey = RedisPrefixConfig.userCellVerification_keyPrefix + userId;
		String sessionString5 = RandomStringUtils.randomAlphanumeric(userCellVerification_authCodeLength).toUpperCase() + DatabaseConfig.redisSeperator + nowTimeStamp;
		redis.set(redisKey, sessionString5);

		userId++;		
		redisKey = RedisPrefixConfig.userCellVerification_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.DAY_OF_WEEK_IN_MONTH, 5);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString6 = RandomStringUtils.randomAlphanumeric(userCellVerification_authCodeLength).toUpperCase() + DatabaseConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString6);

		//UserForgotPassword
		userId++;			
		redisKey = RedisPrefixConfig.userForgotPassword_keyPrefix+ userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 1);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString7 = RandomStringUtils.randomAlphanumeric(userForgotPassword_authCodeLength) + DatabaseConfig.redisSeperator + badTimeStamp;
		redis.set(redisKey, sessionString7);

		userId++;		
		redisKey = RedisPrefixConfig.userForgotPassword_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.HOUR, -5);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString8 = RandomStringUtils.randomAlphanumeric(userForgotPassword_authCodeLength) + DatabaseConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString8);

		//PartnerAuth
		userId++;			
		redisKey = RedisPrefixConfig.partnerSession_web_keyPrefix+ userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 1);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString9 = userId + DatabaseConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(partnerSession_web_authCodeLength) + DatabaseConfig.redisSeperator + badTimeStamp;
		redis.set(redisKey, sessionString9);

		userId++;		
		redisKey = RedisPrefixConfig.partnerSession_web_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.DAY_OF_WEEK_IN_MONTH, -5);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString10 = userId + DatabaseConfig.redisSeperator +RandomStringUtils.randomAlphanumeric(partnerSession_web_authCodeLength) + DatabaseConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString10);

		//PartnerForgotPassword
		userId++;			
		redisKey = RedisPrefixConfig.partnerForgotPassword_keyPrefix+ userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 1);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString11 = RandomStringUtils.randomAlphanumeric(partnerForgotPassword_authCodeLength) + DatabaseConfig.redisSeperator + badTimeStamp;
		redis.set(redisKey, sessionString11);

		userId++;		
		redisKey = RedisPrefixConfig.partnerForgotPassword_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.SECOND, -5);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString12 = RandomStringUtils.randomAlphanumeric(partnerForgotPassword_authCodeLength) + DatabaseConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString12);

		//AdminAuth
		userId++;			
		redisKey = RedisPrefixConfig.adminSession_web_keyPrefix+ userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 1);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString13 = userId + DatabaseConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(adminSession_web_authCodeLength) + DatabaseConfig.redisSeperator + badTimeStamp;
		redis.set(redisKey, sessionString13);

		userId++;		
		redisKey = RedisPrefixConfig.adminSession_web_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.DAY_OF_WEEK_IN_MONTH, -5);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString14 = userId + DatabaseConfig.redisSeperator +RandomStringUtils.randomAlphanumeric(adminSession_web_authCodeLength) + DatabaseConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString14);

		EduDaoBasic.returnJedis(redis);

		SessionCleaner.clean();		

		redis = EduDaoBasic.getJedis();
		Set<String> keys;		
		int itr;

		/* UserModule*/

		//UserAuth
		itr = 0;
		keys = redis.keys(RedisPrefixConfig.userSession_web_keyPrefix+"*");
		if(keys.size() != 1){
			fail();
		}
		for(String key : keys){
			String sessionString = redis.get(key);
			if(itr==0){
				if(!sessionString.equals(sessionString2))fail();
			}	
			itr++;
		}

		//UserBooking		
		keys = redis.keys(RedisPrefixConfig.userBookingVerification_keyPrefix+"*");	
		if(keys.size() != 0){
			fail();
		}		
		//UserCell
		itr = 0;
		keys = redis.keys(RedisPrefixConfig.userCellVerification_keyPrefix+"*");	
		if(keys.size() != 1){
			fail();
		}
		for(String key : keys){
			String sessionString = redis.get(key);
			if(itr==0){
				if(!sessionString.equals(sessionString6))fail();
			}		
			itr++;
		}
		//UserForgotPassword
		itr = 0;
		keys = redis.keys(RedisPrefixConfig.userForgotPassword_keyPrefix+"*");		
		if(keys.size() != 1){
			fail();
		}
		for(String key : keys){
			String sessionString = redis.get(key);
			if(itr==0){
				if(!sessionString.equals(sessionString7))fail();
			}	
			itr++;
		}

		/* PartnerModule */

		//PartnerAuth
		itr = 0;
		keys = redis.keys(RedisPrefixConfig.partnerSession_web_keyPrefix+"*");		
		if(keys.size() != 1){
			fail();
		}
		for(String key : keys){
			String sessionString = redis.get(key);
			if(itr==0){
				if(!sessionString.equals(sessionString9))fail();
			}	
			itr++;
		}
		//PartnerForgotPassword
		itr = 0;
		keys = redis.keys(RedisPrefixConfig.partnerForgotPassword_keyPrefix+"*");		
		if(keys.size() != 1){
			fail();
		}
		for(String key : keys){
			String sessionString = redis.get(key);
			if(itr==0){
				if(!sessionString.equals(sessionString11))fail();
			}	
			itr++;
		}

		/* AdminModule */

		//AdminAuth
		itr = 0;
		keys = redis.keys(RedisPrefixConfig.adminSession_web_keyPrefix+"*");		
		if(keys.size() != 1){
			fail();
		}
		for(String key : keys){
			String sessionString = redis.get(key);
			if(itr==0){
				if(!sessionString.equals(sessionString13))fail();
			}	
			itr++;
		}

		EduDaoBasic.returnJedis(redis);


	}

}
