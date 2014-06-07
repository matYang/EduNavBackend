package UserModule.resources.general;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.common.DebugLog;
import BaseModule.staticDataService.StaticDataService;
import UserModule.resources.UserPseudoResource;

public class GetCategories extends UserPseudoResource{
	private final String apiId = GetCategories.class.getSimpleName();
	
	@Get
	public Representation getDefaultLocation() {
		JSONArray jsonArray = StaticDataService.getCatDataJSON();
		DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), "");
		Representation result = new JsonRepresentation(jsonArray);
		this.addCORSHeader(); 
		return result;
	}
}
