package UserModule.resources.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Course;
import BaseModule.model.representation.CourseSearchRepresentation;
import UserModule.resources.UserPseudoResource;


public class GetCourses extends UserPseudoResource{
	
	@Get
	public Representation searchCourses() {
		
		JSONArray response = new JSONArray();
		
		try {
			CourseSearchRepresentation c_sr = new CourseSearchRepresentation();
			Map<String, String> kvps = new HashMap<String, String>();
			
			ArrayList<String> keys = c_sr.getKeySet();
			for (String key: keys){
				kvps.put(key, this.getQueryVal(key));
			}
			c_sr.storeKvps(kvps);

			ArrayList<Course> searchResult = new ArrayList<Course>();
			searchResult = CourseDaoService.searchCourse(c_sr);
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

	
}
