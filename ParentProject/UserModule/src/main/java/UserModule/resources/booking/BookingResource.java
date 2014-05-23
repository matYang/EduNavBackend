package UserModule.resources.booking;

import java.io.IOException;
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
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.factory.ReferenceFactory;
import BaseModule.model.Booking;
import BaseModule.model.representation.BookingSearchRepresentation;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;
import UserModule.resources.UserPseudoResource;

public class BookingResource extends UserPseudoResource{
	
	@Get
	public Representation getBookings(){
		JSONArray jsonArray = null;
		try {
			int userId = this.validateAuthentication();
			BookingSearchRepresentation b_sr = new BookingSearchRepresentation();
			b_sr.setUserId(userId);
			
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
			int userId = this.validateAuthentication();
			
			booking = parseJSON(entity);
			if (userId != booking.getUserId()){
				throw new ValidationException("不允许替其他用户预约");
			}
			booking = BookingDaoService.createBooking(booking);
			bookingObject = JSONFactory.toJSON(booking);
			
		}catch(PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		}

		Representation result = new JsonRepresentation(bookingObject);

		this.addCORSHeader(); 
		return result;


	}

	protected Booking parseJSON(Representation entity) throws ValidationException {
		Booking booking = null;
		try{
			JSONObject jsonBooking = (new JsonRepresentation(entity)).getJsonObject();
			
			Calendar timeStamp = DateUtility.getCurTimeInstance();
			Calendar startTime = DateUtility.castFromAPIFormat(jsonBooking.getString("startTime"));
			Calendar finishTime = DateUtility.castFromAPIFormat(jsonBooking.getString("finishTime"));
			
			int price = jsonBooking.getInt("price");
			int userId = jsonBooking.getInt("userId");
			int partnerId = jsonBooking.getInt("partnerId");
			int courseId = jsonBooking.getInt("courseId");
			
			String name = EncodingService.decodeURI(jsonBooking.getString("name"));
			String phone = EncodingService.decodeURI(jsonBooking.getString("phone"));
			String reference = ReferenceFactory.generateBookingReference();
			AccountStatus status = AccountStatus.activated;
			
			booking = new Booking(timeStamp, startTime,  finishTime, price, userId, partnerId, courseId,  name,  phone, reference, status);
			ValidationService.validateBooking(booking);
			
		}catch (JSONException|IOException e) {
			throw new ValidationException("无效数据格式");
		}
		
		return booking;
	}

}
