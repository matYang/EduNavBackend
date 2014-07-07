package BaseModule.model.strategy.reflectiveStrategy;

import java.lang.reflect.Field;

import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoReflectiveCommand;
import BaseModule.interfaces.PseudoReflectiveStrategy;

public class InitializationStrategy implements PseudoReflectiveStrategy{
	private final PseudoModel model;
	
	public InitializationStrategy(final PseudoModel model){
		super();
		this.model = model;
	}
	
	@Override
	public void apply(final Field field, PseudoReflectiveCommand applier, boolean isList) throws Exception{
		if (!isList){
			applier.initialize(field, model);
		}
		else{
			applier.initializeList(field, model);
		}
	}
	
	@Override
	public PseudoModel getModel() {
		return this.model;
	}

}
