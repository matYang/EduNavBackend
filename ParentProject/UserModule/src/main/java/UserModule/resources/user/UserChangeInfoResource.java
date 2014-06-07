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

	
	protected JSONObject parseJSON(JSONObject jsonContact) throws ValidationException{

		try {
			jsonContact.put("name", EncodingService.decodeURI(jsonContact.getString("name")));
			jsonContact.put("email", EncodingService.decodeURI(jsonContact.getString("email")));
			
			if (!ValidationService.validateName(jsonContact.getString("name"))){
				throw new ValidationException("姓名格式不正确");
			}
			if (!ValidationService.validateEmail(jsonContact.getString("email"))){
				throw new ValidationException("邮箱格式不正确");
			}
			
		} catch (NullPointerException | JSONException | IOException e) {
			DebugLog.d(e);
			throw new ValidationException("数据格式不正确");
		}
		
		return jsonContact;
	}
	

	@Put
	/**
	 * allows user to change name
	 */
	public Representation changeContactInfo(Representation entity) {
		int userId = -1;
		JSONObject response = new JSONObject();
		JSONObject contact = new JSONObject();
		
		try {
			this.checkEntity(entity);
			userId = this.validateAuthentication();
			JSONObject jsonContact = this.getJSONObj(entity);
			contact = parseJSON(jsonContact);
				
			User user = UserDaoService.getUserById(userId);
			user.setName(contact.getString("name"));
			user.setEmail(contact.getString("email"));
			UserDaoService.updateUser(user);
			
			response = JSONFactory.toJSON(user);
			setStatus(Status.SUCCESS_OK);
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, userId, this.getUserAgent(), jsonContact.toString());
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
