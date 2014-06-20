package BaseModule.service;

import java.io.UnsupportedEncodingException;

public final class EncodingService {
	
	public static String encodeURI(String var) throws UnsupportedEncodingException{
		return var == null ? null : java.net.URLEncoder.encode(var, "utf-8");
	}
	
	public static String decodeURI(String var) throws UnsupportedEncodingException{
		return var == null ? null : java.net.URLDecoder.decode(var, "utf-8");
	}

}
