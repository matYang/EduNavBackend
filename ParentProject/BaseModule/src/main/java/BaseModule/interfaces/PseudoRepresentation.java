package BaseModule.interfaces;

import java.util.ArrayList;
import java.util.Map;

import BaseModule.exception.PseudoException;

public interface PseudoRepresentation {
	
	public String serialize() throws Exception;
	
	public ArrayList<String> getKeySet() throws Exception;
	
	public void storeKvps(Map<String, String> kvps) throws Exception;
	
	
}
