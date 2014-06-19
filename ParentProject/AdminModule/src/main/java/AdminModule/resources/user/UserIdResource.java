package AdminModule.resources.user;

import java.io.IOException;

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
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;

public class UserIdResource extends AdminPseudoResource{
	private final String apiId = UserIdResource.class.getSimpleName();

	protected User parseJSON(JSONObject jsonContact, User user) throws ValidationException{

		try {
			String name = EncodingService.decodeURI(jsonContact.getString("name"));
			String email = EncodingService.decodeURI(jsonContact.getString("email"));
			AccountStatus status = AccountStatus.fromInt(Integer.parseInt(jsonContact.getString("status")));
			
			user.setName(name);
			user.setEmail(email);
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
			int adminId = this.validateAuthentication();
			userId = Integer.parseInt(this.getReqAttr("id"));
			JSONObject jsonContact = this.getJSONObj(entity);
			jsonContact.put("userId", userId);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), jsonContact.toString());
			
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
