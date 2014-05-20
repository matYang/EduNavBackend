package BaseModule.model;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.interfaces.PseudoModel;

public class Course implements PseudoModel{

	private int courseId;
	private int partnerId;
	private Calendar creationTime;
	private Calendar startTime;
	private Calendar finishTime;
	private int price;
	private int seatsTotal;
	private int seatsLeft;
	private AccountStatus status;
	private String category;
	private String subCategory;
	private String title;
	private Partner partner;
	private String location;
	private String city;
	private String district;
	private String reference;
	private String teacherInfo;
	private String teacherImgUrl;
	private String teachingMaterial;
	private String backgroundUrl;

	//SQL Retrieving
	public Course(int courseId, int p_Id, Calendar creationTime,
			Calendar startTime, Calendar finishTime, String t_Info,
			String t_ImgURL, String t_Material, String backgroundURL,
			int price, int seatsTotal, int seatsLeft,AccountStatus status, 
			String category, String subCategory, Partner partner,String title,
			String location, String city, String district, String reference) {
		super();
		this.courseId = courseId;
		this.partnerId = p_Id;
		this.creationTime = creationTime;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.teacherInfo = t_Info;
		this.teacherImgUrl = t_ImgURL;
		this.teachingMaterial = t_Material;
		this.backgroundUrl = backgroundURL;
		this.price = price;
		this.seatsTotal = seatsTotal;
		this.seatsLeft = seatsLeft;
		this.status = status;
		this.category = category;
		this.subCategory = subCategory;
		this.partner = partner;
		this.title = title;
		this.location = location;
		this.city = city;
		this.district = district;
		this.reference = reference;
	}

	//TODO add price, title
	//Normal Construction
	public Course(int p_Id, Calendar startTime, Calendar finishTime,
			int seatsTotal, int seatsLeft, String category,
			String subCategory,AccountStatus status) {
		super();
		this.partnerId = p_Id;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.seatsTotal = seatsTotal;
		this.seatsLeft = seatsLeft;
		this.category = category;
		this.subCategory = subCategory;
		this.status = status;
		this.creationTime = DateUtility.getCurTimeInstance();		
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getTeacherInfo() {
		return teacherInfo;
	}

	public void setTeacherInfo(String teacherInfo) {
		this.teacherInfo = teacherInfo;
	}

	public String getTeacherImgUrl() {
		return teacherImgUrl;
	}

	public void setTeacherImgUrl(String teacherImgURL) {
		this.teacherImgUrl = teacherImgURL;
	}

	public String getTeachingMaterial() {
		return teachingMaterial;
	}

	public void setTeachingMaterial(String teachingMaterial) {
		this.teachingMaterial = teachingMaterial;
	}

	public String getBackgroundUrl() {
		return backgroundUrl;
	}

	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSeatsTotal() {
		return seatsTotal;
	}

	public void setSeatsTotal(int seatsTotal) {
		this.seatsTotal = seatsTotal;
	}

	public int getSeatsLeft() {
		return seatsLeft;
	}

	public void setSeatsLeft(int seatsLeft) {
		this.seatsLeft = seatsLeft;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
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

	public Calendar getCreationTime() {
		return creationTime;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("id", this.courseId);
			jsonSearchRepresentation.put("partnerId", this.partnerId);
			jsonSearchRepresentation.put("partner", this.partner == null ? new JSONObject() : this.partner.toJSON());
			jsonSearchRepresentation.put("title", this.title);
			jsonSearchRepresentation.put("teacherInfo", this.teacherInfo);
			jsonSearchRepresentation.put("teacherImgUrl", this.teacherImgUrl);
			jsonSearchRepresentation.put("teachingMaterial", this.teachingMaterial);
			jsonSearchRepresentation.put("backgroundUrl", this.backgroundUrl);
			jsonSearchRepresentation.put("reference", this.reference);
			jsonSearchRepresentation.put("category", this.category);
			jsonSearchRepresentation.put("subcategory", this.subCategory);
			jsonSearchRepresentation.put("price", this.price);
			jsonSearchRepresentation.put("seatsTotal", this.seatsTotal);
			jsonSearchRepresentation.put("seatsLeft", this.seatsLeft);
			jsonSearchRepresentation.put("status", this.status.code);
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));		
			jsonSearchRepresentation.put("startTime", DateUtility.castToAPIFormat(this.startTime));
			jsonSearchRepresentation.put("finishTime", DateUtility.castToAPIFormat(this.finishTime));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;
	}

	public boolean equals(Course c){		
		if(this.partner==null){
			return this.backgroundUrl.equals(c.getBackgroundUrl()) && this.category.equals(c.getCategory()) &&
					this.subCategory.equals(c.getSubCategory()) && this.courseId == c.getCourseId() && this.title.equals(c.getTitle()) &&
					this.teacherInfo.equals(c.getTeacherInfo()) && this.teacherImgUrl.equals(c.getTeacherImgUrl()) &&
					this.teachingMaterial.equals(c.getTeachingMaterial()) && this.price == c.getPrice() && this.seatsTotal == c.getSeatsTotal() &&
					this.seatsLeft == c.getSeatsLeft() && this.status.code == c.getStatus().code  &&
					this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) && 
					this.startTime.getTime().toString().equals(c.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(c.getFinishTime().getTime().toString()) &&
					this.location.equals(c.getLocation()) && this.city.equals(c.getCity()) && this.district.equals(c.getDistrict()) &&
					this.reference.equals(c.getReference());
		}else{
			return this.backgroundUrl.equals(c.getBackgroundUrl()) && this.category.equals(c.getCategory()) &&
					this.subCategory.equals(c.getSubCategory()) && this.courseId == c.getCourseId() && this.title.equals(c.getTitle()) &&
					this.teacherInfo.equals(c.getTeacherInfo()) && this.teacherImgUrl.equals(c.getTeacherImgUrl()) &&
					this.teachingMaterial.equals(c.getTeachingMaterial()) && this.price == c.getPrice() && this.seatsTotal == c.getSeatsTotal() &&
					this.seatsLeft == c.getSeatsLeft() && this.status.code == c.getStatus().code  &&
					this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) && 
					this.startTime.getTime().toString().equals(c.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(c.getFinishTime().getTime().toString()) &&
					this.location.equals(c.getLocation()) && this.city.equals(c.getCity()) && this.district.equals(c.getDistrict()) &&
					this.reference.equals(c.getReference()) && this.partner.equals(c.getPartner());
		}			
	}
}
