package AdminModule.resources.partner;

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
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;

import BaseModule.model.Partner;

public class PartnerIdResource extends AdminPseudoResource{

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
	public Representation changeContactInfo(Representation entity) {
		int partnerId = -1;
		JSONObject response = new JSONObject();
		JSONObject contact = new JSONObject();
		
		try {
			this.checkEntity(entity);
			this.validateAuthentication();
			partnerId = Integer.parseInt(this.getReqAttr("id"));
			
			contact = parseJSON(entity);
				
			Partner partner = PartnerDaoService.getPartnerById(partnerId);
			partner.setPhone(contact.getString("phone"));
			
			PartnerDaoService.updatePartner(partner);
			
			response = JSONFactory.toJSON(partner);
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
