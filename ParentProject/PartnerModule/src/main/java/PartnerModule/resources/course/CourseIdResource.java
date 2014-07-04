package PartnerModule.resources.course;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Put;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Course;
import PartnerModule.resources.PartnerPseudoResource;

public final class CourseIdResource extends PartnerPseudoResource{
	private final String apiId = CourseIdResource.class.getSimpleName();

	@Put
	public Representation createCourse(Representation entity){
		try{
			int partnerId = this.validateAuthentication();
			int courseId = Integer.parseInt(this.getReqAttr("id"));
			JSONObject jsonCourse = this.getJSONObj(entity);
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, partnerId, this.getUserAgent(), jsonCourse.toString());
		
			Course course = CourseDaoService.getCourseById(courseId);
			if (course.getPartnerId() != partnerId){
				throw new ValidationException("只可以修改自己的课程");
			}
			if (jsonCourse.has("partnerId") && jsonCourse.getInt("partnerId") != course.getPartnerId()){
				throw new ValidationException("不可以更改课程所属机构");
			}
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
