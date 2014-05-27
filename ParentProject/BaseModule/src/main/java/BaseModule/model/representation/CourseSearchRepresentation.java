package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.ClassModel;
import BaseModule.exception.PseudoException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

public class CourseSearchRepresentation implements PseudoModel, PseudoRepresentation{
	
	//used for broad search
	private String category;
	private String subCategory;
	private ClassModel classModel;
	private String city;
	private String district;
	
	private Calendar startTime;
	private Calendar finishTime;
	
	private String institutionName;
	private AccountStatus status;
	
	private int startPrice;
	private int finishPrice;
	
	private String courseReference;
	private String partnerReference;
	
	private int courseId;
	private int partnerId;
	private int userId;
	private Calendar creationTime;
	
	
	public CourseSearchRepresentation(){
		super();
		this.category = null;
		this.subCategory = null;
		this.city = null;
		this.district = null;
		this.startTime = null;
		this.finishTime = null;
		this.institutionName = null;
		this.status = null;
		this.startPrice = -1;
		this.finishPrice = -1;
		this.courseId = -1;
		this.partnerId = -1;
		this.userId = -1;
		this.courseReference = null;
		this.partnerReference = null;
		this.creationTime = null;
		this.classModel = null;
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
	public String serialize() throws IllegalArgumentException, IllegalAccessException {
		return RepresentationReflectiveService.serialize(this);
	}
	
	@Override
	public boolean isEmpty() throws Exception {
		return RepresentationReflectiveService.isEmpty(this);
	}

	@Override
	public JSONObject toJSON() {
		return RepresentationReflectiveService.toJSON(this);
	}
	
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Calendar finishTime) {
		this.finishTime = finishTime;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public int getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(int startPrice) {
		this.startPrice = startPrice;
	}

	public int getFinishPrice() {
		return finishPrice;
	}

	public void setFinishPrice(int finishPrice) {
		this.finishPrice = finishPrice;
	}

	public String getCourseReference() {
		return courseReference;
	}

	public void setCourseReference(String courseReference) {
		this.courseReference = courseReference;
	}

	public String getPartnerReference() {
		return partnerReference;
	}

	public void setPartnerReference(String partnerReference) {
		this.partnerReference = partnerReference;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
	}

	public ClassModel getClassModel() {
		return classModel;
	}

	public void setClassModel(ClassModel classModel) {
		this.classModel = classModel;
	}

	@Override
	public String toString() {
		return "CourseSearchRepresentation [category=" + category
				+ ", subCategory=" + subCategory + ", city=" + city
				+ ", district=" + district + ", startTime=" + startTime
				+ ", finishTime=" + finishTime + ", institutionName="
				+ institutionName + ", status=" + status + ", startPrice="
				+ startPrice + ", finishPrice=" + finishPrice
				+ ", courseReference=" + courseReference
				+", classModel=" + classModel 
				+ ", partnerReference=" + partnerReference + ", courseId="
				+ courseId + ", partnerId=" + partnerId + ", userId=" + userId
				+ ", creationTime=" + creationTime + "]";
	}
	
	
}
