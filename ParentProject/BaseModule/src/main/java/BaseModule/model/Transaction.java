package BaseModule.model;

import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import BaseModule.common.DateUtility;


public class Transaction {

	private int transactionId;
	private int userId;
	private int bookingId;
	private double transactionAmount;
	private Calendar creationTime;
	
	//Normal Construction
	public Transaction(int userId, int bookingId, double transactionAmount) {
		super();
		this.transactionId = -1;
		this.userId = userId;
		this.bookingId = bookingId;
		this.transactionAmount = transactionAmount;
		this.creationTime = DateUtility.getCurTimeInstance();
	}
	
	//SQL Construction
	public Transaction(int transactionId,int userId, int bookingId, double transactionAmount,
			Calendar creationTime) {
		super();
		this.transactionId = transactionId;
		this.userId = userId;
		this.bookingId = bookingId;
		this.transactionAmount = transactionAmount;
		this.creationTime = creationTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
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
			jsonSearchRepresentation.put("amount", this.transactionAmount);		
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;

	}
	
	public boolean equals(Transaction transaction){
		return this.bookingId == transaction.getBookingId() && this.transactionId == transaction.getTransactionId() &&
				this.userId == transaction.getUserId() && this.transactionAmount == transaction.getTransactionAmount() &&
				this.creationTime.getTime().toString().equals(transaction.getCreationTime().getTime().toString());
	}
	
	
}
