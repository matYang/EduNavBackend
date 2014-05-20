package PartnerModule.resources.partner.course;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Course;
import PartnerModule.resources.PartnerPseudoResource;

public class CourseIdResource extends PartnerPseudoResource{

	protected JSONObject parseJSON(Representation entity) throws ValidationException{
		JSONObject jsonContact = null;

		try {
			jsonContact = (new JsonRepresentation(entity)).getJsonObject();
		} catch (JSONException | IOException e) {
			DebugLog.d(e);
			return null;
		}
		
		int partnerId = jsonContact.getInt("partnerId");		
		String category = jsonContact.getString("category");
		String subCategory = jsonContact.getString("subcategory");
		String title = jsonContact.getString("title");
		
		if(partnerId <= 0){
			throw new ValidationException("合作伙伴ID不能为0");
		}
		if(category == null || subCategory == null || title == null
		 ||category.length()==0 || subCategory.length() == 0 || title.length() == 0){
			throw new ValidationException("必填数据不能为空");
		}
		return jsonContact;
		
	}
	
	@Put	
	public Representation changeContactInfo(Representation entity) {
		int courseId = -1;
		JSONObject response = new JSONObject();
		JSONObject contact = new JSONObject();
		
		try {
			this.checkEntity(entity);
			courseId = Integer.parseInt(this.getReqAttr("id"));
			
			contact = parseJSON(entity);
				
			Course course = CourseDaoService.getCourseById(courseId);
			course.setPrice(contact.getInt("price"));
			course.setLocation(contact.getString("location"));
			course.setCity(contact.getString("city"));
			course.setDistrict(contact.getString("district"));
			course.setSeatsTotal(contact.getInt("seatsTotal"));
			course.setSeatsLeft(contact.getInt("seatsLeft"));
			
			CourseDaoService.updateCourse(course);
			
			response = JSONFactory.toJSON(course);
			setStatus(Status.SUCCESS_OK);

		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } catch (Exception e) {
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(response);
		
		this.addCORSHeader(); 
		return result;
	}
	
}
