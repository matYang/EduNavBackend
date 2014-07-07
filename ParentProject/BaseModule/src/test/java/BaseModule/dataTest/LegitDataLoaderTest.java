package BaseModule.dataTest;

import org.junit.Test;
import BaseModule.service.LegitLoadService;
import BaseModule.staticDataService.SystemDataInit;

public class LegitDataLoaderTest {
	
	@Test
	public void testLegitLoad(){
		LegitLoadService.legitLoad();
		SystemDataInit.init();
	}

}
