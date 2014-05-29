package UserModule.resources.general;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.representation.CourseSearchRepresentation;
import UserModule.resources.UserPseudoResource;


public class GetCourses extends UserPseudoResource{
	
	@Get
	public Representation searchCourses() {
		
		JSONArray response = new JSONArray();
		
		try {
			CourseSearchRepresentation c_sr = new CourseSearchRepresentation();
			this.loadRepresentation(c_sr);
			response = JSONFactory.toJSON(CourseDaoService.searchCourse(c_sr));
			
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
