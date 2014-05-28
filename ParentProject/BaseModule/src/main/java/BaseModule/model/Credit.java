package BaseModule.model;

import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CreditStatus;

public class Credit {
	
	public static final long usableThreshould = 604800l;
	public static final long expireThreshould = 31536000l + 604800l;	//1 year

	private long creditId;
	private int bookingId;
	private int userId;
	private int amount;
	private Calendar creationTime;
	private Calendar expireTime;
	private Calendar usableTime;
	
	private CreditStatus status;
	
	//SQL Construction
	public Credit(long creditId, int bookingId, int userId, int amount,
			Calendar creationTime, Calendar expireTime, CreditStatus status,Calendar usableTime) {
		super();
		this.creditId = creditId;
		this.bookingId = bookingId;
		this.userId = userId;
		this.amount = amount;
		this.creationTime = creationTime;
		this.expireTime = expireTime;
		this.status = status;
		this.usableTime = usableTime;
	}

	public Credit(int bookingId, int userId, int amount,
			 Calendar expireTime, CreditStatus status,Calendar usableTime) {
		super();
		this.bookingId = bookingId;
		this.userId = userId;
		this.amount = amount;
		this.creationTime = DateUtility.getCurTimeInstance();
		this.expireTime = expireTime;
		this.status = status;
		this.usableTime = usableTime;
	}
	
	public Credit(int bookingId, int amount, int userId){
		this.creditId = -1;
		this.bookingId = bookingId;
		this.userId = userId;
		this.amount = amount;
		this.creationTime = DateUtility.getCurTimeInstance();
		this.expireTime =DateUtility.getTimeFromLong(DateUtility.getCurTime() + usableThreshould);
		this.status = CreditStatus.awaiting;
		this.usableTime = DateUtility.getTimeFromLong(DateUtility.getCurTime() + expireThreshould);
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

	public Calendar getUsableTime() {
		return usableTime;
	}

	public void setUsableTime(Calendar usableTime) {
		this.usableTime = usableTime;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}
	
	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("credit", this.creditId);
			jsonSearchRepresentation.put("bookingId", this.bookingId);
			jsonSearchRepresentation.put("userId", this.userId);			
			jsonSearchRepresentation.put("amount", this.amount);
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));	
			jsonSearchRepresentation.put("expireTime", DateUtility.castToAPIFormat(this.expireTime));
			jsonSearchRepresentation.put("usableTime", DateUtility.castToAPIFormat(this.usableTime));
			jsonSearchRepresentation.put("status", this.status.code);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;
	}
	
	public boolean equals(Credit c){
		return this.creditId == c.getCreditId() && 
				this.bookingId == c.getBookingId() && 				
				this.userId == c.getUserId() && 
				this.status.code == c.getStatus().code &&
				this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) &&
				this.expireTime.getTime().toString().equals(c.getExpireTime().getTime().toString()) && 
				this.usableTime.getTime().toString().equals(c.getUsableTime().getTime().toString()) &&
				this.amount == c.getAmount();
	}
	
	
}
