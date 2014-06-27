package BaseModule.model;

import java.io.Serializable;
import java.util.Calendar;

import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;

public class AdminAccount implements PseudoModel, Serializable{
	
	private static final long serialVersionUID = 4L;
	
	private int adminId;
	private String password;
	private String name;
	private String phone;
	
	private String reference;
	private Privilege privilege;
	private AccountStatus status;
	
	private Calendar creationTime;
	private Calendar lastLogin;

	//SQL Retrieving
	public AdminAccount(int adminId, Calendar creationTime, Calendar lastLogin,
			String reference, Privilege privilege, AccountStatus status, String name,
			String phone) {
		super();
		this.adminId = adminId;
		this.creationTime = creationTime;
		this.lastLogin = lastLogin;
		this.reference = reference;
		this.privilege = privilege;
		this.status = status;
		this.name = name;
		this.phone = phone;
	}

	//Normal Construction
	public AdminAccount(String name, String phone,String reference, Privilege privilege, AccountStatus status,String password) {
		super();
		this.name = name;
		this.phone = phone;
		this.reference = reference;
		this.privilege = privilege;
		this.status = status;
		this.password = password;
		this.creationTime = DateUtility.getCurTimeInstance();
		if(this.lastLogin==null){
			this.lastLogin = (Calendar) this.creationTime.clone();
		}
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int id) {
		this.adminId = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
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

	public JSONObject toJSON() throws Exception{
		return ModelReflectiveService.toJSON(this);
	}

	public boolean equals(AdminAccount a){
		if (a == null){
			return false;
		}
		return this.adminId == a.getAdminId() && this.name.equals(a.getName()) && this.phone.equals(a.getPhone()) &&
				this.privilege.code == a.getPrivilege().code && this.status.code == a.getStatus().code &&
				this.reference.equals(a.getReference()) && this.creationTime.getTime().toString().equals(a.getCreationTime().getTime().toString()) &&
				this.lastLogin.getTime().toString().equals(a.getLastLogin().getTime().toString());
	}



}
