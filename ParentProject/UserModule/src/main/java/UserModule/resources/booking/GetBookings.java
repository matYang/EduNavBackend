package UserModule.resources.booking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import BaseModule.dbservice.BookingDaoService;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;
import UserModule.resources.UserPseudoResource;

public class GetBookings extends UserPseudoResource{

	@Get
	public Representation getBookings(){
		ArrayList<Booking> allBookings = BookingDaoService.getAllBookings();
		JSONArray jsonArray = JSONFactory.toJSON(allBookings);
		Representation result = new JsonRepresentation(jsonArray);
		this.addCORSHeader();
		return result;
	}
}
