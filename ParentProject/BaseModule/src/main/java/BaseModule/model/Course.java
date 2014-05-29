package BaseModule.model;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import BaseModule.common.DateUtility;
import BaseModule.common.Parser;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.ClassModel;
import BaseModule.configurations.EnumConfig.PartnerQualification;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.configurations.EnumConfig.TeachingMaterialType;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.EncodingService;

public class Course implements PseudoModel{

	private int courseId;
	private int partnerId;	
	
	private int price;
	private int seatsTotal;
	private int seatsLeft;
	private int courseHourNum;
	private int courseHourLength;	
	private int teachingMaterialCost;
	
	private Calendar creationTime;
	private Calendar startTime;
	private Calendar finishTime;	
	
	private String dailyStartTime;
	private String dailyFinishTime;
	private String category;
	private String subCategory;
	private String location;
	private String city;
	private String district;
	private String reference;
	private String teacherIntro;
	private String teacherImgUrl;
	private String teachingMethodsIntro;
	private String teachingMaterialName;
	private String classroomImgUrl;
	private String courseIntro;		
	private String quiz;
	private String certification;
	private String openCourseRequirement;	
	private String suitableStudent;
	private String prerequest;
	private String highScoreReward;		
	private String courseName;
	private String studyDaysNote;
	private String partnerCourseReference;
	private String classroomIntro;	
	private String partnerIntro;	
	private String teachingMaterialIntro;
	private String questionBankIntro;
	private String passAgreement;
	private String extracurricularIntro;;
	private String phone;
	
	private ClassModel classModel;	
	private AccountStatus status;
	private TeachingMaterialType teachingMaterialType;
	private PartnerQualification partnerQualification;
	private boolean teachingMaterialFree;
	private boolean hasDownloadMaterials;
	private boolean provideAssignments;
	private boolean provideMarking;
	
	private ArrayList<String> extracurricular = new ArrayList<String>();
	private ArrayList<Integer> studyDays = new ArrayList<Integer>();
	private ArrayList<String> teachingMethods = new ArrayList<String>();
	private ArrayList<String> questionBank = new ArrayList<String>();
	
	// Partner
	private String logoUrl;
	private String instName;
	private String wholeName;
	
	// SQL Construction;
	public Course(int courseId, int partnerId, Calendar startTime,
			Calendar finishTime, int price, int seatsTotal, int seatsLeft,
			AccountStatus status, String category, String subCategory,
			String location, String city, String district, String reference,
			String teacherIntro, String teacherImgUrl,
			String teachingMethodsIntro, String classroomImgUrl,
			String courseIntro, Calendar creationTime, ClassModel classModel,
			boolean hasDownloadMaterials, String quiz, String certification,
			String openCourseRequirement, ArrayList<String> questionBank,
			String suitableStudent, String prerequest, String highScoreReward,
			ArrayList<String> extracurricular, String courseName,
			String dailyStartTime, String dailyFinishTime,
			ArrayList<Integer> studyDays, String studyDaysNote,
			int courseHourNum, int courseHourLength,
			String partnerCourseReference, String classroomIntro,
			PartnerQualification partnerQualification, String partnerIntro,
			ArrayList<String> teachingMethods,
			TeachingMaterialType teachingMaterialType,
			String teachingMaterialIntro, int teacingMaterialCost,
			boolean teachingMaterialFree, String questionBankIntro,
			String passAgreement, boolean provideAssignments,
			boolean provideMarking, String extracurricularIntro,
			String phone, String logoUrl, String instName, String wholeName,String teachingMaterialName) {
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
		this.location = location;
		this.city = city;
		this.district = district;
		this.reference = reference;
		this.teacherIntro = teacherIntro;
		this.teacherImgUrl = teacherImgUrl;
		this.teachingMethodsIntro = teachingMethodsIntro;
		this.teachingMaterialName = teachingMaterialName;
		this.classroomImgUrl = classroomImgUrl;
		this.courseIntro = courseIntro;
		this.creationTime = creationTime;
		this.classModel = classModel;
		this.hasDownloadMaterials = hasDownloadMaterials;
		this.quiz = quiz;
		this.certification = certification;
		this.openCourseRequirement = openCourseRequirement;
		this.questionBank = questionBank;
		this.suitableStudent = suitableStudent;
		this.prerequest = prerequest;
		this.highScoreReward = highScoreReward;
		this.extracurricular = extracurricular;
		this.courseName = courseName;
		this.dailyStartTime = dailyStartTime;
		this.dailyFinishTime = dailyFinishTime;
		this.studyDays = studyDays;
		this.studyDaysNote = studyDaysNote;
		this.courseHourNum = courseHourNum;
		this.courseHourLength = courseHourLength;
		this.partnerCourseReference = partnerCourseReference;
		this.classroomIntro = classroomIntro;
		this.partnerQualification = partnerQualification;
		this.partnerIntro = partnerIntro;
		this.teachingMethods = teachingMethods;
		this.teachingMaterialType = teachingMaterialType;
		this.teachingMaterialIntro = teachingMaterialIntro;
		this.teachingMaterialCost = teacingMaterialCost;
		this.teachingMaterialFree = teachingMaterialFree;
		this.questionBankIntro = questionBankIntro;
		this.passAgreement = passAgreement;
		this.provideAssignments = provideAssignments;
		this.provideMarking = provideMarking;
		this.extracurricularIntro = extracurricularIntro;
		this.phone = phone;
		this.logoUrl = logoUrl;
		this.instName = instName;
		this.wholeName = wholeName;
	}

