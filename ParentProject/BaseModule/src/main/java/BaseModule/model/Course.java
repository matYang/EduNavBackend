package BaseModule.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import BaseModule.common.DateUtility;
import BaseModule.common.Parser;
import BaseModule.configurations.ImgConfig;
import BaseModule.configurations.ServerConfig;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.CourseStatus;
import BaseModule.configurations.EnumConfig.PartnerQualification;
import BaseModule.configurations.EnumConfig.TeachingMaterialType;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.EncodingService;

public class Course implements PseudoModel, Serializable{
	
	private static final long serialVersionUID = 3L;
	
	private int courseId;
	private int partnerId;	
	private int price;
	private int courseHourNum;
	private int courseHourLength;	
	
	private int classSize;
	private int cashback;
	private int popularity;
	
	private Calendar creationTime;
	private Calendar startDate;
	private Calendar finishDate;	
	
	private int startTime1;
	private int finishTime1;
	private int startTime2;
	private int finishTime2;
	
	private String category;
	private String subCategory;
	private String subSubCategory;
	private String location;
	private String province;
	private String city;
	private String district;
	private String reference;
	
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
	private String partnerIntro;	
	private String teachingMaterialIntro;

	private String questionBank;
	private String passAgreement;
	private String extracurricular;
	private String phone;
	
	private String partnerDistinction;
	private String outline;	//text area
	private String goal;	//test area
	private String classTeacher;
	private String teachingAndExercise;
	private String questionSession;
	private String trail;
	private String assignments;
	private String marking;
	private String bonusService;
	private String downloadMaterials;
	private String teachingMaterialFree;
	
	private CourseStatus status;
	private PartnerQualification partnerQualification;
	
	private ArrayList<Integer> studyDays = new ArrayList<Integer>();
	private ArrayList<String> classImgUrls = new ArrayList<String>();
	private ArrayList<String> teacherIntros = new ArrayList<String>();
	private ArrayList<String> teacherImgUrls = new ArrayList<String>();
	private ArrayList<String> teacherNames = new ArrayList<String>();
	
	// Partner
	private String logoUrl;
	private String instName;
	private String wholeName;

	
	
	// SQL Construction;
	public Course(int courseId, int partnerId, int price, int courseHourNum,
			int courseHourLength, int classSize, int cashback, int popularity,
			Calendar creationTime, Calendar startDate, Calendar finishDate,
			int startTime1, int finishTime1, int startTime2, int finishiTime2,
			String category, String subCategory, String subSubCategory,String location, String province,String city,
			String district, String reference, String courseIntro, String quiz,
			String certification, String openCourseRequirement,
			String suitableStudent, String prerequest, String highScoreReward,
			String courseName, String studyDaysNote,
			String partnerCourseReference, String partnerIntro,
			String teachingMaterialIntro, String questionBank,
			String passAgreement, String extracurricular, String phone,
			String partnerDistinction, String outline, String goal,
			String classTeacher, String teachingAndExercise,
			String questionSession, String trail, String assignments,
			String marking, String bonusService, String downloadMaterials,
			CourseStatus status, PartnerQualification partnerQualification,
			String teachingMaterialFree, ArrayList<Integer> studyDays,
			ArrayList<String> classImgUrls, ArrayList<String> teacherIntros,
			ArrayList<String> teacherImgUrls, ArrayList<String> teacherNames,
			String logoUrl, String instName, String wholeName) {
		super();
		this.courseId = courseId;
		this.partnerId = partnerId;
		this.price = price;
		this.courseHourNum = courseHourNum;
		this.courseHourLength = courseHourLength;
		this.classSize = classSize;
		this.cashback = cashback;
		this.popularity = popularity;
		this.creationTime = creationTime;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.startTime1 = startTime1;
		this.finishTime1 = finishTime1;
		this.startTime2 = startTime2;
		this.finishTime2 = finishiTime2;
		this.category = category;
		this.subCategory = subCategory;
		this.subSubCategory = subSubCategory;
		this.location = location;
		this.province = province;
		this.city = city;
		this.district = district;
		this.reference = reference;
		this.courseIntro = courseIntro;
		this.quiz = quiz;
		this.certification = certification;
		this.openCourseRequirement = openCourseRequirement;
		this.suitableStudent = suitableStudent;
		this.prerequest = prerequest;
		this.highScoreReward = highScoreReward;
		this.courseName = courseName;
		this.studyDaysNote = studyDaysNote;
		this.partnerCourseReference = partnerCourseReference;
		this.partnerIntro = partnerIntro;
		this.teachingMaterialIntro = teachingMaterialIntro;
		this.questionBank = questionBank;
		this.passAgreement = passAgreement;
		this.extracurricular = extracurricular;
		this.phone = phone;
		this.partnerDistinction = partnerDistinction;
		this.outline = outline;
		this.goal = goal;
		this.classTeacher = classTeacher;
		this.teachingAndExercise = teachingAndExercise;
		this.questionSession = questionSession;
		this.trail = trail;
		this.assignments = assignments;
		this.marking = marking;
		this.bonusService = bonusService;
		this.downloadMaterials = downloadMaterials;
		this.status = status;
		this.partnerQualification = partnerQualification;
		this.teachingMaterialFree = teachingMaterialFree;
		this.studyDays = studyDays;
		this.classImgUrls = classImgUrls;
		this.teacherIntros = teacherIntros;
		this.teacherImgUrls = teacherImgUrls;
		this.teacherNames = teacherNames;
		this.logoUrl = logoUrl;
		this.instName = instName;
		this.wholeName = wholeName;
	}



