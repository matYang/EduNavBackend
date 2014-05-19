package BaseModule.factory;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import BaseModule.common.DebugLog;
import BaseModule.interfaces.PseudoModel;
import BaseModule.model.Booking;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.User;
import BaseModule.model.representation.CourseSearchRepresentation;

public class JSONFactory {
	
	public static JSONObject toJSON(PseudoModel obj){
		if (obj == null){
			DebugLog.d("JSONFactory::toJSON_Model receving null obj");
			return new JSONObject();
		}
		else if (obj instanceof User){
			return ((User)obj).toJSON();
		}
		else if (obj instanceof Partner){
			return ((Partner)obj).toJSON();
		}
		else if (obj instanceof Course){
			return ((Course)obj).toJSON();
		}
		else if (obj instanceof Booking){
			return ((Booking)obj).toJSON();
		}
		else if (obj instanceof CourseSearchRepresentation){
			return ((CourseSearchRepresentation)obj).toJSON();
		}
		else{
			return new JSONObject();
		}
	}
	
	public static JSONArray toJSON(ArrayList<? extends PseudoModel> objs){
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
