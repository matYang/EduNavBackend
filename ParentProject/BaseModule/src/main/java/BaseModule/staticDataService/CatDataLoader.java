package BaseModule.staticDataService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import BaseModule.common.DebugLog;

public class CatDataLoader {
	private static final String pathToFile = "src/main/resources/CatData";
	private static final ArrayList<String> catData = new ArrayList<String>();
	
	public static void load(){
		
		DebugLog.d("Starting to load catData: " + pathToFile);
		BufferedReader br = null;
		catData.clear(); 
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(pathToFile));
 
			while ((sCurrentLine = br.readLine()) != null && sCurrentLine.length() > 0) {
				catData.add(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		StaticDataService.storeCatData(catData);
		DebugLog.d("catData loaded succesfully");
	}	
}
