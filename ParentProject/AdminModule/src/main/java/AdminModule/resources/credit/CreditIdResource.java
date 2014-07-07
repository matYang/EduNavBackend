package AdminModule.resources.credit;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.CreditDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Credit;

public class CreditIdResource extends AdminPseudoResource{
	private final String apiId = CreditIdResource.class.getSimpleName();
	
	@Get 	    
	public Representation getCreditById() {
	    JSONObject jsonObject = new JSONObject();

	    try {
			int adminId = this.validateAuthentication();
			int creditId = Integer.parseInt(this.getReqAttr("id"));
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(), String.valueOf(creditId));
			
	    	Credit credit = CreditDaoService.getCreditByCreditId(creditId);
	        jsonObject = JSONGenerator.toJSON(credit);
	        
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
