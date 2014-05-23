package AdminModule.resources.admin.auth;

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
import AdminModule.factory.AdminJSONFactory;
import AdminModule.model.AdminAccount;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.service.EncodingService;

public class AdminChangePassword extends AdminPseudoResource{


	protected JSONObject parseJSON(Representation entity) throws ValidationException{
		try {
			return (new JsonRepresentation(entity)).getJsonObject();
		} catch (JSONException | IOException e) {
			DebugLog.d(e);
			throw new ValidationException("密码格式不正确");
		}	
	}
	
	@Put
	/**
	 * allows admin to change name
	 */
	public Representation changeContactInfo(Representation entity) {
		JSONObject response = new JSONObject();
		JSONObject jsonPassword = new JSONObject();
		
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
			
			jsonPassword = parseJSON(entity);
			

			AdminAccountDaoService.changeAdminPassword(targetAdminId, EncodingService.decodeURI(jsonPassword.getString("password")));
			
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