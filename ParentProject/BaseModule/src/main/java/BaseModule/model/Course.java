package BaseModule.model;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.ClassModel;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.EncodingService;

public class Course implements PseudoModel{

	private int courseId;
	private int partnerId;
	
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
	private String courseInfo;	
	private Calendar creationTime;
	
	private ClassModel classModel;		
	private boolean authenticated;
	private boolean hasDownloadMaterials;
	private boolean ensurePass;
	private boolean quizandassignment;
	private boolean certification;
	private String room;
	private String openClassRequirements;
	private String questionBank;
	private String studyForm;
	private String suitableStudent;
	private String prerequest;
	private String courseOutline;
	private String highscoreaward;	
	private String extracurricular;
	
	//SQL Retrieving		
	public Course(int courseId, int partnerId,  Calendar creationTime,Calendar startTime,
			Calendar finishTime, String teacherInfo,String teacherImgUrl,
			 String teachingMaterial,String backgroundUrl,int price, int seatsTotal, 
			 int seatsLeft,AccountStatus status, String category, String subCategory,
			 Partner partner,String title,String location, String city, String district, 
			 String reference,String courseInfo,ClassModel classModel, String room,
			 boolean authenticated,	boolean hasDownloadMaterials, boolean ensurePass,
			boolean quizandassignment, boolean certification,
			String openClassRequirements, String questionBank, String studyForm,
			String suitableStudent, String prerequest, String courseOutline,
			String highscoreaward, String extracurricular) {
		super();
		this.courseId = courseId;
		this.partnerId = partnerId;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.price = price;
		this.seatsTotal = seatsTotal;
		this.seatsLeft = seatsLeft;
		this.status = status;
		this.category = category;
		this.subCategory = subCategory;
		this.title = title;
		this.partner = partner;
		this.location = location;
		this.city = city;
		this.district = district;
		this.reference = reference;
		this.teacherInfo = teacherInfo;
		this.teacherImgUrl = teacherImgUrl;
		this.teachingMaterial = teachingMaterial;
		this.backgroundUrl = backgroundUrl;
		this.courseInfo = courseInfo;
		this.creationTime = creationTime;
		this.classModel = classModel;
		this.room = room;
		this.authenticated = authenticated;
		this.hasDownloadMaterials = hasDownloadMaterials;
		this.ensurePass = ensurePass;
		this.quizandassignment = quizandassignment;
		this.certification = certification;
		this.openClassRequirements = openClassRequirements;
		this.questionBank = questionBank;
		this.studyForm = studyForm;
		this.suitableStudent = suitableStudent;
		this.prerequest = prerequest;
		this.courseOutline = courseOutline;
		this.highscoreaward = highscoreaward;
		this.extracurricular = extracurricular;
	}

	//Normal Construction
	public Course(int p_Id, Calendar startTime, Calendar finishTime,
			int seatsTotal, int seatsLeft, String category,
			String subCategory,AccountStatus status,int price, String title,String courseInfo) {
		super();
		this.partnerId = p_Id;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.seatsTotal = seatsTotal;
		this.seatsLeft = seatsLeft;
		this.category = category;
		this.subCategory = subCategory;
		this.status = status;
		this.price = price;
		this.title = title;
		this.courseInfo = courseInfo;
		this.classModel = ClassModel.medianclass;
		this.room = "";
		this.authenticated = false;
		this.hasDownloadMaterials = false;
		this.ensurePass = false;
		this.quizandassignment = false;
		this.certification = false;
		this.openClassRequirements = "";
		this.questionBank = "";
		this.studyForm = "";
		this.suitableStudent = "";
		this.prerequest = "";
		this.courseOutline = "";
		this.highscoreaward = "";
		this.extracurricular = "";			
		this.creationTime = DateUtility.getCurTimeInstance();		
	}
	
	public Course(int partnerId, Calendar startTime, Calendar finishTime,
			int price, int seatsTotal, int seatsLeft, AccountStatus status,
			String category, String subCategory, String title, String location,
			String city, String district, String teacherInfo,
			String teacherImgUrl, String teachingMaterial,
			String backgroundUrl, String courseInfo, ClassModel classModel,
			String room, boolean authenticated, boolean hasDownloadMaterials,
			boolean ensurePass, boolean quizandassignment,
			boolean certification, String openClassRequirements,
			String questionBank, String studyForm, String suitableStudent,
			String prerequest, String courseOutline, String highscoreaward,
			String extracurricular) {
		super();
		this.partnerId = partnerId;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.price = price;
		this.seatsTotal = seatsTotal;
		this.seatsLeft = seatsLeft;
		this.status = status;
		this.category = category;
		this.subCategory = subCategory;
		this.title = title;
		this.location = location;
		this.city = city;
		this.district = district;
		this.teacherInfo = teacherInfo;
		this.teacherImgUrl = teacherImgUrl;
		this.teachingMaterial = teachingMaterial;
		this.backgroundUrl = backgroundUrl;
		this.courseInfo = courseInfo;
		this.classModel = classModel;
		this.room = room;
		this.authenticated = authenticated;
		this.hasDownloadMaterials = hasDownloadMaterials;
		this.ensurePass = ensurePass;
		this.quizandassignment = quizandassignment;
		this.certification = certification;
		this.openClassRequirements = openClassRequirements;
		this.questionBank = questionBank;
		this.studyForm = studyForm;
		this.suitableStudent = suitableStudent;
		this.prerequest = prerequest;
		this.courseOutline = courseOutline;
		this.highscoreaward = highscoreaward;
		this.extracurricular = extracurricular;
		this.creationTime = DateUtility.getCurTimeInstance();	
	}

