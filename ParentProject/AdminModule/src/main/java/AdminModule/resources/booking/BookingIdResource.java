package AdminModule.resources.booking;

import java.util.Calendar;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;



public class BookingIdResource extends AdminPseudoResource{

	@Get 	    
    public Representation getBookingById() {
        JSONObject jsonObject = new JSONObject();
        
        try {
			int bookingId = Integer.parseInt(this.getReqAttr("id"));
			
	    	Booking booking = BookingDaoService.getBookingById(bookingId);
	        jsonObject = JSONFactory.toJSON(booking);
	        
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } catch (Exception e) {
			return this.doException(e);
		}
        
        Representation result = new JsonRepresentation(jsonObject);
        this.addCORSHeader();
        return result;
    }	
	
	@Put
	/**
	 * allows admin to change booking's timeStamp,status,phone
	 */
	public Representation changeContactInfo(Representation entity) {
		int bookingId = -1;
		JSONObject response = new JSONObject();
		JSONObject contact = new JSONObject();
		
		try {
			this.checkEntity(entity);
			this.validateAuthentication();
			bookingId = Integer.parseInt(this.getReqAttr("id"));
			
			contact =  (new JsonRepresentation(entity)).getJsonObject();
				
			Booking booking = BookingDaoService.getBookingById(bookingId);
			Calendar timeStamp = DateUtility.castFromAPIFormat(contact.getString("timeStamp"));
			AccountStatus status = AccountStatus.fromInt(contact.getInt("status"));
			String phone = contact.getString("phone");
			booking.setTimeStamp(timeStamp);
			booking.setStatus(status);
			booking.setPhone(phone);					
			BookingDaoService.updateBooking(booking);
			
			response = JSONFactory.toJSON(booking);
			setStatus(Status.SUCCESS_OK);

		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } catch (Exception e) {
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(response);
		
		this.addCORSHeader(); 
		return result;
	}
}
