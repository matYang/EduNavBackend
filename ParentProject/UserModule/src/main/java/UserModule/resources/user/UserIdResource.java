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
	        int id = -1;
	        int intendedUserId = -1;
	        JSONObject jsonObject = new JSONObject();
	        
	        try {
				id = Integer.parseInt(this.getReqAttr("id"));
				String intendedIdString = this.getQueryVal("intendedUserId"); 
				intendedUserId = intendedIdString != null ? Integer.parseInt(this.getQueryVal("intendedUserId")) : id;				
				
				this.validateAuthentication();
				
				//used for personal page, able to retrieve any user's information
		    	User user = UserDaoService.getUserById(intendedUserId);
		    	if (user != null){
		            jsonObject = JSONFactory.toJSON(user);
		    	}
		    	else{
		    		setStatus(Status.CLIENT_ERROR_FORBIDDEN);
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
