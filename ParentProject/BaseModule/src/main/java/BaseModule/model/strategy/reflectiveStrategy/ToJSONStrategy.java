package BaseModule.model.strategy.reflectiveStrategy;

import java.lang.reflect.Field;

import org.json.JSONObject;

import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoReflectiveCommand;
import BaseModule.interfaces.PseudoReflectiveStrategy;

public class ToJSONStrategy implements PseudoReflectiveStrategy{
	
	private final PseudoModel model;
	private final JSONObject jsonRepresentation;
	
	public ToJSONStrategy(final PseudoModel model, final JSONObject jsonRepresentation){
		super();
		this.model = model;
		this.jsonRepresentation = jsonRepresentation;
	}
	
	@Override
	public void apply(final Field field, PseudoReflectiveCommand applier, boolean isList) throws Exception{
		if (!isList){
			applier.toJSON(field, model, jsonRepresentation);
		}
		else{
			applier.toJSONList(field, model, jsonRepresentation);
		}
	}

	@Override
	public PseudoModel getModel() {
		return this.model;
	}
	

}
