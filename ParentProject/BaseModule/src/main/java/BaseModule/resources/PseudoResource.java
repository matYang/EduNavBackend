package BaseModule.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.imgscalr.Scalr;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.engine.header.Header;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import BaseModule.common.DebugLog;
import BaseModule.configurations.ImgConfig;
import BaseModule.configurations.ServerConfig;
import BaseModule.configurations.ValidationConfig;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.EncodingService;
import BaseModule.service.FileService;

public class PseudoResource extends ServerResource{
	
	protected final String moduleId = "baseModule";
	
	protected final String reqId_get = "GET";
	protected final String reqId_post = "POST";
	protected final String reqId_put = "PUT";
	protected final String reqId_delete = "DELETE";
	
	/*set the response header to allow for CORS*/
	public void addCORSHeader(){
		Series<Header> responseHeaders = (Series<Header>) getResponse().getAttributes().get("org.restlet.http.headers");
        
		if (responseHeaders == null) { 
			responseHeaders = new Series(Header.class); 
			responseHeaders.add("Access-Control-Allow-Origin", "*");
			responseHeaders.add("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
			responseHeaders.add("Access-Control-Allow-Headers", "Content-Type");
			responseHeaders.add("Access-Control-Allow-Headers", "authCode");
			responseHeaders.add("Access-Control-Allow-Headers", "origin, x-requested-with, content-type");
		}
		
		if (responseHeaders != null){
            getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders); 
        }
	}

	public void checkEntity(Representation entity) throws ValidationException{
		if (entity != null && entity.getSize() > ValidationConfig.max_PostLength){
			throw new ValidationException("发送内容过大");
		}
	}
	
	public void checkFileEntity(Representation entity) throws NullPointerException, ValidationException{
		if (entity != null && entity.getSize() > ValidationConfig.max_FileLength){
			throw new ValidationException("发送内容过大");
		}
	}
	
	public String getUserAgent(){
		Series<Header> requestHeaders = (Series<Header>) getRequest().getAttributes().get("org.restlet.http.headers");
		if (requestHeaders != null && requestHeaders.getFirstValue("", true) != null){
			return requestHeaders.getFirstValue("user-agent", true);
		}
		else{
			return "undetermined";
		}
	}

	
	
	/******************
	 * 
	 * Query Area
	 * 
	 ******************/
	public String getReqAttr(String fieldName) throws UnsupportedEncodingException{
		Object attr = this.getRequestAttributes().get(fieldName);
		return EncodingService.decodeURI((String)attr);
	}
	
	public String getQueryVal(String fieldName) throws UnsupportedEncodingException{
		String val = getQuery().getValues(fieldName);
		return EncodingService.decodeURI(val);
	}
	
	public String getPlainQueryVal(String fieldName){
		String val = getQuery().getValues(fieldName);
		return val;
	}

	
	public void loadRepresentation(PseudoRepresentation pr) throws Exception{
		Map<String, String> kvps = new HashMap<String, String>();
		
		ArrayList<String> keys = pr.getKeySet();
		for (String key: keys){
			kvps.put(key, this.getQueryVal(key));
		}
		pr.storeKvps(kvps);
	}
	
	
	/******************
	 * 
	 * Exception Handling Area
	 * 
	 ******************/
	public StringRepresentation doPseudoException(PseudoException e){
		DebugLog.d(e);
		switch(e.getCode()){
			case 1: case 2: case 3: case 4: case 5: case 6: case 7 : case 8: 
				//Not Found
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				break;
			case 17:
				//authentication error
				setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				break;
			case 18:
				//ValidationException
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				break;
			default:
				setStatus(Status.SERVER_ERROR_INTERNAL);
				break;
		}
		return new StringRepresentation(e.getExceptionText());
	}
	
	public StringRepresentation doException(Exception e){
		DebugLog.d(e);
		setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		return new StringRepresentation("不好意思..哪里弄错了，请稍后重试");
	}
	

	public Representation quickRespond(String responseText){
		addCORSHeader();
		return new StringRepresentation(responseText);
	}
	
	/******************
	 * 
	 * Take the options
	 * 
	 ******************/
    //needed here since in CORS scenarios will try to send OPTIONS to /id before PUT or DELETE
    @Options
    public Representation takeOptions(Representation entity) {
    	addCORSHeader();
    	setStatus(Status.SUCCESS_OK);
        return new JsonRepresentation(new JSONObject());
    }
    
	/******************
	 * 
	 * Multi-form handling
	 * 
	 ******************/
    public Map<String, String> handleMultiForm(Representation entity, int id, Map<String, String> props) throws FileUploadException, IOException, ValidationException {
    	File imgFile = null;
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
				String imgName;
				String path;
				if (fi.getFieldName().equals("teacherImg")){
					BufferedImage bufferedImage = ImageIO.read(fi.getInputStream());
					bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 800, 600, Scalr.OP_ANTIALIAS);
					
					imgName = ImgConfig.teacherImgPrefix + ImgConfig.imgSize_m + id;
					imgFile = new File(ServerConfig.resourcePrefix + ImgConfig.ImgFolder+ imgName + ".png");
					ImageIO.write(bufferedImage, "png", imgFile);
					//warning: can only call this upload once, as it will delete the image file before it exits
					path = FileService.uploadTeacherImg(id, imgFile, imgName);
					props.put("teacherImgUrl", path);
					
				}
				else if (fi.getFieldName().equals("classroomImg")){
					BufferedImage bufferedImage = ImageIO.read(fi.getInputStream());
					bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 800, 600, Scalr.OP_ANTIALIAS);
					imgName = ImgConfig.classroomImgPrefix + ImgConfig.imgSize_m + id;
					imgFile = new File(ServerConfig.resourcePrefix + ImgConfig.ImgFolder + imgName + ".png");
					ImageIO.write(bufferedImage, "png", imgFile);
					//warning: can only call this upload once, as it will delete the image file before it exits
					path = FileService.uploadBackgroundImg(id, imgFile, imgName);
					props.put("backgroundUrl", path);
				}
				else if (fi.getFieldName().equals("logo")){
					BufferedImage bufferedImage = ImageIO.read(fi.getInputStream());
					bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 300, 300, Scalr.OP_ANTIALIAS);

					imgName = ImgConfig.logoPrefix + ImgConfig.imgSize_m + id;
					imgFile = new File(ServerConfig.resourcePrefix + ImgConfig.ImgFolder+ imgName + ".png");
					ImageIO.write(bufferedImage, "png", imgFile);
					//warning: can only call this upload once, as it will delete the image file before it exits
					path = FileService.uploadLogoImg(id, imgFile, imgName);
					props.put("logoUrl", path);
				}
				else{
					throw new ValidationException("检测到无效照片");
				}                   

			}
		}
		
		return props;
    }
    
    
}
