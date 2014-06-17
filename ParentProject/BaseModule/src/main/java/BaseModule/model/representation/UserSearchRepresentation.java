package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

public class UserSearchRepresentation implements PseudoModel, PseudoRepresentation {
	
	private int userId;

	private int startBalance;
	private int finishBalance;
	private int startCoupon;
	private	int finishCoupon;
	private int startCredit;
	private int finishCredit;
	
	private String name;
	private String phone;
	private String email;
	
	private String invitationalCode;
	private String appliedInvitationalCode;
	private String accountNumber;
	
	private AccountStatus status;
	private Calendar creationTime;
	
	
	public UserSearchRepresentation(){
		this.userId = -1;
		this.startBalance = -1;
		this.finishBalance = -1;
		this.startCoupon = -1;
		this.finishCoupon = -1;
		this.startCredit = -1;
		this.finishCredit = -1;
		this.name = null;
		this.phone = null;
		this.status = null;
		this.creationTime = null;
		this.email = null;
		this.invitationalCode = null;
		this.appliedInvitationalCode = null;
		this.accountNumber = null;
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


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStartBalance() {
		return startBalance;
	}

	public void setStartBalance(int startBalance) {
		this.startBalance = startBalance;
	}

	public int getFinishBalance() {
		return finishBalance;
	}

	public void setFinishBalance(int finishBalance) {
		this.finishBalance = finishBalance;
	}

	public int getStartCoupon() {
		return startCoupon;
	}

	public void setStartCoupon(int startCoupon) {
		this.startCoupon = startCoupon;
	}

	public int getFinishCoupon() {
		return finishCoupon;
	}

	public void setFinishCoupon(int finishiCoupon) {
		this.finishCoupon = finishiCoupon;
	}

	public int getStartCredit() {
		return startCredit;
	}

	public void setStartCredit(int startCredit) {
		this.startCredit = startCredit;
	}

	public int getFinishCredit() {
		return finishCredit;
	}

	public void setFinishCredit(int finishCredit) {
		this.finishCredit = finishCredit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInvitationalCode() {
		return invitationalCode;
	}

	public void setInvitationalCode(String invitationalCode) {
		this.invitationalCode = invitationalCode;
	}

	public String getAppliedInvitationalCode() {
		return appliedInvitationalCode;
	}

	public void setAppliedInvitationalCode(String appliedInvitationalCode) {
		this.appliedInvitationalCode = appliedInvitationalCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
	}
	
	@Override
	public String toString() {
		return "UserSearchRepresentation [userId=" + userId + ", startBalance="
				+ startBalance + ", finishBalance=" + finishBalance
				+ ", startCoupon=" + startCoupon + ", finishCoupon="
				+ finishCoupon + ", startCredit=" + startCredit
				+ ", finishCredit=" + finishCredit + ", name=" + name
				+ ", phone=" + phone + ", email=" + email
				+ ", invitationalCode=" + invitationalCode
				+ ", appliedInvitationalCode=" + appliedInvitationalCode
				+ ", accountNumber=" + accountNumber + ", status=" + status
				+ ", creationTime=" + creationTime + "]";
	}

	public String getSearchQuery() {
		String query = "SELECT * from UserDao ";		
		boolean start = false;	
		
		/* Note:Make sure the order following is the same as that in Dao */
		
		if(this.getUserId()>0){
			query += "where ";
			start = true;
			
			query += "id = ? ";
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
		if(this.getName() != null && this.getName().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "name = ? ";
		}
		if(this.getPhone() != null && this.getPhone().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "phone = ? ";
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
		if(this.getEmail() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "email = ? ";
		}
		if(this.invitationalCode != null && this.invitationalCode.length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "invitationalCode = ? ";
		}
		if(this.appliedInvitationalCode != null && this.appliedInvitationalCode.length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "appliedInvitationalCode = ? ";
		}
		if(this.accountNumber != null && this.accountNumber.length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "accountNum = ? ";
		}
		if(this.startBalance != -1){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "balance >= ? ";
		}
		if(this.finishBalance != -1){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "balance <= ? ";
		}
		if(this.startCoupon != -1){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "coupon >= ? ";
		}
		if(this.finishCoupon != -1){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "coupon <= ? ";
		}
		if(this.startCredit != -1){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "credit >= ? ";
		}
		if(this.finishCredit != -1){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "credit <= ? ";
		}
		return query;
	}
	
	

}
