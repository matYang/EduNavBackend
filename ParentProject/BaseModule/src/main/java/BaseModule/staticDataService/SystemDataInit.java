package BaseModule.staticDataService;

public final class SystemDataInit {
	
	public static void init(){
		CatDataLoader.load();
		LocationDataLoader.load();		
	}

}
