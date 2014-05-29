package UserModule.resources.booking;

import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;
import UserModule.resources.UserPseudoResource;

public class BookingIdResource extends UserPseudoResource{
	
	@Get
	public Representation getBookingById(){
		int bookigId = -1;
		JSONObject bookingObject = new JSONObject();
		try{
			bookigId = Integer.parseInt(this.getReqAttr("id"));
			int userId = this.validateAuthentication();
			
			Booking booking = BookingDaoService.getBookingById(bookigId);
			if (booking.getUserId() != userId){
				throw new AuthenticationException("对不起，您不是该预定的主人");
			}
			bookingObject = JSONFactory.toJSON(booking);			
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
			int userId = this.validateAuthentication();
			bookingId = Integer.parseInt(this.getReqAttr("id"));
			
			Booking booking = BookingDaoService.getBookingById(bookingId);
			BookingStatus previousStatus = booking.getStatus();
			if (booking.getUserId() != userId){
				throw new AuthenticationException("对不起，您不是该预定的主人");
			}
			booking = parseJSON(entity, booking);
			BookingDaoService.updateBooking(booking, previousStatus, -1);

			newBooking = JSONFactory.toJSON(booking);
			setStatus(Status.SUCCESS_OK);

		}catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(newBooking);

		this.addCORSHeader(); 
		return result;
	}

	private Booking parseJSON(Representation entity, Booking booking) throws ValidationException{
		JSONObject jsonBooking = null;
		
		try{
			jsonBooking = (new JsonRepresentation(entity)).getJsonObject();
			
			Calendar timeStamp = DateUtility.getCurTimeInstance();
			BookingStatus status = BookingStatus.fromInt(Integer.parseInt(jsonBooking.getString("status")));
			
			booking.setAdjustTime(timeStamp);
			booking.setStatus(status);
			
			ValidationService.validateBooking(booking);
			
		}catch (JSONException|IOException e) {
			throw new ValidationException("无效数据格式");
		}	
		
		return booking;
	}
	
}
