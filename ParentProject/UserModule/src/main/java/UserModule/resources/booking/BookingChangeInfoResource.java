package UserModule.resources.booking;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;
import UserModule.resources.UserPseudoResource;

public class BookingChangeInfoResource extends UserPseudoResource{

	@Put
	public Representation changeBookingInfo(Representation entity){
		int bookingId = -1;
		JSONObject bookingObject = new JSONObject();
		JSONObject newBooking = new JSONObject();
		try{
			this.checkEntity(entity);
			this.validateAuthentication();
			bookingId = Integer.parseInt(this.getReqAttr("id"));
			bookingObject = parseJSON(entity);
			if(bookingObject != null){
				Booking booking = BookingDaoService.getBookingById(bookingId);
				booking.setPhone(bookingObject.getString("phone"));
				booking.setPrice(bookingObject.getInt("price"));
				booking.setStatus(AccountStatus.fromInt(bookingObject.getInt("status")));
				booking.setTimeStamp(DateUtility.castFromAPIFormat(bookingObject.getString("timeStamp")));
				BookingDaoService.updateBooking(booking);

				newBooking = JSONFactory.toJSON(booking);
				setStatus(Status.SUCCESS_OK);
			}else{
				throw new ValidationException("数据格式不正确");
			}
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

	private JSONObject parseJSON(Representation entity) {
		JSONObject jsonContact = null;

		try {
			jsonContact = (new JsonRepresentation(entity)).getJsonObject();
		} catch (JSONException | IOException e) {
			DebugLog.d(e);
			return null;
		}		
		
		return jsonContact;
	}
}
