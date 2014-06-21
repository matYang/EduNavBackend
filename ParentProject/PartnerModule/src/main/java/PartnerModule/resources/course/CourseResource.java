package PartnerModule.resources.course;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;

import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.CourseStatus;
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
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			int partnerId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, partnerId, this.getUserAgent(), "<Form>");

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			Course course = new Course();
			course.setStatus(CourseStatus.deactivated);
			//initialize the reference at this earlier step
			course.setReference(ReferenceGenerator.generateCourseReference());
			course = CourseDaoService.createCourse(course);
			
			props = this.handleMultiForm(entity, course.getCourseId(), props);
			props.put("partnerId", String.valueOf(partnerId));
			props.put("status", String.valueOf(AccountStatus.activated.code));
			
			course.loadFromMap(props);
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
