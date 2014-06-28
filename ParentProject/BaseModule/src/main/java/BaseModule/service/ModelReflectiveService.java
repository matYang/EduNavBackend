package BaseModule.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.interfaces.PseudoEnum;
import BaseModule.interfaces.PseudoModel;

public final class ModelReflectiveService {
	
	private static Field[] getFields(final PseudoModel model){
		return model.getClass().getDeclaredFields();
	}
	
	/*******************
	 * 
	 * 	this part is used primarily by data models
	 * 
	 *******************/
	//provides toJSON support for virtually all PseudlModels - concrete models or representations
	public static JSONObject toJSON(final PseudoModel model) throws Exception {
		Field[] fields = getFields(model);
		JSONObject jsonRepresentation = new JSONObject();
		
		try{
			for (Field field : fields){
				field.setAccessible(true);
				Class<?> fieldClass = field.getType();
				if (int.class.isAssignableFrom(fieldClass) || Integer.class.isAssignableFrom(fieldClass)){
					int number = field.getInt(model);
					jsonRepresentation.put(field.getName(), number);
				}
				else if (long.class.isAssignableFrom(fieldClass) || Long.class.isAssignableFrom(fieldClass)){
					long number = field.getLong(model);
					jsonRepresentation.put(field.getName(), number);
				}
				else if (boolean.class.isAssignableFrom(fieldClass) || Boolean.class.isAssignableFrom(fieldClass)){
					boolean val = field.getBoolean(model);
					jsonRepresentation.put(field.getName(), val);
				}
				else if (String.class.isAssignableFrom(fieldClass)){
					Object value = field.get(model);
					if (value != null){
						jsonRepresentation.put(field.getName(), EncodingService.encodeURI((String) value));
					}
				}
				else if (Calendar.class.isAssignableFrom(fieldClass)){
					Object value = field.get(model);
					if (value != null){
						jsonRepresentation.put(field.getName(),  DateUtility.castToAPIFormat((Calendar) value)  );
					}
				}
				else if (PseudoEnum.class.isAssignableFrom(fieldClass)){
					Object value = field.get(model);
					if (value != null){
						jsonRepresentation.put(field.getName(),  ((PseudoEnum) value).getCode() );
					}
				}
				else if (PseudoModel.class.isAssignableFrom(fieldClass)){
					Object value = field.get(model);
					if (value != null){
						jsonRepresentation.put(field.getName(),  ((PseudoModel) value).toJSON() );
					}
					else{
						jsonRepresentation.put(field.getName(),  new JSONObject() );
					}
				}
				else if (List.class.isAssignableFrom(fieldClass)){
					Object value = field.get(model);
					List<?> list = (List<?>)value;
					if (list.size() == 0){
						jsonRepresentation.put(field.getName(),  new JSONArray() );
					}
					else{
						ParameterizedType listType = (ParameterizedType) field.getGenericType();
						Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
						if (Integer.class.isAssignableFrom(listClass) || int.class.isAssignableFrom(listClass) || Long.class.isAssignableFrom(listClass) || long.class.isAssignableFrom(listClass)){
							jsonRepresentation.put(field.getName(),  new JSONArray(list) );
						}
						else if (String.class.isAssignableFrom(listClass)){
							ArrayList<String> valArr = new ArrayList<String>();
							for (Object o : list){
								valArr.add(EncodingService.encodeURI((String)o));
							}
							jsonRepresentation.put(field.getName(),  new JSONArray(valArr) );
						}
						else if (PseudoModel.class.isAssignableFrom(listClass)){
							jsonRepresentation.put(field.getName(),  JSONGenerator.toJSON((List<PseudoModel>)list) );
						}
						else{
							throw new RuntimeException("[ERROR][Reflection] ReflectiveService suffered fatal reflection error, field type not matched: " +  fieldClass.getSimpleName());
						}
					}
				}
				else{
					throw new RuntimeException("[ERROR][Reflection] ReflectiveService suffered fatal reflection error, field type not matched: " +  fieldClass.getSimpleName());
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | JSONException | UnsupportedEncodingException e){
			throw new ValidationException("信息数据格式转换失败");
		}
		
		return jsonRepresentation;
	}
	
	
	/*******************
	 * 
	 * 	this method is initially designed for loading representations, also applied to Partner and Course loading, post processing multi-form kvps
	 * 
	 *******************/
	public static void storeKvps(final PseudoModel model, final Map<String, String> kvps) throws Exception {
		Field[] fields = getFields(model);
		
		try{
			for (Field field : fields){
				field.setAccessible(true);

				Class<?> fieldClass = field.getType();
				if (int.class.isAssignableFrom(fieldClass) || Integer.class.isAssignableFrom(fieldClass)){
					String value = EncodingService.decodeURI(kvps.get(field.getName()));
					if (value != null){
						field.setInt(model, Integer.parseInt(value, 10));
					}
				}
				else if (long.class.isAssignableFrom(fieldClass) || Long.class.isAssignableFrom(fieldClass)){
					String value = EncodingService.decodeURI(kvps.get(field.getName()));
					if (value != null){
						field.setLong(model, Long.parseLong(value, 10));
					}
				}
				else if (boolean.class.isAssignableFrom(fieldClass) || Boolean.class.isAssignableFrom(fieldClass)){
					String value = EncodingService.decodeURI(kvps.get(field.getName()));
					if (value != null){
						field.setBoolean(model, Boolean.parseBoolean(value));
					}
				}
				else if (String.class.isAssignableFrom(fieldClass)){
					String value = EncodingService.decodeURI(kvps.get(field.getName()));
					if (value != null){
						field.set(model, value);
					}
				}
				else if (Calendar.class.isAssignableFrom(fieldClass)){
					String value = EncodingService.decodeURI(kvps.get(field.getName()));
					if (value != null){
						field.set(model, DateUtility.castFromAPIFormat(value));
					}
				}
				else if (PseudoEnum.class.isAssignableFrom(fieldClass)){
					String value = EncodingService.decodeURI(kvps.get(field.getName()));
					if (value != null){
						field.set(model, fieldClass.getEnumConstants()[Integer.parseInt(value, 10)]);
					}
				}
				else if (List.class.isAssignableFrom(fieldClass)){
					String keyBase = field.getName();
					
					//if the field is a list, generating all the strings associated with that list, assuming all keys have format: key[i]
					ArrayList<String> valueList = new ArrayList<String>();
					for (int i = 1; kvps.get(keyBase + i) != null; i++){
						valueList.add(EncodingService.decodeURI(kvps.get(keyBase + i)));
					}

					ParameterizedType listType = (ParameterizedType) field.getGenericType();
					Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
					if (Integer.class.isAssignableFrom(listClass) || int.class.isAssignableFrom(listClass)){
						ArrayList<Integer> realList = new ArrayList<Integer>();
						for (String singleValue : valueList){
							realList.add(Integer.parseInt(singleValue, 10));
						}
						field.set(model, realList);
					}
					else if (Long.class.isAssignableFrom(listClass) || long.class.isAssignableFrom(listClass)){
						ArrayList<Long> realList = new ArrayList<Long>();
						for (String singleValue : valueList){
							realList.add(Long.parseLong(singleValue, 10));
						}
						field.set(model, realList);
					}
					else if (String.class.isAssignableFrom(listClass)){
						field.set(model, valueList);
					}
					else if (Calendar.class.isAssignableFrom(fieldClass)){
						ArrayList<Calendar> realList = new ArrayList<Calendar>();
						for (String singleValue : valueList){
							realList.add(DateUtility.castFromAPIFormat(singleValue));
						}
						field.set(model, realList);
					}
					else if (PseudoEnum.class.isAssignableFrom(fieldClass)){
						ArrayList<PseudoEnum> realList = new ArrayList<PseudoEnum>();
						for (String singleValue : valueList){
							realList.add((PseudoEnum)fieldClass.getEnumConstants()[Integer.parseInt(singleValue, 10)]);
						}
						field.set(model, realList);
					}

				}
				else{
					throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService suffered fatal reflection error, field type not matched");
				}

			}
		} catch (NumberFormatException e){
			throw new ValidationException("搜索条件中数字格式错误");
		}
	}
	
	
	/*******************
	 * 
	 * 	this part is used primarily by representations
	 * 
	 *******************/
	public static ArrayList<String> getKeySet(final PseudoModel model) {
		Field[] fields = getFields(model);

		ArrayList<String> keyArr = new ArrayList<String>();
		for (Field field : fields){
			field.setAccessible(true);
			keyArr.add(field.getName());
		}
		return keyArr;
	}
	
	public static boolean isEmpty(final PseudoModel model) {
		Field[] fields = getFields(model);
		
		try{
			for (Field field : fields){
				field.setAccessible(true);
				Class<?> fieldClass = field.getType();
				if (int.class.isAssignableFrom(fieldClass) || Integer.class.isAssignableFrom(fieldClass)){
					int number = field.getInt(model);
					if (number >= 0){
						return false;
					}
				}
				else if (long.class.isAssignableFrom(fieldClass) || Long.class.isAssignableFrom(fieldClass)){
					long number = field.getLong(model);
					if (number >= 0){
						return false;
					}
				}
				//have to ignore booleans here..true or false is not indication of empty
				else if (String.class.isAssignableFrom(fieldClass) || Calendar.class.isAssignableFrom(fieldClass) || PseudoEnum.class.isAssignableFrom(fieldClass)){
					Object value = field.get(model);
					if (value != null){
						return false;
					}
				}
				else{
					throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService suffered fatal reflection error, field type not matched");
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e){
			throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService reflection failed due to IllegalArgument or IllegalAccess");
		}
		
		return true;
	}


}
