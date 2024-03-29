package AdminModule.resources.partner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.generator.ReferenceGenerator;
import BaseModule.model.Partner;
import BaseModule.model.representation.PartnerSearchRepresentation;

public final class PartnerResource extends AdminPseudoResource{
	private final String apiId = PartnerResource.class.getSimpleName();
	
	
	@Get
	public Representation searchPartners(){

		JSONArray response = new JSONArray();
		
		try {
			int adminId = this.validateAuthentication();
			PartnerSearchRepresentation p_sr = new PartnerSearchRepresentation();
			this.loadRepresentation(p_sr);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(), p_sr.toJSON().toString());

			ArrayList<Partner> searchResult = PartnerDaoService.searchPartner(p_sr);
			response = JSONGenerator.toJSON(searchResult);
			
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
	    } catch (Exception e){
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(response);
		this.addCORSHeader();
		return result;
	}
	
	
	
	@Post
	public Representation createPartner(Representation entity){
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			int adminId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, adminId, this.getUserAgent(), "<Form>");

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			Partner partner = Partner.getInstance();
			partner.setStatus(AccountStatus.deleted);
			//initialize the reference at this earlier step
			partner.setReference(ReferenceGenerator.generatePartnerReference());
			partner = PartnerDaoService.createPartner(partner);
			
			ArrayList<Long> idList = new ArrayList<Long>();
			idList.add((long) partner.getPartnerId());
			props = this.handleMultiForm(entity, idList, props);
			partner.loadFromMap(props);
			partner.setStatus(AccountStatus.activated);
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
