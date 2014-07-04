package AdminModule.resources.course;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Put;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.model.Course;

public final class CourseIdResource extends AdminPseudoResource{
	private final String apiId = CourseIdResource.class.getName();
	
	@Put
	public Representation createCourse(Representation entity){
		try{
			int adminId = this.validateAuthentication();
			int courseId = Integer.parseInt(this.getReqAttr("id"));
			JSONObject jsonCourse = this.getJSONObj(entity);
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), jsonCourse.toString());
			
			Course course = CourseDaoService.getCourseById(courseId);
			course.storeJSON(jsonCourse);
			CourseDaoService.updateCourse(course);
			
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
