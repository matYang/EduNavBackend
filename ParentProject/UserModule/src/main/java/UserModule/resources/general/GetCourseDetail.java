package UserModule.resources.general;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Course;
import UserModule.resources.UserPseudoResource;

public class GetCourseDetail extends UserPseudoResource {

	@Get
	public Representation getCourseById(){
		int courseId = -1;
		JSONObject courseObject = new JSONObject();
		try{
			
			courseId =Integer.parseInt(this.getReqAttr("id"));
			Course course = CourseDaoService.getCourseById(courseId);
			if(course != null){
				courseObject = JSONFactory.toJSON(course);
			}else{
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			}
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
