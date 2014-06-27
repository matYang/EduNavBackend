package BaseModule.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		this.password = "";
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
	
	public AdminAccount deepCopy() throws IOException, ClassNotFoundException{
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(this);
        oos.close();
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final AdminAccount clone = (AdminAccount) ois.readObject();
        
        return clone;
	}

	public JSONObject toJSON() throws Exception{
		return ModelReflectiveService.toJSON(this);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdminAccount other = (AdminAccount) obj;
		if (adminId != other.adminId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (privilege != other.privilege)
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

}
