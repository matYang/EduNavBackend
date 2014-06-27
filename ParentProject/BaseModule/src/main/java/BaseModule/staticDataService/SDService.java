package BaseModule.staticDataService;

import java.util.ArrayList;
import java.util.Set;

import org.json.JSONObject;

import BaseModule.cache.StaticDataRamCache;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.model.generic.SDTree;

import redis.clients.jedis.Jedis;

public final class SDService {
	
	private static String catDataRedisKey = "list_catData";
	private static String locationDataRedisKey = "list_locationData";
	
	public static void storeCatData(ArrayList<String> catData){
		Jedis jedis = null;
		
		try{
			jedis = EduDaoBasic.getJedis();
			
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
		Jedis jedis = null;
		
		try{
			jedis = EduDaoBasic.getJedis();
			
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
		
		Jedis jedis = null;
		String catDataString;
		try{
			jedis = EduDaoBasic.getJedis();
			
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
		
		Jedis jedis = null;
		String locationDataString;
		try{
			jedis = EduDaoBasic.getJedis();
			
			locationDataString = jedis.get(locationDataRedisKey);
		} finally{
			EduDaoBasic.returnJedis(jedis);
		}
		
		
		JSONObject locationDataObj = new JSONObject(locationDataString);

		//System.out.println("Cache miss, setting cache");
		StaticDataRamCache.setLocationData(locationDataObj);
		return locationDataObj;
	}
	
	private static void populateSDTree(JSONObject curObj, SDTree<String> parent){
		//counting in the "index" key in every which object
		if (curObj.keySet().size() <= 1){
			return;
		}
		Set<String> keyset = curObj.keySet();
		for (String key : keyset){
			if (!key.equals("index")){
				SDTree<String> node = parent.addLeaf(key);
				populateSDTree(curObj.getJSONObject(key), node);
			}
		}
	}
	
	
	private static SDTree<String> generateSDTree(JSONObject jsonObj){
		SDTree<String> root = new SDTree<String>("root");
		populateSDTree(jsonObj, root);
		return root;
	}
	
	public static SDTree<String> getCatTree(){
		JSONObject catObj = getCatDataJSON();
		return generateSDTree(catObj);
	}
	
	public static SDTree<String> getLocationTree(){
		JSONObject locationObj = getLocationDataJSON();
		return generateSDTree(locationObj);
	}

}
