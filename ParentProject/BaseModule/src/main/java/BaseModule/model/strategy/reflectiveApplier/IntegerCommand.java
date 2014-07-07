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

public class IntegerCommand implements PseudoReflectiveCommand {
	
	@Override
	public void toJSON(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception{
		int number = field.getInt(model);
		jsonRepresentation.put(field.getName(), number);
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
			field.setInt(model, jsonModel.getInt(key));
		}
	}

	@Override
	public void storeJSONList(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception{
		String key = field.getName();
		if (jsonModel.has(key)){
			JSONArray jsonArr = jsonModel.getJSONArray(key);
			ArrayList<Integer> realList = new ArrayList<Integer>();
			for (int i = 0; i < jsonArr.length(); i++){
				realList.add(jsonArr.getInt(i));
			}
			field.set(model, realList);
		}
	}

	@Override
	public void storeKvps(final Field field, final PseudoModel model, final Map<String, String> kvps) throws Exception{
		String value = EncodingService.decodeURI(kvps.get(field.getName()));
		if (value != null){
			field.setInt(model, Integer.parseInt(value, 10));
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
		
		ArrayList<Integer> realList = new ArrayList<Integer>();
		for (String singleValue : valueList){
			realList.add(Integer.parseInt(singleValue, 10));
		}
		field.set(model, realList);
	}

	@Override
	public void initialize(Field field, PseudoModel model) throws Exception {
		field.set(model, 0);
		
	}

	@Override
	public void initializeList(Field field, PseudoModel model) throws Exception {
		ArrayList<Integer> realList = new ArrayList<Integer>();
		field.set(model, realList);
	}

}
