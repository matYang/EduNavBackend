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
import BaseModule.factory.JSONFactory;
import BaseModule.factory.ReferenceFactory;
import BaseModule.model.Partner;
import BaseModule.model.representation.PartnerSearchRepresentation;
import BaseModule.service.EncodingService;
import BaseModule.staticDataService.StaticDataService;

public class PartnerResource extends AdminPseudoResource{
	
	
	@Get
	public Representation searchPartners(){

		JSONArray response = new JSONArray();
		
		try {
			this.validateAuthentication();
			PartnerSearchRepresentation p_sr = new PartnerSearchRepresentation();
			this.loadRepresentation(p_sr);

			ArrayList<Partner> searchResult = PartnerDaoService.searchPartners(p_sr);
			response = JSONFactory.toJSON(searchResult);
			
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

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			Partner partner = new Partner();
			partner.setStatus(AccountStatus.deleted);
			partner = PartnerDaoService.createPartner(partner);

			props = this.handleMultiForm(entity, partner.getPartnerId(), props);
			
			String name = EncodingService.decodeURI(props.get("name"));
			String licence = EncodingService.decodeURI(props.get("licence"));
			String organizationNum = EncodingService.decodeURI(props.get("organizationNum"));
			String reference = ReferenceFactory.generatePartnerReference();
			String password = EncodingService.decodeURI(props.get("password"));
			String phone = EncodingService.decodeURI(props.get("phone"));
			AccountStatus status = AccountStatus.activated;
			String instName = EncodingService.decodeURI(props.get("instName"));
			String logoUrl = EncodingService.decodeURI(props.get("logoUrl"));
			
			partner.setName(name);
			partner.setLicence(licence);
			partner.setOrganizationNum(organizationNum);
			partner.setReference(reference);
			partner.setPassword(password);
			partner.setPhone(phone);
			partner.setStatus(status);
			partner.setInstName(instName);
			partner.setLogoUrl(logoUrl);
			
			StaticDataService.addPName(instName);
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
