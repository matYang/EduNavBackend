package BaseModule.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.interfaces.PseudoModel;
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
	
	private ArrayList<Integer> classPhotoIdList;
	private ArrayList<ClassPhoto> classPhotoList;
	private ArrayList<Integer> teacherIdList;
	private ArrayList<Teacher> teacherList;
	
	private Calendar creationTime;
	private Calendar lastLogin;


	public Partner(int partnerId, String wholeName, String licence,
			String organizationNum, String reference, String password,
			String phone, AccountStatus status, String instName,
			String logoUrl, ArrayList<Integer> classPhotoIdList,
			ArrayList<ClassPhoto> classPhotoList,
			ArrayList<Integer> teacherIdList, ArrayList<Teacher> teacherList,
			Calendar creationTime, Calendar lastLogin) {
		super();
		this.partnerId = partnerId;
		this.wholeName = wholeName;
		this.licence = licence;
		this.organizationNum = organizationNum;
		this.reference = reference;
		this.password = password;
		this.phone = phone;
		this.status = status;
		this.instName = instName;
		this.logoUrl = logoUrl;
		this.classPhotoIdList = classPhotoIdList;
		this.classPhotoList = classPhotoList;
		this.teacherIdList = teacherIdList;
		this.teacherList = teacherList;
		this.creationTime = creationTime;
		this.lastLogin = lastLogin;
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
		this.status = AccountStatus.deactivated;
		this.partnerId = -1;
		this.wholeName = "";
		this.licence = "";
		this.organizationNum = "";
		this.reference = "";
		this.password = "";
		this.phone = "";
		this.instName = "";
		this.logoUrl = "";
		
		this.classPhotoIdList = new ArrayList<Integer>();
		this.classPhotoList = new ArrayList<ClassPhoto>();
		this.teacherIdList = new ArrayList<Integer>();
		this.teacherList = new ArrayList<Teacher>();
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
	
	public ArrayList<Integer> getClassPhotoIdList() {
		return classPhotoIdList;
	}

	public void setClassPhotoIdList(ArrayList<Integer> classPhotoIdList) {
		this.classPhotoIdList = classPhotoIdList;
	}

	public ArrayList<ClassPhoto> getClassPhotoList() {
		return classPhotoList;
	}

	public void setClassPhotoList(ArrayList<ClassPhoto> classPhotoList) {
		this.classPhotoList = classPhotoList;
	}

	public ArrayList<Integer> getTeacherIdList() {
		return teacherIdList;
	}

	public void setTeacherIdList(ArrayList<Integer> teacherIdList) {
		this.teacherIdList = teacherIdList;
	}

	public ArrayList<Teacher> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(ArrayList<Teacher> teacherList) {
		this.teacherList = teacherList;
	}

	public Partner deepCopy() throws IOException, ClassNotFoundException{
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(this);
        oos.close();
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Partner clone = (Partner) ois.readObject();
        
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
		Partner other = (Partner) obj;
		if (instName == null) {
			if (other.instName != null)
				return false;
		} else if (!instName.equals(other.instName))
			return false;
		if (licence == null) {
			if (other.licence != null)
				return false;
		} else if (!licence.equals(other.licence))
			return false;
		if (logoUrl == null) {
			if (other.logoUrl != null)
				return false;
		} else if (!logoUrl.equals(other.logoUrl))
			return false;
		if (organizationNum == null) {
			if (other.organizationNum != null)
				return false;
		} else if (!organizationNum.equals(other.organizationNum))
			return false;
		if (partnerId != other.partnerId)
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (status != other.status)
			return false;
		if (wholeName == null) {
			if (other.wholeName != null)
				return false;
		} else if (!wholeName.equals(other.wholeName))
			return false;
		return true;
	}
	
	public void loadFromMap(Map<String, String> kvps) throws Exception{
		ModelReflectiveService.storeKvps(this, kvps);
	}

	
}
