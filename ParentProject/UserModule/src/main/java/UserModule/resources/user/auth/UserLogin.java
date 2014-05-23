package UserModule.resources.user.auth;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.User;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;
import UserModule.resources.UserPseudoResource;

public class UserLogin extends UserPseudoResource {
	
	@Post
	public Representation loginAuthentication(Representation entity){
		JSONObject jsonString = null;
		User user = null;
		JSONObject jsonObject = new JSONObject();
		String phone = "";
		String password = "";
		

		try {
			this.checkEntity(entity);
			
			jsonString = (new JsonRepresentation(entity)).getJsonObject();
			phone = EncodingService.decodeURI(jsonString.getString("phone"));
			password = EncodingService.decodeURI(jsonString.getString("password"));
			if (!ValidationService.validatePhone(phone)){
				throw new ValidationException("手机号码格式不正确");
			}
			if (!ValidationService.validatePassword(password)){
				throw new ValidationException("密码格式不正确");
			}
			
			DebugLog.d("Log in, receving paramters: " + phone + " " + password);
			user = UserDaoService.authenticateUser(phone, password);
			

			this.openAuthentication(user.getUserId());

			jsonObject = JSONFactory.toJSON(user);
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
