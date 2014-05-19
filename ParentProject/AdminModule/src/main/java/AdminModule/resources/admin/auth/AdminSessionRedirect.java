package AdminModule.resources.admin.auth;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import AdminModule.dbservice.AdminAccountDaoService;
import AdminModule.model.AdminAccount;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;
import AdminModule.factory.JSONFactory;


public class AdminSessionRedirect extends AdminPseudoResource{

	@Get
	public Representation sessionRedirect(Representation entity){
		DebugLog.d("SessionDirect:: Enter session redirect");
		
		AdminAccount account = null;
		JSONObject jsonObject = new JSONObject();
	
		try {
			int accountId = this.validateAuthentication();
			account = AdminAccountDaoService.getAdminAccountById(accountId);
			jsonObject = JSONFactory.toJSON(account);
		} catch (AuthenticationException e){
			//if not authenticated, return default user with id -1
			account = new AdminAccount("default", "default","default", Privilege.economy, AccountStatus.activated,"default");
			account.setAdminId(-1);
			jsonObject = JSONFactory.toJSON(account);
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
