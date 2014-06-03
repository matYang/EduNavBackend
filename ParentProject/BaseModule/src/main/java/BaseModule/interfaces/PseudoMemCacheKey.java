package BaseModule.interfaces;

import java.io.UnsupportedEncodingException;

public interface PseudoMemCacheKey {
	
	public String toCacheKey() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException;

}
