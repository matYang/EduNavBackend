package AdminModule.resources.partner;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Partner;
import BaseModule.model.User;
import BaseModule.model.representation.PartnerSearchRepresentation;
import BaseModule.model.representation.UserSearchRepresentation;
import BaseModule.service.ValidationService;

public class GetPartners extends AdminPseudoResource{
	
	@Get
	public Representation searchPartners(){

		JSONArray response = new JSONArray();
		
		try {
			
			PartnerSearchRepresentation p_sr = new PartnerSearchRepresentation();
			this.loadRepresentation(p_sr);

			ArrayList<Partner> searchResult = PartnerDaoService.searchPartners(p_sr);
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
