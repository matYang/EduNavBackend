package BaseModule.model;

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

public class User implements PseudoModel{

	private int userId;
	private int balance;
	private int coupon;
	private int credit;
	
	private String name;
	private String phone;
	private String password;
	private String email;
	
	private AccountStatus status;
	
	private Calendar creationTime;
	private Calendar lastLogin;
	
	private ArrayList<Coupon> couponList = new ArrayList<Coupon>();
	private ArrayList<Credit> creditList = new ArrayList<Credit>();
	private ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

	//SQL Retrieving
	public User(int userId, String name, String phone, Calendar creationTime,
			Calendar lastLogin, String password, AccountStatus status,int balance,int coupon,int credit,String email) {
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
	}
	
	//Normal Construction
	public User(String name, String phone, String password,AccountStatus status,String email) {
		super();		
		this.balance = 0;
		this.coupon = 0;
		this.credit = 0;
		this.name = name;
		this.phone = phone;
		this.password = password;
		this.status = status;
		this.email = email;
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

	public void setBalance(int amount) {
		this.balance = amount;
	}

	public int getCoupon() {
		return coupon;
	}

	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int score) {
		this.credit = score;
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

	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("id", this.userId);
			jsonSearchRepresentation.put("name", EncodingService.encodeURI(this.name));
			jsonSearchRepresentation.put("phone", EncodingService.encodeURI(this.phone));	
			jsonSearchRepresentation.put("email", EncodingService.encodeURI(this.email));
			jsonSearchRepresentation.put("status", this.status.code);
			jsonSearchRepresentation.put("amount", this.balance);
			jsonSearchRepresentation.put("coupon", this.coupon);
			jsonSearchRepresentation.put("score", this.credit);
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));	
			jsonSearchRepresentation.put("lastLogin", DateUtility.castToAPIFormat(this.lastLogin));
			jsonSearchRepresentation.put("couponList",JSONFactory.toJSON(this.couponList));
			jsonSearchRepresentation.put("creditList",JSONFactory.toJSON(this.creditList));
			jsonSearchRepresentation.put("transactionList",JSONFactory.toJSON(this.transactionList));
		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;

	}

	public boolean equals(User another){	

		return this.name.equals(another.getName()) &&
				this.phone.equals(another.getPhone()) && 
				this.status.code == another.getStatus().code &&
				this.userId == another.getUserId() && 
				this.balance == another.getBalance() && 
				this.credit == another.getCredit() &&
				this.coupon == another.getCoupon() && 
				this.email.equals(another.getEmail());
	}
}
