package BaseModule.model;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Status;
import BaseModule.interfaces.PseudoModel;

public class Course implements PseudoModel{

	private int courseId;
	private int p_Id;
	private Calendar creationTime;
	private Calendar startTime;
	private Calendar finishTime;
	private String t_Info;
	private String t_ImgURL;
	private String t_Material;
	private String backgroundURL;
	private String instName;
	private int price;
	private int seatsTotal;
	private int seatsLeft;
	private Status status;
	private String category;
	private String subCategory;
	private String title;
	private Partner partner;

	//SQL Retrieving
	public Course(int courseId, int p_Id, Calendar creationTime,
			Calendar startTime, Calendar finishTime, String t_Info,
			String t_ImgURL, String t_Material, String backgroundURL,
			String instName, int price, int seatsTotal, int seatsLeft,
			Status status, String category, String subCategory, Partner partner,String title) {
		super();
		this.courseId = courseId;
		this.p_Id = p_Id;
		this.creationTime = creationTime;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.t_Info = t_Info;
		this.t_ImgURL = t_ImgURL;
		this.t_Material = t_Material;
		this.backgroundURL = backgroundURL;
		this.instName = instName;
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
			String instName, int seatsTotal, int seatsLeft, String category,
			String subCategory,Status status) {
		super();
		this.p_Id = p_Id;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.instName = instName;
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
		return p_Id;
	}

	public void setP_Id(int p_Id) {
		this.p_Id = p_Id;
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
		return t_Info;
	}

	public void setT_Info(String t_Info) {
		this.t_Info = t_Info;
	}

	public String getT_ImgURL() {
		return t_ImgURL;
	}

	public void setT_ImgURL(String t_ImgURL) {
		this.t_ImgURL = t_ImgURL;
	}

	public String getT_Material() {
		return t_Material;
	}

	public void setT_Material(String t_Material) {
		this.t_Material = t_Material;
	}

	public String getBackgroundURL() {
		return backgroundURL;
	}

	public void setBackgroundURL(String backgroundURL) {
		this.backgroundURL = backgroundURL;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
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
			jsonSearchRepresentation.put("partner_Id", this.p_Id);
			jsonSearchRepresentation.put("partner", this.partner.toJSON());
			jsonSearchRepresentation.put("title", this.title);
			jsonSearchRepresentation.put("teaching_Info", this.t_Info);
			jsonSearchRepresentation.put("teaching_ImgURL", this.t_ImgURL);
			jsonSearchRepresentation.put("teaching_Material", this.t_Material);
			jsonSearchRepresentation.put("backgroundURL", this.backgroundURL);
			jsonSearchRepresentation.put("category", this.category);
			jsonSearchRepresentation.put("subcategory", this.subCategory);
			jsonSearchRepresentation.put("price", this.price);
			jsonSearchRepresentation.put("seatsTotal", this.seatsTotal);
			jsonSearchRepresentation.put("seatsLeft", this.seatsLeft);
			jsonSearchRepresentation.put("status", this.status.code);
			jsonSearchRepresentation.put("instName", this.instName);
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
			return this.backgroundURL.equals(c.getBackgroundURL()) && this.category.equals(c.getCategory()) &&
					this.subCategory.equals(c.getSubCategory()) && this.courseId == c.getCourseId() && this.title.equals(c.getTitle()) &&
					this.t_Info.equals(c.getT_Info()) && this.t_ImgURL.equals(c.getT_ImgURL()) &&
					this.t_Material.equals(c.getT_Material()) && this.price == c.getPrice() && this.seatsTotal == c.getSeatsTotal() &&
					this.seatsLeft == c.getSeatsLeft() && this.status.code == c.getStatus().code && this.instName.equals(c.getInstName()) &&
					this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) && this.startTime.getTime().toString().equals(c.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(c.getFinishTime().getTime().toString());
		}else{
			return this.backgroundURL.equals(c.getBackgroundURL()) && this.category.equals(c.getCategory()) &&
					this.subCategory.equals(c.getSubCategory()) && this.courseId == c.getCourseId() && this.partner.equals(c.getPartner()) &&
					this.title.equals(c.getTitle()) && this.t_Info.equals(c.getT_Info()) && this.t_ImgURL.equals(c.getT_ImgURL()) &&
					this.t_Material.equals(c.getT_Material()) && this.price == c.getPrice() && this.seatsTotal == c.getSeatsTotal() &&
					this.seatsLeft == c.getSeatsLeft() && this.status.code == c.getStatus().code && this.instName.equals(c.getInstName()) &&
					this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) && this.startTime.getTime().toString().equals(c.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(c.getFinishTime().getTime().toString());
		}			
	}
}