	//default
	public Course() {
		super();
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

	public void setTeacherImgUrl(String teacherImgUrl) {
		this.teacherImgUrl = teacherImgUrl;
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

	public String getCourseInfo() {
		return courseInfo;
	}

	public void setCourseInfo(String courseInfo) {
		this.courseInfo = courseInfo;
	}

	public ClassModel getClassModel() {
		return classModel;
	}

	public void setClassModel(ClassModel classModel) {
		this.classModel = classModel;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public boolean isHasDownloadMaterials() {
		return hasDownloadMaterials;
	}

	public void setHasDownloadMaterials(boolean hasDownloadMaterials) {
		this.hasDownloadMaterials = hasDownloadMaterials;
	}

	public boolean isEnsurePass() {
		return ensurePass;
	}

	public void setEnsurePass(boolean ensurePass) {
		this.ensurePass = ensurePass;
	}

	public boolean isQuizandassignment() {
		return quizandassignment;
	}

	public void setQuizandassignment(boolean quizandassignment) {
		this.quizandassignment = quizandassignment;
	}

	public boolean isCertification() {
		return certification;
	}

	public void setCertification(boolean certification) {
		this.certification = certification;
	}

	public String getOpenClassRequirements() {
		return openClassRequirements;
	}

	public void setOpenClassRequirements(String openClassRequirements) {
		this.openClassRequirements = openClassRequirements;
	}

	public String getQuestionBank() {
		return questionBank;
	}

	public void setQuestionBank(String questionBank) {
		this.questionBank = questionBank;
	}

	public String getStudyForm() {
		return studyForm;
	}

	public void setStudyForm(String studyForm) {
		this.studyForm = studyForm;
	}

	public String getSuitableStudent() {
		return suitableStudent;
	}

	public void setSuitableStudent(String suitableStudent) {
		this.suitableStudent = suitableStudent;
	}

	public String getPrerequest() {
		return prerequest;
	}

	public void setPrerequest(String prerequest) {
		this.prerequest = prerequest;
	}

	public String getCourseOutline() {
		return courseOutline;
	}

	public void setCourseOutline(String courseOutline) {
		this.courseOutline = courseOutline;
	}

	public String getHighscoreaward() {
		return highscoreaward;
	}

	public void setHighscoreaward(String highscoreaward) {
		this.highscoreaward = highscoreaward;
	}

	public String getExtracurricular() {
		return extracurricular;
	}

	public void setExtracurricular(String extracurricular) {
		this.extracurricular = extracurricular;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
	}

	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("id", this.courseId);
			jsonSearchRepresentation.put("partnerId", this.partnerId);
			jsonSearchRepresentation.put("partner", this.partner == null ? new JSONObject() : this.partner.toJSON());
			jsonSearchRepresentation.put("title", EncodingService.encodeURI(this.title));
			jsonSearchRepresentation.put("courseInfo",EncodingService.encodeURI(this.courseInfo));
			jsonSearchRepresentation.put("teacherInfo", EncodingService.encodeURI(this.teacherInfo));
			jsonSearchRepresentation.put("teacherImgUrl", EncodingService.encodeURI(this.teacherImgUrl));
			jsonSearchRepresentation.put("teachingMaterial", EncodingService.encodeURI(this.teachingMaterial));
			jsonSearchRepresentation.put("backgroundUrl", EncodingService.encodeURI(this.backgroundUrl));
			jsonSearchRepresentation.put("reference", EncodingService.encodeURI(this.reference));
			jsonSearchRepresentation.put("category", EncodingService.encodeURI(this.category));
			jsonSearchRepresentation.put("subcategory", EncodingService.encodeURI(this.subCategory));
			jsonSearchRepresentation.put("location", EncodingService.encodeURI(this.location));
			jsonSearchRepresentation.put("city", EncodingService.encodeURI(this.city));
			jsonSearchRepresentation.put("district", EncodingService.encodeURI(this.district));
			jsonSearchRepresentation.put("price", this.price);
			jsonSearchRepresentation.put("seatsTotal", this.seatsTotal);
			jsonSearchRepresentation.put("seatsLeft", this.seatsLeft);
			jsonSearchRepresentation.put("status", this.status.code);
			jsonSearchRepresentation.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));		
			jsonSearchRepresentation.put("startTime", DateUtility.castToAPIFormat(this.startTime));
			jsonSearchRepresentation.put("finishTime", DateUtility.castToAPIFormat(this.finishTime));
			jsonSearchRepresentation.put("classModel", this.classModel.code);				
			jsonSearchRepresentation.put("authenticated", this.authenticated);
			jsonSearchRepresentation.put("hasDownloadMaterials", this.hasDownloadMaterials);
			jsonSearchRepresentation.put("ensurePass", this.ensurePass);
			jsonSearchRepresentation.put("quizandassignment", this.quizandassignment);
			jsonSearchRepresentation.put("certification", this.certification);
			jsonSearchRepresentation.put("room", EncodingService.encodeURI(this.room));
			jsonSearchRepresentation.put("openClassRequirements",EncodingService.encodeURI(this.openClassRequirements));
			jsonSearchRepresentation.put("questionBank",EncodingService.encodeURI(this.questionBank));
			jsonSearchRepresentation.put("studyForm",EncodingService.encodeURI(this.studyForm));
			jsonSearchRepresentation.put("suitableStudent",EncodingService.encodeURI(this.suitableStudent));
			jsonSearchRepresentation.put("prerequest",EncodingService.encodeURI(this.prerequest));
			jsonSearchRepresentation.put("courseOutline",EncodingService.encodeURI(this.courseOutline));
			jsonSearchRepresentation.put("highscoreaward",EncodingService.encodeURI(this.highscoreaward));
			jsonSearchRepresentation.put("extracurricular",EncodingService.encodeURI(this.extracurricular));			
		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;
	}

