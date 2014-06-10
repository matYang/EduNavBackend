package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.ClassModel;
import BaseModule.exception.PseudoException;
import BaseModule.interfaces.PseudoMemCacheKey;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

public class CourseSearchRepresentation implements PseudoModel, PseudoRepresentation, PseudoMemCacheKey{

	//used for broad search
	private ClassModel classModel;

	private String category;
	private String subCategory;	
	private String city;
	private String district;
	private String institutionName;
	private String courseReference;
	private String partnerReference;

	private Calendar startTime;
	private Calendar finishTime;
	private Calendar creationTime;


	private AccountStatus status;

	private int startPrice;
	private int finishPrice;	
	private int courseId;
	private int partnerId;
	private int userId;

	private int useCache;


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
		this.useCache = -1;
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
	public String serialize() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
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


	@Override
	public String toCacheKey() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		return this.serialize();
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

	public void setUseCache(int useCache){
		this.useCache = useCache;
	}

	public int getUseCache(){
		return this.useCache;
	}

	@Override
	public String toString() {
		return "CourseSearchRepresentation [classModel=" + classModel
				+ ", category=" + category + ", subCategory=" + subCategory
				+ ", city=" + city + ", district=" + district
				+ ", institutionName=" + institutionName + ", courseReference="
				+ courseReference + ", partnerReference=" + partnerReference
				+ ", startTime=" + startTime + ", finishTime=" + finishTime
				+ ", creationTime=" + creationTime + ", status=" + status
				+ ", startPrice=" + startPrice + ", finishPrice=" + finishPrice
				+ ", courseId=" + courseId + ", partnerId=" + partnerId
				+ ", userId=" + userId + ", useCache=" + useCache + "]";
	}

	public String getSearchQuery() {

		String query = "SELECT * from CourseDao ";		
		String joinQuery = "JOIN PartnerDao On " +
				"CourseDao.p_Id = PartnerDao.id ";

		boolean joinQ = false;		
		boolean start = false;	

		/* Note:Make sure the order following is the same as that in Dao */

		if(this.getPartnerId() > 0){				
			query += joinQuery;
			joinQ = true;				
		}
		if(this.getInstitutionName() != null && this.getInstitutionName().length() > 0){
			if(!joinQ){										
				query += joinQuery;
				joinQ = true;
			}				
			query += "where ";
			start = true;

			query += "PartnerDao.instName = ? ";
		}
		if(this.getPartnerReference() != null && this.getPartnerReference().length() > 0){
			if(!joinQ){										
				query += joinQuery;
				joinQ = true;
			}
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "PartnerDao.reference = ? ";
		}

		if(this.getCreationTime() != null){			
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.creationTime = ? ";
		}
		if(this.getStartTime() != null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.startTime >= ? ";			
		}
		if(this.getFinishTime() != null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.finishTime <= ? ";
		}	

		if(this.getStartPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "CourseDao.price >= ? ";
		}
		if(this.getFinishPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "CourseDao.price <= ? ";
		}
		if(this.getStatus() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "CourseDao.status = ?  ";
		}
		if(this.getCategory()!=null&&this.getCategory().length()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.category = ? ";
		}
		if(this.getSubCategory()!=null&&this.getSubCategory().length()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.subcategory = ? ";
		}
		if(this.getCity()!=null&&this.getCity().length()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.city = ? ";
		}
		if(this.getDistrict()!=null&&this.getDistrict().length()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.district = ? ";
		}
		if(this.getCourseReference()!=null&&this.getCourseReference().length()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.reference = ? ";
		}
		if(this.getCourseId()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += " CourseDao.id = ? ";
		}
		if(this.getClassModel()!=null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.classModel = ? ";
		}

		return query;
	}


}
