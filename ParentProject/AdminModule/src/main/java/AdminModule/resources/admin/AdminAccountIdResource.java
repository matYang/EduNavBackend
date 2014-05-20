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
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import AdminModule.factory.JSONFactory;


public class AdminAccountIdResource extends AdminPseudoResource{

	@Get 	    
    public Representation getAdminAccountById() {
        JSONObject jsonObject = new JSONObject();
        
        try {
			int adminId = this.validateAuthentication();
			
	    	AdminAccount account = AdminAccountDaoService.getAdminAccountById(adminId);
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
	
	
	
	
	
	
	protected JSONObject parseJSON(Representation entity) throws ValidationException{
		JSONObject jsonContact = null;

		try {
			jsonContact = (new JsonRepresentation(entity)).getJsonObject();
		} catch (JSONException | IOException e) {
			DebugLog.d(e);
			return null;
		}
		
		String name = null;
		try {
			name = java.net.URLDecoder.decode(jsonContact.getString("name"), "utf-8");
		} catch (UnsupportedEncodingException | JSONException e) {
			DebugLog.d(e);
			throw new ValidationException("姓名格式不正确");
		}	
		if (name == null){
			throw new ValidationException("必填数据不能为空");
		}
		
		return jsonContact;
		
	}
	
	@Put
	/**
	 * allows admin to change name
	 */
	public Representation changeContactInfo(Representation entity) {
		int adminId = -1;
		JSONObject response = new JSONObject();
		JSONObject contact = new JSONObject();
		
		try {
			this.checkEntity(entity);
			adminId = this.validateAuthentication();
			
			contact = parseJSON(entity);
				
			AdminAccount account = AdminAccountDaoService.getAdminAccountById(adminId);
			account.setName(contact.getString("name"));					
			AdminAccountDaoService.updateAdminAccount(account);
			
			response = JSONFactory.toJSON(account);
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
