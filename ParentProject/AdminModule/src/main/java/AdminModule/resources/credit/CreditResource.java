package AdminModule.resources.credit;

import java.util.ArrayList;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.CreditDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Credit;
import BaseModule.model.representation.CreditSearchRepresentation;

public final class CreditResource extends AdminPseudoResource{
	private final String apiId = CreditResource.class.getSimpleName();

	@Get
	public Representation searchCredits() {
		
		JSONArray response = new JSONArray();
		
		try {
			int adminId = this.validateAuthentication();
			CreditSearchRepresentation cr_sr = new CreditSearchRepresentation();
			this.loadRepresentation(cr_sr);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(), cr_sr.toJSON().toString());

			ArrayList<Credit> searchResult = CreditDaoService.searchCredit(cr_sr);
			response = JSONGenerator.toJSON(searchResult);
			
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
