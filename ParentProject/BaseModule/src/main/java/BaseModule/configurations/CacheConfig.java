package BaseModule.configurations;

import java.util.ArrayList;

public final class CacheConfig {
	
	public static final int course_expireTime = 1800;		//30 minutes, in seconds
	public static final String course_keyPrefix = "course_";
	
	public static final int courseSearch_expireTime = 900;	//15 minutes, in seconds
	
	
	public static String castIdListToValue(final ArrayList<Integer> idList){
		if (idList == null || idList.size() == 0){
			return "";
		}
		StringBuffer str_buff = new StringBuffer();
		int i = 0;
		for (i = 0; i < idList.size()-1; i++){
			str_buff.append( String.valueOf(idList.get(i)) + "-" );
		}
		str_buff.append( String.valueOf(idList.get(i)) );
		return str_buff.toString();
	}
	
	
	public static ArrayList<Integer> castIdListFromValue(final String value){
		if (value == null || value.length() == 0){
			return new ArrayList<Integer>();
		}
		ArrayList<Integer> idList = new ArrayList<Integer>();
		for (String id : value.split("-")){
			idList.add(Integer.parseInt(id));
		}
		return idList;
	}
	
	

}
