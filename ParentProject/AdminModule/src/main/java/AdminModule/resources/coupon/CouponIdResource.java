package AdminModule.resources.coupon;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;

import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.dbservice.CouponDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.JSONGenerator;
import BaseModule.model.Coupon;

public class CouponIdResource extends AdminPseudoResource{
	private final String apiId = CouponIdResource.class.getSimpleName();
	
	@Put
	public Representation updateCoupon(Representation entity){
		int couponId = -1;
		JSONObject couponObject = new JSONObject();
		try{
			this.checkEntity(entity);
			int adminId = this.validateAuthentication();
			couponId = Integer.parseInt(this.getReqAttr("id"));
			JSONObject jsonCoupon = this.getJSONObj(entity);
			jsonCoupon.put("couponId", couponId);
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, adminId, this.getUserAgent(), couponObject.toString());
			
			Coupon coupon = CouponDaoService.getCouponByCouponId(couponId);
			int previousAmount = coupon.getAmount();
			CouponStatus previousStatus = coupon.getStatus();
			
			coupon = parseJSON(jsonCoupon, coupon);
			CouponDaoService.updateCouponToUser(coupon, previousAmount, previousStatus);
			
			couponObject = JSONGenerator.toJSON(coupon);
			setStatus(Status.SUCCESS_OK);
			
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
	
	protected Coupon parseJSON(JSONObject jsonCoupon, Coupon coupon) throws ParseException,  PseudoException, UnsupportedEncodingException {
		try{

			int amount = jsonCoupon.getInt("amount");
			Calendar expireTime = DateUtility.castFromAPIFormat(jsonCoupon.getString("expireTime"));
			CouponStatus status = CouponStatus.fromInt(jsonCoupon.getInt("status"));
			
			coupon.setAmount(amount);
			coupon.setExpireTime(expireTime);
			coupon.setStatus(status);
		}catch (JSONException e) {
			throw new ValidationException("无效数据格式");
		}
		
		return coupon;
	}

}
