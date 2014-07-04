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
import BaseModule.configurations.EnumConfig.BookingType;
import BaseModule.configurations.EnumConfig.CourseStatus;
import BaseModule.configurations.EnumConfig.PartnerQualification;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;

public class Course implements PseudoModel, Serializable{
	
	private static final long serialVersionUID = 3L;
	
	private int courseId;
	private int partnerId;	
	private int price;
	private int originalPrice;
	private int courseHourNum;
	private int courseHourLength;	
	
	private int classSize;
	private int cashback;
	private int popularity;
	
	private Calendar creationTime;
	private Calendar startDate;
	private Calendar finishDate;
	private Calendar cutoffDate;
	
	private Calendar noRefundDate;
	private Calendar cashbackDate;
	private BookingType bookingType;
	
	private int startUponArrival;	//1 means true, 0 means false
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
	private String goal;	//text area
	private String classTeacher;
	private String teachingAndExercise;
	private String questionSession;
	private String trail;
	private String assignments;
	private String marking;
	private String bonusService;
	private String downloadMaterials;
	private String teachingMaterialFee;
	
	private CourseStatus status;
	private PartnerQualification partnerQualification;
	
	private ArrayList<Integer> studyDays = new ArrayList<Integer>();
	
	private ArrayList<Long> classPhotoIdList;
	private ArrayList<ClassPhoto> classPhotoList;
	private ArrayList<Long> teacherIdList;
	private ArrayList<Teacher> teacherList;
	
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
			ArrayList<Long> classPhotoIdList, ArrayList<ClassPhoto> classPhotoList,
			ArrayList<Long> teacherIdList, ArrayList<Teacher> teacherList,
			String logoUrl, String instName, String wholeName,int startUponArrival,Calendar cutoffDate,
			 Calendar noRefundDate,	Calendar cashbackDate,BookingType bookingType,int originalPrice) {
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
		this.teachingMaterialFee = teachingMaterialFree;
		this.studyDays = studyDays;
		this.classPhotoIdList = classPhotoIdList;
		this.classPhotoList = classPhotoList;
		this.teacherIdList = teacherIdList;
		this.teacherList = teacherList;
		this.logoUrl = logoUrl;
		this.instName = instName;
		this.wholeName = wholeName;
		this.startUponArrival = startUponArrival;
		this.cutoffDate = cutoffDate;
		this.noRefundDate = noRefundDate;
		this.cashbackDate = cashbackDate;
		this.bookingType = bookingType;
		this.originalPrice = originalPrice;
	}



	//testing
	public Course(int partnerId,Calendar startDate, Calendar finishDate, int price,
			int classSize, int popularity, String category, String subCategory, String phone) {
		super();
		this.courseId = -1;
		this.partnerId = partnerId;
		this.price = price;
		this.courseHourNum = 0;
		this.courseHourLength = 0;
		this.classSize = classSize;
		this.cashback = 0;
		this.popularity = popularity;
		this.creationTime = DateUtility.getCurTimeInstance();
		this.startDate = startDate;		
		this.finishDate = finishDate;
		this.startTime1 = 0;
		this.finishTime1 = 0;
		this.startTime2 = 0;
		this.finishTime2 = 0;
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
		this.teachingMaterialFee = "";
		this.studyDays = new ArrayList<Integer>();
		this.classPhotoIdList = new ArrayList<Long>();
		this.classPhotoList = new ArrayList<ClassPhoto>();
		this.teacherIdList = new ArrayList<Long>();
		this.teacherList = new ArrayList<Teacher>();
		this.logoUrl = "";
		this.instName = "";
		this.wholeName = "";
		this.startUponArrival = 0;
		this.cutoffDate = DateUtility.getCurTimeInstance();
		this.noRefundDate = DateUtility.getCurTimeInstance();
		this.cashbackDate = DateUtility.getCurTimeInstance();
		this.bookingType = BookingType.online;
		this.originalPrice = 0;
	}	
	
	

	//default constructor, set minimal behavior and hand over to reflection for filling in content
	public Course(){
		super();
		this.creationTime = DateUtility.getCurTimeInstance();
		this.startDate = DateUtility.getCurTimeInstance();
		this.finishDate = DateUtility.getCurTimeInstance();
		this.cutoffDate = DateUtility.getCurTimeInstance();
		this.noRefundDate = DateUtility.getCurTimeInstance();
		this.cashbackDate = DateUtility.getCurTimeInstance();
		this.bookingType = BookingType.online;
		this.status = CourseStatus.openEnroll;
		this.partnerQualification = PartnerQualification.unverified;
		
		this.studyDays = new ArrayList<Integer>();

		this.classPhotoIdList = new ArrayList<Long>();
		this.classPhotoList = new ArrayList<ClassPhoto>();
		this.teacherIdList = new ArrayList<Long>();
		this.teacherList = new ArrayList<Teacher>();
		
		this.originalPrice = 0;
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

	public int getOriginalPrice() {
		return originalPrice;
	}



	public void setOriginalPrice(int originalPrice) {
		this.originalPrice = originalPrice;
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

	public String getTeachingMaterialFee() {
		return teachingMaterialFee;
	}

	public void setTeachingMaterialFee(String teachingMaterialFee) {
		this.teachingMaterialFee = teachingMaterialFee;
	}

	public ArrayList<Integer> getStudyDays() {
		return studyDays;
	}

	public void setStudyDays(ArrayList<Integer> studyDays) {
		this.studyDays = studyDays;
	}


	public ArrayList<Long> getClassPhotoIdList() {
		return classPhotoIdList;
	}

	public void setClassPhotoIdList(ArrayList<Long> classPhotoIdList) {
		this.classPhotoIdList = classPhotoIdList;
	}

	public ArrayList<ClassPhoto> getClassPhotoList() {
		return classPhotoList;
	}

	public void setClassPhotoList(ArrayList<ClassPhoto> classPhotoList) {
		this.classPhotoList = classPhotoList;
	}

	public ArrayList<Long> getTeacherIdList() {
		return teacherIdList;
	}

	public void setTeacherIdList(ArrayList<Long> teacherIdList) {
		this.teacherIdList = teacherIdList;
	}

	public ArrayList<Teacher> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(ArrayList<Teacher> teacherList) {
		this.teacherList = teacherList;
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

	public Calendar getCutoffDate() {
		return cutoffDate;
	}

	public void setCutoffDate(Calendar cutoffDate) {
		this.cutoffDate = cutoffDate;
	}

	public Calendar getNoRefundDate() {
		return noRefundDate;
	}

	public void setNoRefundDate(Calendar noRefundDate) {
		this.noRefundDate = noRefundDate;
	}

	public Calendar getCashbackDate() {
		return cashbackDate;
	}

	public void setCashbackDate(Calendar cashbackDate) {
		this.cashbackDate = cashbackDate;
	}

	public BookingType getBookingType() {
		return bookingType;
	}

	public void setBookingType(BookingType bookingType) {
		this.bookingType = bookingType;
	}

	public int getStartUponArrival() {
		return startUponArrival;
	}

	public void setStartUponArrival(int startUponArrival) {
		this.startUponArrival = startUponArrival;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	public Course deepCopy() throws IOException, ClassNotFoundException{
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(this);
        oos.close();
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Course clone = (Course) ois.readObject();
        
        return clone;
	}

	public JSONObject toJSON() throws Exception{
		return ModelReflectiveService.toJSON(this);
	}
	
	public void storeJSON(JSONObject jsonModel) throws Exception{
		ModelReflectiveService.storeJSON(this, jsonModel);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (assignments == null) {
			if (other.assignments != null)
				return false;
		} else if (!assignments.equals(other.assignments))
			return false;
		if (bonusService == null) {
			if (other.bonusService != null)
				return false;
		} else if (!bonusService.equals(other.bonusService))
			return false;
		if (bookingType != other.bookingType)
			return false;
		if (cashback != other.cashback)
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (certification == null) {
			if (other.certification != null)
				return false;
		} else if (!certification.equals(other.certification))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (classSize != other.classSize)
			return false;
		if (classTeacher == null) {
			if (other.classTeacher != null)
				return false;
		} else if (!classTeacher.equals(other.classTeacher))
			return false;
		if (courseHourLength != other.courseHourLength)
			return false;
		if (courseHourNum != other.courseHourNum)
			return false;
		if (courseId != other.courseId)
			return false;
		if (courseIntro == null) {
			if (other.courseIntro != null)
				return false;
		} else if (!courseIntro.equals(other.courseIntro))
			return false;
		if (courseName == null) {
			if (other.courseName != null)
				return false;
		} else if (!courseName.equals(other.courseName))
			return false;
		if (district == null) {
			if (other.district != null)
				return false;
		} else if (!district.equals(other.district))
			return false;
		if (downloadMaterials == null) {
			if (other.downloadMaterials != null)
				return false;
		} else if (!downloadMaterials.equals(other.downloadMaterials))
			return false;
		if (extracurricular == null) {
			if (other.extracurricular != null)
				return false;
		} else if (!extracurricular.equals(other.extracurricular))
			return false;
		if (finishTime1 != other.finishTime1)
			return false;
		if (finishTime2 != other.finishTime2)
			return false;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!goal.equals(other.goal))
			return false;
		if (highScoreReward == null) {
			if (other.highScoreReward != null)
				return false;
		} else if (!highScoreReward.equals(other.highScoreReward))
			return false;
		if (instName == null) {
			if (other.instName != null)
				return false;
		} else if (!instName.equals(other.instName))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (logoUrl == null) {
			if (other.logoUrl != null)
				return false;
		} else if (!logoUrl.equals(other.logoUrl))
			return false;
		if (marking == null) {
			if (other.marking != null)
				return false;
		} else if (!marking.equals(other.marking))
			return false;
		if (openCourseRequirement == null) {
			if (other.openCourseRequirement != null)
				return false;
		} else if (!openCourseRequirement.equals(other.openCourseRequirement))
			return false;
		if (originalPrice != other.originalPrice)
			return false;
		if (outline == null) {
			if (other.outline != null)
				return false;
		} else if (!outline.equals(other.outline))
			return false;
		if (partnerCourseReference == null) {
			if (other.partnerCourseReference != null)
				return false;
		} else if (!partnerCourseReference.equals(other.partnerCourseReference))
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
		if (passAgreement == null) {
			if (other.passAgreement != null)
				return false;
		} else if (!passAgreement.equals(other.passAgreement))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (popularity != other.popularity)
			return false;
		if (prerequest == null) {
			if (other.prerequest != null)
				return false;
		} else if (!prerequest.equals(other.prerequest))
			return false;
		if (price != other.price)
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (questionBank == null) {
			if (other.questionBank != null)
				return false;
		} else if (!questionBank.equals(other.questionBank))
			return false;
		if (questionSession == null) {
			if (other.questionSession != null)
				return false;
		} else if (!questionSession.equals(other.questionSession))
			return false;
		if (quiz == null) {
			if (other.quiz != null)
				return false;
		} else if (!quiz.equals(other.quiz))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (startTime1 != other.startTime1)
			return false;
		if (startTime2 != other.startTime2)
			return false;
		if (startUponArrival != other.startUponArrival)
			return false;
		if (status != other.status)
			return false;
		if (studyDaysNote == null) {
			if (other.studyDaysNote != null)
				return false;
		} else if (!studyDaysNote.equals(other.studyDaysNote))
			return false;
		if (subCategory == null) {
			if (other.subCategory != null)
				return false;
		} else if (!subCategory.equals(other.subCategory))
			return false;
		if (subSubCategory == null) {
			if (other.subSubCategory != null)
				return false;
		} else if (!subSubCategory.equals(other.subSubCategory))
			return false;
		if (suitableStudent == null) {
			if (other.suitableStudent != null)
				return false;
		} else if (!suitableStudent.equals(other.suitableStudent))
			return false;
		if (teachingAndExercise == null) {
			if (other.teachingAndExercise != null)
				return false;
		} else if (!teachingAndExercise.equals(other.teachingAndExercise))
			return false;
		if (teachingMaterialFee == null) {
			if (other.teachingMaterialFee != null)
				return false;
		} else if (!teachingMaterialFee.equals(other.teachingMaterialFee))
			return false;
		if (teachingMaterialIntro == null) {
			if (other.teachingMaterialIntro != null)
				return false;
		} else if (!teachingMaterialIntro.equals(other.teachingMaterialIntro))
			return false;
		if (trail == null) {
			if (other.trail != null)
				return false;
		} else if (!trail.equals(other.trail))
			return false;
		if (wholeName == null) {
			if (other.wholeName != null)
				return false;
		} else if (!wholeName.equals(other.wholeName))
			return false;
		return true;
	}

}
