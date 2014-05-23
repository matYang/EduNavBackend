package BaseModule.model;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.EncodingService;

public class Partner implements PseudoModel{
	
	private int partnerId;
	private String name;
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
	public Partner(int id, String name, String licence, String organizationNum,
			String reference, String password, Calendar creationTime,
			Calendar lastLogin, String phone, AccountStatus status, String instName,String logoUrl) {
		super();
		this.partnerId = id;
		this.name = name;
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
	public Partner(String name, String instName,String licence, String organizationNum,
			String reference, String password, String phone,AccountStatus status) {
		super();
		this.name = name;
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
	
	//default constructor
	public Partner(){
		super();
		this.creationTime = DateUtility.getCurTimeInstance();
	}

	public int getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(int id) {
		this.partnerId = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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

	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("partnerId", this.partnerId);
			jsonSearchRepresentation.put("name", EncodingService.encodeURI(this.name));
			jsonSearchRepresentation.put("phone", EncodingService.encodeURI(this.phone));			
			jsonSearchRepresentation.put("licence", EncodingService.encodeURI(this.licence));
			jsonSearchRepresentation.put("organizationNum", EncodingService.encodeURI(this.organizationNum));
			jsonSearchRepresentation.put("reference", EncodingService.encodeURI(this.reference));
			jsonSearchRepresentation.put("status", this.status.code);
			jsonSearchRepresentation.put("instName", EncodingService.encodeURI(this.instName));
			jsonSearchRepresentation.put("logoUrl", EncodingService.encodeURI(this.logoUrl));
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));		
			jsonSearchRepresentation.put("lastLogin", DateUtility.castToAPIFormat(this.lastLogin));

		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;
	}

	public boolean equals(Partner p){
		return this.partnerId==p.getPartnerId() && this.name.equals(p.getName()) &&
				this.organizationNum.equals(p.getOrganizationNum()) && this.reference.equals(p.getReference()) &&
				this.phone.equals(p.getPhone()) && this.licence.equals(p.getLicence()) && this.status.code == p.getStatus().code &&
				this.instName.equals(p.getInstName()) && this.logoUrl.equals(p.getLogoUrl());				
	}
}
