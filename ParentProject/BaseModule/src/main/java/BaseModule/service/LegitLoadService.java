package BaseModule.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.Privilege;
import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.eduDAO.ClassPhotoDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.TeacherDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.generator.ReferenceGenerator;
import BaseModule.model.AdminAccount;
import BaseModule.model.ClassPhoto;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.Teacher;
import BaseModule.staticDataService.SystemDataInit;

public class LegitLoadService {
	

	public static void legitLoad(){
		EduDaoBasic.clearAllDatabase();
		SystemDataInit.init();
		Connection conn = EduDaoBasic.getConnection();
		try{
			loadLegit(conn);//1a 1p 1c

		} finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}

		DebugLog.d("Legit Models loaded successfully");
	}
	
	
	private static void loadLegit(Connection... connections){
		
		try {
			String name = "管理员";
			String phone = "DONOTSEND1234567890" ;
			String reference = "AAAAAAAA";
			Privilege privilege = Privilege.fromInt(0);
			AccountStatus status = AccountStatus.activated;
			String password = "aaaaaaaa";
			AdminAccount account = new AdminAccount(name,phone,reference,privilege,status,password);
			AdminAccountDaoService.createAdminAccount(account);
		} catch (SQLException | PseudoException e) {		
			e.printStackTrace();
		}
		
		
		try {
			String name = "南京ABC教育有限公司";
			String instName = "南京ABC教育";
			String licence = "123456789";
			String organizationNum = "00023";
			String reference = "AAAAAAAA";
			String password = "aaaaaaaa";				
			AccountStatus status = AccountStatus.activated;
			Partner partner = new Partner(name, instName,licence, organizationNum,reference, password,status);
			partner.setLogoUrl("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/lg.jpg");				
			partner.setPartnerIntro("南京ABC有20年专注教育培训,累积培训学员超过1600万,从早教到成人,业务涵盖早教、学前、小学、中学、四六级、考研、出国考试、留学咨询、英语能力提升、国际游学、国际教育、图书、网络课堂等50多所学校");
			partner.setPartnerDistinction("");
			
			Map<String, String> kvps = new HashMap<String, String>();
			kvps.put("licenceImgUrl", "http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/teacherImgUrl-3-123124123.jpg");
			kvps.put("taxRegistrationImgUrl", "http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/teacherImgUrl-3-123124123.jpg");
			kvps.put("eduQualificationImgUrl", "http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/teacherImgUrl-3-123124123.jpg");
			kvps.put("hqLocation", "南京市雨花台区宁双路28号");
			kvps.put("subLocations1", "南京市雨花台区宁双路29号");
			kvps.put("subLocations2", "南京市雨花台区宁双路30号");
			kvps.put("uniformRegistraLocation", "true");
			kvps.put("hqContact", "蒋先生");
			kvps.put("hqContactPhone", "15156924587");
			kvps.put("hqContactSecOpt", "");
			kvps.put("courseContact", "蒋先生");
			kvps.put("courseContactPhone", "15156924587");
			kvps.put("studentInqueryPhone", "15156924587");
			kvps.put("registraContact", "蒋先生");
			kvps.put("registraContactPhone", "15156924587");
			kvps.put("registraContactFax", "");
			kvps.put("defaultCutoffDay", "3");
			kvps.put("defaultCutoffTime", "1730");
			kvps.put("partnerQualification", "1");
			Partner resultPartner = PartnerDaoService.createPartner(partner, connections);
			

			//define all the teachers
			int teacherCount = 2;
			ArrayList<String> teacherIntros = new ArrayList<String>();
			teacherIntros.add("外边大学本科毕业，韩国西湖大学教育学硕士毕业，目前在盐城师范学院外国语学院工作，任副教授，同时担任三多市委、市政府对韩外宾同声翻译专家。");
			teacherIntros.add("外边大学本科毕业，韩国湖三大学教育学硕士毕业，目前在盐城师范学院外国语学院工作，任副教授，同时担任三多市委、市政府对韩外宾同声翻译专家。");
			ArrayList<String> teacherNames = new ArrayList<String>();
			teacherNames.add("李洪爱");
			teacherNames.add("艾轮");
			ArrayList<String> teacherImgs = new ArrayList<String>();
			teacherImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/t1.png");
			teacherImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/t2.png");
			// upload Teachers
			ArrayList<Long> teacherIdList = new ArrayList<Long>();
			ArrayList<Teacher> teacherList = new ArrayList<Teacher>();
			for(int j = 0; j < teacherCount; j++){			
				Teacher teacher = new Teacher(resultPartner.getPartnerId(), teacherImgs.get(j), teacherNames.get(j), teacherIntros.get(j));		
				ArrayList<Teacher> tlist = new ArrayList<Teacher>();
				tlist.add(teacher);
				try {
					tlist = TeacherDao.addTeachersToDataBases(tlist, connections);
				} catch (SQLException e) {				
					e.printStackTrace();
					DebugLog.d(e);
				}
				teacherList.add(teacher);
				teacherIdList.add(teacher.getTeacherId());
			}
			
			//define all the class photos
			int classPhotoCount = 4;
			ArrayList<String> classPhotoImgs = new ArrayList<String>();
			classPhotoImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/c1.jpg");
			classPhotoImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/c2.jpg");
			classPhotoImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/c3.jpg");
			classPhotoImgs.add("http://testimgsbucket.oss-cn-hangzhou.aliyuncs.com/c4.jpg");
			ArrayList<String> classPhotoTitles = new ArrayList<String>();
			classPhotoTitles.add("示例图片1");
			classPhotoTitles.add("示例图片2");
			classPhotoTitles.add("示例图片3");
			classPhotoTitles.add("示例图片4");
			ArrayList<String> classPhotoDescriptions = new ArrayList<String>();
			classPhotoDescriptions.add("教室图片介绍");
			classPhotoDescriptions.add("教室图片介绍");
			classPhotoDescriptions.add("教室图片介绍");
			classPhotoDescriptions.add("教室图片介绍");
			ArrayList<Long> classIdList = new ArrayList<Long>();
			ArrayList<ClassPhoto> classPhotos = new ArrayList<ClassPhoto>();
			// upload ClassPhotos
			for(int j = 0; j < classPhotoCount; j++){
				ClassPhoto classPhoto = new ClassPhoto(resultPartner.getPartnerId(), classPhotoImgs.get(j), classPhotoTitles.get(j), classPhotoDescriptions.get(j));
				ArrayList<ClassPhoto> clist = new ArrayList<ClassPhoto>();
				clist.add(classPhoto);
				try {
					clist = ClassPhotoDao.addClassPhotosToDataBases(clist, connections);
				} catch (SQLException e) {				
					e.printStackTrace();
					DebugLog.d(e);
				}
				classIdList.add(classPhoto.getClassPhotoId());
				classPhotos.add(classPhoto);
			}
			
		} catch (SQLException e) {				
			e.printStackTrace();
		} catch (PseudoException e) {
			e.printStackTrace();
		}
		
		
		
		
		for (int i = 0; i < 7; i++){
			Calendar cutoffDate = null;
			Calendar startDate = null;
			Calendar finishDate = null;
			try {
				cutoffDate = DateUtility.castFromAPIFormat("2014-06-30 17:30:00");
				startDate = DateUtility.castFromAPIFormat("2014-07-02 00:00:00");
				finishDate = DateUtility.castFromAPIFormat("2014-07-22 00:00:00");
			} catch (ValidationException e) {
				e.printStackTrace();
			}
			String title = "";
			String cat = "";
			String subCat = "";
			String subSubCat = "";
			String sST = "";
			int base = 0;
			int courseHourNum = 0;
			
			if (i == 0){
				 title = "新概念第四册精讲班";
				 cat = "语言培训";
				 subCat = "英语";
				 subSubCat = "新概念英语";
				 sST = "顺利完成新概念第三册的学员";
				 base = 1000;
				 courseHourNum = 15;
			}
			if (i == 1){
				 title = "六级提高英语保过班";
				 cat = "语言培训";
				 subCat = "英语";
				 subSubCat = "四六级";
				 sST = "备考六级,词汇基础比较薄弱";
				 base = 500;
				 courseHourNum = 20;
			}
			else if (i == 2){
				 title = "托福保80冲100精品班";
				 cat = "语言培训";
				 subCat = "英语";
				 subSubCat = "托福";
				 sST = "计划三个月内参加托福考试的同学";
				 base = 5000;
				 courseHourNum = 18;
			}
			else if (i == 3){
				 title = "韩语入门基础班";
				 cat = "语言培训";
				 subCat = "小语种";
				 subSubCat = "韩语";
				 sST = "对学习韩语有兴趣且缺乏韩语基础的同学";
				 base = 4000;
				 courseHourNum = 12;
			}
			else if (i == 4){
				 title = "公务员试题攻关培训";
				 cat = "资格认证";
				 subCat = "国家公职";
				 subSubCat = "公务员";
				 sST = "希望提高公务员考试成绩且有一定基础的学生";
				 base = 2000;
				 courseHourNum = 15;
			}
			else if (i == 5){
				 title = "日语二级专业课";
				 cat = "语言培训";
				 subCat = "小语种";
				 subSubCat = "日语";
				 sST = "已经通过日语三级考试，对日语很有兴趣";
				 base = 8000;
				 courseHourNum = 20;
			}
			else if (i == 6){
				 title = "雅思6分基础班";
				 cat = "语言培训";
				 subCat = "英语";
				 subSubCat = "雅思";
				 sST = "准备初次参加雅思考试，仍需提高英语基础的学员";
				 base = 6500;
				 courseHourNum = 15;
			}
			
			int price = base + new Random().nextInt(200)*10;
			
			for (int j = 0; j < 3; j++){
				cutoffDate.add(Calendar.MONTH, 1);
				startDate.add(Calendar.MONTH, 1);
				finishDate.add(Calendar.MONTH, 1);
				
				String outline = "第一讲 \n   1、口语Transport \n   2、听力专项与训练 \n   3、精读文章 \n   4、课后美文欣赏 \n 第二讲 \n   1、口语Different shop";
				String goal = " 1.对初一英语知识点达到灵活运用层次，夯实初一知识 \n 2.对初二秋季知识点达到识记和理解层次，在新学期中占得先机 ";
				
				ArrayList<Integer> studyDays = new ArrayList<Integer>();
				studyDays.add(1);
				studyDays.add(3);
				studyDays.add(5);
				studyDays.add(6);
				studyDays.add(7);
				
				String location = "南京市雨花台区宁双路28号";

				int classSize = 25;
				int popularity = 1;
				int p_Id = 1;
				
				Partner partner = null;
				try {
					partner = PartnerDaoService.getPartnerById(p_Id);
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
				ArrayList<Long> classPhotoIdList = new ArrayList<Long>();
				ArrayList<Long> teacherIdList = new ArrayList<Long>();
				for (ClassPhoto classPhoto : partner.getClassPhotoList()){
					classPhotoIdList.add(classPhoto.getClassPhotoId());
				}
				for (Teacher teacher : partner.getTeacherList()){
					teacherIdList.add(teacher.getTeacherId());
				}
				String province = "江苏";
				String city = "南京";
				String district = "雨花";
				String category = cat;
				String subCategory = subCat;
				String subSubCategory = subSubCat;
					
				Course course = new Course(p_Id, startDate, finishDate,price,classSize,popularity,category,subCategory);
				course.setSubSubCategory(subSubCategory);
				course.setProvince(province);
				course.setCity(city);
				course.setDistrict(district);
				course.setLocation(location);
				course.setGoal(goal);
				course.setOutline(outline);
				course.setTeacherIdList(teacherIdList);
				course.setClassPhotoIdList(classPhotoIdList);
				course.setCashback(course.getPrice()/10);			
				course.setStudyDays(studyDays);			
				course.setClassTeacher("班主任为学生制定学习计划，每周与家长沟通");
				course.setQuestionSession("支持课后多途径教学相关任何疑问解答");			
				course.setTeachingAndExercise("寄语特定教学目标，进行有针对的训练及讲解");
				course.setTrail("");
				course.setBonusService("免费获得教材一份，单次速记手册");
				course.setPrerequest("基础英语语法");
				course.setAssignments("老师布置课后习题，巩固教学内容");
				course.setDownloadMaterials("提供上课课件下载机拷贝，便于课后学习");
				course.setHighScoreReward("报考档期雅思考试且高于8分的学员，快速提升教学成效");
				course.setCourseName(title);
				course.setSuitableStudent(sST);
				course.setExtracurricular("支持QQ学习群，支持每周英语角");
				course.setMarking("专业老师批改作业，纠正错误理解，快速提升教学成效");
				course.setQuiz("通过阶段性测试，便于学员清楚了解自我学习状态");
				course.setOpenCourseRequirement("至少10人");
				course.setTeachingMaterialIntro("自编教材；新东方内训特编");
				course.setCourseIntro("快速提供学员英语六级考试成绩");
				course.setStudyDaysNote("");
				course.setTeachingMaterialFee("免费");
				course.setCertification("结业后，可获得技能证书");
				course.setPassAgreement("入学签约，不过免费重修");
				course.setCourseHourNum(courseHourNum);
				course.setCourseHourLength(60);
				course.setCutoffDate(cutoffDate);
				course.setRegistraLocation(location);
				course.setStartTime1(1900);
				course.setFinishTime1(2010);
				course.setTeachingMethod("分组学习；分组讨论");
				course.setQuestionBank("历年真题+模拟题+预测题+全真机考");
				course.setQualityAssurance("老师不定期跟进学员进度，灵活调整课程计划");
				course.setContact("李洪爱");
				course.setRegistraPhone("18685348619");
				
				try {
					course.setReference(ReferenceGenerator.generateCourseReference());
					CourseDao.addCourseToDatabases(course);			
				} catch (SQLException | PseudoException e) {	
					DebugLog.d(e);
				} 
			}
		}
	}

}