	//testing
	public Course(int partnerId,Calendar startDate, Calendar finishDate, int price,
			int classSize, int popularity, String category, String subCategory, String phone) {
		super();
		this.courseId = -1;
		this.partnerId = partnerId;
		this.price = price;
		this.courseHourNum = -1;
		this.courseHourLength = -1;
		this.classSize = classSize;
		this.cashback = -1;
		this.popularity = popularity;
		this.creationTime = DateUtility.getCurTimeInstance();
		this.startDate = startDate;		
		this.finishDate = finishDate;
		this.startTime1 = -1;
		this.finishTime1 = -1;
		this.startTime2 = -1;
		this.finishTime2 = -1;
		this.category = category;
		this.subCategory = subCategory;
		this.subSubCategory = "";
		this.location = "";
		this.province = "";
		this.city = "";
		this.district = "";
		this.reference = "";
		this.courseIntro = "";
		this.quiz = "";
		this.certification = "";
		this.openCourseRequirement = "";
		this.suitableStudent = "";
		this.prerequest = "";
		this.highScoreReward = "";
		this.courseName = "";
		this.studyDaysNote = "";
		this.partnerCourseReference = "";
		this.partnerIntro = "";
		this.teachingMaterialIntro = "";
		this.questionBank = "";
		this.passAgreement = "";
		this.extracurricular = "";
		this.phone = phone;
		this.partnerDistinction = "";
		this.outline = "";
		this.goal = "";
		this.classTeacher = "";
		this.teachingAndExercise = "";
		this.questionSession = "";
		this.trail = "";
		this.assignments = "";
		this.marking = "";
		this.bonusService = "";
		this.downloadMaterials = "";
		this.status = CourseStatus.openEnroll;
		this.partnerQualification = PartnerQualification.verified;
		this.teachingMaterialFree = "";
		this.studyDays = new ArrayList<Integer>();
		this.classImgUrls = new ArrayList<String>();
		this.teacherIntros = new ArrayList<String>();;
		this.teacherImgUrls = new ArrayList<String>();;
		this.teacherNames = new ArrayList<String>();;
		this.logoUrl = "";
		this.instName = "";
		this.wholeName = "";
	}	
	
	

