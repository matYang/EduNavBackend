package UserModule.resources.booking;

import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Booking;
import UserModule.resources.UserPseudoResource;

public class BookingResource extends UserPseudoResource{

	@Post
	public Representation createBooking(Representation entity){
		Booking booking = null;
		JSONObject bookingObject = new JSONObject();
		try{
			this.checkEntity(entity);
			this.validateAuthentication();
			booking = BookingDaoService.createBooking(parseJSON(entity));
			DebugLog.d("@Post :: resources::createBooking: avaliable: " + booking.getBookingId() + " partner is: " + booking.getPartnerId() + " course is: " + booking.getCourseId());
			bookingObject = JSONFactory.toJSON(booking);
		}catch(PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		}

		Representation result = new JsonRepresentation(bookingObject);

		this.addCORSHeader(); 
		return result;


	}

	protected Booking parseJSON(Representation entity) throws ValidationException {
		JSONObject jsonBooking = null;
		Booking booking = null;
		try{
			jsonBooking = (new JsonRepresentation(entity)).getJsonObject();
			Calendar timeStamp = DateUtility.castFromAPIFormat(jsonBooking.getString("timeStamp"));
			Calendar creationTime = DateUtility.castFromAPIFormat(jsonBooking.getString("creationTime"));
			Calendar startTime = DateUtility.castFromAPIFormat(jsonBooking.getString("startTime"));
			Calendar finishTime = DateUtility.castFromAPIFormat(jsonBooking.getString("finishTime"));
			int bookingId = jsonBooking.getInt("bookingId");
			int price = jsonBooking.getInt("price");
			int userId = jsonBooking.getInt("userId");
			int partnerId = jsonBooking.getInt("partnerId");
			int courseId = jsonBooking.getInt("courseId");
			String name = jsonBooking.getString("name");
			String phone = jsonBooking.getString("phone");
			String reference = jsonBooking.getString("reference");
			AccountStatus status = AccountStatus.fromInt(jsonBooking.getInt("status"));
			booking = new Booking(bookingId, creationTime, timeStamp,startTime,finishTime,price,userId,partnerId,
					courseId, name,phone,status, reference);
		}catch (JSONException|IOException e) {
			throw new ValidationException("无效数据格式");
		}
		return booking;
	}

}
