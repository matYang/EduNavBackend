package BaseModule.staticDataService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import BaseModule.cache.StaticDataRamCache;
import BaseModule.eduDAO.EduDaoBasic;

import redis.clients.jedis.Jedis;

public class StaticDataService {
	
	private static String catDataRedisKey = "list_catData";
	private static String locationDataRedisKey = "list_locationData";
	
	public static void storeCatData(ArrayList<String> catData){
		Jedis jedis = EduDaoBasic.getJedis();
		
		try{
			String[] catDataArray = new String[catData.size()];
			catDataArray = catData.toArray(catDataArray);
			String catDataString = "";
			for (String str : catDataArray){
				catDataString += str;
			}
			jedis.del(catDataRedisKey);
			jedis.set(catDataRedisKey, catDataString);
			
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
	}
	
	public static void storeLocationData(ArrayList<String> locationData){
		Jedis jedis = EduDaoBasic.getJedis();
		
		try{
			String[] locationDataArray = new String[locationData.size()];
			locationDataArray = locationData.toArray(locationDataArray);
			String locationDataString = "";
			for (String str : locationDataArray){
				locationDataString += str;
			}
			jedis.del(locationDataRedisKey);
			jedis.set(locationDataRedisKey, locationDataString);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
	}
	
	
	public static JSONObject getCatDataJSON(){
		JSONObject catData = StaticDataRamCache.getCatData();
		if (catData != null){
			//System.out.println("Hitting cache");
			return catData;
		}
		
		Jedis jedis = EduDaoBasic.getJedis();
		String catDataString;
		try{
			catDataString = jedis.get(catDataRedisKey);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		JSONObject catDatObj = new JSONObject(catDataString);
		
		//System.out.println("Cache miss, setting cache");
		StaticDataRamCache.setCatData(catDatObj);
		return catDatObj;
	}
	
	public static JSONObject getLocationDataJSON(){
		JSONObject locationData = StaticDataRamCache.getLocationData();
		if (locationData != null){
			//System.out.println("Hitting cache");
			return locationData;
		}
		
		Jedis jedis = EduDaoBasic.getJedis();
		String locationDataString;
		try{
			locationDataString = jedis.get(locationDataRedisKey);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		
		JSONObject locationDataObj = new JSONObject(locationDataString);

		//System.out.println("Cache miss, setting cache");
		StaticDataRamCache.setLocationData(locationDataObj);
		return locationDataObj;
	}
	

}
