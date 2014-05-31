package PartnerModule.resources.partner.auth;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;
import BaseModule.exception.partner.PartnerNotFoundException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Partner;
import PartnerModule.resources.PartnerPseudoResource;

public class PartnerSessionRedirect extends PartnerPseudoResource{

	@Get
	public Representation sessionRedirect(Representation entity){
		DebugLog.d("SessionDirect:: Enter session redirect");

		Partner partner = null;
		JSONObject jsonObject = new JSONObject();

		try {
			int partnerId = this.validateAuthentication();
			partner = PartnerDaoService.getPartnerById(partnerId);
			jsonObject = JSONFactory.toJSON(partner);
		} catch (AuthenticationException | PartnerNotFoundException e){
			//if not authenticated, return default user with id -1
			partner = new Partner("default", "default","default", "default",
					"default", "default", "default",AccountStatus.activated);
			partner.setPartnerId(-1);
			jsonObject = JSONFactory.toJSON(partner);
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		}  catch (Exception e) {
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(jsonObject);

		this.addCORSHeader();
		return result;
	}
}
