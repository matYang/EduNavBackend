package UserModule.resources.alipay;

import org.restlet.resource.Post;
import UserModule.resources.UserPseudoResource;
import BaseModule.alipay.AlipayNotify;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.model.Booking;


public class AlipayResource extends UserPseudoResource{

	@Post
	public String processAlipayFeedBack(){
		String notifyId = null;
		String tradeStatus = null;
		String verified = null;	
		String feedback = "fail";
		try{
			notifyId = this.getReqAttr("notify_id");
			verified = AlipayNotify.Verify(notifyId);

			if(verified.equals("true")){
				tradeStatus = this.getReqAttr("trade_status");

				if(tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")){					
					String bookingRef = this.getReqAttr("out_trade_no");
					Booking booking = BookingDaoService.getBookingByReference(bookingRef);
					booking.setStatus(BookingStatus.paid);
					BookingDaoService.updateBookingInfo(booking);			
					feedback = "success";		
				}
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		
		this.addCORSHeader();		
		return feedback;
	}

}
