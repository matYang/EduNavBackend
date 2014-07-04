package AdminModule.resources.partner;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.ReferenceGenerator;
import BaseModule.model.Partner;

public class PartnerPostClassPhotoResource extends AdminPseudoResource {
	private final String apiId = PartnerPostClassPhotoResource.class.getSimpleName();
	
	@Post
	public Representation postClassPhotoResource(Representation entity){
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			int adminId = this.validateAuthentication();
			int partnerId = Integer.parseInt(this.getReqAttr("id"));
			int totalNumber = Integer.parseInt(this.getQueryVal("total"));
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, adminId, this.getUserAgent(), "<Form>");

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}

			props = this.handleMultiForm(entity, partner.getPartnerId(), props);
			
			
			
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
