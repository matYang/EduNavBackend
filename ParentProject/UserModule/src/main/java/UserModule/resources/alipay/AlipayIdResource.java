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
import BaseModule.eduDAO.BookingDao;
import BaseModule.model.Booking;
import BaseModule.service.EncodingService;
import UserModule.resources.UserPseudoResource;

public class AlipayIdResource extends UserPseudoResource {

    @Get
    public String processUserAlipayFeedBack() {
        String success = null;
        String notifyId = null;
        String tradeStatus = null;
        String verified = null;
        Calendar now = DateUtility.getCurTimeInstance();
        now.add(Calendar.SECOND, 60);
        String max = DateUtility.toSQLDateTime(now);

        try {
            success = this.getQueryVal("is_success");
            if (success.equals("T")) {
                DebugLog.b_d("successful");
                // 成功调取
                notifyId = this.getQueryVal("notify_id");
                String notify_time = EncodingService.decodeURI(this
                        .getQueryVal("notify_time"));
                if (max.compareTo(notify_time) <= 0) {
                    DebugLog.b_d("max: " + max);
                    DebugLog.b_d("notify_time: " + notify_time);
                    DebugLog.b_d("too late to check");
                    this.redirectTemporary(AlipayConfig.failureRedirect);
                    return "fail";
                }
                verified = AlipayNotify.Verify(notifyId);
                if (verified.equals("true")) {
                    // 验证通过
                    DebugLog.b_d("verified");
                    tradeStatus = this.getQueryVal("trade_status");
                    if (tradeStatus.equals("TRADE_SUCCESS")
                            || tradeStatus.equals("TRADE_FINISHED")) {
                        String bookingRef = this.getQueryVal("out_trade_no");
                        Booking booking = BookingDaoService
                                .getBookingByReference(bookingRef);
                        booking.setStatus(BookingStatus.paid);
                        BookingDao.updateBookingInDatabases(booking);
                        DebugLog.b_d("status verified");
                        this.redirectTemporary(AlipayConfig.successRedirect);
                        return "success";
                    } else {
                        DebugLog.b_d("status verify failed");
                        this.redirectTemporary(AlipayConfig.failureRedirect);
                        return "fail";
                    }
                }
                DebugLog.b_d("unverified");
            }
            DebugLog.b_d("unsuccessful");
        } catch (Exception e) {
            DebugLog.b_d(e.getMessage());
            this.redirectTemporary(AlipayConfig.failureRedirect);
        } finally{
            this.addCORSHeader();
        }        
        return "fail";
    }
}
