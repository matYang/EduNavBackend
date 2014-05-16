package BaseModule.dataLoaderTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.junit.Test;

import BaseModule.staticDataService.CatDataLoader;
import BaseModule.staticDataService.LocationDataLoader;
import BaseModule.staticDataService.PDataLoader;
import BaseModule.staticDataService.StaticDataService;

public class DataLoaderTest {

	@Test
	public void catMapTest() {
		CatDataLoader.load();
		LinkedHashMap<String, ArrayList<String>> catDataMap = StaticDataService.getCatDataMap();
		System.out.println(catDataMap);
	}
	
	@Test
	public void locationMapTest() {
		LocationDataLoader.load();
		LinkedHashMap<String, ArrayList<String>> locationDataMap = StaticDataService.getLocationDataMap();
		System.out.println(locationDataMap);
	}
	
	@Test
	public void pListTest() {
		PDataLoader.load();
		List<String> pDataList = StaticDataService.getPDataList();
		System.out.println(pDataList);
	}
	
	@Test
	public void catJsonTest() {
		CatDataLoader.load();
		JSONArray catDataArr = StaticDataService.getCatDataJSON();
		System.out.println(catDataArr);
	}
	
	@Test
	public void locationJsonTest() {
		LocationDataLoader.load();
		JSONArray locationDataArr = StaticDataService.getLocationDataJSON();
		System.out.println(locationDataArr);
	}
	
	@Test
	public void pJsonTest() {
		PDataLoader.load();
		JSONArray pDataArr = StaticDataService.getPDataJSON();
		System.out.println(pDataArr);
	}

}
