package UserModule.resources.user.auth;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.User;
import BaseModule.service.EncodingService;
import BaseModule.service.RedisAccessControlService;
import BaseModule.service.ValidationService;
import UserModule.resources.UserPseudoResource;

public final class UserLogin extends UserPseudoResource {
	private final String apiId = UserLogin.class.getSimpleName();

	@Post
	public Representation loginAuthentication(Representation entity){
		JSONObject jsonString = null;
		User user = null;
		JSONObject jsonObject = new JSONObject();
		String phone = "";
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
			phone = EncodingService.decodeURI(jsonString.getString("phone"));
			password = EncodingService.decodeURI(jsonString.getString("password"));
			if (!RedisAccessControlService.isAbleToLogin(this.moduleId, phone)){
				throw new ValidationException("您已多次登录失败，请等待一分钟");
			}
			if (!ValidationService.validatePhone(phone)){
				throw new ValidationException("手机号码格式不正确");
			}
			if (!ValidationService.validatePassword(password)){
				throw new ValidationException("密码格式不正确");
			}
			
			DebugLog.d("Log in, receving paramters: " + phone + " " + password);
			try{
				user = UserDaoService.authenticateUser(phone, password);
			} catch (PseudoException e){
				RedisAccessControlService.fail(this.moduleId, phone);
				throw e;
			}
			
			RedisAccessControlService.success(this.moduleId, phone);
			this.openAuthentication(user.getUserId());
			
			jsonObject = JSONGenerator.toJSON(user);
			setStatus(Status.SUCCESS_OK);
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, user.getUserId(), this.getUserAgent(), phone);
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
