package AdminModule.resources.booking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Booking;
import BaseModule.model.representation.BookingSearchRepresentation;

public final class BookingResource extends AdminPseudoResource{
	private final String apiId = BookingResource.class.getSimpleName();

	@Get
	public Representation searchBookings() {
		
		JSONArray response = new JSONArray();
		
		try {
			int adminId = this.validateAuthentication();
			BookingSearchRepresentation b_sr = new BookingSearchRepresentation();
			this.loadRepresentation(b_sr);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(), b_sr.serialize());

			ArrayList<Booking> searchResult = new ArrayList<Booking>();
			searchResult = BookingDaoService.searchBooking(b_sr);
			response = JSONGenerator.toJSON(searchResult);
			
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
