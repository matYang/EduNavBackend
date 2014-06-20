package AdminModule.resources.coupon;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.dbservice.CouponDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;
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
	
	@Post
	public Representation createCoupon(Representation entity){
		Coupon coupon = null;
		JSONObject couponObject = new JSONObject();
		try{
			this.checkEntity(entity);
			JSONObject jsonCoupon = this.getJSONObj(entity);
			int adminId = this.validateAuthentication();
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, adminId, this.getUserAgent(), couponObject.toString());
			
			coupon = parseJSON(jsonCoupon);

			coupon = CouponDaoService.addCouponToUser(coupon);
			couponObject = JSONGenerator.toJSON(coupon);
			
		} catch(PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e) {
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(couponObject);
		this.addCORSHeader(); 
		return result;
	}
	
	protected Coupon parseJSON(JSONObject jsonCoupon) throws ParseException, SQLException, PseudoException {
		Coupon coupon = null;
		try{
			int userId= jsonCoupon.getInt("userId");
			int amount = jsonCoupon.getInt("amount");
			
		    coupon = new Coupon(userId, amount);
		    coupon.setOrigin(CouponOrigin.admin);
		}catch (JSONException e) {
			throw new ValidationException("无效数据格式");
		}
		
		return coupon;
	}
	
}
