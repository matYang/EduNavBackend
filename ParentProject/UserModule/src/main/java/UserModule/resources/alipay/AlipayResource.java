package UserModule.resources.alipay;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import UserModule.resources.UserPseudoResource;
import BaseModule.alipay.AlipayNotify;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.model.Booking;

public class AlipayResource extends UserPseudoResource {

    @Post
    public Representation processAlipayFeedBack() {
        String notifyId = null;
        String tradeStatus = null;
        String verified = null;
        Representation representation = null;

        try {
            notifyId = this.getQueryVal("notify_id");
            verified = AlipayNotify.Verify(notifyId);
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
                    System.out.println("success");
                    representation = new StringRepresentation("success",
                            MediaType.TEXT_PLAIN);
                    representation.setCharacterSet(CharacterSet.UTF_8);
                    return representation;
                } else {
                    DebugLog.b_d("post: status unverified");
                    representation = new StringRepresentation("fail",
                            MediaType.TEXT_PLAIN);
                    representation.setCharacterSet(CharacterSet.UTF_8);
                    return representation;
                }

            } else {
                DebugLog.b_d("post: unverified");
                representation = new StringRepresentation("fail",
                        MediaType.TEXT_PLAIN);
                representation.setCharacterSet(CharacterSet.UTF_8);
                return representation;
            }
        } catch (Exception e) {
            DebugLog.b_d(e.getMessage());
        } finally {
            this.addCORSHeader();
        }
        representation = new StringRepresentation("fail", MediaType.TEXT_PLAIN);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;

    }
    
    @Get
    public Representation get(){
        System.out.println("success");
        Representation representation = new StringRepresentation("",
                MediaType.TEXT_PLAIN);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }
}
