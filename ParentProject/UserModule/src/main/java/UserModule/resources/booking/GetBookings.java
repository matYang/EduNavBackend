package UserModule.resources.booking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;
import UserModule.resources.UserPseudoResource;

public class GetBookings extends UserPseudoResource{

	@Get
	public Representation getBookings(){
		JSONArray jsonArray = null;
		try {
			this.validateAuthentication();
			ArrayList<Booking> allBookings = BookingDaoService.getAllBookings();
			jsonArray = JSONFactory.toJSON(allBookings);
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(jsonArray);
		this.addCORSHeader();
		return result;
	}
}
