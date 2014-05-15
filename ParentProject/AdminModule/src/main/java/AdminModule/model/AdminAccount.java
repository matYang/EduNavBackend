package AdminModule.model;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.Status;
public class AdminAccount {

	private int id;
	private Calendar creationTime;
	private Calendar lastLogin;
	private String reference;
	private Privilege privilege;
	private Status status;
	private String name;
	private String phone;

	//SQL Retrieving
	public AdminAccount(int id, Calendar creationTime, Calendar lastLogin,
			String reference, Privilege privilege, Status status, String name,
			String phone) {
		super();
		this.id = id;
		this.creationTime = creationTime;
		this.lastLogin = lastLogin;
		this.reference = reference;
		this.privilege = privilege;
		this.status = status;
		this.name = name;
		this.phone = phone;
	}

	//Normal Construction
	public AdminAccount(String name, String phone,String reference, Privilege privilege, Status status) {
		super();
		this.name = name;
		this.phone = phone;
		this.reference = reference;
		this.privilege = privilege;
		this.status = status;
		this.creationTime = DateUtility.getCurTimeInstance();
		if(this.lastLogin==null){
			this.lastLogin = (Calendar) this.creationTime.clone();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Calendar lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public Calendar getCreationTime() {
		return creationTime;
	}

	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("id", this.id);
			jsonSearchRepresentation.put("name", this.name);
			jsonSearchRepresentation.put("phone", this.phone);			
			jsonSearchRepresentation.put("reference", this.reference);
			jsonSearchRepresentation.put("status", this.status.code);
			jsonSearchRepresentation.put("privilege", this.privilege.code);
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));	
			jsonSearchRepresentation.put("lastLogin", DateUtility.castToAPIFormat(this.lastLogin));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;

	}

	public boolean equals(AdminAccount a){
		return this.id == a.getId() && this.name.equals(a.getName()) && this.phone.equals(a.getPhone()) &&
				this.privilege.code == a.getPrivilege().code && this.status.code == a.getStatus().code &&
				this.reference.equals(a.getReference()) && this.creationTime.getTime().toString().equals(a.getCreationTime().getTime().toString()) &&
				this.lastLogin.getTime().toString().equals(a.getLastLogin().getTime().toString());
	}



}
