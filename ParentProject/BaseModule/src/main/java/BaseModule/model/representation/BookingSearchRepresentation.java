package BaseModule.model.representation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.BookingType;
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.ModelReflectiveService;

public class BookingSearchRepresentation implements PseudoModel, PseudoRepresentation {
	
	private int bookingId;
	private int startPrice;
	private int finishPrice;
	private int userId;
	private int partnerId;
	private int courseId;
	private String email;
	private String name;
	private String phone;
	private BookingStatus status;
	private BookingStatus preStatus;
	private String reference;		

	private Calendar startScheduledTime;
	private Calendar finishScheduledTime;
	private Calendar startAdjustTime;
	private Calendar finishAdjustTime;
	private Calendar startCreationTime;
	private Calendar finishCreationTime;
	
	private Calendar startBookingStatusAdjustTime;
	private Calendar finishBookingStatusAdjustTime;
	
	
	private Calendar startServiceFeeStatusAdjustTime;
	private Calendar finishServiceFeeStatusAdjustTime;
	private Calendar startCommissionStatusAdjustTime;
	private Calendar finishCommissionStatusAdjustTime;

	private Calendar startNoRefundDate;
	private Calendar finishNoRefundDate;
	private Calendar startCashbackDate;
	private Calendar finishCashbackDate;
	private BookingType bookingType;
	private ServiceFeeStatus serviceFeeStatus;
	private CommissionStatus commissionStatus;	
	
	private ServiceFeeStatus preServiceFeeStatus;
	private CommissionStatus preCommissionStatus;

	public BookingSearchRepresentation(){
		this.bookingId = -1;
		this.startPrice = -1;
		this.finishPrice = -1;
		this.userId = -1;
		this.partnerId = -1;
		this.courseId = -1;
		this.name = null;
		this.phone = null;
		this.status = null;
		this.preStatus = null;
		this.reference = null;
		this.email = null;		
		
		this.startScheduledTime = null;
		this.finishScheduledTime = null;
		this.startAdjustTime = null;
		this.finishAdjustTime = null;
		this.startCreationTime = null;
		this.finishCreationTime = null;
		
		this.startNoRefundDate = null;
		this.finishNoRefundDate = null;
		this.startCashbackDate = null;
		this.finishCashbackDate = null;
		this.bookingType = null;
		this.serviceFeeStatus = null;
		this.commissionStatus = null;
		
		this.startServiceFeeStatusAdjustTime = null;
		this.finishServiceFeeStatusAdjustTime = null;
		this.startCommissionStatusAdjustTime = null;
		this.finishCommissionStatusAdjustTime = null;
		this.preServiceFeeStatus = null;
		this.preCommissionStatus = null;
		
	}

	@Override
	public ArrayList<String> getKeySet() {
		return ModelReflectiveService.getKeySet(this);
	}

	@Override
	public void storeKvps(Map<String, String> kvps)  throws Exception {
		ModelReflectiveService.storeKvps(this, kvps);
	}

	@Override
	public boolean isEmpty() throws Exception {
		return ModelReflectiveService.isEmpty(this);
	}

	@Override
	public JSONObject toJSON() throws Exception {
		return ModelReflectiveService.toJSON(this);
	}

	
	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(int startPrice) {
		this.startPrice = startPrice;
	}

	public int getFinishPrice() {
		return finishPrice;
	}

