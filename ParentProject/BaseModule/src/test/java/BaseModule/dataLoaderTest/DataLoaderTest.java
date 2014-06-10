package BaseModule.dataLoaderTest;

import static org.junit.Assert.fail;

import org.json.JSONObject;
import org.junit.Test;

import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.service.ModelDataLoaderService;
import BaseModule.staticDataService.CatDataLoader;
import BaseModule.staticDataService.LocationDataLoader;
import BaseModule.staticDataService.StaticDataService;
import BaseModule.staticDataService.SystemDataInit;

public class DataLoaderTest {

	
	@Test
	public void catJsonTest() {
		CatDataLoader.load();
		JSONObject catDataArr = StaticDataService.getCatDataJSON();
		System.out.println(catDataArr);
	}
	
	@Test
	public void locationJsonTest() {
		LocationDataLoader.load();
		JSONObject locationDataArr = StaticDataService.getLocationDataJSON();
		System.out.println(locationDataArr);
	}
	
	@Test
	public void modelLoaderTest(){
		EduDaoBasic.clearAllDatabase();
		try{
			ModelDataLoaderService.load();
			SystemDataInit.init();
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}

}
