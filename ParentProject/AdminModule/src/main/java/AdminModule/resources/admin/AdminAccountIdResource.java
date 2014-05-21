package AdminModule.resources.admin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import AdminModule.dbservice.AdminAccountDaoService;
import AdminModule.model.AdminAccount;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import AdminModule.factory.AdminJSONFactory;


public class AdminAccountIdResource extends AdminPseudoResource{

	@Get 	    
    public Representation getAdminAccountById() {
        JSONObject jsonObject = new JSONObject();
        
        try {
			int adminId = this.validateAuthentication();
			int targetAdminId = Integer.parseInt(this.getReqAttr("id"));
			
			
	    	AdminAccount admin = AdminAccountDaoService.getAdminAccountById(adminId);
	    	AdminAccount targetAccount = AdminAccountDaoService.getAdminAccountById(targetAdminId);
	    	//smaller the code higher the privilege
			if (admin.getPrivilege().code >= targetAccount.getPrivilege().code){
				throw new ValidationException("无权操作");
			}
	    	
	        jsonObject = AdminJSONFactory.toJSON(targetAccount);
	        
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
	
	protected JSONObject parseJSON(Representation entity) throws ValidationException{
		JSONObject jsonContact = null;

		try {
			jsonContact = (new JsonRepresentation(entity)).getJsonObject();
			jsonContact.put("name", java.net.URLDecoder.decode(jsonContact.getString("name"), "utf-8"));
		} catch (JSONException | IOException e) {
			DebugLog.d(e);
			throw new ValidationException("姓名格式不正确");
		}	
		
		return jsonContact;
		
	}
	
	@Put
	/**
	 * allows admin to change name
	 */
	public Representation changeContactInfo(Representation entity) {
		JSONObject response = new JSONObject();
		JSONObject contact = new JSONObject();
		
		try {
			this.checkEntity(entity);

			int adminId = this.validateAuthentication();
			int targetAdminId = Integer.parseInt(this.getReqAttr("id"));
			
			AdminAccount admin = AdminAccountDaoService.getAdminAccountById(adminId);
			AdminAccount targetAccount = AdminAccountDaoService.getAdminAccountById(targetAdminId);
			
			//smaller the code higher the privilege
			if (admin.getPrivilege().code >= targetAccount.getPrivilege().code){
				throw new ValidationException("无权操作");
			}
			
			contact = parseJSON(entity);
			
			targetAccount.setPhone(contact.getString("phone"));
			targetAccount.setPrivilege(Privilege.routine);
			targetAccount.setReference(contact.getString("reference"));
			targetAccount.setStatus(AccountStatus.fromInt(contact.getInt("status")));
			targetAccount.setName(contact.getString("name"));	
			AdminAccountDaoService.updateAdminAccount(targetAccount);
			
			response = AdminJSONFactory.toJSON(targetAccount);
			setStatus(Status.SUCCESS_OK);

		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } catch (Exception e) {
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(response);
		
		this.addCORSHeader(); 
		return result;
	}
	
	
}
