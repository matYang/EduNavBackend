package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import org.junit.Test;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.eduDAO.ClassPhotoDao;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.eduDAO.TeacherDao;
import BaseModule.exception.PseudoException;
import BaseModule.generator.ReferenceGenerator;
import BaseModule.model.ClassPhoto;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.Teacher;
import BaseModule.model.representation.CourseSearchRepresentation;

public class CourseDaoTest {

	@Test
	public void testCreate() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();	
		String name = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = ReferenceGenerator.generateCourseReference();
		String password = "sdf234r";
		String phone = "123545451";	
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name,instName, licence, organizationNum,reference, password, phone,status);		
		partner = PartnerDao.addPartnerToDatabases(partner);
		
		
		int p_Id = partner.getPartnerId();
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal = 50;
		int seatsLeft = 5;
		int price = 1000;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		String phone2 = "12344565654";
		
		ArrayList<String> teacherIntros = new ArrayList<String>();
		teacherIntros.add("尊敬的各位考官、各位评委老师： 通过考试，今天，我以本岗位笔试第一的成大神大神绩进入DSA了面试。对我来说，这次机会显得尤为珍贵。我叫陈日安，今年21岁。浙江工业职业技术");
		teacherIntros.add("XXX，女，1980年7月出生。1999年7月参加教育工作。小学一级教师。1999年至20大神01盛大 年6月在仓埠街丛林小学任教。在两年任教期间，本人吃苦耐劳，勇于挑重担。先后在");
		teacherIntros.add("李开柱，男，1967年1月出生，中共党员，毕业于四川师范大学外语系，英语副教授,基础的撒打算部副主任，公共英语教研室主任。长期从事英语教学，先后承担了《大学英语》、《财经");
		teacherIntros.add("李开柱，男，1967年1月出生，中共党员，毕业于四川师范大学外语系，英语副大神大神教授,基础部副主任，公共英语教研室主任。长期从事英语教学，先后承担了《大学英语》李开柱");
		
		ArrayList<String> teacherNames = new ArrayList<String>();
		teacherNames.add("苍老师");
		teacherNames.add("陈老师");
		teacherNames.add("谢老师");
		teacherNames.add("谢老师");
		
		ArrayList<String> teacherImgs = new ArrayList<String>();
		teacherImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUy");
		teacherImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUy");
		teacherImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUyNT");
		teacherImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUyNT");
		
		ArrayList<String> classrommImgs = new ArrayList<String>();
		classrommImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUy");
		classrommImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUy");
		classrommImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUyNT");
		classrommImgs.add("http://oss.console.aliyun.com/console/index#bW9kdWxlVXJsPWh0dHAlMjUzQSUyNTJGJTI1MkYlMjU3QndlYl9zZXJ2ZXIlMjU3RCUyNT");
		
		ArrayList<Integer> studyDays = new ArrayList<Integer>();
		studyDays.add(1);
		studyDays.add(3);
		studyDays.add(5);
		studyDays.add(6);
		studyDays.add(7);
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,category,subCategory,phone);
		ArrayList<Long> tlist = new ArrayList<Long>();
		ArrayList<Long> cplist = new ArrayList<Long>();
		Teacher teacher = new Teacher(p_Id, "teacherImgUrl", "teacherName","teacherIntro");	
		teacher = TeacherDao.addTeacherToDataBases(teacher);
		tlist.add(teacher.getTeacherId());
		ClassPhoto classPhoto = new ClassPhoto(p_Id, "classImgUrl", "classPhoto","classDescription");
		classPhoto = ClassPhotoDao.addClassPhotoToDataBases(classPhoto);
		cplist.add(classPhoto.getClassPhotoId());
		course.setClassPhotoIdList(cplist);
		course.setTeacherIdList(tlist);
		course.setOutline("sdf");
		course.setGoal("sdfdsf");
		course.setStudyDays(studyDays);
		CourseDao.addCourseToDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
	}
	
	@Test
	public void testGetAndUpdate() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();		
		String name = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = ReferenceGenerator.generateCourseReference();
		String password = "sdf234r";
		String phone = "123545451";	
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name,instName, licence, organizationNum,reference, password, phone,status);
		
		partner = PartnerDao.addPartnerToDatabases(partner);
		int p_Id = partner.getPartnerId();
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal = 50;
		int seatsLeft = 5;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		int price = 1000;				
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal, seatsLeft,category,subCategory,phone);
		ArrayList<Long> tlist = new ArrayList<Long>();
		ArrayList<Long> cplist = new ArrayList<Long>();
		Teacher teacher = new Teacher(p_Id, "teacherImgUrl", "teacherName","teacherIntro");	
		teacher = TeacherDao.addTeacherToDataBases(teacher);
		tlist.add(teacher.getTeacherId());
		ClassPhoto classPhoto = new ClassPhoto(p_Id, "classImgUrl", "classPhoto","classDescription");
		classPhoto = ClassPhotoDao.addClassPhotoToDataBases(classPhoto);
		cplist.add(classPhoto.getClassPhotoId());
		String location = "China";
		String city = "NanJing";
		String district = "JiangNing";
		String reference2 = "testr";
		course.setLocation(location);
		course.setCity(city);
		course.setDistrict(district);
		course.setReference(reference2);
		course.setClassPhotoIdList(cplist);
		course.setTeacherIdList(tlist);
		course.setOutline("sdf");
		course.setGoal("sdfdsf");
		CourseDao.addCourseToDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());		
		course.setTeachingMaterialIntro("Hand and Ass");
		course.setPrice(1111);
		CourseDao.updateCourseInDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
		
		
		if(course.getInstName().equals(partner.getInstName())&&course.getCategory().equals(category)&&course.getSubCategory().equals(subCategory)){
			//Passed;
		}else fail();
		
		Calendar startTime2 = DateUtility.getCurTimeInstance();
		Calendar finishTime2 = DateUtility.getCurTimeInstance();
		finishTime2.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal2 = 50;
		int seatsLeft2 = 5;
		String category2 = "Physics";
		String subCategory2 = "sub-Chin";			
		Course course2 = new Course(p_Id, startTime2, finishTime2,price,seatsTotal2, seatsLeft2,category2,subCategory2,phone);
		course2.setClassPhotoIdList(cplist);
		course2.setTeacherIdList(tlist);
		course2.setOutline("sdf");
		course2.setGoal("sdfdsf");
		CourseDao.addCourseToDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());		
		course2.setLocation(location);
		course2.setCity(city);
		course2.setDistrict(district);
		course2.setReference(ReferenceGenerator.generateCourseReference());	
		course2.setTeachingMaterialIntro("Hand and Ass");
		course2.setPrice(2222);
		CourseDao.updateCourseInDatabases(course2);
		
		Course test = CourseDao.getCourseById(course.getCourseId());
		if(test.equals(course)){
			//Passed;
		}else fail();
		
		test = CourseDao.getCourseById(course2.getCourseId());
		if(test.equals(course2)){
			//Passed;
		}else fail();
		
		ArrayList<Course> clist = new ArrayList<Course>();
		CourseSearchRepresentation c_sr = new CourseSearchRepresentation();
		c_sr.setPartnerId(partner.getPartnerId());
		clist = CourseDao.searchCourse(c_sr);
		if(clist.size()==2&&clist.get(0).equals(course)&&clist.get(1).equals(course2)){
			//Passed;
		}else fail();		
		
		course2.setMarking("true");
		CourseDao.updateCourseInDatabases(course2);
		test = CourseDao.getCourseById(course2.getCourseId());
		
		if(test.getMarking().equals("true")){
			//Passed;
		}else fail();
		
		ArrayList<Integer> idList = new ArrayList<Integer>();
		idList.add(course.getCourseId());
		idList.add(course2.getCourseId());
		clist = CourseDao.getCourseByIdList(idList);
		if(clist.size()==2&&clist.get(0).equals(course)&&clist.get(1).equals(course2)){
			//Passed;
		}else fail();
		
	}	
	
	@Test
	public void testSearch() throws SQLException, PseudoException{
		EduDaoBasic.clearAllDatabase();		
		/* Partner part */
		String name = "partne1";
		String instName = "instName1";
		String licence = "licence1";
		String organizationNum = "organizationNum1";
		String reference = ReferenceGenerator.generatePartnerReference();
		String password = "password1";
		String phone = "phone1";	
		AccountStatus status = AccountStatus.activated;
		Partner partner = new Partner(name,instName, licence, organizationNum,reference, password, phone,status);		
		partner = PartnerDao.addPartnerToDatabases(partner);
		int p_Id = partner.getPartnerId();
		
		String name2 = "partne2";
		String instName2 = "instName2";
		String licence2 = "licence2";
		String organizationNum2 = "organizationNum2";
		String reference2 = ReferenceGenerator.generatePartnerReference();
		String password2 = "password2";
		String phone2 = "phone2";	
		AccountStatus status2 = AccountStatus.activated;
		Partner partner2 = new Partner(name2,instName2, licence2, organizationNum2,reference2, password2, phone2,status2);		
		partner2 = PartnerDao.addPartnerToDatabases(partner2);
		int p_Id2 = partner2.getPartnerId();
		
		String name3 = "partne3";
		String instName3 = "instName3";
		String licence3 = "licence3";
		String organizationNum3 = "organizationNum3";
		String reference3 = ReferenceGenerator.generatePartnerReference();
		String password3 = "password3";
		String phone3 = "phone3";	
		AccountStatus status3 = AccountStatus.activated;
		Partner partner3 = new Partner(name3,instName3, licence3, organizationNum3,reference3, password3, phone3,status3);		
		partner3 = PartnerDao.addPartnerToDatabases(partner3);
		int p_Id3 = partner3.getPartnerId();
		
		/* Course part */
		ArrayList<Long> tlist = new ArrayList<Long>();
		ArrayList<Long> cplist = new ArrayList<Long>();
		Teacher teacher = new Teacher(p_Id, "teacherImgUrl", "teacherName","teacherIntro");	
		teacher = TeacherDao.addTeacherToDataBases(teacher);
		tlist.add(teacher.getTeacherId());
		ClassPhoto classPhoto = new ClassPhoto(p_Id, "classImgUrl", "classPhoto","classDescription");
		classPhoto = ClassPhotoDao.addClassPhotoToDataBases(classPhoto);
		cplist.add(classPhoto.getClassPhotoId());
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int classSize = 0;
		int popularity = 5;
		String category1 = "category1";
		String subCategory1 = "subcategory1";		
		int price1 = 12000;		
		String location1 = "location1";
		String city1 = "city1";
		String district1 = "district1";
		String coursereference1 = ReferenceGenerator.generateCourseReference();	
		Course course = new Course(p_Id, startTime, finishTime,price1,classSize, popularity,category1,subCategory1,phone);
		course.setLocation(location1);
		course.setCity(city1);
		course.setDistrict(district1);
		course.setReference(coursereference1);
		course.setTeachingMaterialIntro("teachingMaterial1");
		course.setCourseName("title1");		
		course.setClassSize(5);
		course.setClassPhotoIdList(cplist);
		course.setTeacherIdList(tlist);
		course.setOutline("sdf");
		course.setGoal("sdfdsf");
		course = CourseDao.addCourseToDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
		
		Calendar startTime2 = DateUtility.getCurTimeInstance();
		Calendar finishTime2 = DateUtility.getCurTimeInstance();
		finishTime2.add(Calendar.DAY_OF_YEAR, 7);				
		String category2 = "category2";
		String subCategory2 = "subcategory2";		
		int price2 = 2000;		
		String location2 = "location2";
		String city2 = "city2";
		String district2 = "district2";
		String coursereference2 = ReferenceGenerator.generateCourseReference();			
		Course course2 = new Course(p_Id2, startTime2, finishTime2,price2,classSize, popularity,category2,subCategory2,phone);
		course2.setLocation(location2);
		course2.setCity(city2);
		course2.setDistrict(district2);
		course2.setReference(coursereference2);
		course2.setTeachingMaterialIntro("teachingMaterial2");
		course2.setCourseName("title2");	
		course2.setClassSize(5);
		course2.setClassPhotoIdList(cplist);
		course2.setTeacherIdList(tlist);
		course2.setOutline("sdf");
		course2.setGoal("sdfdsf");
		course2 = CourseDao.addCourseToDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());
		
		Calendar startTime21 = DateUtility.getCurTimeInstance();
		Calendar finishTime21 = DateUtility.getCurTimeInstance();
		finishTime21.add(Calendar.DAY_OF_YEAR, 14);				
		String category21 = "category2";
		String subCategory21 = "subcategory1";		
		int price21 = 5000;		
		String location21 = "location2";
		String city21 = "city2";
		String district21 = "district2";
		String coursereference21 = ReferenceGenerator.generateCourseReference();				
		Course course21 = new Course(p_Id2, startTime21, finishTime21,price21,classSize, popularity,category21,subCategory21,phone);
		course21.setLocation(location21);
		course21.setCity(city21);
		course21.setDistrict(district21);
		course21.setReference(coursereference21);
		course21.setTeachingMaterialIntro("teachingMaterial21");
		course21.setCourseName("title21");		
		course21.setClassPhotoIdList(cplist);
		course21.setTeacherIdList(tlist);
		course21.setOutline("sdf");
		course21.setGoal("sdfdsf");
		course21 = CourseDao.addCourseToDatabases(course21);
		course21 = CourseDao.getCourseById(course21.getCourseId());
		
		Calendar startTime3 = DateUtility.getCurTimeInstance();
		Calendar finishTime3 = DateUtility.getCurTimeInstance();
		finishTime3.add(Calendar.DAY_OF_YEAR, 17);				
		String category3 = "category3";
		String subCategory3 = "subcategory0";		
		int price3 = 20000;		
		String location3 = "location3";
		String city3 = "city0";
		String district3 = "district0";
		String coursereference3 = ReferenceGenerator.generateCourseReference();		
		Course course3 = new Course(p_Id3, startTime3, finishTime3,price3,classSize, popularity,category3,subCategory3,phone);
		course3.setLocation(location3);
		course3.setCity(city3);
		course3.setDistrict(district3);
		course3.setReference(coursereference3);
		course3.setTeachingMaterialIntro("teachingMaterial3");
		course3.setCourseName("title3");	
		course3.setClassPhotoIdList(cplist);
		course3.setTeacherIdList(tlist);
		course3.setOutline("sdf");
		course3.setGoal("sdfdsf");
		course3 = CourseDao.addCourseToDatabases(course3);
		course3 = CourseDao.getCourseById(course3.getCourseId());
		
		Calendar startTime31 = DateUtility.getCurTimeInstance();
		Calendar finishTime31 = DateUtility.getCurTimeInstance();
		finishTime31.add(Calendar.DAY_OF_YEAR, 20);				
		String category31 = "category3";
		String subCategory31 = "subcategory1";		
		int price31 = 15000;		
		String location31 = "location3";
		String city31 = "city1";
		String district31 = "district1";
		String coursereference31 = ReferenceGenerator.generateCourseReference();
		Course course31 = new Course(p_Id3, startTime31, finishTime31,price31,classSize, popularity,category31,subCategory31,phone);
		course31.setLocation(location31);
		course31.setCity(city31);
		course31.setDistrict(district31);
		course31.setReference(coursereference31);
		course31.setTeachingMaterialIntro("teachingMaterial31");
		course31.setCourseName("title31");
		course31.setClassPhotoIdList(cplist);
		course31.setTeacherIdList(tlist);
		course31.setOutline("sdf");
		course31.setGoal("sdfdsf");
		course31 = CourseDao.addCourseToDatabases(course31);
		course31 = CourseDao.getCourseById(course31.getCourseId());
		
		Calendar startTime32 = DateUtility.getCurTimeInstance();
		Calendar finishTime32 = DateUtility.getCurTimeInstance();
		finishTime32.add(Calendar.DAY_OF_YEAR, 30);				
		String category32 = "category3";
		String subCategory32 = "subcategory2";		
		int price32 = 30000;		
		String location32 = "location3";
		String city32 = "city2";
		String district32 = "district2";
		String coursereference32 = ReferenceGenerator.generateCourseReference();		
		Course course32 = new Course(p_Id3, startTime32, finishTime32,price32,classSize, popularity,category32,subCategory32,phone);
		course32.setLocation(location32);
		course32.setCity(city32);
		course32.setDistrict(district32);
		course32.setReference(coursereference32);
		course32.setTeachingMaterialIntro("teachingMaterial32");
		course32.setCourseName("title32");	
		course32.setClassPhotoIdList(cplist);
		course32.setTeacherIdList(tlist);
		course32.setOutline("sdf");
		course32.setGoal("sdfdsf");
		course32 = CourseDao.addCourseToDatabases(course32);
		course32 = CourseDao.getCourseById(course32.getCourseId());
		
		/* Search part */
		ArrayList<Course> clist = new ArrayList<Course>();
		Calendar startTimeTest = DateUtility.getCurTimeInstance();
		Calendar finishTimeTest = DateUtility.getCurTimeInstance();	
		
		CourseSearchRepresentation sr1 = new CourseSearchRepresentation();
		sr1.setCategory("category3");
		sr1.setStartPrice(0);
		sr1.setFinishPrice(40000);
		clist = CourseDao.searchCourse(sr1);
		if(clist.size()==3 && clist.get(0).equals(course3) && clist.get(1).equals(course31) && 
				clist.get(2).equals(course32)){
			//Passed;
		}else fail();
		
		CourseSearchRepresentation sr2 = new CourseSearchRepresentation();
		sr2.setStartPrice(0);
		sr2.setFinishPrice(40000);
		sr2.setDistrict("district2");
		sr2.setCity("city2");
		sr2.setInstitutionName(partner3.getInstName());
		sr2.setPartnerId(p_Id3);
		sr2.setPartnerReference(partner3.getReference());
		clist = CourseDao.searchCourse(sr2);
		if(clist.size()==1 && clist.get(0).equals(course32)){
			//Passed;
		}else fail();
		
		sr2.setPartnerId(-1);
		clist = CourseDao.searchCourse(sr2);
		if(clist.size()==1 && clist.get(0).equals(course32)){
			//Passed;
		}else fail();
		
		sr2.setPartnerReference(null);
		clist = CourseDao.searchCourse(sr2);
		if(clist.size()==1 && clist.get(0).equals(course32)){
			//Passed;
		}else fail();
		
		sr2.setInstitutionName(null);
		clist = CourseDao.searchCourse(sr2);
		if(clist.size()==3 && clist.get(0).equals(course2) && clist.get(1).equals(course21) && 
				clist.get(2).equals(course32)){
			//Passed;
		}else fail();
		
		CourseSearchRepresentation sr3 = new CourseSearchRepresentation();
		sr3.setStartPrice(2000);
		sr3.setFinishPrice(10000);
		clist = CourseDao.searchCourse(sr3);
		if(clist.size()==2 && clist.get(0).equals(course2) && clist.get(1).equals(course21)){
			//Passed;
		}else fail();
		
		finishTimeTest.add(Calendar.DAY_OF_YEAR, 8);
		sr3.setFinishDate(finishTimeTest);
		clist = CourseDao.searchCourse(sr3);
		if(clist.size()==1 && clist.get(0).equals(course2)){
			//Passed;
		}else fail();
		
		CourseSearchRepresentation sr4 = new CourseSearchRepresentation();
		sr4.setStartPrice(0);
		sr4.setFinishPrice(40000);
		sr4.setCourseReference(coursereference1);
		clist = CourseDao.searchCourse(sr4);
		if(clist.size()==1 && clist.get(0).equals(course)){
			//Passed;
		}else fail();
		
		CourseSearchRepresentation sr5 = new CourseSearchRepresentation();
		startTimeTest.add(Calendar.DAY_OF_YEAR, -1);
		sr5.setStartDate(startTimeTest);
		sr5.setStartPrice(0);
		sr5.setFinishPrice(40000);
		clist = CourseDao.searchCourse(sr5);
		if(clist.size()==6 && clist.get(0).equals(course) && clist.get(1).equals(course2) && 
				clist.get(2).equals(course21) && clist.get(3).equals(course3) && 
				clist.get(4).equals(course31) && clist.get(5).equals(course32)){
			//Passed;
		}else fail();
		
		finishTimeTest = DateUtility.getCurTimeInstance();
		finishTimeTest.add(Calendar.DAY_OF_YEAR, 15);
		sr5.setFinishDate(finishTimeTest);
		sr5.setStartClassSize(3);
		clist = CourseDao.searchCourse(sr5);
		if(clist.size()==2 && clist.get(0).equals(course) && clist.get(1).equals(course2)){
			//Passed;
		}else fail();
	}
	
}
