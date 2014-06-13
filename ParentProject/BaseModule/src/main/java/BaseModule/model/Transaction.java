package BaseModule.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.interfaces.PseudoModel;


public class Transaction implements PseudoModel, Serializable{

	private static final long serialVersionUID = 6L;
	
	private long transactionId;
	private int userId;
	private int bookingId;	
	private int transactionAmount;
	private TransactionType transactionType;
	private Calendar creationTime;

	//SQL Construction
	public Transaction(long transactionId,int userId, int bookingId, 
			int transactionAmount,TransactionType transactionType,Calendar creationTime) {
		super();
		this.transactionId = transactionId;
		this.userId = userId;
		this.bookingId = bookingId;		
		this.transactionAmount = transactionAmount;
		this.transactionType = transactionType;
		this.creationTime = creationTime;
	}
	
	//normal construction
	public Transaction(int userId, int bookingId, int transactionAmount, TransactionType transactionType) {
		super();
		this.transactionId = -1;		
		this.userId = userId;
		this.bookingId = bookingId;
		this.transactionAmount = transactionAmount;
		this.transactionType = transactionType;
		this.creationTime = DateUtility.getCurTimeInstance();
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}
	
	public JSONObject toJSON(){
		JSONObject jsonObj = new JSONObject();
		try{
			jsonObj.put("transactionId", this.transactionId);
			jsonObj.put("bookingId", this.bookingId);
			jsonObj.put("userId", this.userId);			
			jsonObj.put("transactionType", this.transactionType.code);
			jsonObj.put("transactionAmount", this.transactionAmount);		
			jsonObj.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));			
		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonObj;

	}
	
	public boolean equals(Transaction transaction){
		if (transaction == null){
			return false;
		}
		return this.bookingId == transaction.getBookingId() && this.transactionId == transaction.getTransactionId() &&
				this.userId == transaction.getUserId() && this.transactionAmount == transaction.getTransactionAmount() &&
				this.creationTime.getTime().toString().equals(transaction.getCreationTime().getTime().toString()) &&
				this.transactionType.code == transaction.getTransactionType().code;
	}
	
	
}
