package AdminModule.resources.coupon;

import java.util.ArrayList;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.CouponDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Coupon;
import BaseModule.model.representation.CouponSearchRepresentation;

public class CouponResource extends AdminPseudoResource{
	private final String apiId = CouponResource.class.getSimpleName();

	@Get
	public Representation searchCoupons() {
		
		JSONArray response = new JSONArray();
		
		try {
			int adminId = this.validateAuthentication();
			CouponSearchRepresentation coup_sr = new CouponSearchRepresentation();
			this.loadRepresentation(coup_sr);
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, adminId, this.getUserAgent(), coup_sr.serialize());

			ArrayList<Coupon> searchResult = CouponDaoService.searchCoupon(coup_sr);
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
