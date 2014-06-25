package BaseModule.configurations;

import BaseModule.model.dataObj.RedisAccessControlObj;

public final class RedisAccessControlConfig {
	
	/* RedisSperator*/
	public static final String redisAccessControlSeperator = "+";
	public static final String redisAccessControlRegex = "\\+";
	
	/* UserModule */
	public static final String userAccessControl_keyPrefix = "userAccessControl-";
	public static final int userAccessControl_lockCount = 5;
	public static final long userAccessControl_lockThreshold = 60000l;		//1 min
	
	
	/* PartnerModule */
	public static final String partnerAccessControl_keyPrefix = "partnerAccessControl-";
	public static final int partnerAccessControl_lockCount = 3;
	public static final long partnerAccessControl_lockThreshold = 120000l;	//1 min
	
	
	/* AdminModule */
	public static final String adminAccessControl_keyPrefix = "adminAccessControl-";
	public static final int adminAccessControl_lockCount = 3;
	public static final long adminAccessControl_lockThreshold = 300000l;	//5 min
	
	public static RedisAccessControlObj getConfig(final String moduleId){
		final RedisAccessControlObj config = new RedisAccessControlObj();
		if (moduleId.equals("userModule")){
			config.keyPrefix = userAccessControl_keyPrefix;
			config.lockCount = userAccessControl_lockCount;
			config.lockThreshold = userAccessControl_lockThreshold;
		}
		else if (moduleId.equals("partnerModule")){
			config.keyPrefix = partnerAccessControl_keyPrefix;
			config.lockCount = partnerAccessControl_lockCount;
			config.lockThreshold = partnerAccessControl_lockThreshold;
		}
		else if (moduleId.equals("adminModule")){
			config.keyPrefix = adminAccessControl_keyPrefix;
			config.lockCount = adminAccessControl_lockCount;
			config.lockThreshold = adminAccessControl_lockThreshold;
		}
		else{
			throw new RuntimeException("RedisPrefixConfig unable to determine server identifer type");
		}
		return config;
	}
	
	
}