	public Course(int partnerId,Calendar startTime, Calendar finishTime, int price,
			int seatsTotal, int seatsLeft, AccountStatus status,
			String category, String subCategory, String phone) {
		super();
		this.courseId = -1;
		this.partnerId = partnerId;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.price = price;
		this.seatsTotal = seatsTotal;
		this.seatsLeft = seatsLeft;
		this.status = status;
		this.category = category;
		this.subCategory = subCategory;
		this.location = "";
		this.city =  "";
		this.district =  "";
		this.reference =  "";
		this.teacherIntro =  "";
		this.teacherImgUrl =  "";
		this.teachingMethodsIntro =  "";
		this.teachingMaterialName = "";
		this.classroomImgUrl =  "";
		this.courseIntro =  "";		
		this.classModel = ClassModel.smallclass;
		this.hasDownloadMaterials = false;
		this.quiz =  "";
		this.certification =  "";
		this.openCourseRequirement =  "";		
		this.suitableStudent =  "";
		this.prerequest = "";
		this.highScoreReward = "";		
		this.courseName = "";
		this.dailyStartTime = "";
		this.dailyFinishTime = "";		
		this.studyDaysNote = "";
		this.courseHourNum = -1;
		this.courseHourLength = -1;
		this.partnerCourseReference = "";
		this.classroomIntro = "";
		this.partnerQualification = PartnerQualification.unverified;
		this.partnerIntro = "";	
		this.teachingMaterialType = TeachingMaterialType.pub;
		this.teachingMaterialIntro = "";
		this.teachingMaterialCost = -1;
		this.teachingMaterialFree = true;
		this.questionBankIntro = "";
		this.passAgreement = "";
		this.provideAssignments = false;
		this.provideMarking = false;
		this.extracurricularIntro = "";
		this.phone = phone;
		this.logoUrl = "";
		this.instName = "";
		this.wholeName = "";
		this.creationTime = DateUtility.getCurTimeInstance();
	}	
	
