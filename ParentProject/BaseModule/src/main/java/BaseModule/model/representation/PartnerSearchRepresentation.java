package BaseModule.model.representation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.ModelReflectiveService;

public class PartnerSearchRepresentation implements PseudoModel, PseudoRepresentation {
	
	private int partnerId;
	private String wholeName;
	private String licence;
	private String organizationNum;
	private String reference;
	private String phone;
	private AccountStatus status;	
	private String instName;
	private Calendar startCreationTime;
	private Calendar finishCreationTime;
	
	public PartnerSearchRepresentation(){
		this.partnerId = -1;
		this.wholeName = null;
		this.licence = null;
		this.organizationNum = null;
		this.reference = null;
		this.startCreationTime = null;
		this.finishCreationTime = null;
		this.phone = null;
		this.status = null;
		this.instName = null;
	}

	@Override
	public ArrayList<String> getKeySet() {
		return ModelReflectiveService.getKeySet(this);
	}

	@Override
	public void storeKvps(Map<String, String> kvps)  throws Exception {
		ModelReflectiveService.storeKvps(this, kvps);
	}
	
	@Override
	public boolean isEmpty() throws Exception {
		return ModelReflectiveService.isEmpty(this);
	}

	@Override
	public JSONObject toJSON() throws Exception {
		return ModelReflectiveService.toJSON(this);
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

	public Calendar getStartCreationTime() {
		return startCreationTime;
	}

	public void setStartCreationTime(Calendar startCreationTime) {
		this.startCreationTime = startCreationTime;
	}

	public Calendar getFinishCreationTime() {
		return finishCreationTime;
	}

	public void setFinishCreationTime(Calendar finishCreationTime) {
		this.finishCreationTime = finishCreationTime;
	}

	@Override
	public String toString() {
		return "PartnerSearchRepresentation [partnerId=" + partnerId
				+ ", wholeName=" + wholeName + ", licence=" + licence
				+ ", organizationNum=" + organizationNum + ", reference="
				+ reference + ", startCreationTime" + startCreationTime + ", finishCreationTime=" 
				+ finishCreationTime + ", phone="
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
		if(this.getStartCreationTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime >= ? ";
		}
		if(this.getFinishCreationTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime <= ? ";
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
