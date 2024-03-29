package BaseModule.model.representation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.ModelReflectiveService;

public class TransactionSearchRepresentation implements PseudoModel, PseudoRepresentation {
	
	private long transactionId;
	private int userId;	
	private int bookingId;
	private TransactionType transactionType;
	private int startAmount;
	private int finishAmount;
	
	private Calendar startCreationTime;
	private Calendar finishCreationTime;
	
	public TransactionSearchRepresentation(){
		this.transactionId = -1;
		this.userId = -1;		
		this.bookingId = -1;
		this.transactionType = null;
		this.startCreationTime = null;
		this.finishCreationTime = null;
		this.startAmount = -1;
		this.finishAmount = -1;
	}

	@Override
	public ArrayList<String> getKeySet() {
		return ModelReflectiveService.getKeySet(this);
	}

	@Override
	public void storeKvps(Map<String, String> kvps)  throws Exception {
		ModelReflectiveService.storeKvps(this, kvps);
	}
	
	@Override
	public boolean isEmpty() throws Exception {
		return ModelReflectiveService.isEmpty(this);
	}

	@Override
	public JSONObject toJSON() throws Exception {
		return ModelReflectiveService.toJSON(this);
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
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

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
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
		return "TransactionSearchRepresentation [transactionId="
				+ transactionId + ", userId=" + userId + ", bookingId="
				+ bookingId + ", transactionType=" + transactionType
				+ ", startAmount=" + startAmount + ", startCreationTime=" 
				+ startCreationTime + ", finishCreationTime=" + finishCreationTime
				+", finishAmount=" + finishAmount + "]";
	}

	public String getSearchQuery(){
		String query = "SELECT * from Transaction ";
		boolean start = false;
		
		/* Note:Make sure the order following is the same as that in Dao */
		
		if(this.getTransactionId() > 0){
			query += "where ";
			start = true;
			
			query += "transactionId = ? ";
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
		if(this.getUserId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "userId = ? ";
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
		if(this.getTransactionType() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "transactionType = ? ";
		}
		return query;
	}
	

}