	public Course(Calendar startTime, Calendar finishTime, int price,
			int seatsTotal, int seatsLeft, AccountStatus status) {		
		super();
		this.courseId = -1;
		this.partnerId = -1;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.price = price;
		this.seatsTotal = seatsTotal;
		this.seatsLeft = seatsLeft;
		this.status = status;
		this.category = "";
		this.subCategory = "";
		this.location = "";
		this.city =  "";
		this.district =  "";
		this.reference =  "";
		this.teacherIntro =  "";
		this.teacherImgUrl =  "";
		this.teachingMethodsIntro =  "";
		this.teachingMaterialName = "";
		this.classroomImgUrl =  "";
		this.courseIntro =  "";		
		this.classModel = ClassModel.smallclass;
		this.hasDownloadMaterials = false;
		this.quiz =  "";
		this.certification =  "";
		this.openCourseRequirement =  "";		
		this.suitableStudent =  "";
		this.prerequest = "";
		this.highScoreReward = "";		
		this.courseName = "";
		this.dailyStartTime = "";
		this.dailyFinishTime = "";		
		this.studyDaysNote = "";
		this.courseHourNum = -1;
		this.courseHourLength = -1;
		this.partnerCourseReference = "";
		this.classroomIntro = "";
		this.partnerQualification = PartnerQualification.unverified;
		this.partnerIntro = "";	
		this.teachingMaterialType = TeachingMaterialType.pub;
		this.teachingMaterialIntro = "";
		this.teachingMaterialCost = -1;
		this.teachingMaterialFree = true;
		this.questionBankIntro = "";
		this.passAgreement = "";
		this.provideAssignments = false;
		this.provideMarking = false;
		this.extracurricularIntro = "";
		this.phone = "";
		this.logoUrl = "";
		this.instName = "";
		this.wholeName = "";
		this.creationTime = DateUtility.getCurTimeInstance();
	}

