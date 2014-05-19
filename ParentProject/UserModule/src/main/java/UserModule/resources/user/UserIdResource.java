package UserModule.resources.user;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import UserModule.resources.UserPseudoResource;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.User;


public class UserIdResource extends UserPseudoResource{

	 @Get 	    
	    public Representation getUerById() {
	        JSONObject jsonObject = new JSONObject();
	        
	        try {
				int userId = this.validateAuthentication();
				
		    	User user = UserDaoService.getUserById(userId);
		        jsonObject = JSONFactory.toJSON(user);
		        
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
