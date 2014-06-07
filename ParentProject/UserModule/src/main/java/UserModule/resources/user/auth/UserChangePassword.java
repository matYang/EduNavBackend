package UserModule.resources.user.auth;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.User;
import BaseModule.service.EncodingService;
import BaseModule.service.SMSService;
import BaseModule.service.ValidationService;
import UserModule.resources.UserPseudoResource;
import UserModule.service.UserChangePasswordVerificationDaoService;

public class UserChangePassword extends UserPseudoResource{
	private final String apiId = UserChangePassword.class.getSimpleName();

	@Get
	public Representation changePasswordVerification() {
		
		try{
			int userId = this.validateAuthentication();
			
			String authCode = UserChangePasswordVerificationDaoService.openSession(userId);
			User user = UserDaoService.getUserById(userId);
			
			SMSService.sendUserChangePasswordSMS(user.getPhone(), authCode);
			setStatus(Status.SUCCESS_OK);
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, userId, this.getUserAgent(), "");
		} catch(PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e){
			this.addCORSHeader();
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(new JSONObject());
		this.addCORSHeader(); 
		return result;
	}
	

	protected String[] validateForgetPasswordJSON(int userId, Representation entity) throws ValidationException{
		JSONObject jsonPasswords = null;
		String[] passwords = new String[2];
		
		try{
			jsonPasswords = (new JsonRepresentation(entity)).getJsonObject();
			
			String oldPassword = EncodingService.decodeURI(jsonPasswords.getString("oldPassword"));
			String newPassword = EncodingService.decodeURI(jsonPasswords.getString("newPassword"));
			String confirmNewPassword = EncodingService.decodeURI(jsonPasswords.getString("confirmNewPassword"));
			String authCode = EncodingService.decodeURI(jsonPasswords.getString("authCode"));

			if (!ValidationService.validatePassword(oldPassword)){
				throw new ValidationException("旧密码格式不正确");
			}
			if (!ValidationService.validatePassword(newPassword)){
				throw new ValidationException("新码格式不正确");
			}
			if (!newPassword.equals(confirmNewPassword )){
				throw new ValidationException("两次输入密码不相符");
			}
			if (!UserChangePasswordVerificationDaoService.valdiateSession(userId, authCode)){
				throw new ValidationException("手机验证码错误");
			}
			passwords[0] = oldPassword;
			passwords[1] = newPassword;
			
		} catch (JSONException | IOException e){
			throw new ValidationException("无效数据格式");
		}
		
		return passwords;
	}
	

	@Put
	public Representation changePassword(Representation entity) {
		String[] passwords = new String[2];
		String quickResponseText = "";
		
		try {
			this.checkEntity(entity);
			int userId = this.validateAuthentication();

			passwords = validateForgetPasswordJSON(userId, entity);

			UserDaoService.changePassword(userId, passwords[0], passwords[1]);
			UserChangePasswordVerificationDaoService.closeSession(userId);
			
			//open new authentication, log every other client out
			this.closeAuthentication();
			this.openAuthentication(userId);

			setStatus(Status.SUCCESS_OK);
			quickResponseText = "密码修改成功";
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, userId, this.getUserAgent(), (new JsonRepresentation(entity)).getJsonObject().toString());
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } catch (Exception e){
        	return this.doException(e);
		}
		
		this.addCORSHeader();
		return this.quickRespond(quickResponseText);

	}
	

}
