package PartnerModule.resources.partner.auth;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.notFound.PartnerNotFoundException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Partner;
import PartnerModule.resources.PartnerPseudoResource;

public final class PartnerSessionRedirect extends PartnerPseudoResource{
	private final String apiId = PartnerSessionRedirect.class.getSimpleName();

	@Get
	public Representation sessionRedirect(Representation entity){
		DebugLog.d("SessionDirect:: Enter session redirect");

		Partner partner = null;
		JSONObject jsonObject = new JSONObject();

		try {
			int partnerId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, partnerId, this.getUserAgent(), "");
			
			partner = PartnerDaoService.getPartnerById(partnerId);
			jsonObject = JSONGenerator.toJSON(partner);
			
		} catch (AuthenticationException | PartnerNotFoundException e){
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), "");
			
			//if not authenticated, return default user with id -1
			partner = new Partner("default", "default","default", "default",
					"default", "default", "default",AccountStatus.activated);
			partner.setPartnerId(-1);
			try {
				jsonObject = JSONGenerator.toJSON(partner);
			} catch (PseudoException e1) {
				this.addCORSHeader();
				return this.doPseudoException(e1);
			}
			
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
