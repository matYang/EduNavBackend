package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.ClassModel;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.PartnerDao;
import BaseModule.exception.course.CourseNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Course;
import BaseModule.model.Partner;
import BaseModule.model.representation.CourseSearchRepresentation;

public class CourseDaoTest {

	@Test
	public void testCreate(){
		EduDaoBasic.clearAllDatabase();
		int p_Id = 1;
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal = 50;
		int seatsLeft = 5;
		int price = 1000;
		String category = "Physics";
		String subCategory = "sub-Phy";		
		String phone = "12344565654";
		AccountStatus status = AccountStatus.activated;		
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal,seatsLeft,status,category,subCategory,phone);
		try{
			CourseDao.addCourseToDatabases(course);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGetAndUpdate() throws ValidationException, CourseNotFoundException, SQLException{
		EduDaoBasic.clearAllDatabase();
		String name = "XDF";
		String instName = "xiaofeng";
		String licence = "234fdsfsdgergf-dsv,.!@";
		String organizationNum = "1235454361234";
		String reference = "dsf4r";
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
		Course course = new Course(p_Id, startTime, finishTime,price,seatsTotal, seatsLeft,status,category,subCategory,phone);
		String location = "China";
		String city = "NanJing";
		String district = "JiangNing";
		String reference2 = "testr";
		course.setLocation(location);
		course.setCity(city);
		course.setDistrict(district);
		course.setReference(reference2);
		CourseDao.addCourseToDatabases(course);
		course = CourseDao.getCourseById(course.getCourseId());
		course.setClassroomImgUrl("www.hotmail.com");
		course.setTeacherImgUrl("www.google.ca");
		course.setTeachingMethodsIntro("Hand and Ass");
		course.setCourseName("bababa");
		course.setTeacherIntro("sdfkljrghiuoghrer");
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
		Course course2 = new Course(p_Id, startTime2, finishTime2,price,seatsTotal2, seatsLeft2,status,category2,subCategory2,phone);
		CourseDao.addCourseToDatabases(course2);
		course2 = CourseDao.getCourseById(course2.getCourseId());		
		course2.setLocation(location);
		course2.setCity(city);
		course2.setDistrict(district);
		course2.setReference(reference2);
		course2.setClassroomImgUrl("www.hocom");
		course2.setTeacherImgUrl("wwwgle.ca");
		course2.setTeachingMethodsIntro("Hass");
		course2.setCourseName("baa");
		course2.setTeacherIntro("sdfklghrer");
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
		clist = CourseDao.getCoursesFromPartner(partner.getPartnerId());
		if(clist.size()==2&&clist.get(0).equals(course)&&clist.get(1).equals(course2)){
			//Passed;
		}else fail();		
		
		course2.setProvideMarking(true);
		CourseDao.updateCourseInDatabases(course2);
		test = CourseDao.getCourseById(course2.getCourseId());
		
		if(test.isProvideMarking()){
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
	public void testSearch() throws ValidationException, CourseNotFoundException, SQLException{
		EduDaoBasic.clearAllDatabase();
		/* Partner part */
		String name = "partne1";
		String instName = "instName1";
		String licence = "licence1";
		String organizationNum = "organizationNum1";
		String reference = "p-reference1";
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
		String reference2 = "p-reference2";
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
		String reference3 = "p-reference3";
		String password3 = "password3";
		String phone3 = "phone3";	
		AccountStatus status3 = AccountStatus.activated;
		Partner partner3 = new Partner(name3,instName3, licence3, organizationNum3,reference3, password3, phone3,status3);
		partner3 = PartnerDao.addPartnerToDatabases(partner3);
		int p_Id3 = partner3.getPartnerId();
		
		/* Course part */
		Calendar startTime = DateUtility.getCurTimeInstance();
		Calendar finishTime = DateUtility.getCurTimeInstance();
		finishTime.add(Calendar.DAY_OF_YEAR, 1);		
		int seatsTotal = 50;
		int seatsLeft = 5;
		String category1 = "category1";
		String subCategory1 = "subcategory1";		
		int price1 = 12000;		
		String location1 = "location1";
		String city1 = "city1";
		String district1 = "district1";
		String coursereference1 = "course-reference1";	
		Course course = new Course(p_Id, startTime, finishTime,price1,seatsTotal, seatsLeft,status,category1,subCategory1,phone);
		course.setLocation(location1);
		course.setCity(city1);
		course.setDistrict(district1);
		course.setReference(coursereference1);
		course.setTeachingMethodsIntro("teachingMaterial1");
		course.setCourseName("title1");
		course.setTeacherIntro("teachingInfo1");
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
		String coursereference2 = "course-reference2";			
		Course course2 = new Course(p_Id2, startTime2, finishTime2,price2,seatsTotal, seatsLeft,status,category2,subCategory2,phone);
		course2.setLocation(location2);
		course2.setCity(city2);
		course2.setDistrict(district2);
		course2.setReference(coursereference2);
		course2.setTeachingMethodsIntro("teachingMaterial2");
		course2.setCourseName("title2");
		course2.setTeacherIntro("teachingInfo2");
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
		String coursereference21 = "course-reference21";				
		Course course21 = new Course(p_Id2, startTime21, finishTime21,price21,seatsTotal, seatsLeft,status,category21,subCategory21,phone);
		course21.setClassModel(ClassModel.medianclass);
		course21.setLocation(location21);
		course21.setCity(city21);
		course21.setDistrict(district21);
		course21.setReference(coursereference21);
		course21.setTeachingMethodsIntro("teachingMaterial21");
		course21.setCourseName("title21");
		course21.setTeacherIntro("teachingInfo21");
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
		String coursereference3 = "course-reference30";		
		Course course3 = new Course(p_Id3, startTime3, finishTime3,price3,seatsTotal, seatsLeft,status,category3,subCategory3,phone);
		course3.setLocation(location3);
		course3.setCity(city3);
		course3.setDistrict(district3);
		course3.setReference(coursereference3);
		course3.setTeachingMethodsIntro("teachingMaterial3");
		course3.setCourseName("title3");
		course3.setTeacherIntro("teachingInfo3");
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
		String coursereference31 = "course-reference31";
		Course course31 = new Course(p_Id3, startTime31, finishTime31,price31,seatsTotal, seatsLeft,status,category31,subCategory31,phone);
		course31.setLocation(location31);
		course31.setCity(city31);
		course31.setDistrict(district31);
		course31.setReference(coursereference31);
		course31.setTeachingMethodsIntro("teachingMaterial31");
		course31.setCourseName("title31");
		course31.setTeacherIntro("teachingInfo31");
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
		String coursereference32 = "course-reference32";		
		Course course32 = new Course(p_Id3, startTime32, finishTime32,price32,seatsTotal, seatsLeft,status,category32,subCategory32,phone);
		course32.setLocation(location32);
		course32.setCity(city32);
		course32.setDistrict(district32);
		course32.setReference(coursereference32);
		course32.setTeachingMethodsIntro("teachingMaterial32");
		course32.setCourseName("title32");
		course32.setTeacherIntro("teachingInfo32");
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
		sr3.setFinishTime(finishTimeTest);
		clist = CourseDao.searchCourse(sr3);
		if(clist.size()==1 && clist.get(0).equals(course2)){
			//Passed;
		}else fail();
		
		CourseSearchRepresentation sr4 = new CourseSearchRepresentation();
		sr4.setStartPrice(0);
		sr4.setFinishPrice(40000);
		sr4.setCourseReference("course-reference1");
		clist = CourseDao.searchCourse(sr4);
		if(clist.size()==1 && clist.get(0).equals(course)){
			//Passed;
		}else fail();
		
		CourseSearchRepresentation sr5 = new CourseSearchRepresentation();
		startTimeTest.add(Calendar.DAY_OF_YEAR, -1);
		sr5.setStartTime(startTimeTest);
		sr5.setStartPrice(0);
		sr5.setFinishPrice(40000);
		sr5.setClassModel(ClassModel.smallclass);
		clist = CourseDao.searchCourse(sr5);
		if(clist.size()==5 && clist.get(0).equals(course) && clist.get(1).equals(course2) && 
				clist.get(2).equals(course3) && clist.get(3).equals(course31) &&
				clist.get(4).equals(course32)){
			//Passed;
		}else fail();
		
		finishTimeTest = DateUtility.getCurTimeInstance();
		finishTimeTest.add(Calendar.DAY_OF_YEAR, 15);
		sr5.setFinishTime(finishTimeTest);
		clist = CourseDao.searchCourse(sr5);
		if(clist.size()==2 && clist.get(0).equals(course) && clist.get(1).equals(course2)){
			//Passed;
		}else fail();
	}
	
}
