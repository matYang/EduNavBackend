package BaseModule.common;

import java.util.ArrayList;

public class Parser {

	public static String listToString(final ArrayList<?> list,String spliter){
		String serializedList = null;
		for(int i=0; i <list.size(); i++){
			if (serializedList == null){
				serializedList = "";
			}
			serializedList += list.get(i).toString() + spliter;
		}
		return serializedList;
	}

	public static ArrayList<?> stringToList(final String listString, final String spliter, final Object optionFlag){	
		
		String[] strArray = (listString == null || listString.length() == 0) ? null : listString.split(spliter);
		
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