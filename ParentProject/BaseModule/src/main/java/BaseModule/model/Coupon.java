package BaseModule.model;

import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CouponStatus;



public class Coupon {

	private long couponId;
	private int bookingId;
	private int transactionId;
	private int userId;
	private int amount;
	private Calendar creationTime;
	private Calendar expireTime;
	private CouponStatus status;
	
	//SQL Construction
	public Coupon(long couponId, int bookingId, int transactionId, int userId,
			int amount, Calendar creationTime, Calendar expireTime,
			CouponStatus status) {
		super();
		this.couponId = couponId;
		this.bookingId = bookingId;
		this.transactionId = transactionId;
		this.userId = userId;
		this.amount = amount;
		this.creationTime = creationTime;
		this.expireTime = expireTime;
		this.status = status;
	}

	//Normal Construction
	public Coupon(int bookingId, int transactionId, int userId,
			int amount, Calendar expireTime, CouponStatus status) {
		super();
		this.couponId = -1;
		this.bookingId = bookingId;
		this.transactionId = transactionId;
		this.userId = userId;
		this.amount = amount;
		this.expireTime = expireTime;
		this.status = status;
		this.creationTime = DateUtility.getCurTimeInstance();
	}
	
	public Coupon(int bookingId, int userId,
			int amount, Calendar expireTime, CouponStatus status) {
		super();
		this.couponId = -1;
		this.bookingId = bookingId;
		this.transactionId = -1;
		this.userId = userId;
		this.amount = amount;
		this.expireTime = expireTime;
		this.status = status;
		this.creationTime = DateUtility.getCurTimeInstance();
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
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

	public CouponStatus getStatus() {
		return status;
	}

	public void setStatus(CouponStatus status) {
		this.status = status;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}
	
	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("couponId", this.couponId);
			jsonSearchRepresentation.put("bookingId", this.bookingId);
			jsonSearchRepresentation.put("userId", this.userId);
			jsonSearchRepresentation.put("transactionId", this.transactionId);
			jsonSearchRepresentation.put("amount", this.amount);
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));	
			jsonSearchRepresentation.put("expireTime", DateUtility.castToAPIFormat(this.expireTime));
			jsonSearchRepresentation.put("status", this.status.code);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;
	}
	
	public boolean equals(Coupon c){
		return this.couponId == c.getCouponId() && 
				this.bookingId == c.getBookingId() && 
				this.transactionId == c.getTransactionId() &&
				this.userId == c.getUserId() && 
				this.status.code == c.getStatus().code &&
				this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) &&
				this.expireTime.getTime().toString().equals(c.getExpireTime().getTime().toString()) && 
				this.amount == c.getAmount();
	}
	
}
