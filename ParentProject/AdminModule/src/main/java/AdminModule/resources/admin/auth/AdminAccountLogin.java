package AdminModule.resources.admin.auth;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.AdminAccount;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;

public class AdminAccountLogin extends AdminPseudoResource{
	private final String apiId = AdminAccountLogin.class.getSimpleName();

	@Post
	public Representation loginAuthentication(Representation entity){
		JSONObject jsonString = null;
		AdminAccount account = null;
		JSONObject jsonObject = new JSONObject();
		String reference = "";
		String password = "";


		try {
			this.checkEntity(entity);
			
			try{
				this.validateAuthentication();
				throw new ValidationException("请刷新页面或先登出之前的账户");
			} catch (AuthenticationException e){
				//not logged in, proceed
			}

			jsonString = this.getJSONObj(entity);
			reference = EncodingService.decodeURI(jsonString.getString("reference"));
			password = EncodingService.decodeURI(jsonString.getString("password"));

			if (!ValidationService.validatePassword(password)){
				throw new ValidationException("密码格式不正确");
			}

			account = AdminAccountDaoService.authenticateAdminAccount(reference, password);
			this.openAuthentication(account.getAdminId());

			jsonObject = JSONFactory.toJSON(account);
			setStatus(Status.SUCCESS_OK);
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, account.getAdminId(), this.getUserAgent(), "<Password Classified> " + account.getReference());
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} 
		catch (Exception e) {
			return this.doException(e);
		}


		Representation result = new JsonRepresentation(jsonObject);

		this.addCORSHeader();
		return result;
	}
}
