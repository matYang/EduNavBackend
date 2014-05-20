package AdminModule.resources.partner;


import java.util.ArrayList;
import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Partner;
import BaseModule.model.representation.PartnerSearchRepresentation;


public class GetPartners extends AdminPseudoResource{
	
	@Get
	public Representation searchPartners(){

		JSONArray response = new JSONArray();
		
		try {
			this.validateAuthentication();
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
