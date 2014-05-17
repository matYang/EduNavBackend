package BaseModule.model;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Status;
import BaseModule.interfaces.PseudoModel;

public class Course implements PseudoModel{

	private int courseId;
	
	private Calendar creationTime;
	private Calendar startTime;
	private Calendar finishTime;
	private int price;
	private int seatsTotal;
	private int seatsLeft;
	private Status status;
	private String category;
	private String subCategory;
	private String title;
	private Partner partner;
	
	//TODO added
	private String location;
	private String city;
	private String district;
	private String reference;
	//TODO modified
	private int partnerId;
	private String teacherInfo;
	private String teacherImgURL;
	private String teachingMaterial;
	private String backgroundUrl;
	//removed instName
	
	

	//SQL Retrieving
	public Course(int courseId, int p_Id, Calendar creationTime,
			Calendar startTime, Calendar finishTime, String t_Info,
			String t_ImgURL, String t_Material, String backgroundURL,
			int price, int seatsTotal, int seatsLeft,Status status, 
			String category, String subCategory, Partner partner,String title) {
		super();
		this.courseId = courseId;
		this.partnerId = p_Id;
		this.creationTime = creationTime;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.teacherInfo = t_Info;
		this.teacherImgURL = t_ImgURL;
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
	}

	//Normal Construction
	public Course(int p_Id, Calendar startTime, Calendar finishTime,
			int seatsTotal, int seatsLeft, String category,
			String subCategory,Status status) {
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

	public int getP_Id() {
		return partnerId;
	}

	public void setP_Id(int p_Id) {
		this.partnerId = p_Id;
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

	public String getT_Info() {
		return teacherInfo;
	}

	public void setT_Info(String t_Info) {
		this.teacherInfo = t_Info;
	}

	public String getT_ImgURL() {
		return teacherImgURL;
	}

	public void setT_ImgURL(String t_ImgURL) {
		this.teacherImgURL = t_ImgURL;
	}

	public String getT_Material() {
		return teachingMaterial;
	}

	public void setT_Material(String t_Material) {
		this.teachingMaterial = t_Material;
	}

	public String getBackgroundURL() {
		return backgroundUrl;
	}

	public void setBackgroundURL(String backgroundURL) {
		this.backgroundUrl = backgroundURL;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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
			jsonSearchRepresentation.put("partner_Id", this.partnerId);
			jsonSearchRepresentation.put("partner", this.partner.toJSON());
			jsonSearchRepresentation.put("title", this.title);
			jsonSearchRepresentation.put("teaching_Info", this.teacherInfo);
			jsonSearchRepresentation.put("teaching_ImgURL", this.teacherImgURL);
			jsonSearchRepresentation.put("teaching_Material", this.teachingMaterial);
			jsonSearchRepresentation.put("backgroundURL", this.backgroundUrl);
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
			return this.backgroundUrl.equals(c.getBackgroundURL()) && this.category.equals(c.getCategory()) &&
					this.subCategory.equals(c.getSubCategory()) && this.courseId == c.getCourseId() && this.title.equals(c.getTitle()) &&
					this.teacherInfo.equals(c.getT_Info()) && this.teacherImgURL.equals(c.getT_ImgURL()) &&
					this.teachingMaterial.equals(c.getT_Material()) && this.price == c.getPrice() && this.seatsTotal == c.getSeatsTotal() &&
					this.seatsLeft == c.getSeatsLeft() && this.status.code == c.getStatus().code  &&
					this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) && this.startTime.getTime().toString().equals(c.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(c.getFinishTime().getTime().toString());
		}else{
			return this.backgroundUrl.equals(c.getBackgroundURL()) && this.category.equals(c.getCategory()) &&
					this.subCategory.equals(c.getSubCategory()) && this.courseId == c.getCourseId() && this.partner.equals(c.getPartner()) &&
					this.title.equals(c.getTitle()) && this.teacherInfo.equals(c.getT_Info()) && this.teacherImgURL.equals(c.getT_ImgURL()) &&
					this.teachingMaterial.equals(c.getT_Material()) && this.price == c.getPrice() && this.seatsTotal == c.getSeatsTotal() &&
					this.seatsLeft == c.getSeatsLeft() && this.status.code == c.getStatus().code  &&
					this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) && this.startTime.getTime().toString().equals(c.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(c.getFinishTime().getTime().toString());
		}			
	}
}
