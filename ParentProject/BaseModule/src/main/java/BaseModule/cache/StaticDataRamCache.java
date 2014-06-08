package BaseModule.cache;

import org.json.JSONArray;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;

public class StaticDataRamCache {
	//just an fyi...by default reads and writes of long are not atomic in java, however volatile could change that
	private static volatile JSONArray locationDataCache = null;
	private static volatile long locationCacheTimeStamp = 0l;
	private static final long locationCacheExpireTime = 600000l;		//10 min 
	
	private static volatile JSONArray catDataCache = null;
	private static volatile long catCacheTimeStamp = 0l;
	private static final long catCacheExpireTime = 600000l;		//10 min 
	
	
	public synchronized static void setLocationData(JSONArray locationData){
		long curTime = DateUtility.getCurTime();
		locationCacheTimeStamp = curTime;
		locationDataCache = new JSONArray(locationData.toString());
	}
	
	public synchronized static void setCatData(JSONArray catData){
		long curTime = DateUtility.getCurTime();
		catCacheTimeStamp = curTime;
		catDataCache = new JSONArray(catData.toString());
	}
	
	public static JSONArray getLocationData(){
		//expired
		if (DateUtility.getCurTime() - locationCacheTimeStamp > locationCacheExpireTime){
			return null;
		}
		if (locationDataCache == null){
			return null;
		}
		try{
			return new JSONArray(locationDataCache.toString());
		} catch (Exception e){
			DebugLog.d(e);
			return null;
		}
	}
	
	public static JSONArray getCatData(){
		//expired
		if (DateUtility.getCurTime() - catCacheTimeStamp > catCacheExpireTime){
			return null;
		}
		if (catDataCache == null){
			return null;
		}
		try{
			return new JSONArray(catDataCache.toString());
		} catch (Exception e){
			DebugLog.d(e);
			return null;
		}
	}
	

	//Warning: setting the cache to null breaks thread safety, could result in null pointer exceptions, once initialized cache should never be null
	public synchronized static void clear(){
		locationDataCache = null;
		locationCacheTimeStamp = 0l;
		catDataCache = null;
		catCacheTimeStamp = 0l;
	}

}
