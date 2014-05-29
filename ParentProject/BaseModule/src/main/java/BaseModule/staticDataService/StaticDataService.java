package BaseModule.staticDataService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

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
			jedis.del(catDataRedisKey);
			jedis.rpush(catDataRedisKey, catDataArray);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
	}
	
	public static void storeLocationData(ArrayList<String> locationData){
		Jedis jedis = EduDaoBasic.getJedis();
		
		try{
			String[] locationDataArray = new String[locationData.size()];
			locationDataArray = locationData.toArray(locationDataArray);
			jedis.del(locationDataRedisKey);
			jedis.rpush(locationDataRedisKey, locationDataArray);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
	}
	
	
	
	
	public static LinkedHashMap<String, ArrayList<String>> getCatDataMap(){
		Jedis jedis = EduDaoBasic.getJedis();
		List<String> catDataList;
		try{
			catDataList = jedis.lrange(catDataRedisKey, 0, jedis.llen(catDataRedisKey)-1);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		
		LinkedHashMap<String, ArrayList<String>> catDataMap = new LinkedHashMap<String, ArrayList<String>>();
		
		for (String singleCat : catDataList){
			JSONObject singleCatJson = new JSONObject(singleCat);
			String catKey = "";
			ArrayList<String> singleCatSubCat = new ArrayList<String>();
			
			Set<String> keys = singleCatJson.keySet();
			JSONArray singleCatJsonArr = null;
			for (String key: keys){
				catKey = key;
				singleCatJsonArr = singleCatJson.getJSONArray(key);
			}
			for (int i = 0; i < singleCatJsonArr.length(); i++){
				singleCatSubCat.add(singleCatJsonArr.getString(i));
			}
			catDataMap.put(catKey, singleCatSubCat);
		}
		
		return catDataMap;
	}
	
	public static LinkedHashMap<String, ArrayList<String>> getLocationDataMap(){
		Jedis jedis = EduDaoBasic.getJedis();
		List<String> locationDataList;
		try{
			locationDataList = jedis.lrange(locationDataRedisKey, 0, jedis.llen(locationDataRedisKey)-1);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}

		
		LinkedHashMap<String, ArrayList<String>> locationDataMap = new LinkedHashMap<String, ArrayList<String>>();
		
		for (String singleLocation : locationDataList){
			JSONObject singleLocationJson = new JSONObject(singleLocation);
			String locationKey = "";
			ArrayList<String> singleLocationSubLocation = new ArrayList<String>();
			
			Set<String> keys = singleLocationJson.keySet();
			JSONArray singleLocationJsonArr = null;
			for (String key: keys){
				locationKey = key;
				singleLocationJsonArr = singleLocationJson.getJSONArray(key);
			}
			for (int i = 0; i < singleLocationJsonArr.length(); i++){
				singleLocationSubLocation.add(singleLocationJsonArr.getString(i));
			}
			locationDataMap.put(locationKey, singleLocationSubLocation);
		}
		return locationDataMap;
	}
	
	
	
	public static JSONArray getCatDataJSON(){
		Jedis jedis = EduDaoBasic.getJedis();
		List<String> catDataList;
		try{
			catDataList = jedis.lrange(catDataRedisKey, 0, jedis.llen(catDataRedisKey)-1);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		
		JSONArray catDataArr = new JSONArray();
		
		for (String singleCat : catDataList){
			JSONObject singleCatJson = new JSONObject(singleCat);
			catDataArr.put(singleCatJson);
		}
		
		return catDataArr;
	}
	
	public static JSONArray getLocationDataJSON(){
		Jedis jedis = EduDaoBasic.getJedis();
		List<String> locationDataList;
		try{
			locationDataList = jedis.lrange(locationDataRedisKey, 0, jedis.llen(locationDataRedisKey)-1);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		
		JSONArray locationDataArr = new JSONArray();
		for (String singleLocation : locationDataList){
			JSONObject singleLocationJson = new JSONObject(singleLocation);
			locationDataArr.put(singleLocationJson);
		}
		return locationDataArr;
	}
	

}
