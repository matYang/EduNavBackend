package UserModule.resources.alipay;

import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
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

    @Post("application/x-www-form-urlencoded")
    public Representation processAlipayFeedBack(Representation entity) {
        DebugLog.b_d("Enter the Post entity");
        String notifyId = null;
        String tradeStatus = null;
        String verified = null;
        Representation representation = null;
        Form form = new Form(entity);
        
        try {
            notifyId = form.getFirstValue("notify_id");
            verified = AlipayNotify.Verify(notifyId);
            if (verified.equals("true")) {
                DebugLog.b_d("post: verified");
                tradeStatus = form.getFirstValue("trade_status");

                if (tradeStatus.equals("TRADE_SUCCESS")
                        || tradeStatus.equals("TRADE_FINISHED")) {
                    DebugLog.b_d("post: status verified");
                    String bookingRef = form.getFirstValue("out_trade_no");
                    Booking booking = BookingDaoService
                            .getBookingByReference(bookingRef);
                    booking.setStatus(BookingStatus.paid);
                    BookingDaoService.updateBookingInfo(booking);
                    System.out.println("success");
                    representation = new StringRepresentation("",
                            MediaType.TEXT_PLAIN);
                    representation.setCharacterSet(CharacterSet.UTF_8);
                    return representation;
                } else {
                    DebugLog.b_d("post: status unverified");
                    representation = new StringRepresentation("",
                            MediaType.TEXT_PLAIN);
                    representation.setCharacterSet(CharacterSet.UTF_8);
                    return representation;
                }

            } else {
                DebugLog.b_d("post: unverified");
                representation = new StringRepresentation("",
                        MediaType.TEXT_PLAIN);
                representation.setCharacterSet(CharacterSet.UTF_8);
                return representation;
            }
        } catch (Exception e) {
            DebugLog.b_d(e.getMessage());
        } finally {
            this.addCORSHeader();
        }
        representation = new StringRepresentation("", MediaType.TEXT_PLAIN);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;

    }
    
    @Get
    public Representation get(){
        System.out.println("success");
        Representation representation = new StringRepresentation("success",
                MediaType.TEXT_PLAIN);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }
}
