package UserModule.resources.general;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.common.DebugLog;
import BaseModule.staticDataService.StaticDataService;
import UserModule.resources.UserPseudoResource;

public final class GetCategories extends UserPseudoResource{
	private final String apiId = GetCategories.class.getSimpleName();
	
	@Get
	public Representation getDefaultLocation() {
		DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), "");
		
		JSONObject jsonObject = StaticDataService.getCatDataJSON();
		Representation result = new JsonRepresentation(jsonObject);
		this.addCORSHeader(); 
		return result;
	}
}
