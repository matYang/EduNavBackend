package BaseModule.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.BookingType;
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.CourseStatus;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.configurations.ServerConfig;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoEnum;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.model.representation.AdminSearchRepresentation;
import BaseModule.model.representation.BookingSearchRepresentation;
import BaseModule.model.representation.CouponSearchRepresentation;
import BaseModule.model.representation.CourseSearchRepresentation;
import BaseModule.model.representation.CreditSearchRepresentation;
import BaseModule.model.representation.PartnerSearchRepresentation;
import BaseModule.model.representation.TransactionSearchRepresentation;
import BaseModule.model.representation.UserSearchRepresentation;

public final class ModelReflectiveService {
	
	public static ArrayList<String> getKeySet(final PseudoModel representation) {
		Field[] fields = getFields(representation);

		ArrayList<String> keyArr = new ArrayList<String>();
		for (Field field : fields){
			field.setAccessible(true);
			keyArr.add(field.getName());
		}
		return keyArr;
	}

	public static void storeKvps(final PseudoModel representation, final Map<String, String> kvps) throws PseudoException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		Field[] fields = getFields(representation);
		
		try{
			for (Field field : fields){
				field.setAccessible(true);
				String value = EncodingService.decodeURI(kvps.get(field.getName()));
				if (value != null){
					Class<?> fieldClass = field.getType();
					if (int.class.isAssignableFrom(fieldClass)){
						field.setInt(representation, Integer.parseInt(value, 10));
					}
					else if (long.class.isAssignableFrom(fieldClass)){
						field.setLong(representation, Long.parseLong(value, 10));
					}
					else if (String.class.isAssignableFrom(fieldClass)){
						field.set(representation, value);
					}
					else if (Calendar.class.isAssignableFrom(fieldClass)){
						field.set(representation, DateUtility.castFromAPIFormat(value));
					}
					else if (PseudoEnum.class.isAssignableFrom(fieldClass)){
						field.set(representation, fieldClass.getEnumConstants()[Integer.parseInt(value, 10)]);
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
	

	
	public static String serialize(final PseudoModel representation) throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ValidationException {
		Field[] fields = getFields(representation);
		
		ArrayList<String> serializedMembers = new ArrayList<String>();
		for (Field field : fields){
			field.setAccessible(true);
			Class<?> fieldClass = field.getType();
			if (int.class.isAssignableFrom(fieldClass)){
				int number = field.getInt(representation);
				if (number >= 0){
					serializedMembers.add(field.getName() + "_" + String.valueOf(number));
				}
			}
			else if (long.class.isAssignableFrom(fieldClass)){
				long number = field.getLong(representation);
				if (number >= 0){
					serializedMembers.add(field.getName() + "_" + String.valueOf(number));
				}
			}
			else if (String.class.isAssignableFrom(fieldClass)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" + EncodingService.encodeURI((String) value));
				}
			}
			else if (Calendar.class.isAssignableFrom(fieldClass)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  DateUtility.castToAPIFormat((Calendar) value)  );
				}
			}
			else if (PseudoEnum.class.isAssignableFrom(fieldClass)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  String.valueOf(  ((PseudoEnum) value).getCode()  )  );
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
		if (serializedMembers.size() > 0){
			serializedString += serializedMembers.get(serializedMembers.size() - 1);
		}
		return serializedString;
	}


	public static JSONObject toJSON(final PseudoModel representation) throws ValidationException {
		Field[] fields = getFields(representation);
		JSONObject jsonRepresentation = new JSONObject();
		
		try{
			for (Field field : fields){
				field.setAccessible(true);
				Class<?> fieldClass = field.getType();
				if (int.class.isAssignableFrom(fieldClass)){
					int number = field.getInt(representation);
					if (number >= 0){
						jsonRepresentation.put(field.getName(), number);
					}
				}
				else if (long.class.isAssignableFrom(fieldClass)){
					long number = field.getLong(representation);
					if (number >= 0){
						jsonRepresentation.put(field.getName(), number);
					}
				}
				else if (String.class.isAssignableFrom(fieldClass)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(), EncodingService.encodeURI((String) value));
					}
				}
				else if (Calendar.class.isAssignableFrom(fieldClass)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  DateUtility.castToAPIFormat((Calendar) value)  );
					}
				}
				else if (PseudoEnum.class.isAssignableFrom(fieldClass)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  ((PseudoEnum) value).getCode() );
					}
				}
				else{
					throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService suffered fatal reflection error, field type not matched: " +  fieldClass.getSimpleName());
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | JSONException | UnsupportedEncodingException e){
			throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService reflection failed due to IllegalArgument or IllegalAccess or parsing");
		}
		
		return jsonRepresentation;
	}
	
	public static boolean isEmpty(final PseudoModel representation) {
		Field[] fields = getFields(representation);
		
		try{
			for (Field field : fields){
				field.setAccessible(true);
				Class<?> fieldClass = field.getType();
				if (int.class.isAssignableFrom(fieldClass)){
					int number = field.getInt(representation);
					if (number >= 0){
						return false;
					}
				}
				else if (long.class.isAssignableFrom(fieldClass)){
					long number = field.getLong(representation);
					if (number >= 0){
						return false;
					}
				}
				else if (String.class.isAssignableFrom(fieldClass) || Calendar.class.isAssignableFrom(fieldClass) || PseudoEnum.class.isAssignableFrom(fieldClass)){
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
	
	
	private static Field[] getFields(final PseudoModel representation){
		return representation.getClass().getDeclaredFields();
	}

}
