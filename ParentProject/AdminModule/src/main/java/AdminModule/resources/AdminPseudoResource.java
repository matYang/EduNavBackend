package AdminModule.resources;

import java.util.ArrayList;

import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.engine.header.Header;
import org.restlet.util.Series;

import AdminModule.service.AdminAuthenticationService;
import BaseModule.encryption.SessionCrypto;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.resources.PseudoResource;

public class AdminPseudoResource extends PseudoResource{
	
	protected final String moduleId = "adminModule";
	protected final boolean cookieEnabled = true;
	protected final String cookie_adminSession = "adminSessionCookie";
	protected final int cookie_maxAge = -1; //session based
	
	/******************
	 * 
	 *  Authentication Area
	 *  
	 ******************/
	//return negative number for unauthenticated, else user id
	public int validateAuthentication() throws PseudoException{
		return !cookieEnabled ? 1 :AdminAuthenticationService.validateSession(this.getSessionString());
	}

	public void openAuthentication(int adminId) throws Exception{
		String encryptedString = SessionCrypto.encrypt(AdminAuthenticationService.openSession(adminId));
		
		Series<CookieSetting> cookieSettings = this.getResponse().getCookieSettings();
		boolean found = false;
		
		//discard all previous cookies
		for (CookieSetting cookieSetting : cookieSettings){
			if (cookieSetting.getName().equals(cookie_adminSession)){
				cookieSetting.setMaxAge(cookie_maxAge);
				cookieSetting.setPath("/");
				cookieSetting.setDomain("admin.ishangke.cn");
				cookieSetting.setValue(encryptedString);
				found = true;
			}
		}
		
		if (!found){
			CookieSetting newCookie = new CookieSetting(0, cookie_adminSession, encryptedString);
			newCookie.setMaxAge(cookie_maxAge);
			newCookie.setPath("/");
			newCookie.setDomain("admin.ishangke.cn");
			cookieSettings.add(newCookie);
		}
		this.setCookieSettings(cookieSettings);
		this.getResponse().setCookieSettings(cookieSettings);
		

		Series<Cookie> cookies = this.getRequest().getCookies();
		for (Cookie cookie : cookies){
			if (cookie.getName().equals(cookie_adminSession)){
				cookie.setValue(encryptedString);
			}
		}
		this.getRequest().setCookies(cookies);
	}
	
	public void closeAuthentication() throws PseudoException{
		AdminAuthenticationService.closeSession(this.getSessionString());
	}
	
	/******************
     * 
     *  Session String Area
     *  
     ******************/
	
	@SuppressWarnings("unchecked")
	protected String getSessionString() throws PseudoException{
		ArrayList<String> sessionString = new ArrayList<String>();
		String newDecryptedString = "";
		
		//first check header for auth, if not in header, then check for cookies for auth
		Series<Header> requestHeaders = (Series<Header>) getRequest().getAttributes().get("org.restlet.http.headers");
		if (requestHeaders != null) {
			if (requestHeaders.getFirstValue(cookie_adminSession, true) != null){
				sessionString.add(requestHeaders.getFirstValue(cookie_adminSession, true));
			}
		}
		if (sessionString.size() == 0){
			Series<Cookie> cookies = this.getRequest().getCookies();
			for( Cookie cookie : cookies){ 
				if (cookie.getName().equals(cookie_adminSession)){
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
