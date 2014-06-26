package BaseModule.model.representation;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.ModelReflectiveService;

public class AdminSearchRepresentation implements PseudoModel, PseudoRepresentation {
	
	private int adminId;
	private String name;
	private String phone;
	private String reference;
	private Privilege privilege;
	private AccountStatus status;
	
	public AdminSearchRepresentation(){
		this.adminId = -1;
		this.name = null;
		this.phone = null;
		this.reference = null;
		this.privilege = null;
		this.status = null;
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

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
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

	@Override
	public String toString() {
		return "AdminSearchRepresentation [adminId=" + adminId + ", name="
				+ name + ", phone=" + phone + ", reference=" + reference
				+ ", privilege=" + privilege + ", status=" + status + "]";
	}
	
	
	public String getSearchQuery() {
		String query = "SELECT * from AdminAccountDao ";
		boolean start = false;
		
		/* Note:Make sure the order following is the same as that in Dao */
		
		if(this.getAdminId() > 0){
			query += "where ";
			start = true;
			
			query += "id = ? ";
		}
		if(this.getName() != null && this.getName().length() > 0){
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
		if(this.getReference() != null && this.getReference().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "reference = ? ";
		}
		if(this.getPrivilege() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "privilege = ? ";
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
		
		return query;
	}
	
}
