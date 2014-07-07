package BaseModule.dataTest;

import static org.junit.Assert.fail;

import org.json.JSONObject;
import org.junit.Test;

import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.service.ModelDataLoaderService;
import BaseModule.staticDataService.CatDataLoader;
import BaseModule.staticDataService.LocationDataLoader;
import BaseModule.staticDataService.SDService;
import BaseModule.staticDataService.SystemDataInit;

public class DataLoaderTest {

	
	@Test
	public void catJsonTest() {
		CatDataLoader.load();
		JSONObject catDataArr = SDService.getCatDataJSON();
		System.out.println(catDataArr);
	}
	
	@Test
	public void locationJsonTest() {
		LocationDataLoader.load();
		JSONObject locationDataArr = SDService.getLocationDataJSON();
		System.out.println(locationDataArr);
	}
	
	@Test
	public void modelLoaderTest(){
		try{
			ModelDataLoaderService.load();
			SystemDataInit.init();
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void catSDTreeTest(){
		CatDataLoader.load();
		System.out.println(SDService.getCatTree().toString());
	}
	
	@Test
	public void locationSDTreeTest(){
		LocationDataLoader.load();
		System.out.println(SDService.getLocationTree().toString());
	}

}
