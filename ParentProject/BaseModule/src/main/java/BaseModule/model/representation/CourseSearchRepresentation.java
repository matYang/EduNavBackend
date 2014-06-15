package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.CourseStatus;
import BaseModule.exception.PseudoException;
import BaseModule.interfaces.PseudoMemCacheKey;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

public class CourseSearchRepresentation implements PseudoModel, PseudoRepresentation, PseudoMemCacheKey{

	//used for broad search
	private String category;
	private String subCategory;	
	private String subSubCategory;
	private String province;
	private String city;
	private String district;
	private String institutionName;
	private String courseReference;
	private String partnerReference;

	private Calendar startDate;
	private Calendar finishDate;
	private Calendar creationTime;
	
	private int startPrice;
	private int finishPrice;
	private int startClassSize;
	private int finishClassSize;
	private int startCashback;
	private int finishCashback;

	private CourseStatus status;
		
	private int courseId;
	private int partnerId;
	private int userId;

	private int useCache;



	public CourseSearchRepresentation() {
		super();
		this.category = null;
		this.subCategory = null;
		this.subSubCategory = null;
		this.province = null;
		this.city = null;
		this.district = null;
		this.institutionName = null;
		this.courseReference = null;
		this.partnerReference = null;
		this.startDate = null;
		this.finishDate = null;
		this.creationTime = null;
		this.startPrice = -1;
		this.finishPrice = -1;
		this.startClassSize = -1;
		this.finishClassSize = -1;
		this.startCashback = -1;
		this.finishCashback = -1;
		this.status = null;
		this.courseId = -1;
		this.partnerId = -1;
		this.userId = -1;
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

	public String getSubSubCategory() {
		return subSubCategory;
	}

	public void setSubSubCategory(String subSubCategory) {
		this.subSubCategory = subSubCategory;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
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

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
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

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Calendar finishDate) {
		this.finishDate = finishDate;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
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

	public int getStartClassSize() {
		return startClassSize;
	}

	public void setStartClassSize(int startClassSize) {
		this.startClassSize = startClassSize;
	}

	public int getFinishClassSize() {
		return finishClassSize;
	}

	public void setFinishClassSize(int finishClassSize) {
		this.finishClassSize = finishClassSize;
	}

	public int getStartCashback() {
		return startCashback;
	}

	public void setStartCashback(int startCashback) {
		this.startCashback = startCashback;
	}

	public int getFinishCashback() {
		return finishCashback;
	}

	public void setFinishCashback(int finishCashback) {
		this.finishCashback = finishCashback;
	}

	public CourseStatus getStatus() {
		return status;
	}

	public void setStatus(CourseStatus status) {
		this.status = status;
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

	public int getUseCache() {
		return useCache;
	}

	public void setUseCache(int useCache) {
		this.useCache = useCache;
	}
	

	@Override
	public String toString() {
		return "CourseSearchRepresentation [category=" + category
				+ ", subCategory=" + subCategory + ", subSubCategory="
				+ subSubCategory + ", province=" + province + ", city=" + city
				+ ", district=" + district + ", institutionName="
				+ institutionName + ", courseReference=" + courseReference
				+ ", partnerReference=" + partnerReference + ", startDate="
				+ startDate + ", finishDate=" + finishDate + ", creationTime="
				+ creationTime + ", startPrice=" + startPrice
				+ ", finishPrice=" + finishPrice + ", startClassSize="
				+ startClassSize + ", finishClassSize=" + finishClassSize
				+ ", startCashback=" + startCashback + ", finishCashback="
				+ finishCashback + ", status=" + status + ", courseId="
				+ courseId + ", partnerId=" + partnerId + ", userId=" + userId
				+ ", useCache=" + useCache + "]";
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
		if(this.getStartDate() != null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.startDate >= ? ";
		}
		if(this.getFinishDate() != null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.finishDate <= ? ";
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
			query += "CourseDao.subCategory = ? ";
		}
		if(this.getSubSubCategory() != null && this.getSubSubCategory().length() > 0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.subSubCategory = ? ";
		}
		if(this.getProvince() != null && this.getProvince().length() > 0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.province = ? ";
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
		if(this.getStartClassSize() != -1){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.classSize >= ? ";
		}
		if(this.getFinishClassSize() != -1){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.classSize <= ? ";
		}
		if(this.getStartCashback() != -1){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.cashback >= ? ";
		}
		if(this.getFinishCashback() != -1){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.cashback <= ? ";
		}


		return query;
	}


}
