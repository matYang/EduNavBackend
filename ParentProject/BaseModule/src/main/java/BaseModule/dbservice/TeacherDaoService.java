package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.TeacherDao;
import BaseModule.exception.notFound.TeacherNotFoundException;
import BaseModule.model.Teacher;

public class TeacherDaoService {

	public static ArrayList<Teacher> getTeacherByPartnerId(int partnerId) throws SQLException{
		return TeacherDao.getPartnerTeachers(partnerId);
	}

	public static ArrayList<Teacher> createTeacherList(ArrayList<Teacher> teachers) throws SQLException{
		return TeacherDao.addTeachersToDataBases(teachers);
	}

	public static void updateTeacherList(ArrayList<Teacher> teachers, Connection...connections) throws TeacherNotFoundException, SQLException{
		Connection conn = null;
		try{
			conn = EduDaoBasic.getConnection(connections);
			for(Teacher teacher : teachers){
				TeacherDao.updateTeacherInDataBase(teacher, connections);
			}
		}finally{
			EduDaoBasic.closeResources(conn, null, null, EduDaoBasic.shouldConnectionClose(connections));
		}
	}

}
