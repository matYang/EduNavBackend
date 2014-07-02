package UserModule.resources.booking;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Booking;
import BaseModule.model.dataObj.BookingStatusObj;
import UserModule.resources.UserPseudoResource;


public final class BookingChangeStatusResource extends UserPseudoResource{
	private final String apiId = BookingChangeStatusResource.class.getSimpleName();

	@Put
	public Representation changeBookingStatus(Representation entity){
		int bookingId = -1;
		JSONObject newBooking = new JSONObject();
		try{
			this.checkEntity(entity);
			JSONObject jsonStatusObj = this.getJSONObj(entity);
			int userId = this.validateAuthentication();
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, userId, this.getUserAgent(), jsonStatusObj.toString());
			
			bookingId = Integer.parseInt(this.getReqAttr("id"));
			
			Booking booking = BookingDaoService.getBookingById(bookingId);
			if (booking.getUserId() != userId){
				throw new AuthenticationException("对不起，您不是该预定的主人");
			}
			
			BookingStatusObj statusObj = parseJSON(jsonStatusObj);
			BookingDaoService.updateBookingStatuses(booking, statusObj, -1, false);

			newBooking = JSONGenerator.toJSON(booking);
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

	private BookingStatusObj parseJSON(JSONObject jsonStatusObj) throws ValidationException{
		BookingStatus status = BookingStatus.fromInt(jsonStatusObj.getInt("status"));		
		if (status != BookingStatus.cancelled){
			throw new ValidationException("无权执行该操作");
		}
		
		BookingStatusObj statusObj = new BookingStatusObj();
		statusObj.bookingStatus = status;
		return statusObj;
	}
	
}
