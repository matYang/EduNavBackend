package BaseModule.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.ReflectiveOp;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoEnum;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoReflectiveCommand;
import BaseModule.interfaces.PseudoReflectiveStrategy;
import BaseModule.model.strategy.ReflectiveStrategyFactory;
import BaseModule.model.strategy.reflectiveApplier.BooleanCommand;
import BaseModule.model.strategy.reflectiveApplier.CalendarCommand;
import BaseModule.model.strategy.reflectiveApplier.IntegerCommand;
import BaseModule.model.strategy.reflectiveApplier.LongCommand;
import BaseModule.model.strategy.reflectiveApplier.PseudoEnumCommand;
import BaseModule.model.strategy.reflectiveApplier.PseudoModelCommand;
import BaseModule.model.strategy.reflectiveApplier.StringCommand;

public final class ModelReflectiveService {
	
	
	public static  JSONObject toJSON(final PseudoModel model) throws Exception{
		JSONObject json = new JSONObject();
		
		ReflectiveStrategyFactory factory = new ReflectiveStrategyFactory(ReflectiveOp.TOJSON, model, json, null);
		PseudoReflectiveStrategy strategy = factory.getStrategy();
		apply(strategy);
		
		return json;
	}
	
	public static  void storeJSON(final PseudoModel model, JSONObject json) throws Exception{
		ReflectiveStrategyFactory factory = new ReflectiveStrategyFactory(ReflectiveOp.STOREJSON, model, json, null);
		PseudoReflectiveStrategy strategy = factory.getStrategy();
		apply(strategy);
	}

	public static void storeKvps(final PseudoModel model, Map<String, String> kvps) throws Exception{
		ReflectiveStrategyFactory factory = new ReflectiveStrategyFactory(ReflectiveOp.STOREKVPS, model, null, kvps);
		PseudoReflectiveStrategy strategy = factory.getStrategy();
		apply(strategy);
	}
	
	public static void initialize(final PseudoModel model) throws Exception{
		ReflectiveStrategyFactory factory = new ReflectiveStrategyFactory(ReflectiveOp.INITIALIZE, model, null, null);
		PseudoReflectiveStrategy strategy = factory.getStrategy();
		apply(strategy);
	}
	
	private static List<Field> getFields(final PseudoModel model){
		List<Field> fields = new ArrayList<Field>();
		
		Class<?> currentClass = model.getClass();
		while(currentClass.getSuperclass() != null){
			for (Field field : currentClass.getDeclaredFields()) {
			    if (!Modifier.isStatic(field.getModifiers())) {
			    	fields.add(field);
			    }
			}
			currentClass = currentClass.getSuperclass();
		}
		return fields;
	}
	
	private static void apply(final PseudoReflectiveStrategy strategy) throws Exception {
		List<Field> fields = getFields(strategy.getModel());
		
		try{
			for (Field field : fields){
				PseudoReflectiveCommand command = null;
				boolean isList = false;
				
				field.setAccessible(true);
				Class<?> typeClass = field.getType();
				//determine if cur field representations a list
				if (List.class.isAssignableFrom(typeClass)){
					isList = true;
					//find the actual type inside list
					ParameterizedType listType = (ParameterizedType) field.getGenericType();
					typeClass = (Class<?>) listType.getActualTypeArguments()[0];
				}
				
				if (int.class.isAssignableFrom(typeClass) || Integer.class.isAssignableFrom(typeClass)){
					command = new IntegerCommand();
				}
				else if (long.class.isAssignableFrom(typeClass) || Long.class.isAssignableFrom(typeClass)){
					command = new LongCommand();
				}
				else if (boolean.class.isAssignableFrom(typeClass) || Boolean.class.isAssignableFrom(typeClass)){
					command = new BooleanCommand();
				}
				else if (String.class.isAssignableFrom(typeClass)){
					command = new StringCommand();
				}
				else if (Calendar.class.isAssignableFrom(typeClass)){
					command = new CalendarCommand();
				}
				else if (PseudoEnum.class.isAssignableFrom(typeClass)){
					command = new PseudoEnumCommand();
				}
				else if (PseudoModel.class.isAssignableFrom(typeClass)){
					command = new PseudoModelCommand();
				}
				else{
					DebugLog.d("[ERROR][Reflection] ReflectiveService suffered fatal reflection error, field type not matched: " +  typeClass.getSimpleName());
				}
				strategy.apply(field, command, isList);
			}
		} catch (Exception e){
			DebugLog.d(e);
			throw new ValidationException("信息数据格式转换失败");
		}
	}
	

	public static ArrayList<String> getKeySet(final PseudoModel model) {
		List<Field> fields= getFields(model);
		ArrayList<String> keyArr = new ArrayList<String>();
		for (Field field : fields){
			field.setAccessible(true);
			keyArr.add(field.getName());
		}
		return keyArr;
	}
	
	public static boolean isEmpty(final PseudoModel model) {
		List<Field> fields = getFields(model);

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
			}
		} catch (IllegalArgumentException | IllegalAccessException e){
			throw new RuntimeException("[ERROR][Reflection] RepresentationReflectiveService reflection failed due to IllegalArgument or IllegalAccess");
		}
		return true;
	}

}
