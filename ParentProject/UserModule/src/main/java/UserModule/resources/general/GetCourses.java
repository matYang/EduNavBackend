package UserModule.resources.general;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.representation.CourseSearchRepresentation;
import UserModule.resources.UserPseudoResource;


public final class GetCourses extends UserPseudoResource{
	private final String apiId = GetCourses.class.getSimpleName();
	
	@Get
	public Representation searchCourses() {
		JSONArray response = new JSONArray();
		
		try {
			CourseSearchRepresentation c_sr = new CourseSearchRepresentation();
			this.loadRepresentation(c_sr);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), c_sr.serialize());
			
			response = JSONGenerator.toJSON(CourseDaoService.searchCourse(c_sr));
			
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
