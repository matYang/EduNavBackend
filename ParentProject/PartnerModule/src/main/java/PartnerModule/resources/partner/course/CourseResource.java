package PartnerModule.resources.partner.course;

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
import BaseModule.model.Course;
import PartnerModule.resources.PartnerPseudoResource;

public class CourseResource extends PartnerPseudoResource{

	@Post
	public Representation createCourse(Representation entity){
		int courseId = -1;
		File imgFile = null;
		Course course = null;
		Map<String, String> props = new HashMap<String, String>();
		Connection conn = EduDaoBasic.getSQLConnection();
		try{
			this.checkFileEntity(entity);
			this.validateAuthentication();
			course = validateCourseJSON(entity);
			course = CourseDaoService.createCourse(course,conn);
			courseId = course.getCourseId();
			if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)){
				throw new ValidationException("上传数据类型错误");
			}

			// 1/ Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(ImgConfig.img_FactorySize);

			// 2/ Create a new file upload handler
			RestletFileUpload upload = new RestletFileUpload(factory);
			List<FileItem> items;

			// 3/ Request is parsed by the handler which generates a list of FileItems
			items = upload.parseRepresentation(entity); 
			for (final Iterator<FileItem> it = items.iterator(); it.hasNext(); ) {
				FileItem fi = it.next();

				String name = fi.getName();
				if (name == null) {
					props.put(fi.getFieldName(), new String(fi.get(), "UTF-8"));
				} else {
					BufferedImage bufferedImage = ImageIO.read(fi.getInputStream());
					bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 200, 200, Scalr.OP_ANTIALIAS);
					String imgName;
					String path;
					if (fi.getFieldName().equals("image_1")){
						imgName = ImgConfig.teacherImgPrefix + ImgConfig.imgSize_m + courseId;
						imgFile = new File(ServerConfig.resourcePrefix + ServerConfig.ImgFolder+ imgName + ".png");
						ImageIO.write(bufferedImage, "png", imgFile);
						//warning: can only call this upload once, as it will delete the image file before it exits
						path = FileService.uploadTeacherImg(courseId, imgFile, imgName);
					}
					else{
						imgName = ImgConfig.backgroundImgPrefix + ImgConfig.imgSize_m + courseId;
						imgFile = new File(ServerConfig.resourcePrefix + ServerConfig.ImgFolder + imgName + ".png");
						ImageIO.write(bufferedImage, "png", imgFile);
						//warning: can only call this upload once, as it will delete the image file before it exits
						path = FileService.uploadBackgroundImg(courseId, imgFile, imgName);
					}                   

					props.put(fi.getFieldName(), path);
				}
			}
			
			String teacherImgUrl = props.get("image_1");
			String backImgUrl = props.get("image_2");
			course.setTeacherImgUrl(teacherImgUrl);
			course.setBackgroundUrl(backImgUrl);
			
			CourseDaoService.updateCourse(course,conn);
			
		}catch (PseudoException e){
			DebugLog.d(e);
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			DebugLog.d(e);
			return this.doException(e);
		}finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}

		setStatus(Status.SUCCESS_OK);
		Representation result = new StringRepresentation("SUCCESS", MediaType.TEXT_PLAIN);

		this.addCORSHeader();
		return result;
	}

	private Course validateCourseJSON(Representation entity) throws ValidationException {
		JSONObject jsonCourse = null;
		Course course = null;
		try{
			jsonCourse = (new JsonRepresentation(entity)).getJsonObject();
			
			int price = jsonCourse.getInt("price");
			Calendar startTime = DateUtility.castFromAPIFormat(jsonCourse.getString("startTime"));
			Calendar finishTime = DateUtility.castFromAPIFormat(jsonCourse.getString("finishTime"));
			String category = jsonCourse.getString("category");
			String subcategory = jsonCourse.getString("subcategory");
			String title = jsonCourse.getString("title");
			AccountStatus status = AccountStatus.fromInt(jsonCourse.getInt("status"));
			int seatsTotal = jsonCourse.getInt("seatsTotal");
			int seatsLeft = jsonCourse.getInt("seatsLeft");
			int partnerId = jsonCourse.getInt("partnerId");
			
			course = new Course(partnerId, startTime, finishTime,
					seatsTotal, seatsLeft, category,subcategory, status,price,title);
		}catch (JSONException | IOException e) {
			throw new ValidationException("无效数据格式");
		}
		return course;
	}
}
