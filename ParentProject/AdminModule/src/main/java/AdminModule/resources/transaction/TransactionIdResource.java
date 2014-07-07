package AdminModule.resources.transaction;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.TransactionDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Transaction;

public class TransactionIdResource extends AdminPseudoResource{
	private final String apiId = TransactionIdResource.class.getSimpleName();
	
	@Get 	    
	public Representation getTransactionById() {
	    JSONObject jsonObject = new JSONObject();

	    try {
			int adminId = this.validateAuthentication();
			long tranasctionId = Long.parseLong(this.getReqAttr("id"));
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(), String.valueOf(tranasctionId));
			
	    	Transaction transaction = TransactionDaoService.getTransactionByTransactionId(tranasctionId);
	        jsonObject = JSONGenerator.toJSON(transaction);
	        
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
