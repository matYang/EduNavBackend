package UserModule.resources.alipay;

import java.security.Provider.Service;
import java.util.Calendar;

import org.restlet.resource.Get;
import BaseModule.alipay.AlipayConfig;
import BaseModule.alipay.AlipayNotify;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.model.Booking;
import BaseModule.service.EncodingService;
import UserModule.resources.UserPseudoResource;

public class AlipayIdResource extends UserPseudoResource {

    @Get
    public void processUserAlipayFeedBack() {
        String success = null;
        String notifyId = null;
        String tradeStatus = null;
        String verified = null;
        Calendar now = DateUtility.getCurTimeInstance();
        now.add(Calendar.SECOND, 60);        
        String max = DateUtility.toSQLDateTime(now);
        
        try {
            success = this.getReqAttr("is_success");
            if (success.equals("T")) {
                // 成功调取
                notifyId = this.getReqAttr("notify_id");
                String notify_time =EncodingService.decodeURI(this.getReqAttr("notify_time"));
                if (max.compareTo(notify_time) >= 0) {
                    this.redirectTemporary(AlipayConfig.failureRedirect);
                }
                verified = AlipayNotify.Verify(notifyId);
                if (verified.equals("true")) {
                    // 验证通过
                    tradeStatus = this.getReqAttr("trade_status");
                    if (tradeStatus.equals("TRADE_SUCCESS")
                            || tradeStatus.equals("TRADE_FINISHED")) {
                        String bookingRef = this.getReqAttr("out_trade_no");
                        Booking booking = BookingDaoService
                                .getBookingByReference(bookingRef);
                        booking.setStatus(BookingStatus.paid);
                        this.redirectTemporary(AlipayConfig.successRedirect);
                    } else {
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
