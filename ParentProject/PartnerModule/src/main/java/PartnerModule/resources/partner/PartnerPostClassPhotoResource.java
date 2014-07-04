package PartnerModule.resources.partner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.ClassPhotoDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.ClassPhoto;
import PartnerModule.resources.PartnerPseudoResource;

public class PartnerPostClassPhotoResource extends PartnerPseudoResource {
	private final String apiId = PartnerPostClassPhotoResource.class.getSimpleName();
	
	@Post
	public Representation postClassPhotoResource(Representation entity){
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			final int partnerId = this.validateAuthentication();
			final int totalNumber = Integer.parseInt(this.getQueryVal("total"));
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, partnerId, this.getUserAgent(), "<Form>");

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			//get all the ids from database, as they are needed in file name to ensure file name uniqueness and record
			ArrayList<ClassPhoto> modelList = new ArrayList<ClassPhoto>();
			for (int i = 0; i < totalNumber; i++){
				//TODO set visibility
				ClassPhoto model = new ClassPhoto();
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
				//TODO set visibility
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