	public void setFinishPrice(int finishPrice) {
		this.finishPrice = finishPrice;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public BookingStatus getPreStatus() {
		return preStatus;
	}

	public void setPreStatus(BookingStatus preStatus) {
		this.preStatus = preStatus;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Calendar getStartScheduledTime() {
		return startScheduledTime;
	}

	public void setStartScheduledTime(Calendar startScheduledTime) {
		this.startScheduledTime = startScheduledTime;
	}

	public Calendar getFinishScheduledTime() {
		return finishScheduledTime;
	}

	public void setFinishScheduledTime(Calendar finishScheduledTime) {
		this.finishScheduledTime = finishScheduledTime;
	}

	public Calendar getStartAdjustTime() {
		return startAdjustTime;
	}

	public void setStartAdjustTime(Calendar startAdjustTime) {
		this.startAdjustTime = startAdjustTime;
	}

	public Calendar getFinishAdjustTime() {
		return finishAdjustTime;
	}

	public void setFinishAdjustTime(Calendar finishAdjustTime) {
		this.finishAdjustTime = finishAdjustTime;
	}

	public Calendar getStartCreationTime() {
		return startCreationTime;
	}

	public void setStartCreationTime(Calendar startCreationTime) {
		this.startCreationTime = startCreationTime;
	}

	public Calendar getFinishCreationTime() {
		return finishCreationTime;
	}

	public void setFinishCreationTime(Calendar finishCreationTime) {
		this.finishCreationTime = finishCreationTime;
	}

	public Calendar getStartBookingStatusAdjustTime() {
		return startBookingStatusAdjustTime;
	}

	public void setStartBookingStatusAdjustTime(
			Calendar startBookingStatusAdjustTime) {
		this.startBookingStatusAdjustTime = startBookingStatusAdjustTime;
	}

	public Calendar getFinishBookingStatusAdjustTime() {
		return finishBookingStatusAdjustTime;
	}

	public void setFinishBookingStatusAdjustTime(
			Calendar finishBookingStatusAdjustTime) {
		this.finishBookingStatusAdjustTime = finishBookingStatusAdjustTime;
	}

	public Calendar getStartServiceFeeStatusAdjustTime() {
		return startServiceFeeStatusAdjustTime;
	}

	public void setStartServiceFeeStatusAdjustTime(
			Calendar startServiceFeeStatusAdjustTime) {
		this.startServiceFeeStatusAdjustTime = startServiceFeeStatusAdjustTime;
	}

	public Calendar getFinishServiceFeeStatusAdjustTime() {
		return finishServiceFeeStatusAdjustTime;
	}

	public void setFinishServiceFeeStatusAdjustTime(
			Calendar finishServiceFeeStatusAdjustTime) {
		this.finishServiceFeeStatusAdjustTime = finishServiceFeeStatusAdjustTime;
	}

	public Calendar getStartCommissionStatusAdjustTime() {
		return startCommissionStatusAdjustTime;
	}

	public void setStartCommissionStatusAdjustTime(
			Calendar startCommissionStatusAdjustTime) {
		this.startCommissionStatusAdjustTime = startCommissionStatusAdjustTime;
	}

	public Calendar getFinishCommissionStatusAdjustTime() {
		return finishCommissionStatusAdjustTime;
	}

	public void setFinishCommissionStatusAdjustTime(
			Calendar finishCommissionStatusAdjustTime) {
		this.finishCommissionStatusAdjustTime = finishCommissionStatusAdjustTime;
	}

	public Calendar getStartNoRefundDate() {
		return startNoRefundDate;
	}

	public void setStartNoRefundDate(Calendar startNoRefundDate) {
		this.startNoRefundDate = startNoRefundDate;
	}

	public Calendar getFinishNoRefundDate() {
		return finishNoRefundDate;
	}

	public void setFinishNoRefundDate(Calendar finishNoRefundDate) {
		this.finishNoRefundDate = finishNoRefundDate;
	}

	public Calendar getStartCashbackDate() {
		return startCashbackDate;
	}

	public void setStartCashbackDate(Calendar startCashbackDate) {
		this.startCashbackDate = startCashbackDate;
	}

	public Calendar getFinishCashbackDate() {
		return finishCashbackDate;
	}

	public void setFinishCashbackDate(Calendar finishCashbackDate) {
		this.finishCashbackDate = finishCashbackDate;
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
	

	@Override
	public String toString() {
		return "BookingSearchRepresentation [bookingId=" + bookingId
				+ ", startPrice=" + startPrice + ", finishPrice=" + finishPrice
				+ ", userId=" + userId + ", partnerId=" + partnerId
				+ ", courseId=" + courseId + ", email=" + email + ", name="
				+ name + ", phone=" + phone + ", status=" + status
				+ ", preStatus=" + preStatus + ", reference=" + reference
				+ ", startScheduledTime=" + startScheduledTime
				+ ", finishScheduledTime=" + finishScheduledTime
				+ ", startAdjustTime=" + startAdjustTime
				+ ", finishAdjustTime=" + finishAdjustTime
				+ ", startCreationTime=" + startCreationTime
				+ ", finishCreationTime=" + finishCreationTime
				+ ", startBookingStatusAdjustTime="
				+ startBookingStatusAdjustTime
				+ ", finishBookingStatusAdjustTime="
				+ finishBookingStatusAdjustTime
				+ ", startServiceFeeStatusAdjustTime="
				+ startServiceFeeStatusAdjustTime
				+ ", finishServiceFeeStatusAdjustTime="
				+ finishServiceFeeStatusAdjustTime
				+ ", startCommissionStatusAdjustTime="
				+ startCommissionStatusAdjustTime
				+ ", finishCommissionStatusAdjustTime="
				+ finishCommissionStatusAdjustTime + ", startNoRefundDate="
				+ startNoRefundDate + ", finishNoRefundDate="
				+ finishNoRefundDate + ", startCashbackDate="
				+ startCashbackDate + ", finishCashbackDate="
				+ finishCashbackDate + ", bookingType=" + bookingType
				+ ", serviceFeeStatus=" + serviceFeeStatus
				+ ", commissionStatus=" + commissionStatus
				+ ", preServiceFeeStatus=" + preServiceFeeStatus
				+ ", preCommissionStatus=" + preCommissionStatus + "]";
	}

	public String getSearchQuery() {
		String query = "SELECT * from Booking ";
		boolean start = false;
		
		/* Note:Make sure the order following is the same as that in Dao */
		
		if(this.getBookingId() > 0){
			query += "where ";
			start = true;
			
			query += "id = ? ";
		}
		if(this.getCourseId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "course_Id = ? ";
		}
		if(this.getPartnerId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "p_Id = ? ";
		}
		if(this.getUserId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "u_Id = ? ";
		}		
		if(this.getStartPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "price >= ? ";
		}
		if(this.getFinishPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "price <= ? ";
		}		
		if(this.getStatus() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "status = ? ";
		}		
		if(this.getReference() !=null && this.getReference().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "reference = ? ";
		}	
		if(this.getEmail() != null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "email = ? ";
		}
		if(this.getName() != null && this.getName().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "name = ? ";
		}
		if(this.getPhone() != null && this.getPhone().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "phone = ? ";
		}		
		if(this.preStatus != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "preStatus = ? ";
		}
		if(this.startAdjustTime != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "adjustTime >= ? ";
		}
		if(this.finishAdjustTime != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "adjustTime <= ? ";
		}
		if(this.startCreationTime != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime >= ? ";
		}
		if(this.finishCreationTime != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime <= ? ";
		}
		if(this.startScheduledTime != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "scheduledTime >= ? ";
		}
		if(this.finishScheduledTime != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "scheduledTime <= ? ";
		}
		if(this.startNoRefundDate != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "startNoRefundDate >= ? ";
		}
		if(this.finishNoRefundDate != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "finishNoRefundDate <= ? ";
		}
		if(this.startCashbackDate != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "startCashbackDate >= ? ";
		}
		if(this.finishCashbackDate != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "finishCashbackDate <= ? ";
		}
		if(this.bookingType != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "bookingType = ? ";
		}
		if(this.serviceFeeStatus != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "serviceFeeStatus = ? ";
		}
		if(this.commissionStatus != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "commissionStatus = ? ";
		}
		if(this.startServiceFeeStatusAdjustTime != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "serviceFeeAdjustTime >= ? ";
		}
		if(this.finishServiceFeeStatusAdjustTime != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "serviceFeeAdjustTime <= ? ";
		}
		if(this.startCommissionStatusAdjustTime != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "commissionStatusAdjustTime >= ? ";
		}
		if(this.finishCommissionStatusAdjustTime != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "commissionStatusAdjustTime <= ? ";
		}
		if(this.preServiceFeeStatus != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "preServiceFeeStatus = ? ";
		}
		if(this.preCommissionStatus != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "preCommissionStatus = ? ";
		}
		return query;
	}
	

	
}
