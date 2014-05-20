package AdminModule.resources.booking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;
import BaseModule.model.representation.BookingSearchRepresentation;

public class GetBookings extends AdminPseudoResource{

	@Get
	public Representation searchUsers() {
		
		JSONArray response = new JSONArray();
		
		try {
			this.validateAuthentication();
			BookingSearchRepresentation b_sr = new BookingSearchRepresentation();
			this.loadRepresentation(b_sr);

			ArrayList<Booking> searchResult = new ArrayList<Booking>();
			searchResult = BookingDaoService.searchBooking(b_sr);
			response = JSONFactory.toJSON(searchResult);
			
		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
	    } catch (Exception e){
			return this.doException(e);
		}
		
		Representation result = new JsonRepresentation(response);
		this.addCORSHeader();
		return result;
		
	}
}
