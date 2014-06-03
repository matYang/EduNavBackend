package BaseModule.dbservice;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import BaseModule.configurations.CacheConfig;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.course.CourseNotFoundException;
import BaseModule.model.Course;
import BaseModule.model.representation.CourseSearchRepresentation;

public class CourseDaoService {
	
	public static Course getCourseById(int id) throws CourseNotFoundException{
		Object courseObj = EduDaoBasic.getCache(CacheConfig.course_keyPrefix + id);
		//if exist in cache, then simply cast and return
		if (courseObj != null){
			return (Course)courseObj;
		}
		else{
			Course course = CourseDao.getCourseById(id);
			EduDaoBasic.setCache(CacheConfig.course_keyPrefix + course.getCourseId(), CacheConfig.course_expireTime, course);
			return course;
		}
	}
	
	public static void updateCourse(Course course,Connection...connections) throws CourseNotFoundException, SQLException{
		CourseDao.updateCourseInDatabases(course,connections);
		EduDaoBasic.setCache(CacheConfig.course_keyPrefix + course.getCourseId(), CacheConfig.course_expireTime, course);
	}
	
	public static Course createCourse(Course course,Connection...connections) throws SQLException{
		Course storedCourse = CourseDao.addCourseToDatabases(course,connections);
		EduDaoBasic.setCache(CacheConfig.course_keyPrefix + storedCourse.getCourseId(), CacheConfig.course_expireTime, storedCourse);
		return storedCourse;
	}	
	
	public static ArrayList<Course> searchCourse(CourseSearchRepresentation sr) throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException{
		ArrayList<Course> result = new ArrayList<Course>();
		boolean useCache = sr.getUseCache() == 1;
		
		Object obj = !useCache ? null : EduDaoBasic.getCache(sr.toCacheKey());
		if (obj != null){
			ArrayList<Integer> idList = (ArrayList<Integer>) obj;
			ArrayList<String> keyList = new ArrayList<String>();
			for (int i = 0; i < idList.size(); i++){
				keyList.add(CacheConfig.course_keyPrefix + idList.get(i));
			}
			Map<String, Object> resultMap = EduDaoBasic.getBulkCache(keyList);
			ArrayList<Integer> missingIdList = new ArrayList<Integer>();
			for (int i = 0; i < idList.size(); i++){
				Object single = resultMap.get(CacheConfig.course_keyPrefix + idList.get(i));
				if (single == null){
					missingIdList.add(idList.get(i));
				}
				else{
					Course course = (Course) single;
					result.add(course);
				}
			}
			result.addAll(CourseDao.getCourseByIdList(missingIdList));
			return result;
		}
		else{
			result = CourseDao.searchCourse(sr);
			if (useCache){
				ArrayList<Integer> idList = new ArrayList<Integer>();
				for (Course course: result){
					idList.add(course.getCourseId());
					EduDaoBasic.addCache(CacheConfig.course_keyPrefix + course.getCourseId(), CacheConfig.course_expireTime, course);
				}
				EduDaoBasic.setCache(sr.toCacheKey(), CacheConfig.courseSearch_expireTime, idList);
			}
			return result;
		}
	}
	
	public static  ArrayList<Course> getCourseByReference(String reference){
		CourseSearchRepresentation sr = new CourseSearchRepresentation();
		sr.setCourseReference(reference);
		return CourseDao.searchCourse(sr);				
	}
	
	public static boolean isReferenceAvailable(String reference){
		return getCourseByReference(reference).size() == 0;
	}
	
}
