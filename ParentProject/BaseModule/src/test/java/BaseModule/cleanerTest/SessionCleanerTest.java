package BaseModule.cleanerTest;

import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import BaseModule.clean.cleanTasks.SessionCleaner;
import BaseModule.common.DateUtility;
import BaseModule.configurations.RedisAuthenticationConfig;
import BaseModule.eduDAO.EduDaoBasic;

public class SessionCleanerTest {

	private static final int userSession_web_authCodeLength = 15;
	private static final int partnerSession_web_authCodeLength = 15;
	private static final int adminSession_web_authCodeLength = 15;
	private static final int userChangePasswordVerification_authCodeLength = 6;
	private static final int userCellVerification_authCodeLength = 6;
	private static final int userForgotPassword_authCodeLength = 8;
	private static final int partnerForgotPassword_authCodeLength = 8;
	private static final int partnerChangePasswordVerification_authCodeLength = 6;

	@Test
	public void test(){
		EduDaoBasic.clearBothDatabase();
		Jedis redis = EduDaoBasic.getJedis();
		Calendar badTime;
		long badTimeStamp;
		String nowTimeStamp = DateUtility.getTimeStamp();
		//UserAuthOpenSession
		int userId = 1;		
		String redisKey = RedisAuthenticationConfig.userSession_web_keyPrefix + userId;
		String sessionString0 = userId + RedisAuthenticationConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(userSession_web_authCodeLength) + RedisAuthenticationConfig.redisSeperator + nowTimeStamp;		
		redis.set(redisKey, sessionString0);

		userId++;		
		redisKey = RedisAuthenticationConfig.userSession_web_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.DAY_OF_YEAR, -8);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString1 = userId + RedisAuthenticationConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(userSession_web_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString1);

