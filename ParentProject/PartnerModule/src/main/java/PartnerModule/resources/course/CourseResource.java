package PartnerModule.resources.course;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.ReferenceGenerator;
import BaseModule.model.Course;
import PartnerModule.resources.PartnerPseudoResource;

public final class CourseResource extends PartnerPseudoResource{
	private final String apiId = CourseResource.class.getSimpleName();

	@Post
	public Representation createCourse(Representation entity){
		try{
			int partnerId = this.validateAuthentication();
			JSONObject jsonCourse = this.getJSONObj(entity);
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, partnerId, this.getUserAgent(), jsonCourse.toString());
			
			if (jsonCourse.has("partnerId") && jsonCourse.getInt("partnerId") != partnerId){
				throw new ValidationException("只能发布属于自己的课程");
			}
			
			Course course = new Course();
			course.storeJSON(jsonCourse);
			course.setReference(ReferenceGenerator.generateCourseReference());
			CourseDaoService.createCourse(course);
			
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
