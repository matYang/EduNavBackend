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
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;
import BaseModule.model.representation.SearchRepresentation;

public class GetBookings extends AdminPseudoResource{

	@Get
	public Representation searchUsers() {
		
		JSONArray response = new JSONArray();
		
		try {
			String srStr = this.getPlainQueryVal("searchRepresentation");
			DebugLog.d("SearchMessage received searchRepresentation: " + srStr);
			if (srStr == null){
				throw new ValidationException("搜索条件不能为空");
			}
			
			SearchRepresentation sr = new SearchRepresentation(srStr);
			

			ArrayList<Booking> searchResult = new ArrayList<Booking>();
			searchResult = BookingDaoService.searchCourse(sr);
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
