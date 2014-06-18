package UserModule.resources;

import java.util.ArrayList;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.engine.header.Header;
import org.restlet.util.Series;

import UserModule.service.UserAuthenticationService;
import BaseModule.encryption.SessionCrypto;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.resources.PseudoResource;

public class UserPseudoResource extends PseudoResource{
	
	protected final String moduleId = "userModule";
	
	protected final boolean cookieEnabled = true;
	protected final String cookie_userSession = "userSessionCookie";
	protected final int cookie_maxAge = 5184000; //2 days
	
	
	/******************
	 * 
	 *  Authentication Area
	 *  
	 ******************/
	//return negative number for unauthenticated, else user id
	public int validateAuthentication() throws PseudoException{
		return !cookieEnabled ? 1 : UserAuthenticationService.validateSession(this.getSessionString());
	}

	
	public void openAuthentication(int userId) throws Exception{
		String encryptedString = SessionCrypto.encrypt(UserAuthenticationService.openSession(userId));
		
		Series<CookieSetting> cookieSettings = this.getResponse().getCookieSettings();
		boolean found = false;
		
		//discard all previous cookies
		for (CookieSetting cookieSetting : cookieSettings){
			if (cookieSetting.getName().equals(cookie_userSession)){
				cookieSetting.setMaxAge(cookie_maxAge);
				cookieSetting.setPath("/");
				cookieSetting.setDomain("www.ishangke.cn");
				cookieSetting.setValue(encryptedString);
				found = true;
			}
		}
		
		if (!found){
			CookieSetting newCookie = new CookieSetting(0, cookie_userSession, encryptedString);
			newCookie.setMaxAge(cookie_maxAge);
			newCookie.setPath("/");
			newCookie.setDomain("www.ishangke.cn");
			cookieSettings.add(newCookie);
		}
		
		Series<Cookie> cookies = this.getRequest().getCookies();
		for (Cookie cookie : cookies){
			if (cookie.getName().equals(cookie_userSession)){
				cookie.setValue(encryptedString);
			}
		}
		this.getRequest().setCookies(cookies);
		
		this.setCookieSettings(cookieSettings);
		this.getResponse().setCookieSettings(cookieSettings);
	}
	
	public void closeAuthentication() throws PseudoException{
		UserAuthenticationService.closeSession(this.getSessionString());

		Series<Cookie> cookies = this.getRequest().getCookies();
		for (Cookie cookie : cookies){
			if (cookie.getName().equals(cookie_userSession)){
				cookie.setValue("IGNORED");
			}
		}
		this.getRequest().setCookies(cookies);
		
		Series<CookieSetting> cookieSettings = this.getResponse().getCookieSettings();
		for (CookieSetting cookieSetting : cookieSettings){
			if (cookieSetting.getName().equals(cookie_userSession)){
				cookieSetting.setValue("IGNORE");
			}
		}
		
//		CookieSetting newCookie = new CookieSetting(0, cookie_userSession, "");
//		newCookie.setMaxAge(0);
//		newCookie.setPath("/");
//		newCookie.setDomain("www.ishangke.cn");
//		cookieSettings.add(newCookie);
		//cookieSettings.removeAll(cookie_userSession);
		this.setCookieSettings(cookieSettings);
		this.getResponse().setCookieSettings(cookieSettings);
	}
    
    
    /******************
     * 
     *  Session String Area
     *  
     ******************/
	
	protected String getSessionString() throws PseudoException{
		ArrayList<String> sessionString = new ArrayList<String>();
		String newDecryptedString = "";
		
		//first check header for auth, if not in header, then check for cookies for auth
		Series<Header> requestHeaders = (Series<Header>) getRequest().getAttributes().get("org.restlet.http.headers");
		if (requestHeaders != null) {
			if (requestHeaders.getFirstValue(cookie_userSession, true) != null){
				sessionString.add(requestHeaders.getFirstValue(cookie_userSession, true));
			}
		}
		if (sessionString.size() == 0){
			Series<Cookie> cookies = this.getRequest().getCookies();
			for( Cookie cookie : cookies){ 
				if (cookie.getName().equals(cookie_userSession) && !cookie.getValue().equals("IGNORE")){
					sessionString.add(cookie.getValue()); 
				}
			} 
		}

		if (sessionString.size() == 0){
			return null;
		}
		else{
			try{
				newDecryptedString = SessionCrypto.decrypt(sessionString.get(0));
			}
			catch (Exception e){
				e.printStackTrace();
				throw new ValidationException("Cookie编码错误，请尝试刷新页面或者登出");
			}
			return newDecryptedString;
		}
	}

}
