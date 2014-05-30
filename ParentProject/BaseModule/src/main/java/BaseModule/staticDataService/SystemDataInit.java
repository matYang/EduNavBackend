package BaseModule.staticDataService;

public class SystemDataInit {
	
	public static void init(){
		CatDataLoader.load();
		LocationDataLoader.load();		
	}

}
