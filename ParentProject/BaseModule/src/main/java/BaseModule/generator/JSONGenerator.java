package BaseModule.generator;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import BaseModule.common.DebugLog;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.model.*;
import BaseModule.model.representation.*;

public class JSONGenerator {
	
	public static JSONObject toJSON(final PseudoModel obj) throws ValidationException{
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
	
	public static JSONArray toJSON(final ArrayList<? extends PseudoModel> objs) throws ValidationException{
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
