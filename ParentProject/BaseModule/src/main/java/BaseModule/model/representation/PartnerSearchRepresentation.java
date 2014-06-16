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

public class PartnerSearchRepresentation implements PseudoModel, PseudoRepresentation {
	
	private int partnerId;
	private String wholeName;
	private String licence;
	private String organizationNum;
	private String reference;
	private Calendar creationTime;
	private String phone;
	private AccountStatus status;	
	private String instName;
	
	public PartnerSearchRepresentation(){
		this.partnerId = -1;
		this.wholeName = null;
		this.licence = null;
		this.organizationNum = null;
		this.reference = null;
		this.creationTime = null;
		this.phone = null;
		this.status = null;
		this.instName = null;
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
	public boolean isEmpty() throws Exception {
		return RepresentationReflectiveService.isEmpty(this);
	}
	
	@Override
	public String serialize() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ValidationException {
		return RepresentationReflectiveService.serialize(this);
	}

	@Override
	public JSONObject toJSON() throws ValidationException {
		return RepresentationReflectiveService.toJSON(this);
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public String getWholeName() {
		return wholeName;
	}

	public void setWholeName(String wholeName) {
		this.wholeName = wholeName;
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

	public Calendar getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
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

	@Override
	public String toString() {
		return "PartnerSearchRepresentation [partnerId=" + partnerId
				+ ", wholeName=" + wholeName + ", licence=" + licence
				+ ", organizationNum=" + organizationNum + ", reference="
				+ reference + ", creationTime=" + creationTime + ", phone="
				+ phone + ", status=" + status + ", instName=" + instName + "]";
	}
	
	public String getSearchQuery() {
		String query = "SELECT * from PartnerDao ";		
		boolean start = false;		
		
		/* Note:Make sure the order following is the same as that in Dao */
		
		if(this.getPartnerId()>0){
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
		if(this.getWholeName() != null && this.getWholeName().length() > 0){
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
		if(this.getInstName() != null && this.getInstName().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "instName = ? ";
		}
		if(this.getLicence() != null && this.getLicence().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "licence = ? ";
		}
		if(this.getOrganizationNum() != null && this.getOrganizationNum().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "organizationNum = ? ";
		}
		if(this.getReference() != null && this.getReference().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "reference = ? ";
		}		
		return query;
	}
}
