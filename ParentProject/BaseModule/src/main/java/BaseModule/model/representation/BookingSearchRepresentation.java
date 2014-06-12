package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.exception.PseudoException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

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
	}

	@Override
	public ArrayList<String> getKeySet() {
		return RepresentationReflectiveService.getKeySet(this);
	}

	@Override
	public void storeKvps(Map<String, String> kvps) throws IllegalArgumentException, IllegalAccessException, PseudoException, UnsupportedEncodingException {
		RepresentationReflectiveService.storeKvps(this, kvps);
	}
	
	@Override
	public String serialize() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		return RepresentationReflectiveService.serialize(this);
	}
	

	@Override
	public boolean isEmpty() throws Exception {
		return RepresentationReflectiveService.isEmpty(this);
	}

	@Override
	public JSONObject toJSON() {
		return RepresentationReflectiveService.toJSON(this);
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(int price) {
		this.startPrice = price;
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

	public BookingStatus getPreStatus() {
		return preStatus;
	}

	public void setPreStatus(BookingStatus preStatus) {
		this.preStatus = preStatus;
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
				+ ", finishCreationTime=" + finishCreationTime + "]";
	}
	

	public String getSearchQuery() {
		String query = "SELECT * from BookingDao ";
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
		return query;
	}
	

	
}
