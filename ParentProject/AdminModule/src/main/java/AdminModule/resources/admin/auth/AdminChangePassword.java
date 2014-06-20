package AdminModule.resources.admin.auth;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.AdminAccount;
import BaseModule.service.EncodingService;

public final class AdminChangePassword extends AdminPseudoResource{
	private final String apiId = AdminChangePassword.class.getSimpleName();

	
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
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), "<Password Classified> " + targetAdminId);
			
			AdminAccount admin = AdminAccountDaoService.getAdminAccountById(adminId);
			AdminAccount targetAccount = AdminAccountDaoService.getAdminAccountById(targetAdminId);
			
			//only root or higher admin can change password, aka root changes everything and management changes routine
			if (admin.getPrivilege() == Privilege.root || admin.getPrivilege().code >= targetAccount.getPrivilege().code){
				throw new ValidationException("无权操作");
			}
			
			jsonPassword = this.getJSONObj(entity);

			AdminAccountDaoService.changeAdminPassword(targetAdminId, EncodingService.decodeURI(jsonPassword.getString("password")));
			
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