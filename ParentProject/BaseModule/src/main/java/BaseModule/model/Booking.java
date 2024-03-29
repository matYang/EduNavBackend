package BaseModule.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.BookingType;
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;

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
	
	private ServiceFeeStatus preServiceFeeStatus;
	private CommissionStatus preCommissionStatus;
	private Calendar bookingStatusAdjustTime;
	
	private Calendar serviceFeeStatusAdjustTime;
	private Calendar commissionStatusAdjustTime;
	private String serviceFeeActionRecord;
	private String commissionActionRecord;
	
	private String note;
	private int cashbackAmount;
	private String couponRecord;	//id_amount-...
	private String actionRecord;	//id_action_timestamp-..
	
	private transient Course course;
	

	private Booking(){}
	public static Booking getInstance(){
		Booking booking = new Booking();
		try {
			ModelReflectiveService.initialize(booking);
		} catch (Exception e) {
			return null;
		}
		return booking;
	}
	
	//SQL Retrieving
	public Booking(int bookingId, Calendar creationTime, Calendar adjustTime,
			int price, int userId,int partnerId, int courseId, String name, String phone,
			BookingStatus status, String reference, long transactionId, String email,Calendar scheduledTime,
			String note, int cashbackAmount, String couponRecord,
			String actionRecord, Course course,BookingStatus preStatus, Calendar noRefundDate,
			Calendar cashbackDate,BookingType bookingType,ServiceFeeStatus serviceFeeStatus,
			CommissionStatus commissionStatus,Calendar serviceFeeStatusAdjustTime,Calendar commissionStatusAdjustTime,
			String serviceFeeActionRecord,String commissionActionRecord,ServiceFeeStatus preServiceFeeStatus,
			CommissionStatus preCommissionStatus,Calendar bookingStatusAdjustTime) {
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
		this.serviceFeeStatusAdjustTime = serviceFeeStatusAdjustTime;
		this.commissionStatusAdjustTime = commissionStatusAdjustTime;
		this.serviceFeeActionRecord = serviceFeeActionRecord;
		this.commissionActionRecord = commissionActionRecord;
		this.preServiceFeeStatus = preServiceFeeStatus;
		this.preCommissionStatus = preCommissionStatus;
		this.bookingStatusAdjustTime = bookingStatusAdjustTime;
		
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
		this.serviceFeeStatusAdjustTime = DateUtility.getCurTimeInstance();
		this.commissionStatusAdjustTime = DateUtility.getCurTimeInstance();
		this.serviceFeeActionRecord = "";
		this.commissionActionRecord = "";
		this.preServiceFeeStatus = ServiceFeeStatus.refundCharge;
		this.preCommissionStatus = CommissionStatus.refundCharge;
		this.bookingStatusAdjustTime = DateUtility.getCurTimeInstance();
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
	
  	public Calendar getServiceFeeStatusAdjustTime() {
		return serviceFeeStatusAdjustTime;
	}

	public void setServiceFeeStatusAdjustTime(Calendar serviceFeeStatusAdjustTime) {
		this.serviceFeeStatusAdjustTime = serviceFeeStatusAdjustTime;
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

	public void appendServiceFeeActionRecord(ServiceFeeStatus newStatus, int adminId) {
		String actionRecordPiece = newStatus.code + "_" + adminId + "_" + DateUtility.getCurTime();
		this.serviceFeeActionRecord = this.serviceFeeActionRecord.length() == 0 ? actionRecordPiece : this.serviceFeeActionRecord + "-" + actionRecordPiece;
	}


	public String getCommissionActionRecord() {
		return this.commissionActionRecord;
	}


	public void appendCommissionActionRecord(CommissionStatus newStatus, int adminId) {
		String actionRecordPiece = newStatus.code + "_" + adminId + "_" + DateUtility.getCurTime();
		this.commissionActionRecord = this.commissionActionRecord.length() == 0 ? actionRecordPiece : this.commissionActionRecord + "-" + actionRecordPiece;
	}

	public ServiceFeeStatus getPreServiceFeeStatus() {
		return preServiceFeeStatus;
	}

	public void setPreServiceFeeStatus(ServiceFeeStatus preServiceFeeStatus) {
		this.preServiceFeeStatus = preServiceFeeStatus;
	}

	public CommissionStatus getPreCommissionStatus() {
		return preCommissionStatus;
	}

	public void setPreCommissionStatus(CommissionStatus preCommissionStatus) {
		this.preCommissionStatus = preCommissionStatus;
	}

	public Calendar getBookingStatusAdjustTime() {
		return bookingStatusAdjustTime;
	}

	public void setBookingStatusAdjustTime(Calendar bookingStatusAdjustTime) {
		this.bookingStatusAdjustTime = bookingStatusAdjustTime;
	}

	public Booking deepCopy() throws IOException, ClassNotFoundException{
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(this);
        oos.close();
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Booking clone = (Booking) ois.readObject();
        
        return clone;
	}

	public JSONObject toJSON() throws Exception{
		return ModelReflectiveService.toJSON(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Booking other = (Booking) obj;
		if (actionRecord == null) {
			if (other.actionRecord != null)
				return false;
		} else if (!actionRecord.equals(other.actionRecord))
			return false;
		if (bookingId != other.bookingId)
			return false;
		if (bookingType != other.bookingType)
			return false;
		if (cashbackAmount != other.cashbackAmount)
			return false;
		if (commissionActionRecord == null) {
			if (other.commissionActionRecord != null)
				return false;
		} else if (!commissionActionRecord.equals(other.commissionActionRecord))
			return false;
		if (commissionStatus != other.commissionStatus)
			return false;
		if (couponRecord == null) {
			if (other.couponRecord != null)
				return false;
		} else if (!couponRecord.equals(other.couponRecord))
			return false;
		if (courseId != other.courseId)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (partnerId != other.partnerId)
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (preCommissionStatus != other.preCommissionStatus)
			return false;
		if (preServiceFeeStatus != other.preServiceFeeStatus)
			return false;
		if (preStatus != other.preStatus)
			return false;
		if (price != other.price)
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (serviceFeeActionRecord == null) {
			if (other.serviceFeeActionRecord != null)
				return false;
		} else if (!serviceFeeActionRecord.equals(other.serviceFeeActionRecord))
			return false;
		if (serviceFeeStatus != other.serviceFeeStatus)
			return false;
		if (status != other.status)
			return false;
		if (transactionId != other.transactionId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}


	
}
