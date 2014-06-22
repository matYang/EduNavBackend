package BaseModule.common;

import java.util.ArrayList;

public class Parser {

	public static String listToString(final ArrayList<?> list,String spliter){
		String serializedList = "";
		for(int i = 0; i < list.size(); i++){
			if (i == list.size() - 1){
				spliter = "";
			}
			serializedList += list.get(i).toString() + spliter;
		}
		return serializedList;
	}

	public static ArrayList<?> stringToList(final String listString, final String spliter, final Object optionFlag){	
		
		if (optionFlag instanceof Integer){
			ArrayList<Integer> intList = new ArrayList<Integer>();
			if (listString == null || listString.length() == 0){
				return intList;
			}
			String[] strArray = listString.split(spliter);
			for (String str : strArray){
				intList.add(Integer.parseInt(str));
			}
			return intList;
		}
		else if (optionFlag instanceof String){
			ArrayList<String> strList = new ArrayList<String>();
			if (listString == null || listString.length() == 0){
				return strList;
			}
			String[] strArray = listString.split(spliter);
			for (String str : strArray){
				strList.add(str);
			}
			return strList;
		}
		else{
			throw new RuntimeException("[ERR] Parser:: StringToList non-identifierable option flag");
		}
	}

	public static int getCashBackFromCouponRecord(final String crd){
		int cashback = 0;
		if(crd==null || crd.equals("")){
			return cashback;
		}
		
		String[] cashArray = crd.split("-");
		
		for(int i=0;i<cashArray.length;i++){
			cashback += Integer.parseInt(cashArray[i].split("_")[1]);
		}
		
		return cashback;
	}
}