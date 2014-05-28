package UserModule.resources.user;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import UserModule.resources.UserPseudoResource;
import UserModule.service.UserCellVerificationDaoService;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.User;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;

public class UserResource extends UserPseudoResource{

	protected User validateUserJSON(Representation entity) throws ValidationException{
		JSONObject jsonUser = null;
		User user = null;

		try {
			jsonUser = (new JsonRepresentation(entity)).getJsonObject();


			String phone = EncodingService.decodeURI(jsonUser.getString("phone"));
			String name = EncodingService.decodeURI(jsonUser.getString("name"));
			String password = EncodingService.decodeURI(jsonUser.getString("password"));
			String confirmPassword = EncodingService.decodeURI(jsonUser.getString("confirmPassword"));
			String authCode = EncodingService.decodeURI(jsonUser.getString("authCode"));
			String email = EncodingService.decodeURI(jsonUser.getString("email"));
			user = new User(name, phone, password, AccountStatus.activated,email);
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

	/**
	 * Retrieve all users from server. This API is intended solely for testing
	 */
	@Get
	public Representation getAllUsers(){
		JSONArray jsonArray = null;
		try {
			this.validateAuthentication();
			ArrayList<User> allUsers = UserDaoService.getAllUsers();
			jsonArray = JSONFactory.toJSON(allUsers);
		}  catch(PseudoException e){
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
	public Representation createUser(Representation entity) {

		JSONObject newJsonUser = new JSONObject();
		User creationFeedBack = null;

		try{
			this.checkEntity(entity);
			
			User newUser = validateUserJSON(entity);
			ValidationService.validateUser(newUser);
			creationFeedBack = UserDaoService.createUser(newUser);
			//close the sms verification session
			UserCellVerificationDaoService.closeSession(creationFeedBack.getPhone());

			//first close authentication as it is registration, then open brand new authentication
			this.closeAuthentication();
			this.openAuthentication(creationFeedBack.getUserId());

			DebugLog.d("@Post::resources::createUser: available: " + creationFeedBack.getPhone() + " id: " +  creationFeedBack.getUserId());
			newJsonUser = JSONFactory.toJSON(creationFeedBack);

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
