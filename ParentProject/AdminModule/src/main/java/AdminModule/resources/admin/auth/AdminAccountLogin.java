package AdminModule.resources.admin.auth;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import AdminModule.dbservice.AdminAccountDaoService;
import AdminModule.model.AdminAccount;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import AdminModule.factory.JSONFactory;
import BaseModule.service.ValidationService;

public class AdminAccountLogin extends AdminPseudoResource{

	@Post
	public Representation loginAuthentication(Representation entity){
		JSONObject jsonString = null;
		AdminAccount account = null;
		JSONObject jsonObject = new JSONObject();
		String phone = "";
		String password = "";


		try {
			this.checkEntity(entity);

			jsonString = (new JsonRepresentation(entity)).getJsonObject();
			phone = jsonString.getString("phone");
			password = jsonString.getString("password");
			if (!ValidationService.isCellNumValid(phone)){
				throw new ValidationException("手机号码格式不正确");
			}
			if (!ValidationService.isPasswordValid(password)){
				throw new ValidationException("密码格式不正确");
			}

			DebugLog.d("Log in, receving paramters: " + phone + " " + password);
			account = AdminAccountDaoService.authenticateAdminAccount(phone, password);


			this.openAuthentication(account.getAdminId());

			jsonObject = JSONFactory.toJSON(account);
			setStatus(Status.SUCCESS_OK);
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
