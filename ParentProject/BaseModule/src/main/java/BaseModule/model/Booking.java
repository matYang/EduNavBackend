package BaseModule.model;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.EncodingService;

public class Booking implements PseudoModel{

	private int bookingId;
	
	private int userId;
	private int partnerId;
	private int courseId;
	private int price;
	
	private AccountStatus status;
	private String reference;
	private String name;
	private String phone;
	
	private Calendar startTime;
	private Calendar finishTime;
	private Calendar creationTime;
	private Calendar timeStamp;

	
	


	//SQL Retrieving
	public Booking(int bookingId, Calendar creationTime, Calendar timeStamp,
			Calendar startTime, Calendar finishTime, int price, int userId,
			int partnerId, int courseId, String name, String phone,
			AccountStatus status, String reference) {
		super();
		this.bookingId = bookingId;
		this.creationTime = creationTime;
		this.timeStamp = timeStamp;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.price = price;
		this.userId = userId;
		this.partnerId = partnerId;
		this.courseId = courseId;
		this.name = name;
		this.phone = phone;
		this.status = status;
		this.reference = reference;
	}

	//Normal Construction
	public Booking(Calendar timeStamp,Calendar startTime, Calendar finishTime, int price,
			int userId, int partnerId, int courseId, String name, String phone,
			String reference,AccountStatus status) {
		super();
		this.timeStamp = timeStamp;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.price = price;
		this.userId = userId;
		this.partnerId = partnerId;
		this.courseId = courseId;
		this.name = name;
		this.phone = phone;
		this.reference = reference;
		this.status = status;
		this.creationTime = DateUtility.getCurTimeInstance();
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int id) {
		this.bookingId = id;
	}

	public Calendar getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Calendar timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Calendar finishTime) {
		this.finishTime = finishTime;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}	

	public Calendar getCreationTime() {
		return creationTime;
	}

	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("bookingId", this.bookingId);
			jsonSearchRepresentation.put("name", EncodingService.encodeURI(this.name));
			jsonSearchRepresentation.put("phone", EncodingService.encodeURI(this.phone));
			jsonSearchRepresentation.put("price", this.price);
			jsonSearchRepresentation.put("status", this.status.code);
			jsonSearchRepresentation.put("reference", EncodingService.encodeURI(this.reference));
			jsonSearchRepresentation.put("userId", this.userId);
			jsonSearchRepresentation.put("partnerId", this.partnerId);
			jsonSearchRepresentation.put("courseId", this.courseId);
			jsonSearchRepresentation.put("timeStamp", DateUtility.castToAPIFormat(this.timeStamp));
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));	
			jsonSearchRepresentation.put("startTime", DateUtility.castToAPIFormat(this.startTime));
			jsonSearchRepresentation.put("finishTime", DateUtility.castToAPIFormat(this.finishTime));
		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;

	}

	public boolean equals(Booking booking){		
		return this.bookingId==booking.getBookingId() && this.name.equals(booking.getName()) && this.courseId == booking.getCourseId() &&
				this.partnerId == booking.getPartnerId() && this.phone.equals(booking.getPhone()) && this.price == booking.getPrice() &&
				this.creationTime.getTime().toString().equals(booking.getCreationTime().getTime().toString()) && this.startTime.getTime().toString().equals(booking.getStartTime().getTime().toString()) &&
				this.finishTime.getTime().toString().equals(booking.getFinishTime().getTime().toString()) && this.timeStamp.getTime().toString().equals(booking.getTimeStamp().getTime().toString()) &&
				this.reference.equals(booking.getReference()) && this.status.code == booking.getStatus().code && this.userId == booking.getUserId();
	}


}
