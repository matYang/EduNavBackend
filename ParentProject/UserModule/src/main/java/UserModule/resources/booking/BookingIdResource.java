package UserModule.resources.booking;

import java.util.Calendar;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;
import BaseModule.service.ValidationService;
import UserModule.resources.UserPseudoResource;


public class BookingIdResource extends UserPseudoResource{
	private final String apiId = BookingIdResource.class.getSimpleName();
	
	@Get
	public Representation getBookingById(){
		int bookigId = -1;
		JSONObject bookingObject = new JSONObject();
		try{
			bookigId = Integer.parseInt(this.getReqAttr("id"));
			int userId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, userId, this.getUserAgent(), String.valueOf(bookigId));
			
			Booking booking = BookingDaoService.getBookingById(bookigId);
			if (booking.getUserId() != userId){
				throw new AuthenticationException("对不起，您不是该预定的主人");
			}
			bookingObject = JSONFactory.toJSON(booking);
			
		} catch (PseudoException e){
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
			JSONObject jsonBooking = this.getJSONObj(entity);
			int userId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, userId, this.getUserAgent(), jsonBooking.toString());
			
			bookingId = Integer.parseInt(this.getReqAttr("id"));
			
			Booking booking = BookingDaoService.getBookingById(bookingId);
			BookingStatus previousStatus = booking.getStatus();
			if (booking.getUserId() != userId){
				throw new AuthenticationException("对不起，您不是该预定的主人");
			}
			booking = parseJSON(jsonBooking, booking);
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

	private Booking parseJSON(JSONObject jsonBooking, Booking booking) throws ValidationException{
		Calendar timeStamp = DateUtility.getCurTimeInstance();
		BookingStatus status = BookingStatus.fromInt(Integer.parseInt(jsonBooking.getString("status")));
		
		if (status != BookingStatus.canceled){
			throw new ValidationException("无权执行该操作");
		}
		booking.setAdjustTime(timeStamp);
		booking.setStatus(status);
		
		ValidationService.validateBooking(booking);
			
		return booking;
	}
	
}
