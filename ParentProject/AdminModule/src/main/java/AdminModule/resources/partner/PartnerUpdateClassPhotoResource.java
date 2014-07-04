package AdminModule.resources.partner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.ClassPhotoDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.model.ClassPhoto;

public class PartnerUpdateClassPhotoResource  extends AdminPseudoResource {
	private final String apiId = PartnerUpdateClassPhotoResource.class.getSimpleName();
	
	
	@Put
	public Representation updateClassPhoto(Representation entity){
		try{
			this.checkEntity(entity);
			final int adminId = this.validateAuthentication();
			final int partnerId = Integer.parseInt(this.getReqAttr("id"));
			
			JSONArray jsonArr = this.getJSONArr(entity);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), jsonArr.toString());
			
			Map<Long, JSONObject> jsonMap = new HashMap<Long, JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++){
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				jsonMap.put(jsonObj.getLong("classPhotoId"), jsonObj);
			}
			
			ArrayList<ClassPhoto> modelList = ClassPhotoDaoService.getClassPhotoByPartnerId(partnerId);
			for (ClassPhoto model : modelList){
				JSONObject jsonObj = jsonMap.get(model.getClassPhotoId());
				if (jsonObj == null){
					//TODO set visibility
				}
				else{
					model.storeJSON(jsonObj);
				}
				model.setPartnerId(partnerId);
			}
			
			ClassPhotoDaoService.updateClassPhotoList(modelList);


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
