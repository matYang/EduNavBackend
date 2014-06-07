package PartnerModule.resources.partner;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Partner;
import BaseModule.service.EncodingService;

import PartnerModule.resources.PartnerPseudoResource;

public class PartnerIdResource extends PartnerPseudoResource{
	private final String apiId = PartnerIdResource.class.getSimpleName();

	@Get 	    
    public Representation getPartnerById() {
        JSONObject jsonObject = new JSONObject();
        
        try {
			int partnerId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, partnerId, this.getUserAgent(), String.valueOf(partnerId));
			
	    	Partner partner = PartnerDaoService.getPartnerById(partnerId);
	        jsonObject = JSONFactory.toJSON(partner);
	        
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } catch (Exception e) {
			return this.doException(e);
		}
        
        Representation result = new JsonRepresentation(jsonObject);
        this.addCORSHeader();
        return result;
    }
	
	
	

	@Put
	public Representation updatePartner(Representation entity){
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			int partnerId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, partnerId, this.getUserAgent(), "<Form>");
			
			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			Partner partner = PartnerDaoService.getPartnerById(partnerId);
			
			//handle the multi-form, upload images if necessary
			props = this.handleMultiForm(entity, partner.getPartnerId(), props);
			
			
			String wholeName = EncodingService.decodeURI(props.get("wholeName"));
			String licence = EncodingService.decodeURI(props.get("licence"));
			String organizationNum = EncodingService.decodeURI(props.get("organizationNum"));
			String phone = EncodingService.decodeURI(props.get("phone"));
			AccountStatus status = AccountStatus.fromInt(Integer.parseInt(props.get("status")));
			String instName = EncodingService.decodeURI(props.get("instName"));
			String logoUrl = EncodingService.decodeURI(props.get("logoUrl"));
			
			partner.setWholeName(wholeName);
			partner.setLicence(licence);
			partner.setOrganizationNum(organizationNum);
			partner.setPhone(phone);
			partner.setStatus(status);
			partner.setInstName(instName);
			partner.setLogoUrl(logoUrl);
			
			PartnerDaoService.updatePartner(partner);
			
		}catch (PseudoException e){
			DebugLog.d(e);
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			DebugLog.d(e);
			return this.doException(e);
		}

		setStatus(Status.SUCCESS_OK);
		Representation result = new StringRepresentation("SUCCESS", MediaType.TEXT_PLAIN);

		this.addCORSHeader();
		return result;
	}
	
}
