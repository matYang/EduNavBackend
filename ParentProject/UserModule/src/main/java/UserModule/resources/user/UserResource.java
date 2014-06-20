package UserModule.resources.user;

import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import UserModule.resources.UserPseudoResource;
import UserModule.service.UserCellVerificationDaoService;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.factory.ReferenceFactory;
import BaseModule.model.User;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;

public class UserResource extends UserPseudoResource{
	private final String apiId = UserResource.class.getSimpleName();
	

	protected User validateUserJSON(JSONObject jsonUser) throws ValidationException, SQLException{
		User user = null;

		try {

			String phone = EncodingService.decodeURI(jsonUser.getString("phone"));
			String password = EncodingService.decodeURI(jsonUser.getString("password"));
			String confirmPassword = EncodingService.decodeURI(jsonUser.getString("confirmPassword"));
			String authCode = EncodingService.decodeURI(jsonUser.getString("authCode"));
			String appliedInvitationalCode = EncodingService.decodeURI(jsonUser.getString("appliedInvitationalCode"));
			if (appliedInvitationalCode == null || appliedInvitationalCode.length() == 0){
				appliedInvitationalCode = "";
			}
			String invitationalCode = ReferenceFactory.generateUserInvitationalCode();
			String accountNumber = ReferenceFactory.generateUserAccountNumber();
			user = new User(phone, password,appliedInvitationalCode, invitationalCode, accountNumber, AccountStatus.activated);
			user.setName("爱会员");
			
			if (!ValidationService.validatePhone(phone)){
				throw new ValidationException("手机号码格式不正确");
			}
			if (!ValidationService.validatePassword(password)){
				throw new ValidationException("密码格式不正确");
			}
			if (!password.equals(confirmPassword)){
				throw new ValidationException("两次输入密码不相符");
			}
			if (!UserCellVerificationDaoService.valdiateSession(phone, authCode)){
				throw new ValidationException("手机验证码错误");
			}
			if (!UserDaoService.isCellPhoneAvailable(phone)){
				throw new ValidationException("手机号码已经被注册");
			}

		} catch (JSONException | IOException e) {
			throw new ValidationException("无效数据格式");
		}
		return user;
	}



	@Post
	public Representation createUser(Representation entity) {

		JSONObject newJsonUser = new JSONObject();
		User creationFeedBack = null;

		try{
			this.checkEntity(entity);
			try{
				this.validateAuthentication();
				throw new ValidationException("您处于已登录状态，请刷新页面或先登出之前的账户");
			} catch (AuthenticationException e){
				//not logged in, proceed
			}

			JSONObject jsonUser = this.getJSONObj(entity);
			User newUser = validateUserJSON(jsonUser);
			
			ValidationService.validateUser(newUser);
			creationFeedBack = UserDaoService.createUser(newUser);
			
			//close the sms verification session
			UserCellVerificationDaoService.closeSession(creationFeedBack.getPhone());

			//first close authentication as it is registration, then open brand new authentication
			this.closeAuthentication();
			this.openAuthentication(creationFeedBack.getUserId());

			DebugLog.d("@Post::resources::createUser: available: " + creationFeedBack.getPhone() + " id: " +  creationFeedBack.getUserId());
			newJsonUser = JSONFactory.toJSON(creationFeedBack);
			
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, creationFeedBack.getUserId(), this.getUserAgent(), creationFeedBack.getPhone());
		} catch(PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e){
			this.addCORSHeader();
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(newJsonUser);
		this.addCORSHeader(); 
		return result;
	}
}
