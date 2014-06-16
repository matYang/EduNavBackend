package UserModule.resources.user.auth;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import UserModule.resources.UserPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.User;


public class UserSessionRedirect extends UserPseudoResource{
	private final String apiId = UserSessionRedirect.class.getSimpleName();
		
	@Get
	public Representation sessionRedirect(){
		DebugLog.d("SessionDirect:: Enter session redirect");
		
		User user = null;
		JSONObject jsonObject = new JSONObject();
		
		try {
			int userId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, userId, this.getUserAgent(), "");
			
			user = UserDaoService.getUserById(userId);
			jsonObject = JSONFactory.toJSON(user);
			
		} catch (AuthenticationException | UserNotFoundException e){
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), "");
			
			//if not authenticated, return default user with id -1
			user = new User("default","default", "", "", "",AccountStatus.activated);
			user.setUserId(-1);
			try {
				jsonObject = JSONFactory.toJSON(user);
			} catch (PseudoException e1){
				this.addCORSHeader();
				return this.doPseudoException(e1);
			} 
		
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			return this.doException(e);
		}
	
		Representation result = new JsonRepresentation(jsonObject);
	    
	    this.addCORSHeader();
	    return result;
	}

}
