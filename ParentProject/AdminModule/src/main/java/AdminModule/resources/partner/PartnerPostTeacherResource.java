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
import BaseModule.dbservice.TeacherDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Teacher;

public class PartnerPostTeacherResource extends AdminPseudoResource {
	private final String apiId = PartnerPostTeacherResource.class.getSimpleName();
	
	@Post
	public Representation partnerPostTeacherResource(Representation entity){
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
			ArrayList<Teacher> modelList = new ArrayList<Teacher>();
			for (int i = 0; i < totalNumber; i++){
				Teacher model = new Teacher();
				model.setPartnerId(partnerId);
				model.setVisibility(Visibility.invisible);
				modelList.add(model);
			}
			modelList = TeacherDaoService.createTeacherList(modelList);
			
			ArrayList<Long> idList = new ArrayList<Long>();
			ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
			for (Teacher model : modelList){
				idList.add(model.getTeacherId());
				mapList.add(new HashMap<String, String>());
			}
			
			props.put("type", "teacher");
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
			
			TeacherDaoService.updateTeacherList(modelList);
			
			
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
