package BaseModule.model;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Status;
import BaseModule.interfaces.PseudoModel;

public class User implements PseudoModel{

	private int userId;
	private String name;
	private String phone;
	private String password;
	private Status status;
	
	private Calendar creationTime;
	private Calendar lastLogin;

	//SQL Retrieving
	public User(int userId, String name, String phone, Calendar creationTime,
			Calendar lastLogin, String password, Status status) {
		super();
		this.userId = userId;
		this.name = name;
		this.phone = phone;
		this.creationTime = creationTime;
		this.lastLogin = lastLogin;
		this.password = password;
		this.status = status;		
	}
	
	//Normal Construction
	public User(String name, String phone, String password,Status status) {
		super();		
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
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Calendar getCreationTime() {
		return creationTime;
	}

	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("id", this.userId);
			jsonSearchRepresentation.put("name", this.name);
			jsonSearchRepresentation.put("phone", this.phone);
			jsonSearchRepresentation.put("password", this.password);
			jsonSearchRepresentation.put("status", this.status.code);
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));	
			jsonSearchRepresentation.put("lastLogin", DateUtility.castToAPIFormat(this.lastLogin));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;

	}

	public boolean equals(User another){	

		return this.name.equals(another.getName()) && this.password.equals(another.getPassword()) &&
				this.phone.equals(another.getPhone()) && this.status.code == another.getStatus().code &&
				this.userId == another.getUserId();
	}
}