	public boolean equals(Course c){		
		if(this.partner==null){
			return  this.category.equals(c.getCategory()) &&	this.subCategory.equals(c.getSubCategory()) && this.courseId == c.getCourseId() && 
					this.title.equals(c.getTitle()) && 	this.teacherInfo.equals(c.getTeacherInfo()) &&this.teachingMaterial.equals(c.getTeachingMaterial()) && 
					this.price == c.getPrice() && this.seatsTotal == c.getSeatsTotal() &&
					this.seatsLeft == c.getSeatsLeft() && this.status.code == c.getStatus().code  &&
					this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) && 
					this.startTime.getTime().toString().equals(c.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(c.getFinishTime().getTime().toString()) &&
					this.location.equals(c.getLocation()) && this.city.equals(c.getCity()) && this.district.equals(c.getDistrict()) &&
					this.reference.equals(c.getReference()) && this.courseInfo.equals(c.getCourseInfo()) && this.classModel.code == c.getClassModel().code &&
					this.room.equals(c.getRoom()) && this.authenticated == c.isAuthenticated() && this.hasDownloadMaterials == c.isHasDownloadMaterials() &&
					this.ensurePass == c.isEnsurePass() && this.quizandassignment == c.isQuizandassignment() && 
					this.certification == c.isCertification() && this.openClassRequirements.equals(c.getOpenClassRequirements()) &&
					this.questionBank.equals(c.getQuestionBank()) && this.studyForm.equals(c.getStudyForm()) && this.suitableStudent.equals(c.getSuitableStudent()) &&
					this.prerequest.equals(c.getPrerequest()) && this.courseOutline.equals(c.getCourseOutline()) &&
					this.highscoreaward.equals(c.getHighscoreaward()) && this.extracurricular.equals(c.getExtracurricular());
		}else{
			return  this.category.equals(c.getCategory()) &&this.subCategory.equals(c.getSubCategory()) && this.courseId == c.getCourseId() && 
					this.title.equals(c.getTitle()) && this.teacherInfo.equals(c.getTeacherInfo()) &&
					this.teachingMaterial.equals(c.getTeachingMaterial()) && this.price == c.getPrice() && this.seatsTotal == c.getSeatsTotal() &&
					this.seatsLeft == c.getSeatsLeft() && this.status.code == c.getStatus().code  &&
					this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) && 
					this.startTime.getTime().toString().equals(c.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(c.getFinishTime().getTime().toString()) &&
					this.location.equals(c.getLocation()) && this.city.equals(c.getCity()) && this.district.equals(c.getDistrict()) &&
					this.reference.equals(c.getReference()) && this.partner.equals(c.getPartner()) && this.courseInfo.equals(c.getCourseInfo())&& 
					this.classModel.code == c.getClassModel().code &&
					this.room.equals(c.getRoom()) && this.authenticated == c.isAuthenticated() && this.hasDownloadMaterials == c.isHasDownloadMaterials() &&
					this.ensurePass == c.isEnsurePass() && this.quizandassignment == c.isQuizandassignment() && 
					this.certification == c.isCertification() && this.openClassRequirements.equals(c.getOpenClassRequirements()) &&
					this.questionBank.equals(c.getQuestionBank()) && this.studyForm.equals(c.getStudyForm()) && 
					this.suitableStudent.equals(c.getSuitableStudent()) &&
					this.prerequest.equals(c.getPrerequest()) && this.courseOutline.equals(c.getCourseOutline()) &&
					this.highscoreaward.equals(c.getHighscoreaward()) && this.extracurricular.equals(c.getExtracurricular());
		}			
	}
}
