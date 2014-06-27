package BaseModule.interfaces;

import java.util.ArrayList;
import java.util.Map;

public interface PseudoRepresentation {
	
	public ArrayList<String> getKeySet() throws Exception;
	
	public void storeKvps(Map<String, String> kvps) throws Exception;
	
	public boolean isEmpty() throws Exception;
	
	
}
