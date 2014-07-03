package UserModule.resources.alipay;

import org.restlet.resource.Get;
import BaseModule.alipay.AlipayConfig;
import BaseModule.alipay.AlipayNotify;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.model.Booking;
import UserModule.resources.UserPseudoResource;

public class AlipayIdResource extends UserPseudoResource{

	@Get
	public void processUserAlipayFeedBack(){
		String success = null;
		String notifyId = null;
		String tradeStatus = null;
		String verified = null;		
		try{
			success = this.getReqAttr("ss_success");
			if(success.equals("T")){
				//成功调取
				notifyId = this.getReqAttr("notify_id");
				verified = AlipayNotify.Verify(notifyId);
				if(verified.equals("true")){
					//验证通过
					tradeStatus = this.getReqAttr("trade_status");
					if(tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")){					
						String bookingRef = this.getReqAttr("out_trade _no");						
						Booking booking = BookingDaoService.getBookingByReference(bookingRef);
						booking.setStatus(BookingStatus.paid);
						this.redirectTemporary(AlipayConfig.successRedirect);
					}else {
						this.redirectTemporary(AlipayConfig.failureRedirect);
					}
				}
			}			
		} catch (Exception e) {
			DebugLog.b_d(e.getMessage());
		}		
		this.addCORSHeader();
	}
}
