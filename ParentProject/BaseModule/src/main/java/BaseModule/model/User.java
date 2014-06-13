package BaseModule.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.factory.JSONFactory;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.EncodingService;

public class User implements PseudoModel, Serializable{
	
	private static final long serialVersionUID = 1L;

	private int userId;
	private int balance;
	private int coupon;
	private int credit;
	
	private String name;
	private String phone;
	private String password;
	private String email;
	
	private AccountStatus status;	
	
	private String invitationalCode;
	private String appliedInvitationalCode;
	
	private Calendar creationTime;
	private Calendar lastLogin;
	
	private transient ArrayList<Coupon> couponList = new ArrayList<Coupon>();
	private transient ArrayList<Credit> creditList = new ArrayList<Credit>();
	private transient ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

	//SQL Retrieving
	public User(int userId, String name, String phone, Calendar creationTime,
			Calendar lastLogin, String password, AccountStatus status,int balance,
			int coupon,int credit,String email,String invitationalCode,String appliedInvitationalCode) {
		super();
		this.userId = userId;		
		this.name = name;
		this.phone = phone;
		this.creationTime = creationTime;
		this.lastLogin = lastLogin;
		this.password = password;
		this.status = status;	
		this.balance = balance;
		this.coupon = coupon;
		this.credit = credit;
		this.email = email;
		this.invitationalCode = invitationalCode;
		this.appliedInvitationalCode = appliedInvitationalCode;
	}
	
	//Normal Construction
	public User(String phone, String password, String appliedInvitationalCode, String invitationalCode, AccountStatus status) {
		super();		
		this.balance = 0;
		this.coupon = 0;
		this.credit = 0;
		this.name = "";
		this.phone = phone;
		this.password = password;
		this.status = status;
		this.email = "";
		this.invitationalCode = invitationalCode;
		this.appliedInvitationalCode = appliedInvitationalCode;
		this.creationTime = DateUtility.getCurTimeInstance();
		if(this.lastLogin==null){
			this.lastLogin = (Calendar) this.creationTime.clone();
		}		
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public Calendar getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Calendar lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	public int getBalance() {
		return balance;
	}

	public void incBalance(int amount){
		this.balance += amount;
	}
	
	public void decBalance(int amount){
		this.balance -= amount;
	}

	public int getCoupon() {
		return coupon;
	}
	
	public void incCoupon(int amount){
		this.coupon += amount;
	}

	public void decCoupon(int amount){
		this.coupon -= amount;
	}

	public int getCredit() {
		return credit;
	}

	public void incCredit(int amount){
		this.credit += amount;
	}
	
	public void decCredit(int amount){
		this.credit -= amount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<Coupon> getCouponList() {
		return couponList;
	}

	public void setCouponList(ArrayList<Coupon> couponList) {
		this.couponList = couponList;
	}

	public ArrayList<Credit> getCreditList() {
		return creditList;
	}

	public void setCreditList(ArrayList<Credit> creditList) {
		this.creditList = creditList;
	}

	public ArrayList<Transaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(ArrayList<Transaction> transactionList) {
		this.transactionList = transactionList;
	}

	public Calendar getCreationTime() {
		return creationTime;
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

	public JSONObject toJSON(){
		JSONObject jsonObj = new JSONObject();
		try{
			jsonObj.put("id", this.userId);
			jsonObj.put("name", EncodingService.encodeURI(this.name));
			jsonObj.put("phone", EncodingService.encodeURI(this.phone));	
			jsonObj.put("email", EncodingService.encodeURI(this.email));
			jsonObj.put("invitationalCode", EncodingService.encodeURI(this.invitationalCode));
			jsonObj.put("appliedInvitationalCode", EncodingService.encodeURI(this.appliedInvitationalCode));
			jsonObj.put("status", this.status.code);
			jsonObj.put("amount", this.balance);
			jsonObj.put("coupon", this.coupon);
			jsonObj.put("score", this.credit);
			jsonObj.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));	
			jsonObj.put("lastLogin", DateUtility.castToAPIFormat(this.lastLogin));
			jsonObj.put("couponList",JSONFactory.toJSON(this.couponList));
			jsonObj.put("creditList",JSONFactory.toJSON(this.creditList));
			jsonObj.put("transactionList",JSONFactory.toJSON(this.transactionList));
		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonObj;

	}

	public boolean equals(User another){	
		if (another == null){
			return false;
		}
		return this.name.equals(another.getName()) &&
				this.phone.equals(another.getPhone()) && 
				this.status.code == another.getStatus().code &&
				this.userId == another.getUserId() && 
				this.balance == another.getBalance() && 
				this.credit == another.getCredit() &&
				this.coupon == another.getCoupon() && 
				this.email.equals(another.getEmail()) &&
				this.invitationalCode.equals(another.getInvitationalCode()) &&
				this.appliedInvitationalCode.equals(another.getInvitationalCode());
	}
}
