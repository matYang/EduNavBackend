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
import BaseModule.staticDataService.StaticDataService;

public class PartnerIdResource extends AdminPseudoResource{

	@Put
	public Representation createPartner(Representation entity){
		File imgFile = null;
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			int adminId = this.validateAuthentication();
			int partnerId = Integer.parseInt(this.getReqAttr("id"));

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}

			// 1/ Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(ImgConfig.img_FactorySize);

			// 2/ Create a new file upload handler
			RestletFileUpload upload = new RestletFileUpload(factory);
			List<FileItem> items;
			
			// 3/ Get the specified partner
			Partner partner = PartnerDaoService.getPartnerById(partnerId);
			String oldPName = partner.getInstName();
			
			// 4/ Request is parsed by the handler which generates a list of FileItems
			items = upload.parseRepresentation(entity); 
			for (final Iterator<FileItem> it = items.iterator(); it.hasNext(); ) {
				FileItem fi = it.next();

				String name = fi.getName();
				if (name == null) {
					props.put(fi.getFieldName(), new String(fi.get(), "UTF-8"));
				} else {
					BufferedImage bufferedImage = ImageIO.read(fi.getInputStream());
					bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 300, 300, Scalr.OP_ANTIALIAS);
					String imgName;
					String path;

					imgName = ImgConfig.logoPrefix + ImgConfig.imgSize_m + partner.getPartnerId();
					imgFile = new File(ServerConfig.resourcePrefix + ServerConfig.ImgFolder+ imgName + ".png");
					ImageIO.write(bufferedImage, "png", imgFile);
					//warning: can only call this upload once, as it will delete the image file before it exits
					path = FileService.uploadLogoImg(partner.getPartnerId(), imgFile, imgName);
					props.put("logoUrl", path);
				}
			}
			
			String name = props.get("name");
			String licence = props.get("licence");
			String organizationNum = props.get("organizationNum");
			String reference = props.get("reference");
			String password = props.get("password");
			String phone = props.get("phone");
			AccountStatus status = AccountStatus.activated;
			String instName = props.get("instName");
			String logoUrl = props.get("logoUrl");
			
			partner.setName(name);
			partner.setLicence(licence);
			partner.setOrganizationNum(organizationNum);
			partner.setReference(reference);
			partner.setPassword(password);
			partner.setPhone(phone);
			partner.setStatus(status);
			partner.setInstName(instName);
			partner.setLogoUrl(logoUrl);
			
			StaticDataService.updatePName(oldPName, instName);
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
