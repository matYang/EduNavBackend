package UserModule.resources.alipay;

import org.restlet.resource.Post;
import UserModule.resources.UserPseudoResource;
import BaseModule.alipay.AlipayNotify;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.model.Booking;

public class AlipayResource extends UserPseudoResource {

    @Post
    public String processAlipayFeedBack() {
        String notifyId = null;
        String tradeStatus = null;
        String verified = null;
        String feedback = "fail";
        try {
            notifyId = this.getQueryVal("notify_id");
//            verified = AlipayNotify.Verify(notifyId);
            verified = "true";
            if (verified.equals("true")) {
                DebugLog.b_d("post: verified");
                tradeStatus = this.getQueryVal("trade_status");

                if (tradeStatus.equals("TRADE_SUCCESS")
                        || tradeStatus.equals("TRADE_FINISHED")) {
                    DebugLog.b_d("post: status verified");
                    String bookingRef = this.getQueryVal("out_trade_no");
                    Booking booking = BookingDaoService
                            .getBookingByReference(bookingRef);
                    booking.setStatus(BookingStatus.paid);
                    BookingDaoService.updateBookingInfo(booking);
                    feedback = "success";
                } else {
                    DebugLog.b_d("post: status unverified");
                }

            } else {
                DebugLog.b_d("post: unverified");
            }
        } catch (Exception e) {
            DebugLog.b_d(e.getMessage());
        } finally {
            this.addCORSHeader();
        }

        return feedback;
    }

}
