package AdminModule.resources.booking;

import java.text.ParseException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;
import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.AdminAccount;
import BaseModule.model.Booking;
import BaseModule.model.dataObj.BookingStatusObj;


public final class BookingChangeStatusSupervisorCallResource extends AdminPseudoResource{
	private final String apiId = BookingChangeStatusSupervisorCallResource.class.getSimpleName();
	

	@Put
	//this method does not change the state of the booking
	public Representation changeBookingInfo(Representation entity){
		int bookingId = -1;
		JSONObject newBooking = new JSONObject();
		try{
			this.checkEntity(entity);
			int adminId = this.validateAuthentication();
			AdminAccount admin = AdminAccountDaoService.getAdminAccountById(adminId);
			if (admin.getPrivilege() != Privilege.root){
				throw new AuthenticationException("您无权进行该操作");
			}
			
			bookingId = Integer.parseInt(this.getReqAttr("id"));
			JSONObject jsonStatusObj = this.getJSONObj(entity);

			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), jsonStatusObj.toString());
			
			Booking booking = BookingDaoService.getBookingById(bookingId);			
			BookingStatusObj statusObj = parseJSON(jsonStatusObj);
			BookingDaoService.updateBookingStatuses(booking, statusObj, adminId, true);

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
