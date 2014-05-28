package BaseModule.model;

import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.interfaces.PseudoModel;


public class Transaction implements PseudoModel{

	private int transactionId;
	private int userId;
	private int bookingId;
	private long couponId;
	private int transactionAmount;
	private Calendar creationTime;
	private TransactionType transactionType;
	
	//Normal Construction
	public Transaction(int userId, int bookingId, int transactionAmount) {
		super();
		this.transactionId = -1;
		this.couponId = -1;
		this.userId = userId;
		this.bookingId = bookingId;
		this.transactionAmount = transactionAmount;
		this.transactionType = TransactionType.deposit;
		this.creationTime = DateUtility.getCurTimeInstance();
	}
	
	public Transaction(int userId, int bookingId, long couponId,int transactionAmount, TransactionType transactionType) {
		super();
		this.transactionId = -1;
		this.couponId = couponId;
		this.userId = userId;
		this.bookingId = bookingId;
		this.transactionAmount = transactionAmount;
		this.transactionType = transactionType;
		this.creationTime = DateUtility.getCurTimeInstance();
	}
	
	//SQL Construction
	public Transaction(int transactionId,int userId, int bookingId, long couponId,
			int transactionAmount,TransactionType transactionType,Calendar creationTime) {
		super();
		this.transactionId = transactionId;
		this.userId = userId;
		this.bookingId = bookingId;
		this.couponId = couponId;
		this.transactionAmount = transactionAmount;
		this.transactionType = transactionType;
		this.creationTime = creationTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(int transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}
	
	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("transactionId", this.transactionId);
			jsonSearchRepresentation.put("bookingId", this.bookingId);
			jsonSearchRepresentation.put("userId", this.userId);
			jsonSearchRepresentation.put("couponId", this.couponId);
			jsonSearchRepresentation.put("transactionType", this.transactionType.code);
			jsonSearchRepresentation.put("transactionAmount", this.transactionAmount);		
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;

	}
	
	public boolean equals(Transaction transaction){
		return this.bookingId == transaction.getBookingId() && this.transactionId == transaction.getTransactionId() &&
				this.userId == transaction.getUserId() && this.transactionAmount == transaction.getTransactionAmount() &&
				this.creationTime.getTime().toString().equals(transaction.getCreationTime().getTime().toString()) &&
				this.couponId == transaction.getCouponId() && this.transactionType.code == transaction.getTransactionType().code;
	}
	
	
}
