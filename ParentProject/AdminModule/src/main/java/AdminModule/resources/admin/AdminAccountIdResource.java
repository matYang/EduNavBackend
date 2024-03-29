package AdminModule.resources.admin;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.AdminAccount;
import BaseModule.service.EncodingService;


public final class AdminAccountIdResource extends AdminPseudoResource{
	private final String apiId = AdminAccountIdResource.class.getSimpleName();

	@Get 	    
    public Representation getAdminAccountById() {
        JSONObject jsonObject = new JSONObject();
        
        try {
			int adminId = this.validateAuthentication();
			int targetAdminId = Integer.parseInt(this.getReqAttr("id"));
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(),String.valueOf(targetAdminId));
			
	    	AdminAccount admin = AdminAccountDaoService.getAdminAccountById(adminId);
	    	AdminAccount targetAccount = AdminAccountDaoService.getAdminAccountById(targetAdminId);
	    	//smaller the code higher the privilege, can view same level accounts
			if (admin.getPrivilege().code > targetAccount.getPrivilege().code){
				throw new ValidationException("无权操作");
			}
	    	
	        jsonObject = JSONGenerator.toJSON(targetAccount);
	        
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
	
	protected AdminAccount parseJSON(JSONObject contact, AdminAccount account) throws ValidationException{

		try {

			account.setName(EncodingService.decodeURI(contact.getString("name")));
			account.setPhone(EncodingService.decodeURI(contact.getString("phone")));
			account.setPrivilege(Privilege.fromInt(contact.getInt("privilege")));
			account.setStatus(AccountStatus.fromInt(contact.getInt("status")));
		} catch (JSONException | IOException e) {
			DebugLog.d(e);
			throw new ValidationException("姓名格式不正确");
		}	
		
		return account;
		
	}
	
	@Put
	/**
	 * allows admin to change name
	 */
	public Representation changeContactInfo(Representation entity) {
		JSONObject response = new JSONObject();
		
		try {
			this.checkEntity(entity);

			int adminId = this.validateAuthentication();
			int targetAdminId = Integer.parseInt(this.getReqAttr("id"));
			JSONObject jsonContact = this.getJSONObj(entity);
			jsonContact.put("adminId", targetAdminId);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), jsonContact.toString());
			
			AdminAccount admin = AdminAccountDaoService.getAdminAccountById(adminId);
			AdminAccount targetAccount = AdminAccountDaoService.getAdminAccountById(targetAdminId);
			
			//can not change an admin with a privilege level that is not lower than self, unless it is root
			if (admin.getPrivilege() == Privilege.root || admin.getPrivilege().code >= targetAccount.getPrivilege().code){
				throw new ValidationException("无权操作");
			}
			
			targetAccount = parseJSON(jsonContact, targetAccount);
			
			//after the change, preconditions must still hold, and admin can not change its info by itself unless root
			if (admin.getPrivilege() == Privilege.root || admin.getPrivilege().code >= targetAccount.getPrivilege().code){
				throw new ValidationException("无权操作");
			}
			AdminAccountDaoService.updateAdminAccount(targetAccount);
			
			response = JSONGenerator.toJSON(targetAccount);
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
