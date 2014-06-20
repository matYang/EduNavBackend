package BaseModule.service;

public final class RedisUtilityService {
	
	public static boolean isValuedStored(final String previousRecord){
		return (previousRecord != null && previousRecord.length() != 0 && !previousRecord.equals("nil"));
	}
}
