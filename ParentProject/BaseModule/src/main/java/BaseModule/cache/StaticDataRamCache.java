package BaseModule.cache;

import org.json.JSONArray;
import BaseModule.common.DateUtility;

public class StaticDataRamCache {
	
	private static volatile JSONArray locationDataCache = null;
	private static volatile long locationCacheTimeStamp = 0l;
	private static final long locationCacheExpireTime = 600000l;		//10 min 
	
	private static volatile JSONArray catDataCache = null;
	private static volatile long catCacheTimeStamp = 0l;
	private static final long catCacheExpireTime = 600000l;		//10 min 
	
	
	public synchronized static void setLocationData(JSONArray locationData){
		long curTime = DateUtility.getCurTime();
		locationCacheTimeStamp = curTime;
		locationDataCache = new JSONArray(locationData);
	}
	
	public synchronized static void setCatData(JSONArray catData){
		long curTime = DateUtility.getCurTime();
		catCacheTimeStamp = curTime;
		catDataCache = new JSONArray(catData);
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
			return new JSONArray(locationDataCache);
		} catch (Exception e){
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
			return new JSONArray(catDataCache);
		} catch (Exception e){
			return null;
		}
	}
	

	
	public static void clear(){
		locationDataCache = null;
		locationCacheTimeStamp = 0l;
		catDataCache = null;
		catCacheTimeStamp = 0l;
	}

}
