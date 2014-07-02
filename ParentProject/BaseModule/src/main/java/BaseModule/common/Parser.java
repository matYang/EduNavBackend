package BaseModule.common;

import java.util.ArrayList;

public class Parser {

	public static <T> String listToString(final ArrayList<T> list,String spliter){
		String serializedList = "";
		for(int i = 0; i < list.size(); i++){
			if (i == list.size() - 1){
				spliter = "";
			}
			serializedList += list.get(i).toString() + spliter;
		}
		return serializedList;
	}

	public static <T> ArrayList<T> stringToList(final String listString, final String spliter, final Class<T> clazz){
		ArrayList<T> list = new ArrayList<T>();
		if (listString == null || listString.length() == 0){
			return list;
		}
		String[] strArray = listString.split(spliter);
		
		if (Integer.class.isAssignableFrom(clazz)){
			for (String str : strArray){
				list.add(clazz.cast(Integer.parseInt(str)));
			}
			return list;
		}
		else if (String.class.isAssignableFrom(clazz)){
			for (String str : strArray){
				list.add(clazz.cast(str));
			}
			return list;
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