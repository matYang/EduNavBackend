package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.exception.PseudoException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

public class CreditSearchRepresentation implements PseudoModel, PseudoRepresentation {
	
	private long creditId;
	private int bookingId;
	private int userId;
	private int startPrice;
	private int finishPrice;
	private Calendar creationTime;
	private Calendar expireTime;
	private Calendar usableTime;
	private CreditStatus status;
	
	
	public CreditSearchRepresentation() {
		this.creditId = -1l;
		this.bookingId = -1;
		this.userId = -1;
		this.startPrice = -1;
		this.finishPrice = -1;
		this.creationTime = null;
		this.expireTime = null;
		this.usableTime = null;
		this.status = null;
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

	public Calendar getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
	}

	public Calendar getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Calendar expireTime) {
		this.expireTime = expireTime;
	}

	public Calendar getUsableTime() {
		return usableTime;
	}

	public void setUsableTime(Calendar usableTime) {
		this.usableTime = usableTime;
	}

	public CreditStatus getStatus() {
		return status;
	}

	public void setStatus(CreditStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CreditSearchRepresentation [creditId=" + creditId
				+ ", bookingId=" + bookingId + ", userId=" + userId
				+ ", startPrice=" + startPrice + ", finishPrice=" + finishPrice
				+ ", creationTime=" + creationTime + ", expireTime="
				+ expireTime + ", usableTime=" + usableTime + ", status="
				+ status + "]";
	}
	
	public String getSearchQuery(){
		String query = "SELECT * from CreditDao ";
		boolean start = false;

		/* Note:Make sure the order following is the same as that in Dao */

		if(this.getCreditId() > 0){
			query += "where ";
			start = true;

			query += "couponId = ? ";
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
		if(this.getStartPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "amount >= ? ";
		}
		if(this.getFinishPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "amount <= ? ";
		}
		if(this.getUsableTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "useableTime = ? ";
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
		if(this.getCreationTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime = ? ";
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