		userId++;		
		redisKey = RedisAuthenticationConfig.userSession_web_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 5);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString2 = userId + RedisAuthenticationConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(userSession_web_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString2);

		//UserChangePasswordOpenSession
		userId++;			
		redisKey = RedisAuthenticationConfig.userChangePasswordVerification_keyPrefix + userId;
		String sessionString3 = RandomStringUtils.randomAlphanumeric(userChangePasswordVerification_authCodeLength).toUpperCase() + RedisAuthenticationConfig.redisSeperator + nowTimeStamp;
		redis.set(redisKey, sessionString3);

		userId++;		
		redisKey = RedisAuthenticationConfig.userChangePasswordVerification_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.DAY_OF_YEAR, -19);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString4 = RandomStringUtils.randomAlphanumeric(userChangePasswordVerification_authCodeLength).toUpperCase() + RedisAuthenticationConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString4);

		//UserCellOpenSession
		userId++;			
		redisKey = RedisAuthenticationConfig.userCellVerification_keyPrefix + userId;
		String sessionString5 = RandomStringUtils.randomAlphanumeric(userCellVerification_authCodeLength).toUpperCase() + RedisAuthenticationConfig.redisSeperator + nowTimeStamp;
		redis.set(redisKey, sessionString5);

		userId++;		
		redisKey = RedisAuthenticationConfig.userCellVerification_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.HOUR_OF_DAY, -7);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString6 = RandomStringUtils.randomAlphanumeric(userCellVerification_authCodeLength).toUpperCase() + RedisAuthenticationConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString6);

		//UserForgotPassword
		userId++;			
		redisKey = RedisAuthenticationConfig.userForgotPassword_keyPrefix+ userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 1);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString7 = RandomStringUtils.randomAlphanumeric(userForgotPassword_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;
		redis.set(redisKey, sessionString7);

		userId++;		
		redisKey = RedisAuthenticationConfig.userForgotPassword_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.HOUR_OF_DAY, -15);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString8 = RandomStringUtils.randomAlphanumeric(userForgotPassword_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString8);

		//PartnerAuth
		userId++;			
		redisKey = RedisAuthenticationConfig.partnerSession_web_keyPrefix+ userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 1);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString9 = userId + RedisAuthenticationConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(partnerSession_web_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;
		redis.set(redisKey, sessionString9);

		userId++;		
		redisKey = RedisAuthenticationConfig.partnerSession_web_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.DAY_OF_YEAR, -25);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString10 = userId + RedisAuthenticationConfig.redisSeperator +RandomStringUtils.randomAlphanumeric(partnerSession_web_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString10);

		//PartnerForgotPassword
		userId++;			
		redisKey = RedisAuthenticationConfig.partnerForgotPassword_keyPrefix+ userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 1);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString11 = RandomStringUtils.randomAlphanumeric(partnerForgotPassword_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;
		redis.set(redisKey, sessionString11);

		userId++;		
		redisKey = RedisAuthenticationConfig.partnerForgotPassword_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.HOUR_OF_DAY, -9);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString12 = RandomStringUtils.randomAlphanumeric(partnerForgotPassword_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString12);

		//PartnerChangePassword
		userId++;			
		redisKey = RedisAuthenticationConfig.partnerChangePasswordVerification_keyPrefix+ userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 1);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString13 = RandomStringUtils.randomAlphanumeric(partnerChangePasswordVerification_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;
		redis.set(redisKey, sessionString13);

		userId++;		
		redisKey = RedisAuthenticationConfig.partnerChangePasswordVerification_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.HOUR_OF_DAY, -9);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString14 = RandomStringUtils.randomAlphanumeric(partnerChangePasswordVerification_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString14);

		//AdminAuth
		userId++;			
		redisKey = RedisAuthenticationConfig.adminSession_web_keyPrefix+ userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.MINUTE, 1);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString15 = userId + RedisAuthenticationConfig.redisSeperator + RandomStringUtils.randomAlphanumeric(adminSession_web_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;
		redis.set(redisKey, sessionString15);

		userId++;		
		redisKey = RedisAuthenticationConfig.adminSession_web_keyPrefix + userId;
		badTime = DateUtility.getCurTimeInstance();
		badTime.add(Calendar.DAY_OF_WEEK_IN_MONTH, -10);
		badTimeStamp = badTime.getTimeInMillis();
		String sessionString16 = userId + RedisAuthenticationConfig.redisSeperator +RandomStringUtils.randomAlphanumeric(adminSession_web_authCodeLength) + RedisAuthenticationConfig.redisSeperator + badTimeStamp;		
		redis.set(redisKey, sessionString16);

		EduDaoBasic.returnJedis(redis);

		SessionCleaner.clean();		

		redis = EduDaoBasic.getJedis();
		Set<String> keys;		
		int itr;

		/* UserModule*/

		//UserAuth
		itr = 0;
		keys = redis.keys(RedisAuthenticationConfig.userSession_web_keyPrefix+"*");
		if(keys.size() != 2){
			fail();
		}
		for(String key : keys){
			String sessionString = redis.get(key);
			if(itr==0){
				if(!sessionString.equals(sessionString0))fail();
			}else if(itr==1){
				if(!sessionString.equals(sessionString2))fail();
			}
			itr++;
		}

		//UserChangePassword		
		keys = redis.keys(RedisAuthenticationConfig.userChangePasswordVerification_keyPrefix+"*");	
		if(keys.size() != 1){
			fail();
		}	
		for(String key : keys){
			String sessionString = redis.get(key);
			if(itr==0){
				if(!sessionString.equals(sessionString3))fail();
			}
			itr++;
		}
		//UserCell
		itr = 0;
		keys = redis.keys(RedisAuthenticationConfig.userCellVerification_keyPrefix+"*");	
		if(keys.size() != 1){
			fail();
		}
		for(String key : keys){
			String sessionString = redis.get(key);
			if(itr==0){
				if(!sessionString.equals(sessionString5))fail();
			}		
			itr++;
		}
		//UserForgotPassword
		itr = 0;
		keys = redis.keys(RedisAuthenticationConfig.userForgotPassword_keyPrefix+"*");		
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
		keys = redis.keys(RedisAuthenticationConfig.partnerSession_web_keyPrefix+"*");		
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
		keys = redis.keys(RedisAuthenticationConfig.partnerForgotPassword_keyPrefix+"*");		
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
		//PartnerChangePassword
		itr = 0;
		keys = redis.keys(RedisAuthenticationConfig.partnerChangePasswordVerification_keyPrefix+"*");		
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

		/* AdminModule */

		//AdminAuth
		itr = 0;
		keys = redis.keys(RedisAuthenticationConfig.adminSession_web_keyPrefix+"*");		
		if(keys.size() != 1){
			fail();
		}
		for(String key : keys){
			String sessionString = redis.get(key);
			if(itr==0){
				if(!sessionString.equals(sessionString15))fail();
			}	
			itr++;
		}

		EduDaoBasic.returnJedis(redis);


	}

}
