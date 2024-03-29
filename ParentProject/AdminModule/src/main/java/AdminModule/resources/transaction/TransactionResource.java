package AdminModule.resources.transaction;

import java.util.ArrayList;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.TransactionDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Transaction;
import BaseModule.model.representation.TransactionSearchRepresentation;

public final class TransactionResource extends AdminPseudoResource{
	private final String apiId = TransactionResource.class.getSimpleName();

	@Get
	public Representation searchTransactions() {
		
		JSONArray response = new JSONArray();
		
		try {
			int adminId = this.validateAuthentication();
			TransactionSearchRepresentation t_sr = new TransactionSearchRepresentation();
			this.loadRepresentation(t_sr);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(), t_sr.toJSON().toString());

			ArrayList<Transaction> searchResult = TransactionDaoService.searchTransaction(t_sr);
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