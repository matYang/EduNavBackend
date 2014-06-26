package AdminModule.resources.admin.auth;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.notFound.AdminAccountNotFoundException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.AdminAccount;


public final class AdminSessionRedirect extends AdminPseudoResource{
	private final String apiId = AdminSessionRedirect.class.getSimpleName();

	@Get
	public Representation sessionRedirect(Representation entity){
		DebugLog.d("SessionDirect:: Enter session redirect");
		
		AdminAccount account = null;
		JSONObject jsonObject = new JSONObject();
	
		try {
			int accountId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, accountId, this.getUserAgent(), "");
			
			account = AdminAccountDaoService.getAdminAccountById(accountId);
			jsonObject = JSONGenerator.toJSON(account);
		} catch (AuthenticationException | AdminAccountNotFoundException e){
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), "");
			
			//if not authenticated, return default user with id -1
			account = new AdminAccount("default", "default","default", Privilege.routine, AccountStatus.activated,"default");
			account.setAdminId(-1);
			try {
				jsonObject = JSONGenerator.toJSON(account);
			} catch (PseudoException e1){
				this.addCORSHeader();
				return this.doPseudoException(e1);
			} catch (Exception e1) {
				return this.doException(e);
			}
			
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		}  catch (Exception e) {
			return this.doException(e);
		}
	
		Representation result = new JsonRepresentation(jsonObject);
	    
	    this.addCORSHeader();
	    return result;
	}
}
