package BaseModule.staticDataService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import BaseModule.common.DebugLog;


public class PDataLoader {
	
	private static final String pathToFile = "src/main/resources/PData";
	private static final ArrayList<String> pData = new ArrayList<String>();
	
	public static void load(){
		
		DebugLog.d("Starting to load pData: " + pathToFile);
		BufferedReader br = null;
		pData.clear(); 
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(pathToFile));
 
			while ((sCurrentLine = br.readLine()) != null) {
				pData.add(sCurrentLine);
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

		StaticDataService.storePData(pData);
		DebugLog.d("pData loaded succesfully");
	}
}
