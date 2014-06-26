package BaseModule.generator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import BaseModule.common.DebugLog;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;

public class JSONGenerator {
	
	public static JSONObject toJSON(final PseudoModel obj) throws Exception{
		if (obj == null){
			return new JSONObject();
		}
		try{
			return obj.toJSON();
		} catch (Exception e){
			DebugLog.d(e);
			throw new ValidationException("数据格式错误");
		}
	}
	
	public static JSONArray toJSON(final List<? extends PseudoModel> objs) throws Exception{
		ArrayList<JSONObject> temps = new ArrayList<JSONObject>();
		if (objs == null){
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
