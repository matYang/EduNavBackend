package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

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
		this.creationTime = null;
		this.startAmount = -1;
		this.finishAmount = -1;
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
	public String serialize() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ValidationException {
		return RepresentationReflectiveService.serialize(this);
	}
	
	@Override
	public boolean isEmpty() throws Exception {
		return RepresentationReflectiveService.isEmpty(this);
	}

	@Override
	public JSONObject toJSON() throws ValidationException {
		return RepresentationReflectiveService.toJSON(this);
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

	public Calendar getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
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

	@Override
	public String toString() {
		return "TransactionSearchRepresentation [transactionId="
				+ transactionId + ", userId=" + userId + ", bookingId="
				+ bookingId + ", transactionType=" + transactionType
				+ ", creationTime=" + creationTime + ", startAmount="
				+ startAmount + ", finishAmount=" + finishAmount + "]";
	}

	public String getSearchQuery(){
		String query = "SELECT * from TransactionDao ";
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
		if(this.getCreationTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime = ? ";
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
