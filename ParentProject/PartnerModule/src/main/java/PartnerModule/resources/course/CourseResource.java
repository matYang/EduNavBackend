package PartnerModule.resources.course;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.imgscalr.Scalr;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ImgConfig;
import BaseModule.configurations.ServerConfig;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.dbservice.FileService;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.ReferenceFactory;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import PartnerModule.resources.PartnerPseudoResource;

public class CourseResource extends PartnerPseudoResource{

	@Post
	public Representation createCourse(Representation entity){
		File imgFile = null;
		Map<String, String> props = new HashMap<String, String>();
		try{
			this.checkFileEntity(entity);
			int partnerId = this.validateAuthentication();

			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}

			// 1/ Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(ImgConfig.img_FactorySize);

			// 2/ Create a new file upload handler
			RestletFileUpload upload = new RestletFileUpload(factory);
			List<FileItem> items;
			
			// 3/ Create a default empty course to get its id later
			Course course = new Course();
			course.setStatus(AccountStatus.deleted);
			course = CourseDaoService.createCourse(course);

			// 4/ Request is parsed by the handler which generates a list of FileItems
			items = upload.parseRepresentation(entity); 
			for (final Iterator<FileItem> it = items.iterator(); it.hasNext(); ) {
				FileItem fi = it.next();

				String name = fi.getName();
				if (name == null) {
					props.put(fi.getFieldName(), new String(fi.get(), "UTF-8"));
				} else {
					BufferedImage bufferedImage = ImageIO.read(fi.getInputStream());
					bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 800, 600, Scalr.OP_ANTIALIAS);
					String imgName;
					String path;
					if (fi.getFieldName().equals("teacherImg")){
						imgName = ImgConfig.teacherImgPrefix + ImgConfig.imgSize_m + course.getCourseId();
						imgFile = new File(ServerConfig.resourcePrefix + ServerConfig.ImgFolder+ imgName + ".png");
						ImageIO.write(bufferedImage, "png", imgFile);
						//warning: can only call this upload once, as it will delete the image file before it exits
						path = FileService.uploadTeacherImg(course.getCourseId(), imgFile, imgName);
						props.put("teacherImgUrl", path);
						
					}
					else{
						imgName = ImgConfig.backgroundImgPrefix + ImgConfig.imgSize_m + course.getCourseId();
						imgFile = new File(ServerConfig.resourcePrefix + ServerConfig.ImgFolder + imgName + ".png");
						ImageIO.write(bufferedImage, "png", imgFile);
						//warning: can only call this upload once, as it will delete the image file before it exits
						path = FileService.uploadBackgroundImg(course.getCourseId(), imgFile, imgName);
						props.put("backgroundUrl", path);
					}                   

				}
			}
			
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
			String reference = ReferenceFactory.generateCourseReference();
			
			course.setPartnerId(partnerId);
			course.setStartTime(startTime);
			course.setFinishTime(finishTime);
			course.setTeacherInfo(teacherInfo);
			course.setTeachingMaterial(teachingMaterial);
			course.setPrice(price);
			course.setSeatsTotal(seatsTotal);
			course.setSeatsLeft(seatsTotal);
			course.setStatus(AccountStatus.activated);
			course.setCategory(category);
			course.setSubCategory(subCategory);
			course.setTitle(title);
			course.setLocation(location);
			course.setCity(city);
			course.setDistrict(district);
			course.setCourseInfo(courseInfo);
			course.setTeacherImgUrl(teacherImgUrl);
			course.setBackgroundUrl(backgroundUrl);
			course.setReference(reference);
			
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
