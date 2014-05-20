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
import BaseModule.service.ValidationService;

public class UserChangeInfoResource extends UserPseudoResource{

	protected JSONObject parseJSON(Representation entity) throws ValidationException{
		JSONObject jsonContact = null;

		try {
			jsonContact = (new JsonRepresentation(entity)).getJsonObject();
			jsonContact.put("name", java.net.URLDecoder.decode(jsonContact.getString("name"), "utf-8"));
			
			if (ValidationService.validateName(jsonContact.getString("name"))){
				throw new ValidationException("姓名格式不正确");
			}
			
		} catch (NullPointerException | JSONException | IOException e) {
			DebugLog.d(e);
			throw new ValidationException("姓名格式不正确");
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
			
			contact = parseJSON(entity);
				
			User user = UserDaoService.getUserById(userId);
			user.setName(contact.getString("name"));					
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
