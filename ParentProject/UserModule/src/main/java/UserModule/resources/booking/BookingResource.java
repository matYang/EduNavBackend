package UserModule.resources.booking;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.factory.ReferenceFactory;
import BaseModule.model.Booking;
import BaseModule.model.Course;
import BaseModule.model.representation.BookingSearchRepresentation;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;
import UserModule.resources.UserPseudoResource;

public class BookingResource extends UserPseudoResource{
	private final String apiId = BookingResource.class.getSimpleName();
	
	@Get
	public Representation getBookings(){
		JSONArray jsonArray = null;
		try {
			int userId = this.validateAuthentication();
			BookingSearchRepresentation b_sr = new BookingSearchRepresentation();
			b_sr.setUserId(userId);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, userId, this.getUserAgent(), b_sr.serialize());
			
			ArrayList<Booking> result = BookingDaoService.searchBooking(b_sr);
			jsonArray = JSONFactory.toJSON(result);
			
			
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(jsonArray);
		this.addCORSHeader();
		return result;
	}
	

	@Post
	public Representation createBooking(Representation entity){
		Booking booking = null;
		JSONObject bookingObject = new JSONObject();
		try{
			this.checkEntity(entity);
			JSONObject jsonBooking = this.getJSONObj(entity);
			int userId = this.validateAuthentication();
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, userId, this.getUserAgent(), jsonBooking.toString());
			
			booking = parseJSON(jsonBooking);
			if (userId != booking.getUserId()){
				throw new ValidationException("不允许替其他用户预约");
			}
			Course course = CourseDaoService.getCourseById(booking.getCourseId());
			if (course.getPrice() != booking.getPrice()){
				throw new ValidationException("预约与课程价格不一致");
			}
			
			booking = BookingDaoService.createBooking(booking);
			bookingObject = JSONFactory.toJSON(booking);
			
		} catch(PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(bookingObject);

		this.addCORSHeader(); 
		return result;
	}

	protected Booking parseJSON(JSONObject jsonBooking) throws ParseException, SQLException, PseudoException {
		Booking booking = null;
		try{
			
			Calendar adjustTime = DateUtility.getCurTimeInstance();
			Calendar scheduledTime = DateUtility.castFromAPIFormat(jsonBooking.getString("scheduledTime"));			
			
			int price = jsonBooking.getInt("price");
			int userId = jsonBooking.getInt("userId");
			int partnerId = jsonBooking.getInt("partnerId");
			int courseId = jsonBooking.getInt("courseId");
			int cashbackAmount = jsonBooking.getInt("cashbackAmount");
			
			String name = EncodingService.decodeURI(jsonBooking.getString("name"));
			String phone = EncodingService.decodeURI(jsonBooking.getString("phone"));
			String email = EncodingService.decodeURI(jsonBooking.getString("email"));
			String reference = ReferenceFactory.generateBookingReference();		
			
			BookingStatus status = BookingStatus.awaiting;		
			
		    booking = new Booking(scheduledTime,adjustTime, 
					price,userId, partnerId, courseId, name,
					phone,email,reference,status,cashbackAmount);
			
			ValidationService.validateBooking(booking);
			
		}catch (JSONException|IOException e) {
			throw new ValidationException("无效数据格式");
		}
		
		return booking;
	}

}
