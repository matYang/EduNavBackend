package BaseModule.model.strategy.reflectiveStrategy;

import java.lang.reflect.Field;
import java.util.Map;

import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoReflectiveCommand;
import BaseModule.interfaces.PseudoReflectiveStrategy;

public class StoreKvpsStrategy implements PseudoReflectiveStrategy{
	
	private final PseudoModel model;
	private final Map<String, String> kvps;

	public StoreKvpsStrategy(final PseudoModel model, final Map<String, String> kvps){
		super();
		this.model = model;
		this.kvps = kvps;
	}
	
	@Override
	public void apply(final Field field, PseudoReflectiveCommand applier, boolean isList) throws Exception{
		if (!isList){
			applier.storeKvps(field, model, kvps);
		}
		else{
			applier.storeKvpsList(field, model, kvps);
		}
	}

	@Override
	public PseudoModel getModel() {
		return this.model;
	}
}
