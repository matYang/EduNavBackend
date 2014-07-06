package AdminModule.resources.partner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.Visibility;
import BaseModule.dbservice.ClassPhotoDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.ClassPhoto;

public class PartnerPostClassPhotoResource extends AdminPseudoResource {
	private final String apiId = PartnerPostClassPhotoResource.class.getSimpleName();
	
	@Post
	public Representation postClassPhotoResource(Representation entity){
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			final int adminId = this.validateAuthentication();
			final int partnerId = Integer.parseInt(this.getReqAttr("id"));
			final int totalNumber = Integer.parseInt(this.getQueryVal("total"));
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, adminId, this.getUserAgent(), "<Form>");

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			//get all the ids from database, as they are needed in file name to ensure file name uniqueness and record
			ArrayList<ClassPhoto> modelList = new ArrayList<ClassPhoto>();
			for (int i = 0; i < totalNumber; i++){
				ClassPhoto model = new ClassPhoto();
				model.setPartnerId(partnerId);
				model.setVisibility(Visibility.invisible);
				modelList.add(model);
			}
			modelList = ClassPhotoDaoService.createClassPhotoList(modelList);
			
			ArrayList<Long> idList = new ArrayList<Long>();
			ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
			for (ClassPhoto model : modelList){
				idList.add(model.getClassPhotoId());
				mapList.add(new HashMap<String, String>());
			}
			
			props.put("type", "classPhoto");
			props = this.handleMultiForm(entity, idList, props);
			props.remove("type");
			
			for (String key : props.keySet()){
				int index = Integer.parseInt(key.substring(key.length()-1)) - 1;
				mapList.get(index).put(key.substring(0, key.length() - 1), props.get(key));
			}

			for (int i = 0; i < totalNumber; i++){
				modelList.get(i).loadFromMap(props);
				modelList.get(i).setPartnerId(partnerId);
				modelList.get(i).setVisibility(Visibility.visible);
			}
			
			ClassPhotoDaoService.updateClassPhotoList(modelList);
			
			
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
