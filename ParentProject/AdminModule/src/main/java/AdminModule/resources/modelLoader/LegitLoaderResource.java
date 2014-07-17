package AdminModule.resources.modelLoader;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.service.LegitLoadService;

public class LegitLoaderResource extends AdminPseudoResource{
	
	@Get
	public Representation load(){
		try{		
			LegitLoadService.legitLoad();
			setStatus(Status.SUCCESS_OK);
		} catch (Exception e){
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(new JSONObject());
		this.addCORSHeader();
		return result;
	}
}
