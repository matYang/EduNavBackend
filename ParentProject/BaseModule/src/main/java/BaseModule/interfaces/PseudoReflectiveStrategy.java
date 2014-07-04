package BaseModule.interfaces;

import java.lang.reflect.Field;

public interface PseudoReflectiveStrategy{
	
	public PseudoModel getModel();
	
	public void apply(final Field field, PseudoReflectiveCommand applier, boolean isList) throws Exception;
}
