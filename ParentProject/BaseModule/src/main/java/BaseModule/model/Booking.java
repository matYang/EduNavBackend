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

	private int couponId;
	private int transactionId;
	private int adminId;
	private int userId;
	private int partnerId;
	private int courseId;
	private int price;

	private Course course;

	private AccountStatus status;
	private String reference;
	private String name;
	private String phone;
	private String email;

	private Calendar startTime;
	private Calendar finishTime;
	private Calendar creationTime;
	private Calendar adjustTime;
	private Calendar scheduledTime;



	//SQL Retrieving
	public Booking(int bookingId, Calendar creationTime, Calendar adjustTime,
			Calendar startTime, Calendar finishTime, int price, int userId,
			int partnerId, int courseId, String name, String phone,
			AccountStatus status, String reference, int couponId, int transactionId,
			int adminId,Course course,String email,Calendar scheduledTime) {
		super();
		this.bookingId = bookingId;
		this.creationTime = creationTime;
		this.adjustTime = adjustTime;
		this.startTime = startTime;
		this.finishTime = finishTime;
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
	}

	//Normal Construction
	public Booking(int transactionId,Calendar scheduledTime,Calendar adjustTime,Calendar startTime, 
			Calendar finishTime, int price,	int userId, int partnerId, int courseId, String name, 
			String phone, String email,	String reference,AccountStatus status) {
		super();
		this.transactionId = transactionId;
		this.couponId = -1;
		this.adminId = -1;
		this.scheduledTime = scheduledTime;
		this.adjustTime = adjustTime;
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
		this.email = email;		
		this.creationTime = DateUtility.getCurTimeInstance();
	}
	
	public Booking(Calendar scheduledTime,Calendar adjustTime,Calendar startTime,
			Calendar finishTime, int price,	int userId, int partnerId, int courseId, 
			String name, String phone, String email,
			String reference,AccountStatus status) {
		super();
		this.transactionId = -1;
		this.couponId = -1;
		this.adminId = -1;
		this.scheduledTime = scheduledTime;
		this.adjustTime = adjustTime;
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
		this.email = email;		
		this.creationTime = DateUtility.getCurTimeInstance();
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
			jsonSearchRepresentation.put("startTime", DateUtility.castToAPIFormat(this.startTime));
			jsonSearchRepresentation.put("finishTime", DateUtility.castToAPIFormat(this.finishTime));
			jsonSearchRepresentation.put("scheduledTime", DateUtility.castToAPIFormat(this.scheduledTime));
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
					this.startTime.getTime().toString().equals(booking.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(booking.getFinishTime().getTime().toString()) && 
					this.adjustTime.getTime().toString().equals(booking.getAdjustTime().getTime().toString()) &&
					this.scheduledTime.getTime().toString().equals(booking.getScheduledTime().getTime().toString()) &&
					this.reference.equals(booking.getReference()) && this.status.code == booking.getStatus().code && 
					this.userId == booking.getUserId();
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
					this.startTime.getTime().toString().equals(booking.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(booking.getFinishTime().getTime().toString()) && 
					this.adjustTime.getTime().toString().equals(booking.getAdjustTime().getTime().toString()) &&
					this.scheduledTime.getTime().toString().equals(booking.getScheduledTime().getTime().toString()) &&
					this.reference.equals(booking.getReference()) && this.status.code == booking.getStatus().code && 
					this.userId == booking.getUserId() &&
					this.course.equals(booking.getCourse());
		}
	}


}
