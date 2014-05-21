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
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.ReferenceFactory;
import AdminModule.factory.JSONFactory;
import BaseModule.service.ValidationService;

public class AdminAccountResource extends AdminPseudoResource{
	public static final String superAdminKey = "fhoFSE8932hDFfds9HFS";
	
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
	

	@Post
	public Representation createAdminAccount(Representation entity) {
		
		JSONObject newJsonAdmin = new JSONObject();
		AdminAccount creationFeedBack = null;
		
		try{
			this.checkEntity(entity);
			AdminAccount account = validateAdminAccountJSON(entity);
			try{
				int adminId = this.validateAuthentication();
				AdminAccount admin = AdminAccountDaoService.getAdminAccountById(adminId);
				if (admin.getPrivilege() == Privilege.root){
					account.setPrivilege(Privilege.mamagement);
				}
				else if (admin.getPrivilege() == Privilege.mamagement){
					account.setPrivilege(Privilege.routine);
				}
				else{
					throw new ValidationException("无权操作");
				}
			} catch (AuthenticationException e){
				String secret = this.getQueryVal("secret");
				if (!superAdminKey.equals(secret)){
					throw e;
				}
				else{
					account.setPrivilege(Privilege.root);
				}
			}
				
			
			creationFeedBack = AdminAccountDaoService.createAdminAccount(account);			
			
			//first close authentication as it is registration, then open brand new authentication
			this.closeAuthentication();
			this.openAuthentication(creationFeedBack.getAdminId());

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
			String reference = ReferenceFactory.generateAdminReference();
			String password = jsonAdmin.getString("password");
			String confirmPassword = jsonAdmin.getString("confirmPassword");			
			Privilege privilege = Privilege.routine;
			AccountStatus status = AccountStatus.fromInt(jsonAdmin.getInt("status"));
			
			account = new  AdminAccount(name, phone,reference,privilege,status,password);
			if (!ValidationService.validateName(name)){
				throw new ValidationException("姓名格式不正确");
			}
			if (!ValidationService.validatePhone(phone)){
				throw new ValidationException("手机号码格式不正确");
			}
			if (!ValidationService.validatePassword(password)){
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

}
