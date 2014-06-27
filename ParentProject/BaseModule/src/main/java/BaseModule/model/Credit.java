package BaseModule.model;

import java.io.Serializable;
import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;


public class Credit implements PseudoModel, Serializable{
	
	private static final long serialVersionUID = 8L;
	
	public static final long usableThreshould = 31536000000l;
	public static final long expireThreshould = 31536000000l;	//1 year

	private long creditId;
	private int bookingId;
	private int userId;
	private int amount;
	private Calendar creationTime;
	private Calendar expireTime;	
	
	private CreditStatus status;
	
	//SQL Construction
	public Credit(long creditId, int bookingId, int userId, int amount,
			Calendar creationTime, Calendar expireTime, CreditStatus status) {
		super();
		this.creditId = creditId;
		this.bookingId = bookingId;
		this.userId = userId;
		this.amount = amount;
		this.creationTime = creationTime;
		this.expireTime = expireTime;
		this.status = status;
		
	}
	
	//normal construction
	public Credit(int bookingId, int userId, int amount){
		this.creditId = -1;
		this.bookingId = bookingId;
		this.userId = userId;
		this.amount = amount;
		this.creationTime = DateUtility.getCurTimeInstance();
		this.expireTime = DateUtility.getTimeFromLong(DateUtility.getCurTime() + usableThreshould);
		this.status = CreditStatus.usable;		
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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


	public Calendar getCreationTime() {
		return creationTime;
	}
	
	public JSONObject toJSON() throws Exception{
		return ModelReflectiveService.toJSON(this);
	}
	
	public boolean equals(Credit c){
		if (c == null){
			return false;
		}
		return this.creditId == c.getCreditId() && 
				this.bookingId == c.getBookingId() && 				
				this.userId == c.getUserId() && 
				this.status.code == c.getStatus().code &&
				this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) &&
				this.expireTime.getTime().toString().equals(c.getExpireTime().getTime().toString()) && 
				this.amount == c.getAmount();
	}
	
	
}
