package UserModule.resources.user;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import UserModule.resources.UserPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.User;


public class UserIdResource extends UserPseudoResource{
	private final String apiId = UserIdResource.class.getSimpleName(); 

	@Get 	    
	public Representation getUserById() {
	    JSONObject jsonObject = new JSONObject();

	    try {
			int userId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, userId, this.getUserAgent(), String.valueOf(userId));
			
	    	User user = UserDaoService.getUserById(userId);
	        jsonObject = JSONGenerator.toJSON(user);
	        
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