	public Course(){
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

	public String getTeacherIntro() {
		return teacherIntro;
	}

	public void setTeacherIntro(String teacherIntro) {
		this.teacherIntro = teacherIntro;
	}

	public String getTeacherImgUrl() {
		return teacherImgUrl;
	}

	public void setTeacherImgUrl(String teacherImgUrl) {
		this.teacherImgUrl = teacherImgUrl;
	}

	public String getTeachingMethodsIntro() {
		return teachingMethodsIntro;
	}

	public void setTeachingMethodsIntro(String teachingMethodsIntro) {
		this.teachingMethodsIntro = teachingMethodsIntro;
	}

	public String getClassroomImgUrl() {
		return classroomImgUrl;
	}

	public void setClassroomImgUrl(String classroomImgUrl) {
		this.classroomImgUrl = classroomImgUrl;
	}

	public String getCourseIntro() {
		return courseIntro;
	}

	public void setCourseIntro(String courseIntro) {
		this.courseIntro = courseIntro;
	}

	public ClassModel getClassModel() {
		return classModel;
	}

	public void setClassModel(ClassModel classModel) {
		this.classModel = classModel;
	}

	public boolean isHasDownloadMaterials() {
		return hasDownloadMaterials;
	}

	public void setHasDownloadMaterials(boolean hasDownloadMaterials) {
		this.hasDownloadMaterials = hasDownloadMaterials;
	}

	public String getQuiz() {
		return quiz;
	}

	public void setQuiz(String quiz) {
		this.quiz = quiz;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public String getOpenCourseRequirement() {
		return openCourseRequirement;
	}

	public void setOpenCourseRequirement(String openCourseRequirement) {
		this.openCourseRequirement = openCourseRequirement;
	}

	public ArrayList<String> getQuestionBank() {
		return questionBank;
	}

	public void setQuestionBank(ArrayList<String> questionBank) {
		this.questionBank = questionBank;
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

	public String getHighScoreReward() {
		return highScoreReward;
	}

	public void setHighScoreReward(String highScoreReward) {
		this.highScoreReward = highScoreReward;
	}

	public ArrayList<String> getExtracurricular() {
		return extracurricular;
	}

	public void setExtracurricular(ArrayList<String> extracurricular) {
		this.extracurricular = extracurricular;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getDailyStartTime() {
		return dailyStartTime;
	}

	public void setDailyStartTime(String dailyStartTime) {
		this.dailyStartTime = dailyStartTime;
	}

	public String getDailyFinishTime() {
		return dailyFinishTime;
	}

	public void setDailyFinishTime(String dailyFinishTime) {
		this.dailyFinishTime = dailyFinishTime;
	}

	public ArrayList<Integer> getStudyDays() {
		return studyDays;
	}

	public void setStudyDays(ArrayList<Integer> studyDays) {
		this.studyDays = studyDays;
	}

	public String getStudyDaysNote() {
		return studyDaysNote;
	}

	public void setStudyDaysNote(String studyDaysNote) {
		this.studyDaysNote = studyDaysNote;
	}

	public int getCourseHourNum() {
		return courseHourNum;
	}

	public void setCourseHourNum(int courseHourNum) {
		this.courseHourNum = courseHourNum;
	}

	public int getCourseHourLength() {
		return courseHourLength;
	}

	public void setCourseHourLength(int courseHourLength) {
		this.courseHourLength = courseHourLength;
	}

	public String getPartnerCourseReference() {
		return partnerCourseReference;
	}

	public void setPartnerCourseReference(String partnerCourseReference) {
		this.partnerCourseReference = partnerCourseReference;
	}

	public String getClassroomIntro() {
		return classroomIntro;
	}

	public void setClassroomIntro(String classroomIntro) {
		this.classroomIntro = classroomIntro;
	}

	public PartnerQualification getPartnerQualification() {
		return partnerQualification;
	}

	public void setPartnerQualification(PartnerQualification partnerQualification) {
		this.partnerQualification = partnerQualification;
	}

	public String getPartnerIntro() {
		return partnerIntro;
	}

	public void setPartnerIntro(String partnerIntro) {
		this.partnerIntro = partnerIntro;
	}

	public ArrayList<String> getTeachingMethods() {
		return teachingMethods;
	}

	public void setTeachingMethods(ArrayList<String> teachingMethods) {
		this.teachingMethods = teachingMethods;
	}

	public TeachingMaterialType getTeachingMaterialType() {
		return teachingMaterialType;
	}

	public void setTeachingMaterialType(TeachingMaterialType teachingMaterialType) {
		this.teachingMaterialType = teachingMaterialType;
	}

	public String getTeachingMaterialIntro() {
		return this.teachingMaterialIntro;
	}

	public void setTeachingMaterialIntro(String teachingMaterialIntro) {
		this.teachingMaterialIntro = teachingMaterialIntro;
	}

	public int getTeacingMaterialCost() {
		return teachingMaterialCost;
	}

	public void setTeacingMaterialCost(int teacingMaterialCost) {
		this.teachingMaterialCost = teacingMaterialCost;
	}

	public boolean isTeachingMaterialFree() {
		return teachingMaterialFree;
	}

	public void setTeachingMaterialFree(boolean teachingMaterialFree) {
		this.teachingMaterialFree = teachingMaterialFree;
	}

	public String getQuestionBankIntro() {
		return questionBankIntro;
	}

	public void setQuestionBankIntro(String questionBankIntro) {
		this.questionBankIntro = questionBankIntro;
	}

	public String getPassAgreement() {
		return passAgreement;
	}

	public void setPassAgreement(String passAgreement) {
		this.passAgreement = passAgreement;
	}

	public boolean isProvideAssignments() {
		return provideAssignments;
	}

	public void setProvideAssignments(boolean provideAssignments) {
		this.provideAssignments = provideAssignments;
	}

	public boolean isProvideMarking() {
		return provideMarking;
	}

	public void setProvideMarking(boolean provideMarking) {
		this.provideMarking = provideMarking;
	}

	public String getExtracurricularIntro() {
		return extracurricularIntro;
	}

	public void setExtracurricularIntro(String extracurricularIntro) {
		this.extracurricularIntro = extracurricularIntro;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getWholeName() {
		return wholeName;
	}

	public void setWholeName(String wholeName) {
		this.wholeName = wholeName;
	}

	public int getTeachingMaterialCost() {
		return teachingMaterialCost;
	}

	public void setTeachingMaterialCost(int teachingMaterialCost) {
		this.teachingMaterialCost = teachingMaterialCost;
	}

	public String getTeachingMaterialName() {
		return teachingMaterialName;
	}

	public void setTeachingMaterialName(String teachingMaterialName) {
		this.teachingMaterialName = teachingMaterialName;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	public JSONObject toJSON(){
		JSONObject jsonSearchRepresentation = new JSONObject();
		try{
			jsonSearchRepresentation.put("id", this.courseId);
			jsonSearchRepresentation.put("partnerId", this.partnerId);
			jsonSearchRepresentation.put("partnerIntro", EncodingService.encodeURI(this.partnerIntro));
			jsonSearchRepresentation.put("partnerQualification", this.partnerQualification.code);
			jsonSearchRepresentation.put("partnerCourseReference", EncodingService.encodeURI(this.partnerCourseReference));
			jsonSearchRepresentation.put("courseName", EncodingService.encodeURI(this.courseName));
			jsonSearchRepresentation.put("courseIntro",EncodingService.encodeURI(this.courseIntro));
			jsonSearchRepresentation.put("teacherIntro", EncodingService.encodeURI(this.teacherIntro));
			jsonSearchRepresentation.put("teacherImgUrl", EncodingService.encodeURI(this.teacherImgUrl));
			jsonSearchRepresentation.put("teachingMethods", EncodingService.encodeURI(Parser.listToString(this.teachingMethods)));
			jsonSearchRepresentation.put("teachingMethodsIntro", EncodingService.encodeURI(this.teachingMethodsIntro));
			jsonSearchRepresentation.put("teachingMaterialIntro", EncodingService.encodeURI(this.teachingMaterialIntro));
			jsonSearchRepresentation.put("teachingMaterialName", EncodingService.encodeURI(this.teachingMaterialName));
			jsonSearchRepresentation.put("teachingMaterialType",this.teachingMaterialType.code);
			jsonSearchRepresentation.put("teachingMaterialCost",this.teachingMaterialCost);
			jsonSearchRepresentation.put("teachingMaterialFree",this.teachingMaterialFree);
			jsonSearchRepresentation.put("classroomIntro", EncodingService.encodeURI(this.classroomIntro));
			jsonSearchRepresentation.put("classroomImgUrl", EncodingService.encodeURI(this.classroomImgUrl));
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
			jsonSearchRepresentation.put("dailyStartTime", this.dailyStartTime);
			jsonSearchRepresentation.put("dailyFinishTime", this.dailyFinishTime);
			jsonSearchRepresentation.put("studyDays", EncodingService.encodeURI(Parser.listToString(this.studyDays)));
			jsonSearchRepresentation.put("studyDaysNote", this.studyDaysNote);
			jsonSearchRepresentation.put("courseHourNum", this.courseHourNum);
			jsonSearchRepresentation.put("courseHourLength", this.courseHourLength);
			jsonSearchRepresentation.put("classModel", this.classModel.code);			
			jsonSearchRepresentation.put("hasDownloadMaterials", this.hasDownloadMaterials);
			jsonSearchRepresentation.put("passAgreement", EncodingService.encodeURI(this.passAgreement));
			jsonSearchRepresentation.put("provideAssignments", this.provideAssignments);
			jsonSearchRepresentation.put("provideMarking", this.provideMarking);
			jsonSearchRepresentation.put("quiz", EncodingService.encodeURI(this.quiz));
			jsonSearchRepresentation.put("questionBank", EncodingService.encodeURI(Parser.listToString(this.questionBank)));
			jsonSearchRepresentation.put("questionBankIntro", EncodingService.encodeURI(this.questionBankIntro));
			jsonSearchRepresentation.put("certification", EncodingService.encodeURI(this.certification));			
			jsonSearchRepresentation.put("openCourseRequirement",EncodingService.encodeURI(this.openCourseRequirement));
			jsonSearchRepresentation.put("suitableStudent",EncodingService.encodeURI(this.suitableStudent));
			jsonSearchRepresentation.put("prerequest",EncodingService.encodeURI(this.prerequest));		
			jsonSearchRepresentation.put("highScoreReward",EncodingService.encodeURI(this.highScoreReward));
			jsonSearchRepresentation.put("extracurricular", EncodingService.encodeURI(Parser.listToString(this.extracurricular)));
			jsonSearchRepresentation.put("extracurricularIntro",EncodingService.encodeURI(this.extracurricularIntro));
			jsonSearchRepresentation.put("phone",EncodingService.encodeURI(this.phone));
			jsonSearchRepresentation.put("logoUrl",EncodingService.encodeURI(this.logoUrl));
			jsonSearchRepresentation.put("instName",EncodingService.encodeURI(this.instName));
			jsonSearchRepresentation.put("wholeName",EncodingService.encodeURI(this.wholeName));
			
		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonSearchRepresentation;
	}

	public boolean equals(Course c){	
			return  this.category.equals(c.getCategory()) &&
					this.subCategory.equals(c.getSubCategory()) && 
					this.courseId == c.getCourseId() && 
					this.partnerId == c.getPartnerId() &&					
					this.price == c.getPrice() && 
					this.seatsTotal == c.getSeatsTotal() &&
					this.seatsLeft == c.getSeatsLeft() && 
					this.status.code == c.getStatus().code  &&
					this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) && 
					this.startTime.getTime().toString().equals(c.getStartTime().getTime().toString()) &&
					this.finishTime.getTime().toString().equals(c.getFinishTime().getTime().toString()) &&
					this.location.equals(c.getLocation()) && 
					this.city.equals(c.getCity()) && 
					this.district.equals(c.getDistrict()) &&
					this.reference.equals(c.getReference()) && 
					this.courseIntro.equals(c.getCourseIntro())&& 
					this.classModel.code == c.getClassModel().code &&
				    this.hasDownloadMaterials == c.isHasDownloadMaterials() &&				 
					this.openCourseRequirement.equals(c.getOpenCourseRequirement()) &&
					this.suitableStudent.equals(c.getSuitableStudent()) &&
					this.prerequest.equals(c.getPrerequest()) && 					
					this.teacherImgUrl.equals(c.getTeacherImgUrl()) &&
					this.classroomImgUrl.equals(c.getClassroomImgUrl()) && 					
					this.certification.equals(c.getCertification()) &&
					this.courseName.equals(c.getCourseName()) &&
					this.dailyStartTime.equals(c.getDailyStartTime()) &&
					this.dailyFinishTime.equals(c.getDailyFinishTime());			
				
	}
	
	//TODO inform frontend of list formats in table
	public void loadFromMap(Map<String, String> kvps) throws PseudoException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ParseException{
		Field[] fields = this.getClass().getDeclaredFields();
		
		try{
			for (Field field : fields){
				field.setAccessible(true);
				String value = EncodingService.decodeURI(kvps.get(field.getName()));
				if (value != null){
					Class<?> fieldClass = field.getType();
					
					if (fieldClass.isAssignableFrom(int.class)){
						field.setInt(this, Integer.parseInt(value, 10));
					}
					else if (fieldClass.isAssignableFrom(String.class)){
						field.set(this, value);
					}
					else if (fieldClass.isAssignableFrom(Calendar.class)){
						field.set(this, DateUtility.castFromAPIFormat(value));
					}
					else if (fieldClass.isAssignableFrom(boolean.class)){
						field.set(this, Boolean.parseBoolean(value));
					}
					else if (fieldClass.isAssignableFrom(AccountStatus.class)){
						field.set(this, AccountStatus.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(ClassModel.class)){
						field.set(this, ClassModel.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(TeachingMaterialType.class)){
						field.set(this, TeachingMaterialType.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(PartnerQualification.class)){
						field.set(this, PartnerQualification.fromInt(Integer.parseInt(value, 10)));
					}
					else if (fieldClass.isAssignableFrom(ArrayList.class)){
						if (field.getName().equals("studyDays")){
							String[] vals = value.split("-");
							ArrayList<Integer> intList = new ArrayList<Integer>();
							for (String val : vals){
								intList.add(Integer.parseInt(val, 10));
							}
							field.set(this, intList);
						}
						else{
							String[] vals = value.split("-");
							ArrayList<String> strList = new ArrayList<String>(Arrays.asList(vals)); 
							field.set(this, strList);
						}
					}
					else{
						throw new RuntimeException("[ERROR][Reflection] Course loadFromMap suffered fatal reflection error, field type not matched");
					}
				}
				else{
					//null value from kvps, do nothing
				}
			}
		} catch (NumberFormatException e){
			throw new ValidationException("课程数据中数字格式错误");
		}
	}
}
