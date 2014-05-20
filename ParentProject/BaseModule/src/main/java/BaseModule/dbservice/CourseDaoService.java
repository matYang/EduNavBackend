package BaseModule.dbservice;

import java.sql.Connection;
import java.util.ArrayList;

import BaseModule.eduDAO.CourseDao;
import BaseModule.exception.course.CourseNotFoundException;
import BaseModule.model.Course;
import BaseModule.model.representation.SearchRepresentation;

public class CourseDaoService {

	public static ArrayList<Course> getCoursesFromPartner(int partnerId){
		return CourseDao.getCoursesFromPartner(partnerId);
	}
	
	public static Course getCourseById(int id) throws CourseNotFoundException{
		return CourseDao.getCourseById(id);
	}
	
	public static void updateCourse(Course course,Connection...connections) throws CourseNotFoundException{
		CourseDao.updateCourseInDatabases(course,connections);
	}
	
	public static Course createCourse(Course course,Connection...connections){
		return CourseDao.addCourseToDatabases(course,connections);
	}
	
	
	//TODO
	public static ArrayList<Course> searchCourse(SearchRepresentation sr){
		return null;
	}
	
}
