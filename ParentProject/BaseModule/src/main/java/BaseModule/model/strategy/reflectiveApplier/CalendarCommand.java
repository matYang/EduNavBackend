package BaseModule.model.strategy.reflectiveApplier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoReflectiveCommand;
import BaseModule.service.EncodingService;

public class CalendarCommand implements PseudoReflectiveCommand {
	
	@Override
	public void toJSON(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception{
		Object value = field.get(model);
		if (value != null){
			jsonRepresentation.put(field.getName(),  DateUtility.castToAPIFormat((Calendar) value)  );
		}
	}
	
	@Override
	public void toJSONList(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception{
		Object value = field.get(model);
		List<?> list = (List<?>)value;
		
		ArrayList<String> valArr = new ArrayList<String>();
		for (Object o : list){
			valArr.add(EncodingService.encodeURI( DateUtility.castToAPIFormat((Calendar)o) ));
		}
		jsonRepresentation.put(field.getName(),  new JSONArray(valArr) );
	}
	
	@Override
	public void storeJSON(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception{
		String key = field.getName();
		if (jsonModel.has(key)){
			field.set(model, DateUtility.castFromAPIFormat(EncodingService.decodeURI(jsonModel.getString(key))));
		}
	}
	
	@Override
	public void storeJSONList(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception{
		String key = field.getName();
		if (jsonModel.has(key)){
			JSONArray jsonArr = jsonModel.getJSONArray(key);
			ArrayList<Calendar> realList = new ArrayList<Calendar>();
			for (int i = 0; i < jsonArr.length(); i++){
				realList.add(DateUtility.castFromAPIFormat(EncodingService.decodeURI(jsonArr.getString(i))));
			}
			field.set(model, realList);
		}
	}
	
	@Override
	public void storeKvps(final Field field, final PseudoModel model, final Map<String, String> kvps) throws Exception{
		String value = EncodingService.decodeURI(kvps.get(field.getName()));
		if (value != null){
			field.set(model, DateUtility.castFromAPIFormat(value));
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
		
		ArrayList<Calendar> realList = new ArrayList<Calendar>();
		for (String singleValue : valueList){
			realList.add(DateUtility.castFromAPIFormat(singleValue));
		}
		field.set(model, realList);
	}

	@Override
	public void initialize(Field field, PseudoModel model) throws Exception {
		field.set(model, DateUtility.getCurTimeInstance());
	}

	@Override
	public void initializeList(Field field, PseudoModel model) throws Exception {
		ArrayList<Calendar> realList = new ArrayList<Calendar>();
		field.set(model, realList);
	}


}
