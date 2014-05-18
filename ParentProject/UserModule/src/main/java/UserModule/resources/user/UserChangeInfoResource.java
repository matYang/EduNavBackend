package UserModule.resources.user;
import java.io.UnsupportedEncodingException;
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
import BaseModule.exception.user.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.User;

public class UserChangeInfoResource extends UserPseudoResource{

	protected JSONObject parseJSON(Representation entity) throws ValidationException{
		JSONObject jsonContact = null;

		try {
			jsonContact = (new JsonRepresentation(entity)).getJsonObject();
		} catch (JSONException | IOException e) {
			DebugLog.d(e);
			return null;
		}
		
		String name = null;
		try {
			name = java.net.URLDecoder.decode(jsonContact.getString("name"), "utf-8");
		} catch (UnsupportedEncodingException | JSONException e) {
			DebugLog.d(e);
			throw new ValidationException("姓名格式不正确");
		}	
		
		if (name == null){
			throw new ValidationException("必填数据不能为空");
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
			
			userId = Integer.parseInt(this.getReqAttr("id"));
			this.validateAuthentication();
			
			contact = parseJSON(entity);
			if (contact != null){
				
				User user = UserDaoService.getUserById(userId);
				user.setName(contact.getString("name"));					
				UserDaoService.updateUser(user);
				
				response = JSONFactory.toJSON(user);
				setStatus(Status.SUCCESS_OK);
			}
			else{
				throw new ValidationException("数据格式不正确");
			}

		} catch (UserNotFoundException e){
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
