package AdminModule.resources.partner;

import java.util.ArrayList;
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
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;

import BaseModule.model.Partner;

public final class PartnerIdResource extends AdminPseudoResource{
	private final String apiId = PartnerIdResource.class.getSimpleName();
	
	@Get 	    
    public Representation getPartnerById() {
        JSONObject jsonObject = new JSONObject();
        
        try {
			int adminId = this.validateAuthentication();
			int partnerId = Integer.parseInt(this.getReqAttr("id"));
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(), String.valueOf(partnerId));
			
	    	Partner partner = PartnerDaoService.getPartnerById(partnerId);
	        jsonObject = JSONGenerator.toJSON(partner);
	        
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
			int adminId = this.validateAuthentication();
			int partnerId = Integer.parseInt(this.getReqAttr("id"));
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), "<Form> "+partnerId);

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			Partner partner = PartnerDaoService.getPartnerById(partnerId);

			ArrayList<Long> idList = new ArrayList<Long>();
			idList.add((long) partner.getPartnerId());
			props = this.handleMultiForm(entity, idList, props);
			partner.loadFromMap(props);
			
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
