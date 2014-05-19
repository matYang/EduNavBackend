package UserModule.resources.booking;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;
import UserModule.resources.UserPseudoResource;

public class GetBookingDetail extends UserPseudoResource{

	@Get
	public Representation getBookingById(){
		int id = -1;
		JSONObject bookingObject = new JSONObject();
		try{
			id = Integer.parseInt(this.getReqAttr("id"));
			Booking booking = BookingDaoService.getBookingById(id);
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
}
