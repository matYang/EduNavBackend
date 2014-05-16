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

	protected Object parseJSON(Representation entity) throws PseudoException{
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
	 *  Cookie Area
	 *  
	 ******************/
	public boolean validateAuthentication(int userId) throws PseudoException{
		return !ServerConfig.cookieEnabled ? true : AuthenticationResource.validateCookieSession(userId, this.getSessionString());
	}

	
	public void addAuthenticationSession(int userId) throws PseudoException{
		Series<CookieSetting> cookieSettings = this.getResponse().getCookieSettings(); 
		CookieSetting newCookie = AuthenticationResource.openCookieSession(userId);
		cookieSettings.clear();
		cookieSettings.add(newCookie);
		this.setCookieSettings(cookieSettings);
	}
	
	public void closeAuthenticationSession(int userId) throws PseudoException{
		Series<Cookie> cookies = this.getRequest().getCookies();
		AuthenticationResource.closeCookieSession(cookies);
		Series<CookieSetting> cookieSettings = this.getResponse().getCookieSettings(); 
		cookieSettings.clear();
		this.setCookieSettings(cookieSettings);
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
    
    
    /******************
     * 
     *  New Session Area
     *  
     ******************/
//    protected boolean validateAuthentication() throws PseudoException{
//		return !ServerConfig.cookieEnabled ? true : validateCookieSession();
//	}
//
//	protected String generateAuthenticationSessionString(int userId) throws PseudoException{
//		return generateSesstionString(userId);
//	}
//	
//	protected void openAuthenticationSession(int userId) throws PseudoException{
//		Series<CookieSetting> cookieSettings = this.getResponse().getCookieSettings(); 
//		CookieSetting newCookie = openSession(userId);
//		cookieSettings.add(newCookie);
//		this.setCookieSettings(cookieSettings);
//	}
//	
//	protected void closeAuthenticationSession() throws PseudoException{
//		closeSession();
//	}
//	
//	protected String getSessionString() throws PseudoException{
//		ArrayList<String> sessionString = new ArrayList<String>();
//		String newDecryptedString = "";
//		
//		//first check header for auth, if not in header, then check for cookies for auth
//		Series<Header> requestHeaders = (Series<Header>) getRequest().getAttributes().get("org.restlet.http.headers");
//		if (requestHeaders != null) {
//			if (requestHeaders.getFirstValue(ServerConfig.cookie_userSession, true) != null){
//				sessionString.add(requestHeaders.getFirstValue(ServerConfig.cookie_userSession, true));
//			}
//		}
//		if (sessionString.size() == 0){
//			Series<Cookie> cookies = this.getRequest().getCookies();
//			for( Cookie cookie : cookies){ 
//				if (cookie.getName().equals(ServerConfig.cookie_userSession)){
//					sessionString.add(cookie.getValue()); 
//				}
//			} 
//		}
//		
////		if (sessionString.size() > 1){
////			throw new DuplicateSessionCookieException();
////		}
//		if (sessionString.size() == 0){
//			return "";
//		}
//		else{
//			try{
//				newDecryptedString = SessionCrypto.decrypt(sessionString.get(0));
//			}
//			catch (Exception e){
//				e.printStackTrace();
//				throw new SessionEncodingException();
//			}
//			return newDecryptedString;
//		}
//	}
//	
//	protected int getUserIdFromSessionString(String sessionString)throws PseudoException{
//		String userIdStr = sessionString.split(DatabaseConfig.redisSeperatorRegex)[1];
//		int userId = -1;
//		try{
//			userId = Integer.parseInt(userIdStr);
//		} catch (NumberFormatException e){
//			throw new AccountAuthenticationException("UserCookieResource:: getSessionString:: Session does not exist");
//		}
//		
//		return userId;
//	}
//	
    
    /******************
     * 
     *  Authentication Area
     *  
     ******************/
//	private String generateSesstionString(int userId) throws PseudoException{
//		// generate session string and stores session in Redis
//		 String sessionString = AuthDaoService.generateUserSession(userId);
//		 try{
//			 String encrypted = SessionCrypto.encrypt(sessionString);
//			 return encrypted;
//		 } catch (Exception e){
//			 throw new SessionEncodingException();
//		 }
//	}
//	
//	
//	private boolean validateCookieSession() throws PseudoException{
//		String sessionString = getSessionString();
//		if (sessionString == null || sessionString.length() == 0){
//			return false;
//		}
//		int userId = getUserIdFromSessionString(sessionString);
//		if (userId == -1){
//			throw new AccountAuthenticationException("UserCookieResource:: validateCookieSession:: Invalid ID, ID is -1");
//		}
//		boolean login = false;
//		
//		try{
//			String decryptedString = SessionCrypto.decrypt(sessionString);
//			login =  AuthDaoService.validateUserSession(userId, decryptedString);
//		}
//		catch (Exception e){
//			e.printStackTrace();
//			throw new SessionEncodingException();
//		}
//
//		if (!login){
//			throw new AccountAuthenticationException("UserCookieResource:: validateCookieSession:: Session Validation Failed");
//		}
//		return login;
//	}
//	
//	
//	private CookieSetting openSession(int userId) throws PseudoException{
//        String encryptedString = generateSesstionString(userId);
//        CookieSetting newCookieSetting;
//        try{
//        	 newCookieSetting = new CookieSetting(0, ServerConfig.cookie_userSession, encryptedString);
//        	 newCookieSetting.setMaxAge(ServerConfig.cookie_maxAge);
//        }
//        catch (Exception e){
//			throw new SessionEncodingException();
//		}
//        
//       return newCookieSetting;
//	}
//	
//	
//	private boolean closeSession() throws PseudoException{
//		try{
//			String sessionString = getSessionString();
//			String decryptedString = SessionCrypto.decrypt(sessionString);
//			return AuthDaoService.closeUserSession(decryptedString);
//		}
//		catch (AccountAuthenticationException e){
//			DebugLog.d(e);
//			return true;
//		}
//		catch (Exception e){
//			DebugLog.d(e);
//			throw new SessionEncodingException();
//		}
//	}
	
}
