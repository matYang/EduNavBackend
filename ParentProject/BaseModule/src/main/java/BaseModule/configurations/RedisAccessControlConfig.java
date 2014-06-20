package BaseModule.configurations;

import BaseModule.model.configObj.RedisAccessControlObj;

public final class RedisAccessControlConfig {
	
	/* RedisSperator*/
	public static final String redisAccessControlSeperator = "+";
	public static final String redisAccessControlRegex = "\\+";
	
	/* UserModule */
	public static final String userAccessControl_keyPrefix = "userAccessControl-";
	public static final int userAccessControl_lockCount = 5;
	public static final long userAccessControl_lockThreshold = 60000l;		//1 min
	public static final long userAccessControl_releaseThreshold = 120000l;	//2 min
	
	
	/* PartnerModule */
	public static final String partnerAccessControl_keyPrefix = "partnerAccessControl-";
	public static final int partnerAccessControl_lockCount = 3;
	public static final long partnerAccessControl_lockThreshold = 60000l;	//1 min
	public static final long partnerAccessControl_releaseThreshold = 150000l;//2.5 min
	
	
	/* AdminModule */
	public static final String adminAccessControl_keyPrefix = "adminAccessControl-";
	public static final int adminAccessControl_lockCount = 3;
	public static final long adminAccessControl_lockThreshold = 60000l;		//1 min
	public static final long adminAccessControl_releaseThreshold = 180000l;	//3 min
	
	public static RedisAccessControlObj getConfig(final int acServiceIdentifier){
		final RedisAccessControlObj config = new RedisAccessControlObj();
		switch (acServiceIdentifier){
			case 1:
				config.keyPrefix = userAccessControl_keyPrefix;
				config.lockCount = userAccessControl_lockCount;
				config.lockThreshold = userAccessControl_lockThreshold;
				config.releaseThreshould = userAccessControl_releaseThreshold;
				break;
				
			case 2:
				config.keyPrefix = partnerAccessControl_keyPrefix;
				config.lockCount = partnerAccessControl_lockCount;
				config.lockThreshold = partnerAccessControl_lockThreshold;
				config.releaseThreshould = partnerAccessControl_releaseThreshold;
				break;
				
			case 3:
				config.keyPrefix = adminAccessControl_keyPrefix;
				config.lockCount = adminAccessControl_lockCount;
				config.lockThreshold = adminAccessControl_lockThreshold;
				config.releaseThreshould = adminAccessControl_releaseThreshold;
				break;
				
			default:
				throw new RuntimeException("RedisPrefixConfig unable to determine server identifer type");
		}
		return config;
	}
	
	
}
