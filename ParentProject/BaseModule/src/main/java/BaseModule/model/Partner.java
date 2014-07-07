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
import BaseModule.configurations.EnumConfig.PartnerQualification;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;

public class Partner implements PseudoModel, Serializable{
	
	private static final long serialVersionUID = 2L;
	
	private int partnerId;
	private String wholeName;
	private String instName;
	private String licence;
	private String organizationNum;	
	private String logoUrl;
	private ArrayList<ClassPhoto> classPhotoList;
	private ArrayList<Teacher> teacherList;	
	private String reference;
	private String password;
	private AccountStatus status;
	private Calendar creationTime;
	private Calendar lastLogin;
	private String partnerIntro;	
	private String partnerDistinction;	
	private String licenceImgUrl;	//营业执照副本扫描件（对内）	
	private String taxRegistrationImgUrl;	//税务登记证副本扫描件（对内）	
	private String eduQualificationImgUrl;	//教育资质扫描件或复印件（对内）		
	private String hqLocation;	//公司总部地址	
	private ArrayList<String> subLocations;	//校区地址
	private boolean uniformRegistraLocation;	//不同课程报名地点是否一致
	private String hqContact;	//总部合作关系对接人
	private String hqContactPhone;	//总部合作关系对接人联系电话
	private String hqContactSecOpt;	//总部合作关系其他联系方式
	private String courseContact;	//课程详情联络人
	private String courseContactPhone;	//课程详情联络人联系电话
	private String studentInqueryPhone;	//学员咨询联系电话
	private String registraContact;	//报名学员入学状态对接人
	private String registraContactPhone;	//报名学员入学状态联系电话
	private String registraContactFax;	//报名学员入学状态传真号码
	private int defaultCutoffDay;	//默认课程开始前多少天前截止
	private int defaultCutoffTime;	//默认截止日几点截止
	private PartnerQualification partnerQualification;	
	
	private Partner(){}
	public static Partner getInstance(){
		Partner partner = new Partner();
		try {
			ModelReflectiveService.initialize(partner);
		} catch (Exception e) {
			return null;
		}
		return partner;
	}

	public Partner(int partnerId, String wholeName, String instName,
			String licence, String organizationNum, String logoUrl,
			ArrayList<ClassPhoto> classPhotoList,
			ArrayList<Teacher> teacherList, String reference, String password,
			AccountStatus status, Calendar creationTime, Calendar lastLogin,
			String partnerIntro, String partnerDistinction,
			String liscenceImgUrl, String taxRegistrationImgUrl,
			String eduQualificationImgUrl, String hqLocation,
			ArrayList<String> subLocations, boolean uniformRegistraLocation,
			String hqContact, String hqContactPhone, String hqContactSecOpt,
			String courseContact, String courseContactPhone,
			String studentInqueryPhone, String registraContact,
			String registraContactPhone, String registraContactFax,
			int defaultCutoffDay, int defaultCutoffTime,
			PartnerQualification partnerQualification) {
		super();
		this.partnerId = partnerId;
		this.wholeName = wholeName;
		this.instName = instName;
		this.licence = licence;
		this.organizationNum = organizationNum;
		this.logoUrl = logoUrl;
		this.classPhotoList = classPhotoList;
		this.teacherList = teacherList;
		this.reference = reference;
		this.password = password;
		this.status = status;
		this.creationTime = creationTime;
		this.lastLogin = lastLogin;
		this.partnerIntro = partnerIntro;
		this.partnerDistinction = partnerDistinction;
		this.licenceImgUrl = liscenceImgUrl;
		this.taxRegistrationImgUrl = taxRegistrationImgUrl;
		this.eduQualificationImgUrl = eduQualificationImgUrl;
		this.hqLocation = hqLocation;
		this.subLocations = subLocations;
		this.uniformRegistraLocation = uniformRegistraLocation;
		this.hqContact = hqContact;
		this.hqContactPhone = hqContactPhone;
		this.hqContactSecOpt = hqContactSecOpt;
		this.courseContact = courseContact;
		this.courseContactPhone = courseContactPhone;
		this.studentInqueryPhone = studentInqueryPhone;
		this.registraContact = registraContact;
		this.registraContactPhone = registraContactPhone;
		this.registraContactFax = registraContactFax;
		this.defaultCutoffDay = defaultCutoffDay;
		this.defaultCutoffTime = defaultCutoffTime;
		this.partnerQualification = partnerQualification;
	}

