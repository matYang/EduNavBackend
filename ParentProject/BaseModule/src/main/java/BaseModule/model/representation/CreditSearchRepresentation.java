package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.ModelReflectiveService;

public class CreditSearchRepresentation implements PseudoModel, PseudoRepresentation {
	
	private long creditId;
	private int bookingId;
	private int userId;
	private int startAmount;
	private int finishAmount;
	private Calendar expireTime;
	private CreditStatus status;
	
	private Calendar startCreationTime;
	private Calendar finishCreationTime;
	
	
	public CreditSearchRepresentation() {
		this.creditId = -1l;
		this.bookingId = -1;
		this.userId = -1;
		this.startAmount = -1;
		this.finishAmount = -1;
		this.startCreationTime = null;
		this.finishCreationTime = null;
		this.expireTime = null;		
		this.status = null;
	}

	@Override
	public ArrayList<String> getKeySet() {
		return ModelReflectiveService.getKeySet(this);
	}

	@Override
	public void storeKvps(Map<String, String> kvps) throws IllegalArgumentException, IllegalAccessException, PseudoException, UnsupportedEncodingException {
		ModelReflectiveService.storeKvps(this, kvps);
	}
	
	@Override
	public String serialize() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ValidationException {
		return ModelReflectiveService.serialize(this);
	}
	
	@Override
	public boolean isEmpty() throws Exception {
		return ModelReflectiveService.isEmpty(this);
	}

	@Override
	public JSONObject toJSON() throws ValidationException {
		return ModelReflectiveService.toJSON(this);
	}

	public long getCreditId() {
		return creditId;
	}

	public void setCreditId(long creditId) {
		this.creditId = creditId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(int startAmount) {
		this.startAmount = startAmount;
	}

	public int getFinishAmount() {
		return finishAmount;
	}

	public void setFinishAmount(int finishAmount) {
		this.finishAmount = finishAmount;
	}	

	public Calendar getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Calendar expireTime) {
		this.expireTime = expireTime;
	}
	
	public CreditStatus getStatus() {
		return status;
	}

	public void setStatus(CreditStatus status) {
		this.status = status;
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
		return "CreditSearchRepresentation [creditId=" + creditId
				+ ", bookingId=" + bookingId + ", userId=" + userId
				+ ", startAmount=" + startAmount + ", finishAmount="
				+ finishAmount + ", startCreationTime=" + startCreationTime + ", finishCreationTime=" + finishCreationTime 
				+ ", expireTime=" + expireTime + ", status=" + status + "]";
	}

	public String getSearchQuery(){
		String query = "SELECT * from CreditDao ";
		boolean start = false;

		/* Note:Make sure the order following is the same as that in Dao */

		if(this.getCreditId() > 0){
			query += "where ";
			start = true;

			query += "creditId = ? ";
		}
		if(this.getBookingId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "bookingId = ? ";
		}
		if(this.getStartAmount() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "amount >= ? ";
		}
		if(this.getFinishAmount() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "amount <= ? ";
		}		
		if(this.getUserId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "userId = ? ";
		}		
		if(this.getStartCreationTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime >= ? ";
		}
		if(this.getFinishCreationTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime <= ? ";
		}
		if(this.getExpireTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "expireTime = ? ";
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
		return query;
	}	
	

}
