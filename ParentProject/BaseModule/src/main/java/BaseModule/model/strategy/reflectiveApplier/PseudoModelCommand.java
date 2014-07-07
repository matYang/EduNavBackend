package BaseModule.model.strategy.reflectiveApplier;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.generator.JSONGenerator;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoReflectiveCommand;

public class PseudoModelCommand implements PseudoReflectiveCommand {

	@Override
	public void toJSON(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception{
		Object value = field.get(model);
		if (value != null){
			jsonRepresentation.put(field.getName(),  ((PseudoModel) value).toJSON() );
		}
		else{
			jsonRepresentation.put(field.getName(),  new JSONObject() );
		}
	}

	@Override
	public void toJSONList(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception{
		Object value = field.get(model);
		List<?> list = (List<?>)value;
		
		jsonRepresentation.put(field.getName(),  JSONGenerator.toJSON((List<PseudoModel>)list) );
	}

	@Override
	public void storeJSON(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception{
		//do nothing
	}

	@Override
	public void storeJSONList(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception{
		//do nothing
	}

	@Override
	public void storeKvps(final Field field, final PseudoModel model, final Map<String, String> kvps) throws Exception{
		//do nothing
	}

	@Override
	public void storeKvpsList(final Field field, final PseudoModel model, final Map<String, String> kvps) throws Exception{
		//do nothing
	}

	@Override
	public void initialize(Field field, PseudoModel model) throws Exception {
		//do nothing
	}

	@Override
	public void initializeList(Field field, PseudoModel model) throws Exception {
		//do nothing
	}


}
