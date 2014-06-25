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
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Booking;
import BaseModule.model.dataObj.BookingStatusObj;
import BaseModule.service.EncodingService;
import BaseModule.service.ValidationService;


public final class BookingChangeStatusResource extends AdminPseudoResource{
	private final String apiId = BookingChangeStatusResource.class.getSimpleName();
	

	@Put
	//this method does not change the state of the booking
	public Representation changeBookingInfo(Representation entity){
		int bookingId = -1;
		JSONObject newBooking = new JSONObject();
		try{
			this.checkEntity(entity);
			int adminId = this.validateAuthentication();
			bookingId = Integer.parseInt(this.getReqAttr("id"));
			JSONObject jsonStatusObj = this.getJSONObj(entity);

			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), jsonStatusObj.toString());
			
			Booking booking = BookingDaoService.getBookingById(bookingId);			
			BookingStatusObj statusObj = parseJSON(jsonStatusObj);
			BookingDaoService.updateBookingStatuses(booking, statusObj, adminId);

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

	private BookingStatusObj parseJSON(JSONObject jsonStatusObj) throws ParseException{
		BookingStatus bookingStatus = null;
		ServiceFeeStatus serviceFeeStatus = null;
		CommissionStatus commissionStatus = null;
		try{
			bookingStatus = BookingStatus.fromInt(jsonStatusObj.getInt("status"));		
		} catch (Exception e){}
		try{
			serviceFeeStatus = ServiceFeeStatus.fromInt(jsonStatusObj.getInt("serviceFeeStatus"));
		} catch (Exception e){}
		try{
			commissionStatus = CommissionStatus.fromInt(jsonStatusObj.getInt("commissionStatus"));
		} catch (Exception e){}
		
		
		BookingStatusObj statusObj = new BookingStatusObj();
		statusObj.bookingStatus = bookingStatus;
		statusObj.serviceFeeStatus = serviceFeeStatus;
		statusObj.commissionStatus = commissionStatus;
		return statusObj;
	}
}
