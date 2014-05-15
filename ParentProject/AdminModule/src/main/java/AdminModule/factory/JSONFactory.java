package AdminModule.factory;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import AdminModule.model.AdminAccount;
import BaseModule.common.DebugLog;

public class JSONFactory {
	public static JSONObject toJSON(AdminAccount obj){
		if (obj == null){
			DebugLog.d("JSONFactory::toJSON_Model receving null obj");
			return new JSONObject();
		}
		return obj.toJSON();
	}
	
	public static JSONArray toJSON(ArrayList<AdminAccount> objs){
		ArrayList<JSONObject> temps = new ArrayList<JSONObject>();
		if (objs == null){
			DebugLog.d("JSONFactory::toJSON_ArrayList receving null objs");
			return new JSONArray();
		}
		for (int i = 0; i < objs.size(); i++){
			if (objs.get(i) != null){
				JSONObject jsonResult = toJSON(objs.get(i));
				temps.add(jsonResult);

			}
		}
		return new JSONArray(temps);
	}

}
