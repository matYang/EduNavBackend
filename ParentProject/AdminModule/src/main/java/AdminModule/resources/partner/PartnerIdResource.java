package AdminModule.resources.partner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.imgscalr.Scalr;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Put;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ImgConfig;
import BaseModule.configurations.ServerConfig;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.FileService;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;

import BaseModule.model.Partner;

public class PartnerIdResource extends AdminPseudoResource{

	@Put
	public Representation updatePartner(Representation entity){
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			int adminId = this.validateAuthentication();
			int partnerId = Integer.parseInt(this.getReqAttr("id"));

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			Partner partner = PartnerDaoService.getPartnerById(partnerId);
			String oldPName = partner.getInstName();

			props = this.handleMultiForm(entity, partner.getPartnerId(), props);
			
			String name = props.get("name");
			String licence = props.get("licence");
			String organizationNum = props.get("organizationNum");
			String phone = props.get("phone");
			AccountStatus status = AccountStatus.fromInt(Integer.parseInt(props.get("status")));
			String logoUrl = props.get("logoUrl");
			
			partner.setName(name);
			partner.setLicence(licence);
			partner.setOrganizationNum(organizationNum);
			partner.setPhone(phone);
			partner.setStatus(status);
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
