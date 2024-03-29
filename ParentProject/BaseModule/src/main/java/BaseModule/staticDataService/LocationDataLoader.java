package BaseModule.staticDataService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import BaseModule.common.DebugLog;


public final class LocationDataLoader {
	
	private static final String pathToFile = "src/main/resources/LocationData";

	
	public static void load(){
		DebugLog.d("Starting to load locationData: " + pathToFile);
		BufferedReader br = null;
		ArrayList<String> locationData = new ArrayList<String>();
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(pathToFile));
 
			while ((sCurrentLine = br.readLine()) != null && sCurrentLine.length() > 0) {
				locationData.add(sCurrentLine);
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

		SDService.storeLocationData(locationData);
		DebugLog.d("locationData loaded succesfully");
	}	
}