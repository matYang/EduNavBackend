package AdminModule.resources.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.User;
import BaseModule.model.representation.UserSearchRepresentation;

public class GetUsers extends AdminPseudoResource{

	@Get
	public Representation searchUsers() {
		
		JSONArray response = new JSONArray();
		
		try {
			this.validateAuthentication();
			UserSearchRepresentation u_sr = new UserSearchRepresentation();
			this.loadRepresentation(u_sr);

			ArrayList<User> searchResult = UserDaoService.searchUser(u_sr);
			response = JSONFactory.toJSON(searchResult);
			
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
	    } catch (Exception e){
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(response);
		this.addCORSHeader();
		return result;
		
	}
}
