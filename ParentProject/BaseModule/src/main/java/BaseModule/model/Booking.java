package BaseModule.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.BookingType;
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.EncodingService;

public class Booking implements PseudoModel, Serializable{
	
	public static final long cashbackDelay = 777600l;	//unconditioanlly 9 days
	private static final long serialVersionUID = 5L;

	private int bookingId;
	private long transactionId;
	private int userId;
	private int partnerId;
	private int courseId;
	private int price;

	private BookingStatus status;
	private BookingStatus preStatus;
	private String reference;
	private String name;
	private String phone;
	private String email;
	private Calendar scheduledTime;
	private Calendar creationTime;
	private Calendar adjustTime;
		
	private Calendar noRefundDate;
	private Calendar cashbackDate;
	private BookingType bookingType;
	private ServiceFeeStatus serviceFeeStatus;
	private CommissionStatus commissionStatus;
	//TODO
	private ServiceFeeStatus preServiceFeeStatus;
	private CommissionStatus preCommissionStatus;

	private Calendar serviceFeeAdjustTime;
	private Calendar commissionStatusAdjustTime;
	private String serviceFeeActionRecord;
	private String commissionActionRecord;

	
	private String note;
	private int cashbackAmount;
	private String couponRecord;	//id_amount-...
	private String actionRecord;	//id_action_timestamp-..
	
	private transient Course course;

	
	//SQL Retrieving
	public Booking(int bookingId, Calendar creationTime, Calendar adjustTime,
			int price, int userId,int partnerId, int courseId, String name, String phone,
			BookingStatus status, String reference, long transactionId, String email,Calendar scheduledTime,
			String note, int cashbackAmount, String couponRecord,
			String actionRecord, Course course,BookingStatus preStatus, Calendar noRefundDate,
			Calendar cashbackDate,BookingType bookingType,ServiceFeeStatus serviceFeeStatus,
			CommissionStatus commissionStatus,Calendar serviceFeeAdjustTime,Calendar commissionStatusAdjustTime,
			String serviceFeeActionRecord,String commissionActionRecord) {
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
		this.transactionId = transactionId;		
		this.note = note;
		this.cashbackAmount = cashbackAmount;
		this.couponRecord = couponRecord;
		this.actionRecord = actionRecord;
		this.course = course;
		this.preStatus = preStatus;
		this.noRefundDate = noRefundDate;
		this.cashbackDate = cashbackDate;
		this.bookingType = bookingType;
		this.serviceFeeStatus = serviceFeeStatus;
		this.commissionStatus = commissionStatus;
		this.serviceFeeAdjustTime = serviceFeeAdjustTime;
		this.commissionStatusAdjustTime = commissionStatusAdjustTime;
		this.serviceFeeActionRecord = serviceFeeActionRecord;
		this.commissionActionRecord = commissionActionRecord;
		
		
		
	}

