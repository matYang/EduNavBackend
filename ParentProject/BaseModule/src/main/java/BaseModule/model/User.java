package BaseModule.model;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.EncodingService;

public class User implements PseudoModel{

	private int userId;
	private int amount;
	private int coupon;
	private int score;
	private String name;
	private String phone;
	private String password;
	private AccountStatus status;
	
	private Calendar creationTime;
	private Calendar lastLogin;

	//SQL Retrieving
	public User(int userId, String name, String phone, Calendar creationTime,
			Calendar lastLogin, String password, AccountStatus status,int amount,int coupon,int score) {
		super();
		this.userId = userId;		
		this.name = name;
		this.phone = phone;
		this.creationTime = creationTime;
		this.lastLogin = lastLogin;
		this.password = password;
		this.status = status;	
		this.amount = amount;
		this.coupon = coupon;
		this.score = score;
	}
	
	//Normal Construction
	public User(String name, String phone, String password,AccountStatus status) {
		super();		
		this.amount = 0;
		this.coupon = 0;
		this.score = 0;
		this.name = name;
		this.phone = phone;
		this.password = password;
		this.status = status;
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
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getCoupon() {
		return coupon;
	}

	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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
			jsonSearchRepresentation.put("status", this.status.code);
			jsonSearchRepresentation.put("amount", this.amount);
			jsonSearchRepresentation.put("coupon", this.coupon);
			jsonSearchRepresentation.put("score", this.score);
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));	
			jsonSearchRepresentation.put("lastLogin", DateUtility.castToAPIFormat(this.lastLogin));
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
				this.amount == another.getAmount() && 
				this.score == another.getScore() &&
				this.coupon == another.getCoupon();
	}
}
