package BaseModule.model.strategy.reflectiveStrategy;

import java.lang.reflect.Field;

import org.json.JSONObject;

import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoReflectiveCommand;
import BaseModule.interfaces.PseudoReflectiveStrategy;

public class StoreJSONStrategy implements PseudoReflectiveStrategy{
	
	private final PseudoModel model;
	private final JSONObject jsonModel;
	
	public StoreJSONStrategy(final PseudoModel model, final JSONObject jsonModel){
		super();
		this.model = model;
		this.jsonModel = jsonModel;
	}
	
	@Override
	public void apply(final Field field, PseudoReflectiveCommand applier, boolean isList) throws Exception{
		if (!isList){
			applier.storeJSON(field, model, jsonModel);
		}
		else{
			applier.storeJSONList(field, model, jsonModel);
		}
	}
	
	@Override
	public PseudoModel getModel() {
		return this.model;
	}

}
