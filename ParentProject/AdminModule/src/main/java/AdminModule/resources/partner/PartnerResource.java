package AdminModule.resources.partner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.imgscalr.Scalr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ImgConfig;
import BaseModule.configurations.ServerConfig;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.FileService;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Partner;
import BaseModule.model.representation.PartnerSearchRepresentation;

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
		int partnerId = -1;
		File imgFile = null;
		Partner partner = null;
		Map<String, String> props = new HashMap<String, String>();
		Connection conn = EduDaoBasic.getSQLConnection();
		try{
			this.checkFileEntity(entity);
			this.validateAuthentication();
			partner = validatePartnerJSON(entity);
			partner = PartnerDaoService.createPartner(partner,conn);
			partnerId = partner.getPartnerId();
			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}

			// 1/ Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(ImgConfig.img_FactorySize);

			// 2/ Create a new file upload handler
			RestletFileUpload upload = new RestletFileUpload(factory);
			List<FileItem> items;

			// 3/ Request is parsed by the handler which generates a list of FileItems
			items = upload.parseRepresentation(entity); 
			for (final Iterator<FileItem> it = items.iterator(); it.hasNext(); ) {
				FileItem fi = it.next();

				String name = fi.getName();
				if (name == null) {
					props.put(fi.getFieldName(), new String(fi.get(), "UTF-8"));
				} else {
					BufferedImage bufferedImage = ImageIO.read(fi.getInputStream());
					bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 200, 200, Scalr.OP_ANTIALIAS);
					String imgName;
					String path;

					imgName = ImgConfig.logoPrefix + ImgConfig.imgSize_m + partnerId;
					imgFile = new File(ServerConfig.resourcePrefix + ServerConfig.ImgFolder+ imgName + ".png");
					ImageIO.write(bufferedImage, "png", imgFile);
					//warning: can only call this upload once, as it will delete the image file before it exits
					path = FileService.uploadLogoImg(partnerId, imgFile, imgName);


					props.put(fi.getFieldName(), path);
				}
			}

			String logoImgUrl = props.get("image_1");		
			partner.setLogoUrl(logoImgUrl);
			PartnerDaoService.updatePartner(partner, conn);


		}catch (PseudoException e){
			DebugLog.d(e);
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			DebugLog.d(e);
			return this.doException(e);
		}finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}

		setStatus(Status.SUCCESS_OK);
		Representation result = new StringRepresentation("SUCCESS", MediaType.TEXT_PLAIN);

		this.addCORSHeader();
		return result;
	}

	private Partner validatePartnerJSON(Representation entity) throws ValidationException {
		JSONObject jsonPartner = null;
		Partner partner = null;
		try{
			jsonPartner = (new JsonRepresentation(entity)).getJsonObject();

			String name = jsonPartner.getString("name");
			String instName = jsonPartner.getString("instName");
			String licence = jsonPartner.getString("licence");
			String organizationNum = jsonPartner.getString("organizationNum");
			String reference = jsonPartner.getString("reference");
			String password = jsonPartner.getString("password");
			String phone = jsonPartner.getString("phone");
			AccountStatus status = AccountStatus.fromInt(jsonPartner.getInt("status"));

			partner = new Partner(name, instName, licence, organizationNum,
					reference, password, phone,status);

		}catch (JSONException | IOException e) {
			throw new ValidationException("无效数据格式");
		}
		return partner;
	}
}
