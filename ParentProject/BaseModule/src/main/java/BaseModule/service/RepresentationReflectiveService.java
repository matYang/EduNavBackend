package BaseModule.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.CourseStatus;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.configurations.ServerConfig;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.model.representation.AdminSearchRepresentation;
import BaseModule.model.representation.BookingSearchRepresentation;
import BaseModule.model.representation.CouponSearchRepresentation;
import BaseModule.model.representation.CourseSearchRepresentation;
import BaseModule.model.representation.CreditSearchRepresentation;
import BaseModule.model.representation.PartnerSearchRepresentation;
import BaseModule.model.representation.TransactionSearchRepresentation;
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

	public static void storeKvps(PseudoRepresentation representation, Map<String, String> kvps) throws PseudoException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		Field[] fields = getFields(representation);
		
		try{
			for (Field field : fields){
				field.setAccessible(true);
				String value = EncodingService.decodeURI(kvps.get(field.getName()));
				if (value != null){
					Class<?> fieldClass = field.getType();
					if (fieldClass.isAssignableFrom(int.class)){
						field.setInt(representation, Integer.parseInt(value, 10));
					}
					else if (fieldClass.isAssignableFrom(String.class)){
						field.set(representation, value);
					}
					else if (fieldClass.isAssignableFrom(Calendar.class)){
						field.set(representation, DateUtility.castFromRepresentationFormat(value));
					}
					else if (fieldClass.isAssignableFrom(AccountStatus.class)){
						field.set(representation, AccountStatus.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(Privilege.class)){
						field.set(representation, Privilege.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(CourseStatus.class)){
						field.set(representation, CourseStatus.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(TransactionType.class)){
						field.set(representation, TransactionType.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(CouponStatus.class)){
						field.set(representation, CouponStatus.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(CouponOrigin.class)){
						field.set(representation, CouponOrigin.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(CreditStatus.class)){
						field.set(representation, CreditStatus.fromInt(Integer.parseInt(value, 10)));
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
	

	
	public static String serialize(PseudoRepresentation representation) throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
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
			else if (fieldClass.isAssignableFrom(String.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" + EncodingService.encodeURI((String) value));
				}
			}
			else if (fieldClass.isAssignableFrom(Calendar.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  DateUtility.castToRepresentationFormat((Calendar) value)  );
				}
			}
			else if (fieldClass.isAssignableFrom(AccountStatus.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  String.valueOf(  ((AccountStatus) value).code  )  );
				}
			}
			else if (fieldClass.isAssignableFrom(Privilege.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  String.valueOf(  ((Privilege) value).code  )  );
				}
			}
			else if (fieldClass.isAssignableFrom(CourseStatus.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  String.valueOf(  ((CourseStatus) value).code  )  );
				}
			}
			else if (fieldClass.isAssignableFrom(TransactionType.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  String.valueOf(  ((TransactionType) value).code  )  );
				}
			}
			else if (fieldClass.isAssignableFrom(CouponStatus.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  String.valueOf(  ((CouponStatus) value).code  )  );
				}
			}
			else if (fieldClass.isAssignableFrom(CouponOrigin.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  String.valueOf(  ((CouponOrigin) value).code  )  );
				}
			}
			else if (fieldClass.isAssignableFrom(CreditStatus.class)){
				Object value = field.get(representation);
				if (value != null){
					serializedMembers.add(field.getName() + "_" +  String.valueOf(  ((CreditStatus) value).code  )  );
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
						jsonRepresentation.put(field.getName(), number);
					}
				}
				else if (fieldClass.isAssignableFrom(String.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(), EncodingService.encodeURI((String) value));
					}
				}
				else if (fieldClass.isAssignableFrom(Calendar.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  DateUtility.castToRepresentationFormat((Calendar) value)  );
					}
				}
				else if (fieldClass.isAssignableFrom(AccountStatus.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  ((AccountStatus) value).code );
					}
				}
				else if (fieldClass.isAssignableFrom(Privilege.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  ((Privilege) value).code );
					}
				}
				else if (fieldClass.isAssignableFrom(CourseStatus.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  ((CourseStatus) value).code );
					}
				}
				else if (fieldClass.isAssignableFrom(TransactionType.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  ((TransactionType) value).code );
					}
				}
				else if (fieldClass.isAssignableFrom(CouponStatus.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  ((CouponStatus) value).code );
					}
				}
				else if (fieldClass.isAssignableFrom(CouponOrigin.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  ((CouponOrigin) value).code );
					}
				}
				else if (fieldClass.isAssignableFrom(CreditStatus.class)){
					Object value = field.get(representation);
					if (value != null){
						jsonRepresentation.put(field.getName(),  ((CreditStatus) value).code );
					}
				}
				else{
					throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService suffered fatal reflection error, field type not matched");
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | JSONException | UnsupportedEncodingException e){
			throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService reflection failed due to IllegalArgument or IllegalAccess or parsing");
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
				else if (fieldClass.isAssignableFrom(String.class) || fieldClass.isAssignableFrom(Calendar.class) || fieldClass.isAssignableFrom(AccountStatus.class) || fieldClass.isAssignableFrom(Privilege.class) || fieldClass.isAssignableFrom(CourseStatus.class) || fieldClass.isAssignableFrom(TransactionType.class) || fieldClass.isAssignableFrom(CouponStatus.class) || fieldClass.isAssignableFrom(CouponOrigin.class) || fieldClass.isAssignableFrom(CreditStatus.class)){
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
		else if (representation instanceof AdminSearchRepresentation){
			fields = AdminSearchRepresentation.class.getDeclaredFields();
		}
		else if (representation instanceof TransactionSearchRepresentation){
			fields = TransactionSearchRepresentation.class.getDeclaredFields();
		} 
		else if (representation instanceof CouponSearchRepresentation){
			fields = CouponSearchRepresentation.class.getDeclaredFields();
		}
		else if (representation instanceof CreditSearchRepresentation){
			fields = CreditSearchRepresentation.class.getDeclaredFields();
		}
		else{
			throw new RuntimeException("[ERROR] RepresentationReflectiveService unable to determine representation instance type");
		}
		
		return fields;
	}

}
