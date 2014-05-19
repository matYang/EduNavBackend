package PartnerModule.resources.partner;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Partner;

import PartnerModule.resources.PartnerPseudoResource;

public class PartnerIdResource extends PartnerPseudoResource{

	 @Get 	    
	    public Representation getPartnerById() {
	        JSONObject jsonObject = new JSONObject();
	        
	        try {
				int partnerId = this.validateAuthentication();
				
		    	Partner partner = PartnerDaoService.getPartnerById(partnerId);
		        jsonObject = JSONFactory.toJSON(partner);
		        
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
