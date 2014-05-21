package UserModule.resources.user.auth;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import UserModule.resources.UserPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.User;


public class UserSessionRedirect extends UserPseudoResource{
		
	@Get
	public Representation sessionRedirect(Representation entity){
		DebugLog.d("SessionDirect:: Enter session redirect");
		
		User user = null;
		JSONObject jsonObject = new JSONObject();
	
		try {
			int userId = this.validateAuthentication();
			user = UserDaoService.getUserById(userId);
			jsonObject = JSONFactory.toJSON(user);
		} catch (AuthenticationException e){
			//if not authenticated, return default user with id -1
			user = new User("default","default","default", AccountStatus.activated);
			user.setUserId(-1);
			jsonObject = JSONFactory.toJSON(user);
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		}  catch (Exception e) {
			return this.doException(e);
		}
	
		Representation result = new JsonRepresentation(jsonObject);
	    
	    this.addCORSHeader();
	    return result;
	}

}
