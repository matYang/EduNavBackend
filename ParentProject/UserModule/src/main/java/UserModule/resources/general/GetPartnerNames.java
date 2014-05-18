package UserModule.resources.general;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.staticDataService.StaticDataService;
import UserModule.resources.UserPseudoResource;

public class GetPartnerNames extends UserPseudoResource{
	
	@Get
	public Representation getDefaultLocation() {
		JSONArray jsonArray = StaticDataService.getPDataJSON();
		Representation result = new JsonRepresentation(jsonArray);
		this.addCORSHeader(); 
		return result;
	}
	
}
