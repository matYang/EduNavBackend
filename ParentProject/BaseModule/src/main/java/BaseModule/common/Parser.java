package BaseModule.common;

import java.util.ArrayList;

public class Parser {

	public static String listToString(ArrayList<?> list){
		String serializedList = null;
		for(int i=0; i <list.size(); i++){
			if (serializedList == null){
				serializedList = "";
			}
			serializedList += list.get(i).toString() +"-";
		}
		return serializedList;
	}
	
	public static ArrayList<?> stringToList(String listString, Object optionFlag){
		String[] strArray = listString != null ? listString.split("-") : null;
		if (optionFlag instanceof Integer){
			ArrayList<Integer> intList = new ArrayList<Integer>();

			for (int i = 0; strArray != null && i < strArray.length; i++){
				intList.add(new Integer(strArray[i]));
			}
			return intList;
		}
		else if (optionFlag instanceof String){
			ArrayList<String> strList = new ArrayList<String>();
			for (int i = 0; strArray != null && i < strArray.length; i++){
				strList.add(strArray[i]);
			}
			return strList;
		}
		
		return null;
	}
}