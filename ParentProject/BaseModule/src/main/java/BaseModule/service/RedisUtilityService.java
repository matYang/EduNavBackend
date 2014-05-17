package BaseModule.service;

public class RedisUtilityService {
	
	public static boolean isValuedStored(String previousRecord){
		return (previousRecord != null && previousRecord.length() != 0 && previousRecord != "nil");
	}
}
