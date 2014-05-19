package PartnerModule.resources.partner.auth;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;

import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;
import PartnerModule.resources.PartnerPseudoResource;

public class PartnerLogout extends PartnerPseudoResource{

	@Put
	public Representation logoutAuthentication(Representation entity){

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
