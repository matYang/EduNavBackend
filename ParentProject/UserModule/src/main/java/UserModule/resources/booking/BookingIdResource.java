package UserModule.resources.booking;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Booking;
import UserModule.resources.UserPseudoResource;


public final class BookingIdResource extends UserPseudoResource{
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
			bookingObject = JSONGenerator.toJSON(booking);
			
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
	
}