	//normal construction
	public Booking(Calendar scheduledTime, Calendar adjustTime,
			int price, int userId, int partnerId, int courseId, 
			String name, String phone, String email,
			String reference,BookingStatus status, int cashbackAmount) {
		super();
		this.bookingId = -1;
		this.transactionId = -1;
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
		this.preStatus = status;
		this.email = email;		
		this.creationTime = DateUtility.getCurTimeInstance();		
		this.note = "";
		this.cashbackAmount = cashbackAmount;
		this.couponRecord = "";
		this.actionRecord = "";
		this.course = null;
		this.noRefundDate = DateUtility.getCurTimeInstance();
		this.cashbackDate = DateUtility.getCurTimeInstance();
		this.bookingType = BookingType.online;
		this.serviceFeeStatus = ServiceFeeStatus.refundCharge;
		this.commissionStatus = CommissionStatus.refundCharge;
		this.serviceFeeAdjustTime = DateUtility.getCurTimeInstance();
		this.commissionStatusAdjustTime = DateUtility.getCurTimeInstance();
		this.serviceFeeActionRecord = "";
		this.commissionActionRecord = "";
		
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

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
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
	
	public void setActionRecord(String actionRecord) {
		this.actionRecord = actionRecord;
	}


	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BookingStatus getPreStatus() {
		return preStatus;
	}


	public void setPreStatus(BookingStatus preStatus) {
		this.preStatus = preStatus;
	}


	public int getCashbackAmount() {
		return cashbackAmount;
	}

	public void setCashbackAmount(int cashbackAmount) {
		this.cashbackAmount = cashbackAmount;
	}


	public String getCouponRecord() {
		return couponRecord;
	}

	public void setCouponRecord(String couponRecord) {
		this.couponRecord = couponRecord;
	}


	public void appendActionRecord(BookingStatus newStatus, int adminId){
		String actionRecordPiece = newStatus.code + "_" + adminId + "_" + DateUtility.getCurTime();
		this.actionRecord = this.actionRecord.length() == 0 ? actionRecordPiece : this.actionRecord + "-" + actionRecordPiece;
	}
	
	public String getActionRecord(){
		return this.actionRecord;
	}

	public Calendar getNoRefundDate() {
		return noRefundDate;
	}

	public void setNoRefundDate(Calendar noRefundDate) {
		this.noRefundDate = noRefundDate;
	}

	public Calendar getCashbackDate() {
		return cashbackDate;
	}

	public void setCashbackDate(Calendar cashbackDate) {
		this.cashbackDate = cashbackDate;
	}

	public BookingType getBookingType() {
		return bookingType;
	}

	public void setBookingType(BookingType bookingType) {
		this.bookingType = bookingType;
	}

	public ServiceFeeStatus getServiceFeeStatus() {
		return serviceFeeStatus;
	}

	public void setServiceFeeStatus(ServiceFeeStatus serviceFeeStatus) {
		this.serviceFeeStatus = serviceFeeStatus;
	}

	public CommissionStatus getCommissionStatus() {
		return commissionStatus;
	}

	public void setCommissionStatus(CommissionStatus commissionStatus) {
		this.commissionStatus = commissionStatus;
	}	
	

	public Calendar getServiceFeeAdjustTime() {
		return serviceFeeAdjustTime;
	}

	public void setServiceFeeAdjustTime(Calendar serviceFeeAdjustTime) {
		this.serviceFeeAdjustTime = serviceFeeAdjustTime;
	}

	public Calendar getCommissionStatusAdjustTime() {
		return commissionStatusAdjustTime;
	}

	public void setCommissionStatusAdjustTime(Calendar commissionStatusAdjustTime) {
		this.commissionStatusAdjustTime = commissionStatusAdjustTime;
	}

	public String getServiceFeeActionRecord() {
		return serviceFeeActionRecord;
	}

	public void setServiceFeeActionRecord(String serviceFeeActionRecord) {
		this.serviceFeeActionRecord = serviceFeeActionRecord;
	}

	public String getCommissionActionRecord() {
		return commissionActionRecord;
	}

	public void setCommissionActionRecord(String commissionActionRecord) {
		this.commissionActionRecord = commissionActionRecord;
	}

	public Booking deepCopy() throws IOException, ClassNotFoundException{
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(this);
        oos.close();
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Booking clone = (Booking) ois.readObject();
        
        return clone;
	}

	public JSONObject toJSON() throws ValidationException{
		JSONObject jsonObj = new JSONObject();
		try{
			jsonObj.put("bookingId", this.bookingId);
			jsonObj.put("transactionId", this.transactionId);
			jsonObj.put("email", EncodingService.encodeURI(this.email));
			jsonObj.put("name", EncodingService.encodeURI(this.name));
			jsonObj.put("phone", EncodingService.encodeURI(this.phone));
			jsonObj.put("price", this.price);
			jsonObj.put("status", this.status.code);
			jsonObj.put("preStaus", this.preStatus.code);
			jsonObj.put("reference", EncodingService.encodeURI(this.reference));
			jsonObj.put("userId", this.userId);
			jsonObj.put("partnerId", this.partnerId);
			jsonObj.put("courseId", this.courseId);
			jsonObj.put("adjustTime", DateUtility.castToAPIFormat(this.adjustTime));
			jsonObj.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));			
			jsonObj.put("scheduledTime", DateUtility.castToAPIFormat(this.scheduledTime));			
			jsonObj.put("note", EncodingService.encodeURI(this.note));
			jsonObj.put("cashbackAmount", this.cashbackAmount);
			jsonObj.put("couponRecord", EncodingService.encodeURI(this.couponRecord));
			jsonObj.put("actionRecord", EncodingService.encodeURI(this.actionRecord));
			jsonObj.put("course", this.course == null ? new JSONObject() : this.course.toJSON());
			jsonObj.put("noRefundDate",DateUtility.castToAPIFormat(this.noRefundDate));
			jsonObj.put("cashbackDate",DateUtility.castToAPIFormat(this.cashbackDate));
			jsonObj.put("bookingType", this.bookingType.code);
			jsonObj.put("serviceFeeStatus", this.serviceFeeStatus.code);
			jsonObj.put("commissionStatus", this.commissionStatus.code);
			jsonObj.put("serviceFeeAdjustTime", DateUtility.castToAPIFormat(this.serviceFeeAdjustTime));
			jsonObj.put("commissionStatusAdjustTime", DateUtility.castToAPIFormat(this.commissionStatusAdjustTime));
			jsonObj.put("serviceFeeActionRecord",EncodingService.encodeURI(this.serviceFeeActionRecord));
			jsonObj.put("commissionActionRecord",EncodingService.encodeURI(this.commissionActionRecord));			
			
		} catch (JSONException | UnsupportedEncodingException e) {
			DebugLog.d(e);
			throw new ValidationException("信息数据格式转换失败");
		}
		return jsonObj;

	}

	public boolean equals(Booking booking){
		boolean courseEqualResult = true;
		if (booking == null){
			return false;
		}
		if (this.course != null && booking.course != null){
			courseEqualResult = this.course.equals(booking.getCourse());
		}
		return this.bookingId==booking.getBookingId() && 
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
				this.preStatus.code == booking.getPreStatus().code &&
				this.userId == booking.getUserId() &&				
				this.couponRecord.equals(booking.getCouponRecord()) && 
				this.actionRecord.equals(booking.actionRecord) &&
				this.bookingType.code == booking.getBookingType().code &&
				this.serviceFeeStatus.code == booking.getServiceFeeStatus().code &&
				this.commissionStatus.code == booking.getCommissionStatus().code &&
				courseEqualResult;

	}


}
