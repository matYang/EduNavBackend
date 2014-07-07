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

public class BooleanCommand implements PseudoReflectiveCommand{
	
	@Override
	public void toJSON(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception{
		boolean val = field.getBoolean(model);
		jsonRepresentation.put(field.getName(), val);
	}
	
	@Override
	public void toJSONList(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception{
		Object value = field.get(model);
		List<?> list = (List<?>)value;
		
		jsonRepresentation.put(field.getName(),  new JSONArray(list) );
	}
	
	@Override
	public void storeJSON(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception{
		String key = field.getName();
		if (jsonModel.has(key)){
			field.setBoolean(model, jsonModel.getBoolean(key));
		}
	}
	
	@Override
	public void storeJSONList(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception{
		String key = field.getName();
		if (jsonModel.has(key)){
			JSONArray jsonArr = jsonModel.getJSONArray(key);
			ArrayList<Boolean> realList = new ArrayList<Boolean>();
			for (int i = 0; i < jsonArr.length(); i++){
				realList.add(jsonArr.getBoolean(i));
			}
			field.set(model, realList);
		}
	}
	
	@Override
	public void storeKvps(final Field field, final PseudoModel model, final Map<String, String> kvps) throws Exception{
		String value = EncodingService.decodeURI(kvps.get(field.getName()));
		if (value != null){
			field.setBoolean(model, Boolean.parseBoolean(value));
		}
	}
	
	@Override
	public void storeKvpsList(final Field field, final PseudoModel model, final Map<String, String> kvps) throws Exception{
		String keyBase = field.getName();
		
		//if the field is a list, generating all the strings associated with that list, assuming all keys have format: key[i]
		ArrayList<String> valueList = new ArrayList<String>();
		for (int i = 1; kvps.get(keyBase + i) != null; i++){
			valueList.add(EncodingService.decodeURI(kvps.get(keyBase + i)));
		}
		
		ArrayList<Boolean> realList = new ArrayList<Boolean>();
		for (String singleValue : valueList){
			realList.add(Boolean.parseBoolean(singleValue));
		}
		field.set(model, realList);
	}

	@Override
	public void initialize(Field field, PseudoModel model) throws Exception {
		field.setBoolean(model, false);
	}

	@Override
	public void initializeList(Field field, PseudoModel model) throws Exception {
		ArrayList<Boolean> realList = new ArrayList<Boolean>();
		field.set(model, realList);
	}

}
