package AdminModule.resources.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.User;
import BaseModule.service.ValidationService;

public class UserIdResource extends AdminPseudoResource{

	protected User parseJSON(Representation entity, User user) throws ValidationException{
		JSONObject jsonContact = null;

		try {
			jsonContact = (new JsonRepresentation(entity)).getJsonObject();
			String name = java.net.URLDecoder.decode(jsonContact.getString("name"), "utf-8");
			AccountStatus status = AccountStatus.fromInt(Integer.parseInt(jsonContact.getString("status")));
			
			user.setName(name);
			user.setStatus(status);
			ValidationService.validateUser(user);
			
		} catch (NullPointerException | JSONException | IOException e) {
			DebugLog.d(e);
			throw new ValidationException("姓名格式不正确");
		}	
		
		return user;
	}
	
	@Put
	/**
	 * allows admin to change user's name
	 */
	public Representation changeContactInfo(Representation entity) {
		int userId = -1;
		JSONObject response = new JSONObject();
		
		try {
			this.checkEntity(entity);
			this.validateAuthentication();
			userId = Integer.parseInt(this.getReqAttr("id"));
			
			User user = UserDaoService.getUserById(userId);
			user = parseJSON(entity, user);
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
