package UserModule.resources.user.auth;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;

import BaseModule.common.DebugLog;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import UserModule.resources.UserPseudoResource;

public final class UserLogout extends UserPseudoResource {
	private final String apiId = UserLogout.class.getSimpleName();
	
	
	@Put
	public Representation logoutAuthentication(){
		DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, -1, this.getUserAgent(), "");
		try {

			this.closeAuthentication();
			setStatus(Status.SUCCESS_OK);
			
		} catch (AuthenticationException e){
			//if authentication exception, then user is not logged in, then logout is considered a successful request
			setStatus(Status.SUCCESS_OK);
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } catch (Exception e) {
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(new JSONObject());

        this.addCORSHeader();
        return result;
	}
	
}
