package AdminModule.resources.admin.auth;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;

public final class AdminAccountLogout extends AdminPseudoResource{
	private final String apiId = AdminAccountLogout.class.getSimpleName();

	@Put
	public Representation logoutAuthentication(Representation entity){

		try {
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, -1, this.getUserAgent(), "");
			
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
