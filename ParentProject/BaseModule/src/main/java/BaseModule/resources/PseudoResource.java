package BaseModule.resources;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.restlet.data.CookieSetting;
import org.restlet.data.Status;
import org.restlet.engine.header.Header;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;
import BaseModule.configurations.ValidationConfig;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;

public class PseudoResource extends ServerResource{
	
	/*set the response header to allow for CORS*/
	public void addCORSHeader(){
		Series<Header> responseHeaders = (Series<Header>) getResponse().getAttributes().get("org.restlet.http.headers");
        
		if (responseHeaders == null) { 
			responseHeaders = new Series(Header.class); 
			responseHeaders.add("Access-Control-Allow-Origin", "*");
			responseHeaders.add("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
			responseHeaders.add("Access-Control-Allow-Headers", "Content-Type");
			responseHeaders.add("Access-Control-Allow-Headers", "authCode");
			responseHeaders.add("Access-Control-Allow-Headers", "origin, x-requested-with, content-type");
		}
		
		if (responseHeaders != null){
            getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders); 
        }
	}

	protected Object examUserJSON(Representation entity) throws PseudoException{
		return null;
	}
	
	public void checkEntity(Representation entity) throws ValidationException{
		if (entity != null && entity.getSize() > ValidationConfig.max_PostLength){
			throw new ValidationException("发送内容过大");
		}
	}
	
	public void checkFileEntity(Representation entity) throws NullPointerException, ValidationException{
		if (entity != null && entity.getSize() > ValidationConfig.max_FileLength){
			throw new ValidationException("发送内容过大");
		}
	}

	
	
	/******************
	 * 
	 * Query Area
	 * 
	 ******************/
	public String getReqAttr(String fieldName) throws UnsupportedEncodingException{
		Object attr = this.getRequestAttributes().get(fieldName);
		return attr != null ? java.net.URLDecoder.decode((String)attr, "utf-8") : null;
	}
	
	public String getQueryVal(String fieldName) throws UnsupportedEncodingException{
		String val = getQuery().getValues(fieldName);
		return val != null ? java.net.URLDecoder.decode(val, "utf-8") : null;
	}
	
	public String getPlainQueryVal(String fieldName){
		String val = getQuery().getValues(fieldName);
		return val;
	}
	
	public String getToUtf(String var) throws UnsupportedEncodingException{
		return java.net.URLEncoder.encode(var, "utf-8");
	}
	
	
	/******************
	 * 
	 * Exception Handling Area
	 * 
	 ******************/
	public StringRepresentation doPseudoException(PseudoException e){
		DebugLog.d(e);
		switch(e.getCode()){
			case 1: case 2: case 4: case 5: 
				//Not Found
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				break;
			case 6:
				//authentication error
				setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				break;
			case 18:
				//ValidationException
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				break;
			default:
				setStatus(Status.SERVER_ERROR_INTERNAL);
				break;
		}
		return new StringRepresentation(e.getExceptionText());
	}
	
	public StringRepresentation doException(Exception e){
		DebugLog.d(e);
		setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		return new StringRepresentation("不好意思..哪里弄错了，请稍后重试");
	}
	

	public Representation quickRespond(String responseText){
		addCORSHeader();
		return new StringRepresentation(responseText);
	}
	
	/******************
	 * 
	 * Take the options
	 * 
	 ******************/
    //needed here since backbone will try to send OPTIONS to /id before PUT or DELETE
    @Options
    public Representation takeOptions(Representation entity) {
    	addCORSHeader();
    	setStatus(Status.SUCCESS_OK);
        return new JsonRepresentation(new JSONObject());
    }
    
}
