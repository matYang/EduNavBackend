package AdminModule.resources.course;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Put;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
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
			int adminId = this.validateAuthentication();
			int courseId = Integer.parseInt(this.getReqAttr("id"));

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			Course course = CourseDaoService.getCourseById(courseId);

			props = this.handleMultiForm(entity, course.getCourseId(), props);
			
			int partnerId = Integer.parseInt(props.get("partnerId"));
			Calendar startTime = DateUtility.castFromAPIFormat(props.get("startTime"));
			Calendar finishTime = DateUtility.castFromAPIFormat(props.get("finishTime"));
			String teacherInfo = props.get("teacherInfo");
			String teachingMaterial = props.get("teachingMaterial");
			int price = Integer.parseInt(props.get("price"), 10);
			int seatsTotal = Integer.parseInt(props.get("seatsTotal"));
			String category = props.get("category");
			String subCategory = props.get("subCategory");
			String title = props.get("title");
			String location = props.get("location");
			String city = props.get("city");
			String district = props.get("district");
			String courseInfo = props.get("courseInfo");
			String teacherImgUrl = props.get("teacherImgUrl");
			String backgroundUrl = props.get("backgroundUrl");
			AccountStatus status = AccountStatus.fromInt(Integer.parseInt(props.get("status")));
			
			course.setPartnerId(partnerId);
			course.setStartTime(startTime);
			course.setFinishTime(finishTime);
			course.setTeacherInfo(teacherInfo);
			course.setTeachingMaterial(teachingMaterial);
			course.setPrice(price);
			course.setSeatsTotal(seatsTotal);
			course.setSeatsLeft(seatsTotal);
			course.setStatus(status);
			course.setCategory(category);
			course.setSubCategory(subCategory);
			course.setTitle(title);
			course.setLocation(location);
			course.setCity(city);
			course.setDistrict(district);
			course.setCourseInfo(courseInfo);
			course.setTeacherImgUrl(teacherImgUrl);
			course.setBackgroundUrl(backgroundUrl);
			
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
