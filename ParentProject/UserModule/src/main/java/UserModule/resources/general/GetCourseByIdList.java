package UserModule.resources.general;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.representation.CourseSearchRepresentation;
import BaseModule.staticDataService.StaticDataService;
import UserModule.resources.UserPseudoResource;

public class GetCourseByIdList extends UserPseudoResource{
	private final String apiId = GetCourseByIdList.class.getSimpleName();
	
	@Get
	public Representation searchCourses() {
		JSONArray response = new JSONArray();
		
		try {
			String idListStr = this.getQueryVal("idList");
			ArrayList<Integer> idList = new ArrayList<Integer>();
			if (idListStr != null){
				for (String id : idListStr.split("-")){
					idList.add(Integer.parseInt(id));
				}
				
				DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), idListStr);
				
				response = JSONGenerator.toJSON(CourseDaoService.getCourseByIdList(idList));
			}
			
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
	
}