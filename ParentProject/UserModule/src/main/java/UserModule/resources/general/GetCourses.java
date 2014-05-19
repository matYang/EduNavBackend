package UserModule.resources.general;

import java.util.ArrayList;

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
import BaseModule.model.representation.SearchRepresentation;
import UserModule.resources.UserPseudoResource;


public class GetCourses extends UserPseudoResource{
	
	@Get
	public Representation searchCourses() {
		
		JSONArray response = new JSONArray();
		
		try {
			String srStr = this.getPlainQueryVal("searchRepresentation");
			DebugLog.d("SearchMessage received searchRepresentation: " + srStr);
			if (srStr == null){
				throw new ValidationException("搜索条件不能为空");
			}
			
			SearchRepresentation sr = new SearchRepresentation(srStr);
			

			ArrayList<Course> searchResult = new ArrayList<Course>();
			searchResult = CourseDaoService.searchCourse(sr);
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
