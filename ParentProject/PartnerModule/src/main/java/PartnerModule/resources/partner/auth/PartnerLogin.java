package PartnerModule.resources.partner.auth;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Partner;
import BaseModule.service.ValidationService;
import PartnerModule.resources.PartnerPseudoResource;

public class PartnerLogin extends PartnerPseudoResource{

	@Post
	public Representation loginAuthentication(Representation entity){
		JSONObject jsonString = null;
		Partner partner = null;
		JSONObject jsonObject = new JSONObject();
		String phone = "";
		String password = "";
		

		try {
			this.checkEntity(entity);
			
			jsonString = (new JsonRepresentation(entity)).getJsonObject();
			phone = jsonString.getString("phone");
			password = jsonString.getString("password");
			if (!ValidationService.isCellNumValid(phone)){
				throw new ValidationException("手机号码格式不正确");
			}
			if (!ValidationService.isPasswordValid(password)){
				throw new ValidationException("密码格式不正确");
			}
			
			DebugLog.d("Log in, receving paramters: " + phone + " " + password);
			partner = PartnerDaoService.authenticatePartner(phone, password);
			

			this.openAuthentication(partner.getPartnerId());

			jsonObject = JSONFactory.toJSON(partner);
			setStatus(Status.SUCCESS_OK);
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } 
		catch (Exception e) {
			return this.doException(e);
		}
			
		
		Representation result = new JsonRepresentation(jsonObject);

        this.addCORSHeader();
        return result;
	}
}