	//default constructor, set minimal behavior and hand over to reflection for filling in content
	public Course(){
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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

	public int getClassSize() {
		return classSize;
	}

	public void setClassSize(int classSize) {
		this.classSize = classSize;
	}

	public int getCashback() {
		return cashback;
	}

	public void setCashback(int cashback) {
		this.cashback = cashback;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
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

	public int getStartTime1() {
		return startTime1;
	}

	public void setStartTime1(int startTime1) {
		this.startTime1 = startTime1;
	}

	public int getFinishTime1() {
		return finishTime1;
	}

	public void setFinishTime1(int finishTime1) {
		this.finishTime1 = finishTime1;
	}

	public int getStartTime2() {
		return startTime2;
	}

	public void setStartTime2(int startTime2) {
		this.startTime2 = startTime2;
	}

	public int getFinishTime2() {
		return finishTime2;
	}

	public void setFinishTime2(int finishTime2) {
		this.finishTime2 = finishTime2;
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

	public String getCourseIntro() {
		return courseIntro;
	}

	public void setCourseIntro(String courseIntro) {
		this.courseIntro = courseIntro;
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

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getStudyDaysNote() {
		return studyDaysNote;
	}

	public void setStudyDaysNote(String studyDaysNote) {
		this.studyDaysNote = studyDaysNote;
	}

	public String getPartnerCourseReference() {
		return partnerCourseReference;
	}

	public void setPartnerCourseReference(String partnerCourseReference) {
		this.partnerCourseReference = partnerCourseReference;
	}

	public String getPartnerIntro() {
		return partnerIntro;
	}

	public void setPartnerIntro(String partnerIntro) {
		this.partnerIntro = partnerIntro;
	}

	public String getTeachingMaterialIntro() {
		return teachingMaterialIntro;
	}

	public void setTeachingMaterialIntro(String teachingMaterialIntro) {
		this.teachingMaterialIntro = teachingMaterialIntro;
	}

	public String getQuestionBank() {
		return questionBank;
	}

	public void setQuestionBank(String questionBank) {
		this.questionBank = questionBank;
	}

	public String getPassAgreement() {
		return passAgreement;
	}

	public void setPassAgreement(String passAgreement) {
		this.passAgreement = passAgreement;
	}

	public String getExtracurricular() {
		return extracurricular;
	}

	public void setExtracurricular(String extracurricular) {
		this.extracurricular = extracurricular;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPartnerDistinction() {
		return partnerDistinction;
	}

	public void setPartnerDistinction(String partnerDistinction) {
		this.partnerDistinction = partnerDistinction;
	}

	public String getOutline() {
		return outline;
	}

	public void setOutline(String outline) {
		this.outline = outline;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getClassTeacher() {
		return classTeacher;
	}

	public void setClassTeacher(String classTeacher) {
		this.classTeacher = classTeacher;
	}

	public String getTeachingAndExercise() {
		return teachingAndExercise;
	}

	public void setTeachingAndExercise(String teachingAndExercise) {
		this.teachingAndExercise = teachingAndExercise;
	}

	public String getQuestionSession() {
		return questionSession;
	}

	public void setQuestionSession(String questionSession) {
		this.questionSession = questionSession;
	}

	public String getTrail() {
		return trail;
	}

	public void setTrail(String trail) {
		this.trail = trail;
	}

	public String getAssignments() {
		return assignments;
	}

	public void setAssignments(String assignments) {
		this.assignments = assignments;
	}

	public String getMarking() {
		return marking;
	}

	public void setMarking(String marking) {
		this.marking = marking;
	}

	public String getBonusService() {
		return bonusService;
	}

	public void setBonusService(String bonusService) {
		this.bonusService = bonusService;
	}

	public String getDownloadMaterials() {
		return downloadMaterials;
	}

	public void setDownloadMaterials(String downloadMaterials) {
		this.downloadMaterials = downloadMaterials;
	}

	public CourseStatus getStatus() {
		return status;
	}

	public void setStatus(CourseStatus status) {
		this.status = status;
	}

	public PartnerQualification getPartnerQualification() {
		return partnerQualification;
	}

	public void setPartnerQualification(PartnerQualification partnerQualification) {
		this.partnerQualification = partnerQualification;
	}

	public String getTeachingMaterialFree() {
		return teachingMaterialFree;
	}

	public void setTeachingMaterialFree(String teachingMaterialFree) {
		this.teachingMaterialFree = teachingMaterialFree;
	}

	public ArrayList<Integer> getStudyDays() {
		return studyDays;
	}

	public void setStudyDays(ArrayList<Integer> studyDays) {
		this.studyDays = studyDays;
	}

	public ArrayList<String> getClassImgUrls() {
		return classImgUrls;
	}

	public void setClassImgUrls(ArrayList<String> classImgUrls) {
		this.classImgUrls = classImgUrls;
	}

	public ArrayList<String> getTeacherIntros() {
		return teacherIntros;
	}

	public void setTeacherIntros(ArrayList<String> teacherIntros) {
		this.teacherIntros = teacherIntros;
	}

	public ArrayList<String> getTeacherImgUrls() {
		return teacherImgUrls;
	}

	public void setTeacherImgUrls(ArrayList<String> teacherImgUrls) {
		this.teacherImgUrls = teacherImgUrls;
	}

	public ArrayList<String> getTeacherNames() {
		return teacherNames;
	}

	public void setTeacherNames(ArrayList<String> teacherNames) {
		this.teacherNames = teacherNames;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}



	public Course deepCopy() throws IOException, ClassNotFoundException{
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(this);
        oos.close();
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Course clone = (Course) ois.readObject();
        
        return clone;
	}

	public JSONObject toJSON(){
		JSONObject jsonObj = new JSONObject();
		try{
			jsonObj.put("id", this.courseId);
			jsonObj.put("partnerId", this.partnerId);
			jsonObj.put("price", this.price);
			jsonObj.put("courseHourNum", this.courseHourNum);
			jsonObj.put("courseHourLength", this.courseHourLength);
			jsonObj.put("classSize", this.classSize);
			jsonObj.put("cashback", this.cashback);
			jsonObj.put("popularity", this.popularity);
			jsonObj.put("creationTime", DateUtility.castToAPIFormat(this.creationTime));		
			jsonObj.put("startDate", DateUtility.castToAPIFormat(this.startDate));
			jsonObj.put("finishDate", DateUtility.castToAPIFormat(this.finishDate));
			jsonObj.put("startTime1", this.startTime1);
			jsonObj.put("finishTime1", this.finishTime1);
			jsonObj.put("startTime2", this.startTime2);
			jsonObj.put("finishTime2", this.finishTime2);
			jsonObj.put("category", EncodingService.encodeURI(this.category));
			jsonObj.put("subCategory", EncodingService.encodeURI(this.subCategory));
			jsonObj.put("subSubCategory", EncodingService.encodeURI(this.subSubCategory));
			jsonObj.put("location", EncodingService.encodeURI(this.location));
			jsonObj.put("province", EncodingService.encodeURI(this.province));
			jsonObj.put("city", EncodingService.encodeURI(this.city));
			jsonObj.put("district", EncodingService.encodeURI(this.district));
			jsonObj.put("reference", EncodingService.encodeURI(this.reference));
			jsonObj.put("courseIntro",EncodingService.encodeURI(this.courseIntro));
			jsonObj.put("quiz", EncodingService.encodeURI(this.quiz));
			jsonObj.put("certification", EncodingService.encodeURI(this.certification));
			jsonObj.put("openCourseRequirement",EncodingService.encodeURI(this.openCourseRequirement));
			jsonObj.put("suitableStudent",EncodingService.encodeURI(this.suitableStudent));
			jsonObj.put("prerequest",EncodingService.encodeURI(this.prerequest));	
			jsonObj.put("highScoreReward",EncodingService.encodeURI(this.highScoreReward));
			jsonObj.put("courseName", EncodingService.encodeURI(this.courseName));
			jsonObj.put("studyDaysNote", this.studyDaysNote);
			jsonObj.put("partnerCourseReference", EncodingService.encodeURI(this.partnerCourseReference));			
			jsonObj.put("partnerIntro", EncodingService.encodeURI(this.partnerIntro));
			jsonObj.put("teachingMaterialIntro", EncodingService.encodeURI(this.teachingMaterialIntro));
			jsonObj.put("questionBank", EncodingService.encodeURI(this.questionBank));
			jsonObj.put("passAgreement", EncodingService.encodeURI(this.passAgreement));
			jsonObj.put("extracurricular", EncodingService.encodeURI(this.extracurricular));
			jsonObj.put("phone",EncodingService.encodeURI(this.phone));
			jsonObj.put("partnerDistinction",EncodingService.encodeURI(this.partnerDistinction));
			jsonObj.put("outline",EncodingService.encodeURI(this.outline));			
			jsonObj.put("goal",EncodingService.encodeURI(this.goal));
			jsonObj.put("classTeacher",EncodingService.encodeURI(this.classTeacher));
			jsonObj.put("teachingAndExercise",EncodingService.encodeURI(this.teachingAndExercise));
			jsonObj.put("questionSession",EncodingService.encodeURI(this.questionSession));
			jsonObj.put("trail",EncodingService.encodeURI(this.trail));
			jsonObj.put("assignments",EncodingService.encodeURI(this.assignments));
			jsonObj.put("marking",EncodingService.encodeURI(this.marking));
			jsonObj.put("bonusService",EncodingService.encodeURI(this.bonusService));
			jsonObj.put("downloadMaterials", this.downloadMaterials);
			jsonObj.put("status", this.status.code);
			jsonObj.put("partnerQualification", this.partnerQualification.code);
			jsonObj.put("teachingMaterialFree",this.teachingMaterialFree);
			jsonObj.put("studyDays", new JSONArray(this.studyDays));
			jsonObj.put("classImgUrls", new JSONArray(this.classImgUrls));
			jsonObj.put("teacherImgUrls", new JSONArray(this.teacherImgUrls));
			ArrayList<String> tempEncodedIntros = new ArrayList<String>();
			for (String intro : this.teacherIntros){
				tempEncodedIntros.add(EncodingService.encodeURI(intro));
			}
			jsonObj.put("teacherIntros", new JSONArray(tempEncodedIntros));
			ArrayList<String> tempEncodedNames = new ArrayList<String>();
			for (String name : this.teacherNames){
				tempEncodedNames.add(EncodingService.encodeURI(name));
			}
			jsonObj.put("teacherNames", new JSONArray(tempEncodedNames));			
			jsonObj.put("logoUrl",EncodingService.encodeURI(this.logoUrl));
			jsonObj.put("instName",EncodingService.encodeURI(this.instName));
			jsonObj.put("wholeName",EncodingService.encodeURI(this.wholeName));
			
		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	public boolean equals(Course c){
		if (c == null){
			return false;
		}
			return  this.category.equals(c.getCategory()) &&
					this.subCategory.equals(c.getSubCategory()) && 
					this.courseId == c.getCourseId() && 
					this.partnerId == c.getPartnerId() &&					
					this.price == c.getPrice() && 
					this.classSize == c.getClassSize() && 
					this.status.code == c.getStatus().code  &&
					this.creationTime.getTime().toString().equals(c.getCreationTime().getTime().toString()) && 
					this.startDate.getTime().toString().equals(c.getStartDate().getTime().toString()) &&
					this.finishDate.getTime().toString().equals(c.getFinishDate().getTime().toString()) &&
					this.startTime1 == c.getStartTime1() && this.finishTime1 == c.getFinishTime1() && this.startTime2 == c.getStartTime2() && this.finishTime2 == c.getFinishTime2() &&
					this.location.equals(c.getLocation()) && 
					this.province.equals(c.getProvince()) &&
					this.city.equals(c.getCity()) && 
					this.district.equals(c.getDistrict()) &&
					this.reference.equals(c.getReference()) && 
					this.courseIntro.equals(c.getCourseIntro())&& 					
				    this.downloadMaterials.equals(c.getDownloadMaterials()) &&				 
					this.openCourseRequirement.equals(c.getOpenCourseRequirement()) &&
					this.suitableStudent.equals(c.getSuitableStudent()) &&
					this.prerequest.equals(c.getPrerequest()) && 									
					this.certification.equals(c.getCertification()) &&
					this.courseName.equals(c.getCourseName()) &&
					this.classTeacher.equals(c.getClassTeacher());		
				
	}
	

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
					else if (fieldClass.isAssignableFrom(CourseStatus.class)){
						field.set(this, CourseStatus.fromInt(Integer.parseInt(value, 10)));
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
					}
					else{
						throw new RuntimeException("[ERROR][Reflection] Course loadFromMap suffered fatal reflection error, field type not matched");
					}
				}
				else if (field.getName().equals("classImgUrls")){
					ArrayList<String> classImgUrlList = new ArrayList<String>();
					for (int i = 1; i <= 5; i++){
						String val = kvps.get("url-classImg" + i);
						if (val == null){
							throw new ValidationException("教室图片 " + i + " 不能为null");
						}
						classImgUrlList.add(val);
					}
					field.set(this, classImgUrlList);
				}
				else if (field.getName().equals("teacherIntros")){
					ArrayList<String> teacherIntroList = new ArrayList<String>();
					for (int i = 1; i <= 4; i++){
						String val = EncodingService.decodeURI(kvps.get("teacherIntro" + i));
						if (val == null){
							break;
						}
						teacherIntroList.add(val);
					}
					field.set(this, teacherIntroList);
				}
				else if (field.getName().equals("teacherImgUrls")){
					ArrayList<String> teacherImgUrlList = new ArrayList<String>();
					for (int i = 1; i <= 4; i++){
						String val = kvps.get("url-teacherImg" + i);
						if (val == null){
							break;
						}
						teacherImgUrlList.add(val);
					}
					field.set(this, teacherImgUrlList);
				}
				else if (field.getName().equals("teacherNames")){
					ArrayList<String> teacherNameList = new ArrayList<String>();
					for (int i = 1; i <= 4; i++){
						String val = EncodingService.decodeURI(kvps.get("teacherName" + i));
						if (val == null){
							break;
						}
						teacherNameList.add(val);
					}
					field.set(this, teacherNameList);
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
