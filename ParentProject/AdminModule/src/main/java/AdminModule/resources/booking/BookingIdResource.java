package AdminModule.resources.booking;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Booking;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;


public class BookingIdResource extends AdminPseudoResource{
	private final String apiId = BookingIdResource.class.getSimpleName();

	@Get
	public Representation getBookingById(){
		int bookingId = -1;
		JSONObject bookingObject = new JSONObject();
		try{
			bookingId = Integer.parseInt(this.getReqAttr("id"));
			int adminId = this.validateAuthentication();
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(), String.valueOf(bookingId));
			
			Booking booking = BookingDaoService.getBookingById(bookingId);

			bookingObject = JSONGenerator.toJSON(booking);			
		}catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(bookingObject);
		this.addCORSHeader();
		return result;
	}
	
	

	@Put
	public Representation changeBookingInfo(Representation entity){
		int bookingId = -1;
		JSONObject newBooking = new JSONObject();
		try{
			this.checkEntity(entity);
			int adminId = this.validateAuthentication();
			bookingId = Integer.parseInt(this.getReqAttr("id"));
			JSONObject jsonBooking = this.getJSONObj(entity);
			jsonBooking.put("bookingId", bookingId);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), jsonBooking.toString());
			
			Booking booking = BookingDaoService.getBookingById(bookingId);
			BookingStatus previousStatus = booking.getStatus();

			booking = parseJSON(jsonBooking, booking);
			BookingDaoService.updateBooking(booking, previousStatus, adminId);

			newBooking = JSONGenerator.toJSON(booking);
			setStatus(Status.SUCCESS_OK);

		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(newBooking);

		this.addCORSHeader(); 
		return result;
	}

	private Booking parseJSON(JSONObject jsonBooking, Booking booking) throws ValidationException, ParseException{
		
		try{
			
			Calendar timeStamp = DateUtility.getCurTimeInstance();
			Calendar scheduledTime = DateUtility.castFromAPIFormat(jsonBooking.getString("scheduledTime"));
			String name = EncodingService.decodeURI(jsonBooking.getString("name"));
			String phone = EncodingService.decodeURI(jsonBooking.getString("phone"));
			BookingStatus status = BookingStatus.fromInt(jsonBooking.getInt("status"));
			String email = EncodingService.decodeURI(jsonBooking.getString("email"));
			String note = EncodingService.decodeURI(jsonBooking.getString("note"));
			
			booking.setAdjustTime(timeStamp);
			booking.setScheduledTime(scheduledTime);
			booking.setName(name);
			booking.setPhone(phone);
			booking.setStatus(status);
			booking.setEmail(email);
			booking.setNote(note);
			
			ValidationService.validateBooking(booking);
		}catch (JSONException|IOException e) {
			throw new ValidationException("无效数据格式");
		}	
		
		return booking;
	}
}
