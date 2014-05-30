package BaseModule.model;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.EncodingService;

public class Booking implements PseudoModel{

	private int bookingId;

	private int couponId;
	private int transactionId;
	private int adminId;
	private int userId;
	private int partnerId;
	private int courseId;
	private int price;

	private BookingStatus status;
	private String reference;
	private String name;
	private String phone;
	private String email;
	private Calendar scheduledTime;
	
	private Calendar creationTime;
	private Calendar adjustTime;
	
	private Course course;
	
	//TODO added
	private boolean wasConfirmed;
	private String actionRecord;

	//SQL Retrieving
	//TODO
	public Booking(int bookingId, Calendar creationTime, Calendar adjustTime,
			int price, int userId,int partnerId, int courseId, String name, String phone,
			BookingStatus status, String reference, int couponId, int transactionId,
			int adminId,Course course,String email,Calendar scheduledTime,boolean wasConfirmed,
			String actionRecord) {
		super();
		this.bookingId = bookingId;
		this.creationTime = creationTime;
		this.adjustTime = adjustTime;		
		this.scheduledTime = scheduledTime;
		this.price = price;
		this.userId = userId;
		this.partnerId = partnerId;
		this.courseId = courseId;
		this.name = name;
		this.phone = phone;
		this.status = status;
		this.reference = reference;
		this.email = email;
		this.adminId = adminId;
		this.course = course;
		this.transactionId = transactionId;
		this.couponId = couponId;
		this.wasConfirmed = wasConfirmed;
		this.actionRecord = actionRecord;
	}

	
	public Booking(Calendar scheduledTime,Calendar adjustTime,
			int price, int couponId, int userId, int partnerId, int courseId, 
			String name, String phone, String email,
			String reference,BookingStatus status) {
		super();
		this.transactionId = -1;
		this.couponId = couponId;
		this.adminId = -1;
		this.scheduledTime = scheduledTime;
		this.adjustTime = adjustTime;		
		this.price = price;
		this.userId = userId;
		this.partnerId = partnerId;
		this.courseId = courseId;
		this.name = name;
		this.phone = phone;
		this.reference = reference;
		this.status = status;		
		this.email = email;		
		this.creationTime = DateUtility.getCurTimeInstance();
		this.wasConfirmed = false;
		this.actionRecord = "";
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int id) {
		this.bookingId = id;
	}

	public Calendar getAdjustTime() {
		return adjustTime;
	}

	public void setAdjustTime(Calendar adjustTime) {
		this.adjustTime = adjustTime;
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

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}	

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Calendar getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Calendar scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}
	
	public void setWasConfirmed(boolean wasConfirmed){
		this.wasConfirmed = wasConfirmed;
	}
	
	public boolean isWasConfirmed(){
		return this.wasConfirmed;
	}
	
	public void setActionRecord(String actionRecord) {
		this.actionRecord = actionRecord;
	}


	public void appendActionRecord(BookingStatus newStatus, int adminId){
		String actionRecordPiece = newStatus.code + "_" + adminId + "_" + DateUtility.getCurTime();
		this.actionRecord = this.actionRecord.length() == 0 ? actionRecordPiece : this.actionRecord + "-" + actionRecordPiece;
	}
	
	public String getActionRecord(){
		return this.actionRecord;
	}

	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("bookingId", this.bookingId);
			jsonSearchRepresentation.put("adminId", this.adminId);
			jsonSearchRepresentation.put("couponId", this.couponId);
			jsonSearchRepresentation.put("transactionId", this.transactionId);
			jsonSearchRepresentation.put("email", EncodingService.encodeURI(this.email));
			jsonSearchRepresentation.put("name", EncodingService.encodeURI(this.name));
			jsonSearchRepresentation.put("phone", EncodingService.encodeURI(this.phone));
			jsonSearchRepresentation.put("price", this.price);
			jsonSearchRepresentation.put("status", this.status.code);
			jsonSearchRepresentation.put("reference", EncodingService.encodeURI(this.reference));
			jsonSearchRepresentation.put("userId", this.userId);
			jsonSearchRepresentation.put("partnerId", this.partnerId);
			jsonSearchRepresentation.put("courseId", this.courseId);
			jsonSearchRepresentation.put("adjustTime", DateUtility.castToAPIFormat(this.adjustTime));
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));			
			jsonSearchRepresentation.put("scheduledTime", DateUtility.castToAPIFormat(this.scheduledTime));
			jsonSearchRepresentation.put("wasConfirmed", this.wasConfirmed);
			jsonSearchRepresentation.put("actionRecord", EncodingService.encodeURI(this.actionRecord));
			jsonSearchRepresentation.put("course", this.course == null ? new JSONObject() : this.course.toJSON());
		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;

	}

	public boolean equals(Booking booking){		
		if(this.course == null){
			return this.bookingId==booking.getBookingId() && 
					this.adminId == booking.getAdminId() &&
					this.couponId == booking.getCouponId() &&
					this.transactionId == booking.getTransactionId() &&
					this.email.equals(booking.getEmail()) &&
					this.name.equals(booking.getName()) && 
					this.courseId == booking.getCourseId() &&
					this.partnerId == booking.getPartnerId() && 
					this.phone.equals(booking.getPhone()) && 
					this.price == booking.getPrice() &&
					this.creationTime.getTime().toString().equals(booking.getCreationTime().getTime().toString()) && 					
					this.adjustTime.getTime().toString().equals(booking.getAdjustTime().getTime().toString()) &&
					this.scheduledTime.getTime().toString().equals(booking.getScheduledTime().getTime().toString()) &&
					this.reference.equals(booking.getReference()) && this.status.code == booking.getStatus().code && 
					this.userId == booking.getUserId() &&
					this.wasConfirmed == booking.wasConfirmed &&
					this.actionRecord.equals(booking.actionRecord);
		}
		else{
			return this.bookingId==booking.getBookingId() && 
					this.adminId == booking.getAdminId() &&
					this.couponId == booking.getCouponId() &&
					this.transactionId == booking.getTransactionId() &&
					this.email.equals(booking.getEmail()) &&
					this.name.equals(booking.getName()) && 
					this.courseId == booking.getCourseId() &&
					this.partnerId == booking.getPartnerId() && 
					this.phone.equals(booking.getPhone()) && 
					this.price == booking.getPrice() &&
					this.creationTime.getTime().toString().equals(booking.getCreationTime().getTime().toString()) && 					
					this.adjustTime.getTime().toString().equals(booking.getAdjustTime().getTime().toString()) &&
					this.scheduledTime.getTime().toString().equals(booking.getScheduledTime().getTime().toString()) &&
					this.reference.equals(booking.getReference()) && this.status.code == booking.getStatus().code && 
					this.userId == booking.getUserId() &&
					this.course.equals(booking.getCourse()) &&
					this.wasConfirmed == booking.wasConfirmed &&
					this.actionRecord.equals(booking.actionRecord);
		}
	}


}
