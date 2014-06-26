package BaseModule.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.EncodingService;
import BaseModule.service.ModelReflectiveService;

public class Partner implements PseudoModel, Serializable{
	
	private static final long serialVersionUID = 2L;
	
	private int partnerId;
	private String wholeName;
	private String licence;
	private String organizationNum;
	private String reference;
	private String password;
	
	private String phone;
	private AccountStatus status;	
	private String instName;
	private String logoUrl;
	
	private Calendar creationTime;
	private Calendar lastLogin;

	//SQL Retrieving	
	public Partner(int id, String wholeName, String licence, String organizationNum,
			String reference, String password, Calendar creationTime,
			Calendar lastLogin, String phone, AccountStatus status, String instName,String logoUrl) {
		super();
		this.partnerId = id;
		this.wholeName = wholeName;
		this.licence = licence;
		this.organizationNum = organizationNum;
		this.reference = reference;
		this.password = password;
		this.creationTime = creationTime;
		this.lastLogin = lastLogin;
		this.phone = phone;
		this.status = status;
		this.instName = instName;
		this.logoUrl = logoUrl;
	}

	//Normal Construction
	public Partner(String wholeName, String instName,String licence, String organizationNum,
			String reference, String password, String phone,AccountStatus status) {
		super();
		this.wholeName = wholeName;
		this.instName = instName;
		this.licence = licence;
		this.organizationNum = organizationNum;
		this.reference = reference;
		this.password = password;
		this.phone = phone;
		this.status = status;
		this.creationTime = DateUtility.getCurTimeInstance();
		this.logoUrl = "";
		if(this.lastLogin==null){
			this.lastLogin = (Calendar) this.creationTime.clone();
		}
	}
	
	//default constructor, set minimum default behavior and hand over to reflection for filling in content
	public Partner(){
		super();
		this.creationTime = DateUtility.getCurTimeInstance();
		this.lastLogin = DateUtility.getCurTimeInstance();
		this.status = AccountStatus.fromInt(0);
	}

	public int getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(int id) {
		this.partnerId = id;
	}
	public String getWholeName() {
		return wholeName;
	}
	public void setWholeName(String name) {
		this.wholeName = name;
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	public String getOrganizationNum() {
		return organizationNum;
	}
	public void setOrganizationNum(String organizationNum) {
		this.organizationNum = organizationNum;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	public JSONObject toJSON() throws Exception{
		return ModelReflectiveService.toJSON(this);
	}

	public boolean equals(Partner p){
		if (p == null){
			return false;
		}
		return this.partnerId==p.getPartnerId() && this.wholeName.equals(p.getWholeName()) &&
				this.organizationNum.equals(p.getOrganizationNum()) && this.reference.equals(p.getReference()) &&
				this.phone.equals(p.getPhone()) && this.licence.equals(p.getLicence()) && this.status.code == p.getStatus().code &&
				this.instName.equals(p.getInstName()) && this.logoUrl.equals(p.getLogoUrl());				
	}
	
}
