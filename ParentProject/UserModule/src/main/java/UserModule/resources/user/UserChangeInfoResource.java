package UserModule.resources.user;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import org.json.JSONException;

import UserModule.resources.UserPseudoResource;

import java.io.IOException;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.User;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;

public class UserChangeInfoResource extends UserPseudoResource{
	private final String apiId = UserChangeInfoResource.class.getSimpleName();

	
	protected User parseJSON(JSONObject jsonContact, User user) throws ValidationException{

		try {
			String name = EncodingService.decodeURI(jsonContact.getString("name"));
			String email = EncodingService.decodeURI(jsonContact.getString("email"));
			
			if (name != null && name.length() != 0 && !ValidationService.validateName(jsonContact.getString("name"))){
				throw new ValidationException("姓名格式不正确");
			}
			if (email != null && email.length() != 0 & !ValidationService.validateEmail(jsonContact.getString("email"))){
				throw new ValidationException("邮箱格式不正确");
			}
			user.setName(name);
			user.setEmail(email);
			
		} catch (NullPointerException | JSONException | IOException e) {
			DebugLog.d(e);
			throw new ValidationException("数据格式不正确");
		}
		
		return user;
	}
	

	@Put
	/**
	 * allows user to change name
	 */
	public Representation changeContactInfo(Representation entity) {
		int userId = -1;
		JSONObject response = new JSONObject();
		
		try {
			this.checkEntity(entity);
			userId = this.validateAuthentication();
			JSONObject jsonContact = this.getJSONObj(entity);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, userId, this.getUserAgent(), jsonContact.toString());
			
			
			User user = UserDaoService.getAndLock(userId);
			user = parseJSON(jsonContact, user);
			UserDaoService.updateUser(user);
			
			response = JSONFactory.toJSON(user);
			setStatus(Status.SUCCESS_OK);
			
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } catch (Exception e) {
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(response);
		
		this.addCORSHeader(); 
		return result;
	}
}
