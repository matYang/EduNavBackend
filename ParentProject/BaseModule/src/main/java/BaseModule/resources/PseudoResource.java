package BaseModule.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
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
import org.json.JSONArray;
import org.json.JSONException;
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
	
	public void addRequestHeaders(){
	    Series<Header> requestHeaders = (Series<Header>) getRequest().getAttributes().get("org.restlet.http.headers");
	    
	    if (requestHeaders == null) { 
	        requestHeaders = new Series(Header.class); 
	        requestHeaders.add("Access-Control-Allow-Origin", "*");
	        requestHeaders.add("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
	        requestHeaders.add("Access-Control-Allow-Headers", "Content-Type");
	        requestHeaders.add("Content-Type", "application/json");
	        requestHeaders.add("Access-Control-Allow-Headers", "authCode");
	        requestHeaders.add("Access-Control-Allow-Headers", "origin, x-requested-with, content-type");
        }
        
        if (requestHeaders != null){
            getRequest().getAttributes().put("org.restlet.http.headers", requestHeaders); 
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
		if (requestHeaders != null && requestHeaders.getFirstValue("User-agent", true) != null){
			return requestHeaders.getFirstValue("User-agent", true);
		}
		else{
			return "undetermined";
		}
	}
	
	public JSONObject getJSONObj(Representation entity)throws ValidationException{
		try {
			JSONObject json = (new JsonRepresentation(entity)).getJsonObject();
			return json;
		} catch (JSONException | IOException | NullPointerException e) {
			throw new ValidationException("无效数据格式");
		}
	}
	
	public JSONArray getJSONArr(Representation entity)throws ValidationException{
		try {
			JSONArray json = (new JsonRepresentation(entity)).getJsonArray();
			return json;
		} catch (JSONException | IOException | NullPointerException e) {
			throw new ValidationException("无效数据格式");
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
			case 18: case 16:
				//ValidationException | Password hashing exception
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
		if (e instanceof SQLException){
			if (e.getMessage().contains("Duplicate")){
				return new StringRepresentation("关键信息重复，请重新填写");
			}
			else if (e.getMessage().contains("Deadlock") || e.getMessage().contains("Lock") || e.getMessage().contains("lock")){
				return new StringRepresentation("系统繁忙，请稍后再试");
			}
			else if (e.getMessage().contains("too long") || e.getMessage().contains("Data truncation")){
				return new StringRepresentation("数据过长，请删减过长项");
			}
			else{
				return new StringRepresentation("数据库遇到问题，请重试");
			}
		}
		else{
			return new StringRepresentation("不好意思..哪里弄错了，请稍后重试");
		}
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
    
	
    
    public Map<String, String> handleMultiForm(Representation entity, ArrayList<Long> idList, Map<String, String> props) throws FileUploadException, IOException, ValidationException {
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
				String fieldName = fi.getFieldName();
				props.put(fieldName, new String(fi.get(), "UTF-8"));
			} else {
				String imgName;
				String path;
				String fieldName = fi.getFieldName();
				if (fieldName.contains("imgUrl")){
					if (props.get("type").equalsIgnoreCase("classPhoto")){
						try{
							int index = Integer.parseInt(fieldName.substring(fieldName.length()-1)) - 1;
							
							BufferedImage bufferedImage = ImageIO.read(fi.getInputStream());
							bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, 120, 120, Scalr.OP_ANTIALIAS);
							
							imgName = ImgConfig.teacherImgPrefix + fieldName + "-" + idList.get(index);
							imgFile = new File(ServerConfig.resourcePrefix + ImgConfig.imgFolder+ imgName + ".jpg");
							ImageIO.write(bufferedImage, "jpg", imgFile);
							//warning: can only call this upload once, as it will delete the image file before it exits
							path = FileService.uploadTeacherImg(idList.get(index), imgFile, imgName);
							props.put("imgUrl" + (index+1), path);
						}
						catch (NullPointerException e){
							DebugLog.d(e);
							//nothing to do here
						}
					}
					else if (props.get("type").equalsIgnoreCase("teacher")){
						try{
							int index = Integer.parseInt(fieldName.substring(fieldName.length()-1)) - 1;
									
							BufferedImage bufferedImage = ImageIO.read(fi.getInputStream());
							bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, 216, 160, Scalr.OP_ANTIALIAS);
							imgName = ImgConfig.classImgPrefix + fieldName + "-" + idList.get(index);
							imgFile = new File(ServerConfig.resourcePrefix + ImgConfig.imgFolder + imgName + ".jpg");
							ImageIO.write(bufferedImage, "jpg", imgFile);
							//warning: can only call this upload once, as it will delete the image file before it exits
							path = FileService.uploadClassPhotoImg(idList.get(index), imgFile, imgName);
							props.put("imgUrl" + (index+1), path);
						} 
						catch (NullPointerException e){
							DebugLog.d(e);
							//nothing to do here
						}
					}
					
				}
				else if (fieldName.equals("logoUrl")){
					try{
						BufferedImage bufferedImage = ImageIO.read(fi.getInputStream());
						bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 100, 100, Scalr.OP_ANTIALIAS);

						imgName = ImgConfig.logoPrefix + idList.get(0);
						imgFile = new File(ServerConfig.resourcePrefix + ImgConfig.imgFolder+ imgName + ".jpg");
						ImageIO.write(bufferedImage, "jpg", imgFile);
						//warning: can only call this upload once, as it will delete the image file before it exits
						path = FileService.uploadLogoImg(idList.get(0), imgFile, imgName);
						props.put(fieldName, path);
					} 
					catch (NullPointerException e){
						DebugLog.d(e);
						//do nothing
					}
				}
				else{
					throw new ValidationException("检测到无效照片");
				}                   

			}
		}
		
		return props;
    }
    
    
}
