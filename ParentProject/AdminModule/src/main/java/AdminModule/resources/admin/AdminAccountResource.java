package AdminModule.resources.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.encryption.AdminSecretCrypto;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.factory.ReferenceFactory;
import BaseModule.model.AdminAccount;
import BaseModule.model.representation.AdminSearchRepresentation;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;

public class AdminAccountResource extends AdminPseudoResource{
	private final String apiId = AdminAccountResource.class.getSimpleName();
	
	private static final String superAdminKey_1 = "09b15bc3a4654b33bdb01211c933e53fd9c8544503d3f42ebac4e8d1f7f80517924e319250db4a8555fcdb10f04fbc97420ae368ba853d07715524f52315004e:28e88722e173c317c6304f7fd416f5e412c55e037bfe8de5d9d227c4f248c28a8f27c5641010a59f9541004bc24be99a25a2a499a5540a619e5471acb59882a5";
	private static final String superAdminKey_2 = "3a700b28c85920c179ff1a2f1401fe663bf875bddb5bd5760367e1673129a3b44dff311280e5e1925bdc516350ffe26d2a7579ae7c55f70030e7ce95cda1cf51:ca71821f46aec63fe2ea0e51402d29373a11ad5f7cabc873f82dc55e7ef57e6191caf2bccbf0f42b365169c63fc09fa025669e53f9f5b922b4cf72fb74750a67";
	private static final String superAdminKey_3 = "9a35138d0b2acb5378884909e45f0cd7d8767262acb3fa743e7b4049e40ccd2ad991a8e74fd1bf52d0116c2d6d3add7afa987f171206059aef7e9d7cc183c9d5:0610f9d9a44e572e05addae3969d45ad812bea9e723fcdecbc3e1789168913372fa72e98f2efed55562ca01586e7b63c937290ce1428a80f4849ea6b592ff6a7";
	

	@Get
	public Representation SearchAccounts(){
		JSONArray jsonArray = null;
			
		try{
			AdminSearchRepresentation a_sr = new AdminSearchRepresentation();
			this.loadRepresentation(a_sr);
			
			String secret_1 = this.getQueryVal("secret1");
			String secret_2 = this.getQueryVal("secret2");
			String secret_3 = this.getQueryVal("secret3");
			if (!AdminSecretCrypto.validatePassword(secret_1, superAdminKey_1) || !AdminSecretCrypto.validatePassword(secret_2, superAdminKey_2) || !AdminSecretCrypto.validatePassword(secret_3, superAdminKey_3) ){
				int adminId = this.validateAuthentication();
				DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(), a_sr.serialize());
				
				AdminAccount admin = AdminAccountDaoService.getAdminAccountById(adminId);
				if (admin.getPrivilege() != Privilege.root){
					throw new AuthenticationException();
				}
			}
			else{
				DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), a_sr.serialize());
			}
			
			
			ArrayList<AdminAccount> admins = AdminAccountDaoService.searchAdminAccount(a_sr);
			jsonArray = JSONFactory.toJSON(admins);
		} catch(PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e){
			this.addCORSHeader();
			return this.doException(e);
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
			JSONObject jsonAdmin = this.getJSONObj(entity);
			AdminAccount account = validateAdminAccountJSON(jsonAdmin);
			try{
				int adminId = this.validateAuthentication();
				DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, adminId, this.getUserAgent(), "<Password Classified> " + account.getReference());
				
				AdminAccount admin = AdminAccountDaoService.getAdminAccountById(adminId);
				if (admin.getPrivilege() == Privilege.root){
					//do nothing, root can manage everyone
				}
				else if (admin.getPrivilege() == Privilege.mamagement){
					//management can only modify routine
					if (account.getPrivilege() != Privilege.routine){
						throw new ValidationException("无权创建该类型账号");
					}
				}
				else{
					//
					throw new ValidationException("无权操作");
				}
			} catch (AuthenticationException e){
				String secret_1 = this.getQueryVal("secret1");
				String secret_2 = this.getQueryVal("secret2");
				String secret_3 = this.getQueryVal("secret3");
				if (!AdminSecretCrypto.validatePassword(secret_1, superAdminKey_1) || !AdminSecretCrypto.validatePassword(secret_2, superAdminKey_2) || !AdminSecretCrypto.validatePassword(secret_3, superAdminKey_3) ){
					throw e;
				}
				else{
					DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, -1, this.getUserAgent(), "<Password Classified> " + account.getReference());
				}
			}
				
			
			creationFeedBack = AdminAccountDaoService.createAdminAccount(account);			
			
			//first close authentication as it is registration, then open brand new authentication
			//not really logging in here as it is created by another admin
			//this.closeAuthentication();
			//this.openAuthentication(creationFeedBack.getAdminId());

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
	
	
	protected AdminAccount validateAdminAccountJSON(JSONObject jsonAdmin) throws ValidationException, SQLException{
		AdminAccount account = null;
		
		try {
			
			String phone = EncodingService.decodeURI(jsonAdmin.getString("phone"));
			String name = EncodingService.decodeURI(jsonAdmin.getString("name"));
			String reference = ReferenceFactory.generateAdminReference();
			String password = EncodingService.decodeURI(jsonAdmin.getString("password"));
			String confirmPassword = EncodingService.decodeURI(jsonAdmin.getString("confirmPassword"));			
			Privilege privilege = Privilege.fromInt(jsonAdmin.getInt("privilege"));
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
