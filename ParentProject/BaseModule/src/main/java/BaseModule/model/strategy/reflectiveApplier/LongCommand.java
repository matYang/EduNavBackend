package BaseModule.model.strategy.reflectiveApplier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoReflectiveCommand;
import BaseModule.service.EncodingService;

public class LongCommand implements PseudoReflectiveCommand {
	
	public void toJSON(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception{
		long number = field.getLong(model);
		jsonRepresentation.put(field.getName(), number);
	}
	
	public void toJSONList(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception{
		Object value = field.get(model);
		List<?> list = (List<?>)value;
		
		jsonRepresentation.put(field.getName(),  new JSONArray(list) );
	}
	
	public void storeJSON(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception{
		String key = field.getName();
		if (jsonModel.has(key)){
			field.setLong(model, jsonModel.getLong(key));
		}
	}
	
	public void storeJSONList(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception{
		String key = field.getName();
		if (jsonModel.has(key)){
			JSONArray jsonArr = jsonModel.getJSONArray(key);
			ArrayList<Long> realList = new ArrayList<Long>();
			for (int i = 0; i < jsonArr.length(); i++){
				realList.add(jsonArr.getLong(i));
			}
			field.set(model, realList);
		}
	}
	
	public void storeKvps(final Field field, final PseudoModel model, final Map<String, String> kvps) throws Exception{
		String value = EncodingService.decodeURI(kvps.get(field.getName()));
		if (value != null){
			field.setLong(model, Long.parseLong(value, 10));
		}
	}
	
	public void storeKvpsList(final Field field, final PseudoModel model, final Map<String, String> kvps) throws Exception{
		String keyBase = field.getName();
		
		//if the field is a list, generating all the strings associated with that list, assuming all keys have format: key[i]
		ArrayList<String> valueList = new ArrayList<String>();
		for (int i = 1; kvps.get(keyBase + i) != null; i++){
			valueList.add(EncodingService.decodeURI(kvps.get(keyBase + i)));
		}
		
		ArrayList<Long> realList = new ArrayList<Long>();
		for (String singleValue : valueList){
			realList.add(Long.parseLong(singleValue, 10));
		}
		field.set(model, realList);
	}


}
