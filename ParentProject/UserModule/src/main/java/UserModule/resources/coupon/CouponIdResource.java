package UserModule.resources.coupon;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;

import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.dbservice.CouponDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Coupon;
import UserModule.resources.UserPseudoResource;

public class CouponIdResource extends UserPseudoResource{
	private final String apiId = CouponIdResource.class.getSimpleName();
	
	@Put
	public Representation activateCoupon(Representation entity){
		int couponId = -1;
		JSONObject couponObject = new JSONObject();
		try{
			this.checkEntity(entity);
			int userId = this.validateAuthentication();
			couponId = Integer.parseInt(this.getReqAttr("id"));
			//JSONObject jsonCoupon = this.getJSONObj(entity);
			//jsonCoupon.put("couponId", couponId);
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_put, userId, this.getUserAgent(), couponObject.toString());
			
			Coupon coupon = CouponDaoService.getCouponByCouponId(couponId);
			if (coupon.getUserId() != userId){
				throw new AuthenticationException("该消费券不属于您");
			}
			if (coupon.getOrigin() != CouponOrigin.invitation){
				throw new ValidationException("该消费券不是由邀请码生成，不可在此激活");
			}
			if (coupon.getStatus() != CouponStatus.inactive){
				throw new ValidationException("该消费券已经不处于未激活状态，请刷新");
			}
			
			coupon.setStatus(CouponStatus.usable);
			int previousAmount = coupon.getAmount();
			CouponStatus previousStatus = coupon.getStatus();
			CouponDaoService.updateCouponToUser(coupon, previousAmount, previousStatus);
			
			couponObject = JSONFactory.toJSON(coupon);
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
	

}