	//Normal Construction
	public Partner(String wholeName, String instName,String licence, String organizationNum,
			String reference, String password,AccountStatus status) {
		super();
		this.wholeName = wholeName;
		this.instName = instName;
		this.licence = licence;
		this.organizationNum = organizationNum;
		this.reference = reference;
		this.password = password;		
		this.status = status;
		this.creationTime = DateUtility.getCurTimeInstance();
		this.logoUrl = "";
		this.subLocations = new ArrayList<String>();
		this.classPhotoList = new ArrayList<ClassPhoto>();
		this.teacherList = new ArrayList<Teacher>();
		this.partnerQualification = PartnerQualification.unverified;
		if(this.lastLogin==null){
			this.lastLogin = (Calendar) this.creationTime.clone();
		}
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

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
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

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public ArrayList<ClassPhoto> getClassPhotoList() {
		return classPhotoList;
	}

	public void setClassPhotoList(ArrayList<ClassPhoto> classPhotoList) {
		this.classPhotoList = classPhotoList;
	}

	public ArrayList<Teacher> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(ArrayList<Teacher> teacherList) {
		this.teacherList = teacherList;
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

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public Calendar getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Calendar lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getPartnerIntro() {
		return partnerIntro;
	}

	public void setPartnerIntro(String partnerIntro) {
		this.partnerIntro = partnerIntro;
	}

	public String getPartnerDistinction() {
		return partnerDistinction;
	}

	public void setPartnerDistinction(String partnerDistinction) {
		this.partnerDistinction = partnerDistinction;
	}

	public String getLiscenceImgUrl() {
		return licenceImgUrl;
	}

	public void setLiscenceImgUrl(String liscenceImgUrl) {
		this.licenceImgUrl = liscenceImgUrl;
	}

	public String getTaxRegistrationImgUrl() {
		return taxRegistrationImgUrl;
	}

	public void setTaxRegistrationImgUrl(String taxRegistrationImgUrl) {
		this.taxRegistrationImgUrl = taxRegistrationImgUrl;
	}

	public String getEduQualificationImgUrl() {
		return eduQualificationImgUrl;
	}

	public void setEduQualificationImgUrl(String eduQualificationImgUrl) {
		this.eduQualificationImgUrl = eduQualificationImgUrl;
	}

	public String getHqLocation() {
		return hqLocation;
	}

	public void setHqLocation(String hqLocation) {
		this.hqLocation = hqLocation;
	}

	public ArrayList<String> getSubLocations() {
		return subLocations;
	}

	public void setSubLocations(ArrayList<String> subLocations) {
		this.subLocations = subLocations;
	}

	public boolean isUniformRegistraLocation() {
		return uniformRegistraLocation;
	}

	public void setUniformRegistraLocation(boolean uniformRegistraLocation) {
		this.uniformRegistraLocation = uniformRegistraLocation;
	}

	public String getHqContact() {
		return hqContact;
	}

	public void setHqContact(String hqContact) {
		this.hqContact = hqContact;
	}

	public String getHqContactPhone() {
		return hqContactPhone;
	}

	public void setHqContactPhone(String hqContactPhone) {
		this.hqContactPhone = hqContactPhone;
	}

	public String getHqContactSecOpt() {
		return hqContactSecOpt;
	}

	public void setHqContactSecOpt(String hqContactSecOpt) {
		this.hqContactSecOpt = hqContactSecOpt;
	}

	public String getCourseContact() {
		return courseContact;
	}

	public void setCourseContact(String courseContact) {
		this.courseContact = courseContact;
	}

	public String getCourseContactPhone() {
		return courseContactPhone;
	}

	public void setCourseContactPhone(String courseContactPhone) {
		this.courseContactPhone = courseContactPhone;
	}

	public String getStudentInqueryPhone() {
		return studentInqueryPhone;
	}

	public void setStudentInqueryPhone(String studentInqueryPhone) {
		this.studentInqueryPhone = studentInqueryPhone;
	}

	public String getRegistraContact() {
		return registraContact;
	}

	public void setRegistraContact(String registraContact) {
		this.registraContact = registraContact;
	}

	public String getRegistraContactPhone() {
		return registraContactPhone;
	}

	public void setRegistraContactPhone(String registraContactPhone) {
		this.registraContactPhone = registraContactPhone;
	}

	public String getRegistraContactFax() {
		return registraContactFax;
	}

	public void setRegistraContactFax(String registraContactFax) {
		this.registraContactFax = registraContactFax;
	}

	public int getDefaultCutoffDay() {
		return defaultCutoffDay;
	}

	public void setDefaultCutoffDay(int defaultCutoffDay) {
		this.defaultCutoffDay = defaultCutoffDay;
	}

	public int getDefaultCutoffTime() {
		return defaultCutoffTime;
	}

	public void setDefaultCutoffTime(int defaultCutoffTime) {
		this.defaultCutoffTime = defaultCutoffTime;
	}

	public PartnerQualification getPartnerQualification() {
		return partnerQualification;
	}

	public void setPartnerQualification(PartnerQualification partnerQualification) {
		this.partnerQualification = partnerQualification;
	}

	public Calendar getCreationTime() {
		return creationTime;
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
		if (classPhotoList == null) {
			if (other.classPhotoList != null)
				return false;
		} else if (!classPhotoList.equals(other.classPhotoList))
			return false;
		if (courseContact == null) {
			if (other.courseContact != null)
				return false;
		} else if (!courseContact.equals(other.courseContact))
			return false;
		if (courseContactPhone == null) {
			if (other.courseContactPhone != null)
				return false;
		} else if (!courseContactPhone.equals(other.courseContactPhone))
			return false;		
		if (defaultCutoffDay != other.defaultCutoffDay)
			return false;
		if (defaultCutoffTime != other.defaultCutoffTime)
			return false;
		if (eduQualificationImgUrl == null) {
			if (other.eduQualificationImgUrl != null)
				return false;
		} else if (!eduQualificationImgUrl.equals(other.eduQualificationImgUrl))
			return false;
		if (hqContact == null) {
			if (other.hqContact != null)
				return false;
		} else if (!hqContact.equals(other.hqContact))
			return false;
		if (hqContactPhone == null) {
			if (other.hqContactPhone != null)
				return false;
		} else if (!hqContactPhone.equals(other.hqContactPhone))
			return false;
		if (hqContactSecOpt == null) {
			if (other.hqContactSecOpt != null)
				return false;
		} else if (!hqContactSecOpt.equals(other.hqContactSecOpt))
			return false;
		if (hqLocation == null) {
			if (other.hqLocation != null)
				return false;
		} else if (!hqLocation.equals(other.hqLocation))
			return false;
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
		if (licenceImgUrl == null) {
			if (other.licenceImgUrl != null)
				return false;
		} else if (!licenceImgUrl.equals(other.licenceImgUrl))
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
		if (partnerDistinction == null) {
			if (other.partnerDistinction != null)
				return false;
		} else if (!partnerDistinction.equals(other.partnerDistinction))
			return false;
		if (partnerId != other.partnerId)
			return false;
		if (partnerIntro == null) {
			if (other.partnerIntro != null)
				return false;
		} else if (!partnerIntro.equals(other.partnerIntro))
			return false;
		if (partnerQualification != other.partnerQualification)
			return false;			
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (registraContact == null) {
			if (other.registraContact != null)
				return false;
		} else if (!registraContact.equals(other.registraContact))
			return false;
		if (registraContactFax == null) {
			if (other.registraContactFax != null)
				return false;
		} else if (!registraContactFax.equals(other.registraContactFax))
			return false;
		if (registraContactPhone == null) {
			if (other.registraContactPhone != null)
				return false;
		} else if (!registraContactPhone.equals(other.registraContactPhone))
			return false;
		if (status != other.status)
			return false;
		if (studentInqueryPhone == null) {
			if (other.studentInqueryPhone != null)
				return false;
		} else if (!studentInqueryPhone.equals(other.studentInqueryPhone))
			return false;
		if (subLocations == null) {
			if (other.subLocations != null)
				return false;
		} else if (!subLocations.equals(other.subLocations))
			return false;
		if (taxRegistrationImgUrl == null) {
			if (other.taxRegistrationImgUrl != null)
				return false;
		} else if (!taxRegistrationImgUrl.equals(other.taxRegistrationImgUrl))
			return false;
		if (teacherList == null) {
			if (other.teacherList != null)
				return false;
		} else if (!teacherList.equals(other.teacherList))
			return false;
		if (uniformRegistraLocation != other.uniformRegistraLocation)
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
