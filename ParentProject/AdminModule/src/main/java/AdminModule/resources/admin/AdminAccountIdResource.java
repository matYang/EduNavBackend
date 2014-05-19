package AdminModule.resources.admin;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import AdminModule.dbservice.AdminAccountDaoService;
import AdminModule.model.AdminAccount;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.exception.PseudoException;
import AdminModule.factory.JSONFactory;


public class AdminAccountIdResource extends AdminPseudoResource{

	@Get 	    
    public Representation getAdminAccountById() {
        JSONObject jsonObject = new JSONObject();
        
        try {
			int accountId = this.validateAuthentication();
			
	    	AdminAccount account = AdminAccountDaoService.getAdminAccountById(accountId);
	        jsonObject = JSONFactory.toJSON(account);
	        
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
