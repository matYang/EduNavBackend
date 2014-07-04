package BaseModule.interfaces;

import java.lang.reflect.Field;
import java.util.Map;

import org.json.JSONObject;

public interface PseudoReflectiveCommand {
	
	public void toJSON(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception;
	
	public void toJSONList(final Field field, final PseudoModel model, final JSONObject jsonRepresentation) throws Exception;
	
	public void storeJSON(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception;
	
	public void storeJSONList(final Field field, final PseudoModel model, final JSONObject jsonModel) throws Exception;
	
	public void storeKvps(final Field field, final PseudoModel model, final Map<String, String> kvps) throws Exception;
	
	public void storeKvpsList(final Field field, final PseudoModel model, final Map<String, String> kvps) throws Exception;
}
