package BaseModule.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.ServerConfig;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.model.representation.BookingSearchRepresentation;
import BaseModule.model.representation.CourseSearchRepresentation;
import BaseModule.model.representation.PartnerSearchRepresentation;
import BaseModule.model.representation.UserSearchRepresentation;

public class RepresentationReflectiveService {
	
	public static ArrayList<String> getKeySet(PseudoRepresentation representation) {
		Field[] fields = getFields(representation);

		ArrayList<String> keyArr = new ArrayList<String>();
		for (Field field : fields){
			field.setAccessible(true);
			keyArr.add(field.getName());
		}
		return keyArr;
	}

	public static void storeKvps(PseudoRepresentation representation, Map<String, String> kvps) throws PseudoException, IllegalArgumentException, IllegalAccessException {
		Field[] fields = getFields(representation);
		
		try{
			for (Field field : fields){
				field.setAccessible(true);
				String value = kvps.get(field.getName());
				if (value != null){
					Class<?> fieldClass = field.getType();
					if (fieldClass.isAssignableFrom(int.class)){
						field.setInt(representation, Integer.parseInt(value, 10));
					}
					else if (fieldClass.isAssignableFrom(AccountStatus.class)){
						field.set(representation, AccountStatus.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(String.class)){
						field.set(representation, value);
					}
					else if (fieldClass.isAssignableFrom(Calendar.class)){
						field.set(representation, DateUtility.castFromRepresentationFormat(value));
					}
					else{
						throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService suffered fatal reflection error, field type not matched");
					}
				}
				else{
					//null value from kvps, do nothing
				}
			}
		} catch (NumberFormatException e){
			throw new ValidationException("搜索条件中数字格式错误");
		}
		
	}
	

	
	public static String serialize(PseudoRepresentation representation) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = getFields(representation);
		
		ArrayList<String> serializedMembers = new ArrayList<String>();
		for (Field field : fields){
			field.setAccessible(true);
			Class<?> fieldClass = field.getType();
			if (fieldClass.isAssignableFrom(int.class)){
				int number = field.getInt(representation);
				if (number > 0){
					serializedMembers.add(field.getName() + "_" + String.valueOf(number));
				}
			}
			else if (fieldClass.isAssignableFrom(AccountStatus.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  String.valueOf(  ((AccountStatus) value).code  )  );
				}
			}
			else if (fieldClass.isAssignableFrom(String.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +(String) value);
				}
			}
			else if (fieldClass.isAssignableFrom(Calendar.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  DateUtility.castToRepresentationFormat((Calendar) value)  );
				}
			}
			else{
				throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService suffered fatal reflection error, field type not matched");
			}
		}
		
		
		String serializedString = "";
		for (int i = 0; i < serializedMembers.size() - 1; i++){
			serializedString += serializedMembers.get(i) + ServerConfig.urlSeperator;
		}
		serializedString += serializedMembers.get(serializedMembers.size() - 1);
		return serializedString;
	}


	public static JSONObject toJSON(PseudoRepresentation representation) {
		Field[] fields = getFields(representation);
		JSONObject jsonRepresentation = new JSONObject();
		
		try{
			for (Field field : fields){
				field.setAccessible(true);
				Class<?> fieldClass = field.getType();
				if (fieldClass.isAssignableFrom(int.class)){
					int number = field.getInt(representation);
					if (number > 0){
						jsonRepresentation.put(field.getName(), String.valueOf(number));
					}
				}
				else if (fieldClass.isAssignableFrom(AccountStatus.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(), String.valueOf(  ((AccountStatus) value).code  )  );
					}
				}
				else if (fieldClass.isAssignableFrom(String.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(), (String) value);
					}
				}
				else if (fieldClass.isAssignableFrom(Calendar.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  DateUtility.castToRepresentationFormat((Calendar) value)  );
					}
				}
				else{
					throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService suffered fatal reflection error, field type not matched");
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e){
			throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService reflection failed due to IllegalArgument or IllegalAccess");
		}
		
		return jsonRepresentation;
	}
	
	public static boolean isEmpty(PseudoRepresentation representation) {
		Field[] fields = getFields(representation);
		
		try{
			for (Field field : fields){
				field.setAccessible(true);
				Class<?> fieldClass = field.getType();
				if (fieldClass.isAssignableFrom(int.class)){
					int number = field.getInt(representation);
					if (number > 0){
						return false;
					}
				}
				else if (fieldClass.isAssignableFrom(AccountStatus.class)){
					Object value = field.get(representation);
					if (value != null){
						return false;
					}
				}
				else if (fieldClass.isAssignableFrom(String.class)){
					Object value = field.get(representation);
					if (value != null){
						return false;
					}
				}
				else if (fieldClass.isAssignableFrom(Calendar.class)){
					Object value = field.get(representation);
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
	
	private static Field[] getFields(PseudoRepresentation representation){
		Field[] fields;
		if (representation instanceof CourseSearchRepresentation){
			fields = CourseSearchRepresentation.class.getDeclaredFields();
		}
		else if (representation instanceof UserSearchRepresentation){
			fields = UserSearchRepresentation.class.getDeclaredFields();
		}
		else if (representation instanceof PartnerSearchRepresentation){
			fields = PartnerSearchRepresentation.class.getDeclaredFields();
		}
		else if (representation instanceof BookingSearchRepresentation){
			fields = BookingSearchRepresentation.class.getDeclaredFields();
		}
		else{
			throw new RuntimeException("[ERROR] RepresentationReflectiveService unable to determine representation instance type");
		}
		
		return fields;
	}

}
