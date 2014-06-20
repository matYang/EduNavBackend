package UserModule.resources.general;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Course;
import UserModule.resources.UserPseudoResource;

public class GetCourseDetail extends UserPseudoResource {
	private final String apiId = GetCourseDetail.class.getSimpleName();

	@Get
	public Representation getCourseById(){
		int courseId = -1;
		JSONObject courseObject = new JSONObject();
		try{			
			courseId =Integer.parseInt(this.getReqAttr("id"));
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), String.valueOf(courseId));
			
			Course course = CourseDaoService.getCourseById(courseId);
			courseObject = JSONGenerator.toJSON(course);
			
		}catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } catch (Exception e) {
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(courseObject);
        this.addCORSHeader();
        return result;
	}
	
}
