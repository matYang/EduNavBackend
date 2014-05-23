package PartnerModule.resources.course;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Put;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Course;
import BaseModule.service.EncodingService;
import PartnerModule.resources.PartnerPseudoResource;

public class CourseIdResource extends PartnerPseudoResource{

	@Put
	public Representation createCourse(Representation entity){
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			int partnerId = this.validateAuthentication();
			int courseId = Integer.parseInt(this.getReqAttr("id"));

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}
			
			Course course = CourseDaoService.getCourseById(courseId);
			
			props = this.handleMultiForm(entity, course.getCourseId(), props);
			
			Calendar startTime = DateUtility.castFromAPIFormat(props.get("startTime"));
			Calendar finishTime = DateUtility.castFromAPIFormat(props.get("finishTime"));
			String teacherInfo = EncodingService.decodeURI(props.get("teacherInfo"));
			String teachingMaterial = EncodingService.decodeURI(props.get("teachingMaterial"));
			int price = Integer.parseInt(props.get("price"), 10);
			int seatsTotal = Integer.parseInt(props.get("seatsTotal"));
			String category = EncodingService.decodeURI(props.get("category"));
			String subCategory = EncodingService.decodeURI(props.get("subCategory"));
			String title = EncodingService.decodeURI(props.get("title"));
			String location = EncodingService.decodeURI(props.get("location"));
			String city = EncodingService.decodeURI(props.get("city"));
			String district = EncodingService.decodeURI(props.get("district"));
			String courseInfo = EncodingService.decodeURI(props.get("courseInfo"));
			String teacherImgUrl = EncodingService.decodeURI(props.get("teacherImgUrl"));
			String backgroundUrl = EncodingService.decodeURI(props.get("backgroundUrl"));
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
