package AdminModule.resources.admin;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import AdminModule.dbservice.AdminAccountDaoService;
import AdminModule.model.AdminAccount;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import AdminModule.factory.JSONFactory;
import BaseModule.service.ValidationService;

public class AdminAccountResource extends AdminPseudoResource{

	@Post
	public Representation createAdminAccount(Representation entity) {
		
		JSONObject newJsonAdmin = new JSONObject();
		AdminAccount creationFeedBack = null;
		
		try{
			this.checkEntity(entity);			
			AdminAccount account = validateAdminAccountJSON(entity);
			creationFeedBack = AdminAccountDaoService.createAdminAccount(account);			
			
			//first close authentication as it is registration, then open brand new authentication
			this.closeAuthentication();
			this.openAuthentication(creationFeedBack.getAdminId());
			DebugLog.d("@Post::resources::createAdminAccount: available: " + creationFeedBack.getPhone() + " id: " +  creationFeedBack.getAdminId());
			newJsonAdmin = JSONFactory.toJSON(creationFeedBack);

		} catch(PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e){
			this.addCORSHeader();
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(newJsonAdmin);
		
		this.addCORSHeader(); 
		return result;
	}
	protected AdminAccount validateAdminAccountJSON(Representation entity) throws ValidationException{
		JSONObject jsonAdmin = null;
		AdminAccount account = null;
		
		try {
			jsonAdmin = (new JsonRepresentation(entity)).getJsonObject();
			
			
			String phone = jsonAdmin.getString("phone");
			String name = jsonAdmin.getString("name");
			String reference = jsonAdmin.getString("reference");
			String password = jsonAdmin.getString("password");
			String confirmPassword = jsonAdmin.getString("confirmPassword");			
			Privilege privilege = Privilege.fromInt(jsonAdmin.getInt("privilege"));
			AccountStatus status = AccountStatus.fromInt(jsonAdmin.getInt("status"));
			
			account = new  AdminAccount(name, phone,reference,privilege,status,password);
			if (!ValidationService.isNameValid(name)){
				throw new ValidationException("姓名格式不正确");
			}
			if (!ValidationService.isCellNumValid(phone)){
				throw new ValidationException("手机号码格式不正确");
			}
			if (!ValidationService.isPasswordValid(password)){
				throw new ValidationException("密码格式不正确");
			}
			if (!password.equals(confirmPassword)){
				throw new ValidationException("两次输入密码不相符");
			}			

		} catch (JSONException | IOException e) {
			throw new ValidationException("无效数据格式");
		}
		return account;
	}
	
	/**
	 * Retrieve all admins from server. This API is intended solely for testing
	 */
	@Get
	public Representation getAllAdminAccounts(){
		JSONArray jsonArray = null;
		try {
			this.validateAuthentication();
			ArrayList<AdminAccount> allAdminAccounts = AdminAccountDaoService.getAllAdminAccounts();
			jsonArray = JSONFactory.toJSON(allAdminAccounts);
		}  catch(PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		}	
		
		Representation result = new JsonRepresentation(jsonArray);

		this.addCORSHeader();
		return result;
	}
}
