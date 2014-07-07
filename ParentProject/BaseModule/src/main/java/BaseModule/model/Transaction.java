package BaseModule.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import org.json.JSONObject;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;

public class Transaction implements PseudoModel, Serializable{

	private static final long serialVersionUID = 6L;
	
	private long transactionId;
	private int userId;
	private int bookingId;	
	private int transactionAmount;
	private TransactionType transactionType;
	private Calendar creationTime;
	
	private Transaction(){}
	public static Transaction getInstance(){
		Transaction transaction = new Transaction();
		try {
			ModelReflectiveService.initialize(transaction);
		} catch (Exception e) {
			return null;
		}
		return transaction;
	}

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
	
	public Transaction deepCopy() throws IOException, ClassNotFoundException{
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(this);
        oos.close();
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Transaction clone = (Transaction) ois.readObject();
        
        return clone;
	}
	
	public JSONObject toJSON() throws Exception{
		return ModelReflectiveService.toJSON(this);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (bookingId != other.bookingId)
			return false;
		if (transactionAmount != other.transactionAmount)
			return false;
		if (transactionId != other.transactionId)
			return false;
		if (transactionType != other.transactionType)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
	
	
}
