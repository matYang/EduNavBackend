package AdminModule.resources.course;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Put;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Course;

public class CourseIdResource extends AdminPseudoResource{
	
	
	@Put
	public Representation createCourse(Representation entity){
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			this.validateAuthentication();
			int courseId = Integer.parseInt(this.getReqAttr("id"));

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			Course course = CourseDaoService.getCourseById(courseId);

			props = this.handleMultiForm(entity, course.getCourseId(), props);
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
