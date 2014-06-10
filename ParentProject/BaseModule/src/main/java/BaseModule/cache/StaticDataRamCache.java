package BaseModule.cache;

import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;

public class StaticDataRamCache {
	//just an fyi...by default reads and writes of long are not atomic in java, however volatile could change that
	private static volatile JSONObject locationDataCache = null;
	private static volatile long locationCacheTimeStamp = 0l;
	private static final long locationCacheExpireTime = 600000l;		//10 min 
	
	private static volatile JSONObject catDataCache = null;
	private static volatile long catCacheTimeStamp = 0l;
	private static final long catCacheExpireTime = 600000l;		//10 min 
	
	/**
	 * Thread safety logic:
	 * read / writes of references and 32 bit primitive types are atomic, read and writes of volatile longs are atomic
	 * reads of contents of JSONObject may not be atomic, but there are no writes so it is thread safe
	 * reference of locationDataCache and catDataCache will never be null once the cache is initialized, thus there will be no null pointer exceptions as long as clear() is not called concurrently
	 * setters are synchronized to ensure the synchronization of time stamp and cache
	 * if the rare event of exception occur, in writes exception will be caught to avoid any side effect, in reads null is returned to force a fetch from Redis
	 * all of above assures data availability and integrity
	 */
	
	public synchronized static void setLocationData(JSONObject locationData){
		try{
			long curTime = DateUtility.getCurTime();
			locationCacheTimeStamp = curTime;
			locationDataCache = new JSONObject(locationData.toString());
		} catch (Exception e){
			DebugLog.d(e);
		}
	}
	
	public synchronized static void setCatData(JSONObject catData){
		try{
			long curTime = DateUtility.getCurTime();
			catCacheTimeStamp = curTime;
			catDataCache = new JSONObject(catData.toString());
		}
		catch (Exception e){
			DebugLog.d(e);
		}
	}
	
	public static JSONObject getLocationData(){
		try{
			//expired
			if (DateUtility.getCurTime() - locationCacheTimeStamp > locationCacheExpireTime){
				return null;
			}
			if (locationDataCache == null){
				return null;
			}
			
			return new JSONObject(locationDataCache.toString());
		} catch (Exception e){
			DebugLog.d(e);
			return null;
		}
	}
	
	public static JSONObject getCatData(){
		try{
			//expired
			if (DateUtility.getCurTime() - catCacheTimeStamp > catCacheExpireTime){
				return null;
			}
			if (catDataCache == null){
				return null;
			}
			
			return new JSONObject(catDataCache.toString());
		} catch (Exception e){
			DebugLog.d(e);
			return null;
		}
	}
	

	//Warning: setting the cache to null breaks thread safety guarantee, could result in null pointer exceptions, once initialized cache should never be null
	public synchronized static void clear(){
		locationDataCache = null;
		locationCacheTimeStamp = 0l;
		catDataCache = null;
		catCacheTimeStamp = 0l;
	}

}
