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
import BaseModule.factory.JSONFactory;
import BaseModule.model.AdminAccount;
import BaseModule.service.EncodingService;


public class AdminAccountIdResource extends AdminPseudoResource{
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
	    	//smaller the code higher the privilege
			if (admin.getPrivilege().code >= targetAccount.getPrivilege().code){
				throw new ValidationException("无权操作");
			}
	    	
	        jsonObject = JSONFactory.toJSON(targetAccount);
	        
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
	
	protected JSONObject parseJSON(JSONObject jsonContact) throws ValidationException{

		try {

			jsonContact.put("name", EncodingService.decodeURI(jsonContact.getString("name")));
			jsonContact.put("phone", EncodingService.decodeURI(jsonContact.getString("phone")));
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
			JSONObject jsonContact = this.getJSONObj(entity);
			jsonContact.put("adminId", targetAdminId);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), jsonContact.toString());
			
			AdminAccount admin = AdminAccountDaoService.getAdminAccountById(adminId);
			AdminAccount targetAccount = AdminAccountDaoService.getAdminAccountById(targetAdminId);
			
			//smaller the code higher the privilege
			if (admin.getPrivilege().code >= targetAccount.getPrivilege().code){
				throw new ValidationException("无权操作");
			}
			
			contact = parseJSON(jsonContact);
			
			targetAccount.setName(EncodingService.decodeURI(contact.getString("name")));
			targetAccount.setPhone(EncodingService.decodeURI(contact.getString("phone")));
			targetAccount.setPrivilege(Privilege.routine);
			targetAccount.setStatus(AccountStatus.fromInt(contact.getInt("status")));
			
			//can not changhe an admin to a privilege level higher than self
			if (admin.getPrivilege().code >= targetAccount.getPrivilege().code){
				throw new ValidationException("无权操作");
			}
			AdminAccountDaoService.updateAdminAccount(targetAccount);
			
			response = JSONFactory.toJSON(targetAccount);
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
