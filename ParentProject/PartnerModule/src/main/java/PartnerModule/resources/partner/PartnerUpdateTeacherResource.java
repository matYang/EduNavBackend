package PartnerModule.resources.partner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;

import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.Visibility;
import BaseModule.dbservice.TeacherDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.model.Teacher;
import PartnerModule.resources.PartnerPseudoResource;

public class PartnerUpdateTeacherResource extends PartnerPseudoResource {
	private final String apiId = PartnerUpdateTeacherResource.class.getSimpleName();
	
	
	@Put
	public Representation updateTeacher(Representation entity){
		try{
			this.checkEntity(entity);
			final int partnerId = this.validateAuthentication();
			
			JSONArray jsonArr = this.getJSONArr(entity);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, partnerId, this.getUserAgent(), jsonArr.toString());
			
			Map<Long, JSONObject> jsonMap = new HashMap<Long, JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++){
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				jsonMap.put(jsonObj.getLong("teacherId"), jsonObj);
			}
			
			ArrayList<Teacher> modelList = TeacherDaoService.getTeacherByPartnerId(partnerId);
			for (Teacher model : modelList){
				JSONObject jsonObj = jsonMap.get(model.getTeacherId());
				if (jsonObj == null){
					model.setVisibility(Visibility.invisible);
				}
				else{
					model.storeJSON(jsonObj);
				}
				model.setPartnerId(partnerId);
			}
			
			TeacherDaoService.updateTeacherList(modelList);


		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(new JSONObject());

		this.addCORSHeader(); 
		return result;
	}
}
