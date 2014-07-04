package BaseModule.eduDAOTest;

import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.TeacherDao;
import BaseModule.exception.notFound.TeacherNotFoundException;
import BaseModule.model.Teacher;

public class TeacherDaoTest {

	@Test
	public void testAdd(){
		EduDaoBasic.clearAllDatabase();
		int p_Id = 1;
		Teacher teacher = new Teacher(p_Id,"teacherImgUrl", "teacherName", "teacherIntro");			
		try {
			TeacherDao.addTeacherToDataBases(teacher);
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGet() throws SQLException{
		EduDaoBasic.clearAllDatabase();
		int p_Id = 1;
		Teacher teacher = new Teacher(p_Id,"teacherImgUrl", "teacherName", "teacherIntro");
		TeacherDao.addTeacherToDataBases(teacher);
		Teacher teacher2 = new Teacher(p_Id,"teacherImgUrl2", "teacherName2", "teacherIntro2");
		TeacherDao.addTeacherToDataBases(teacher2);
		ArrayList<Long> teacherIdList = new ArrayList<Long>();
		teacherIdList.add(teacher.getTeacherId());
		teacherIdList.add(teacher2.getTeacherId());
		ArrayList<Teacher> tlist = TeacherDao.getTeachers(teacherIdList);
		if(tlist.size()==2 && tlist.get(0).equals(teacher) && tlist.get(1).equals(teacher2)){
			//Passed;
		}else fail();		
		
		tlist = TeacherDao.getPartnerTeachers(p_Id);
		if(tlist.size()==2 && tlist.get(0).equals(teacher) && tlist.get(1).equals(teacher2)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpDate() throws SQLException, TeacherNotFoundException{
		EduDaoBasic.clearAllDatabase();
		int p_Id = 1;
		Teacher teacher = new Teacher(p_Id,"teacherImgUrl", "teacherName", "teacherIntro");
		TeacherDao.addTeacherToDataBases(teacher);
		Teacher teacher2 = new Teacher(p_Id,"teacherImgUrl2", "teacherName2", "teacherIntro2");
		TeacherDao.addTeacherToDataBases(teacher2);
		teacher.setName("HarryXiong");
		TeacherDao.updateTeacherInDataBase(teacher);
		ArrayList<Long> teacherIdList = new ArrayList<Long>();
		teacherIdList.add(teacher.getTeacherId());
		ArrayList<Teacher> tlist = TeacherDao.getTeachers(teacherIdList);
		if(tlist.size()==1 && tlist.get(0).getName().equals("HarryXiong")){
			//Passed;
		}else fail();
	}
}
