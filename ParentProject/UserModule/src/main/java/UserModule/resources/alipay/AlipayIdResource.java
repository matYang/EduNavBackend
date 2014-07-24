package UserModule.resources.alipay;

import java.util.Calendar;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
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
    public Representation processUserAlipayFeedBack() {
        String success = null;
        String notifyId = null;
        String tradeStatus = null;
        String verified = null;
        Representation representation = null;
        Calendar now = DateUtility.getCurTimeInstance();
        now.add(Calendar.SECOND, 60);
        String max = DateUtility.toSQLDateTime(now);

        try {
            success = this.getQueryVal("is_success");
            if (success.equals("T")) {
                DebugLog.b_d("get: successful");
                // 成功调取
                notifyId = this.getQueryVal("notify_id");
                String notify_time = EncodingService.decodeURI(this
                        .getQueryVal("notify_time"));
                if (max.compareTo(notify_time) <= 0) {
                    DebugLog.b_d("get: max: " + max);
                    DebugLog.b_d("get: notify_time: " + notify_time);
                    DebugLog.b_d("get: too late to check");
                    this.redirectTemporary(AlipayConfig.failureRedirect);
                    representation = new StringRepresentation("fail",
                            MediaType.TEXT_PLAIN);
                    representation.setCharacterSet(CharacterSet.UTF_8);
                    return representation;
                }
                verified = AlipayNotify.Verify(notifyId);
                if (verified.equals("true")) {
                    // 验证通过
                    DebugLog.b_d("get: verified");
                    tradeStatus = this.getQueryVal("trade_status");
                    if (tradeStatus.equals("TRADE_SUCCESS")
                            || tradeStatus.equals("TRADE_FINISHED")) {
                        String bookingRef = this.getQueryVal("out_trade_no");
                        Booking booking = BookingDaoService
                                .getBookingByReference(bookingRef);
                        booking.setStatus(BookingStatus.paid);
                        BookingDaoService.updateBookingInfo(booking);
                        DebugLog.b_d("get: status verified");
                        this.redirectTemporary(AlipayConfig.successRedirect);
                        representation = new StringRepresentation("success",
                                MediaType.TEXT_PLAIN);
                        representation.setCharacterSet(CharacterSet.UTF_8);
                        return representation;
                    } else {
                        DebugLog.b_d("get: status verify failed");
                        this.redirectTemporary(AlipayConfig.failureRedirect);
                        representation = new StringRepresentation("fail",
                                MediaType.TEXT_PLAIN);
                        representation.setCharacterSet(CharacterSet.UTF_8);
                        return representation;
                    }
                } else {
                    DebugLog.b_d("get: unverified");
                    this.redirectTemporary(AlipayConfig.failureRedirect);
                    representation = new StringRepresentation("fail",
                            MediaType.TEXT_PLAIN);
                    representation.setCharacterSet(CharacterSet.UTF_8);
                    return representation;
                }

            } else {
                DebugLog.b_d("get: unsuccessful");
                this.redirectTemporary(AlipayConfig.failureRedirect);
                representation = new StringRepresentation("fail",
                        MediaType.TEXT_PLAIN);
                representation.setCharacterSet(CharacterSet.UTF_8);
                return representation;
            }
        } catch (Exception e) {
            DebugLog.b_d(e.getMessage());
            this.redirectTemporary(AlipayConfig.failureRedirect);
        } finally {
            this.addCORSHeader();
        }
        representation = new StringRepresentation("fail", MediaType.TEXT_PLAIN);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }
}
