package BaseModule.model.representation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.exception.PseudoException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

public class BookingSearchRepresentation implements PseudoModel, PseudoRepresentation {
	
	private int bookingId;
	private Calendar creationTime;
	private Calendar startTime;
	private Calendar finishTime;
	private int price;
	private int userId;
	private int partnerId;
	private int courseId;
	private String name;
	private String phone;
	private AccountStatus status;
	private String reference;
	
	public BookingSearchRepresentation(){
		this.bookingId = -1;
		this.creationTime = null;
		this.startTime = null;
		this.finishTime = null;
		this.price = -1;
		this.userId = -1;
		this.partnerId = -1;
		this.courseId = -1;
		this.name = null;
		this.phone = null;
		this.status = null;
		this.reference = null;
	}

	@Override
	public ArrayList<String> getKeySet() {
		return RepresentationReflectiveService.getKeySet(this);
	}

	@Override
	public void storeKvps(Map<String, String> kvps) throws IllegalArgumentException, IllegalAccessException, PseudoException {
		RepresentationReflectiveService.storeKvps(this, kvps);

	}
	
	@Override
	public String serialize() throws IllegalArgumentException, IllegalAccessException {
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

	public Calendar getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
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

	@Override
	public String toString() {
		return "BookingSearchRepresentation [bookingId=" + bookingId
				+ ", creationTime=" + creationTime + ", startTime=" + startTime
				+ ", finishTime=" + finishTime + ", price=" + price
				+ ", userId=" + userId + ", partnerId=" + partnerId
				+ ", courseId=" + courseId + ", name=" + name + ", phone="
				+ phone + ", status=" + status + ", reference=" + reference
				+ "]";
	}

	
}
