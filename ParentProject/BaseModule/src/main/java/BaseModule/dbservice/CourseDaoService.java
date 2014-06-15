package BaseModule.dbservice;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import BaseModule.cache.CourseRamCache;
import BaseModule.configurations.CacheConfig;
import BaseModule.eduDAO.CourseDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CourseNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Course;
import BaseModule.model.representation.CourseSearchRepresentation;

public class CourseDaoService {
	
	public static Course getCourseById(int id) throws PseudoException, SQLException{
		Course ramCourse = CourseRamCache.get(id);
		if (ramCourse == null){
			
			Object courseObj = EduDaoBasic.getCache(CacheConfig.course_keyPrefix + id);
			//if exist in cache, then simply cast and return
			if (courseObj != null){
				Course course = (Course)courseObj;
				CourseRamCache.set(course);
				return course;
			}
			else{
				Course course = CourseDao.getCourseById(id);
				EduDaoBasic.setCache(CacheConfig.course_keyPrefix + course.getCourseId(), CacheConfig.course_expireTime, course);
				CourseRamCache.set(course);
				return course;
			}
		}
		else{
			return ramCourse;
		}
	}
	
	public static void updateCourse(Course course,Connection...connections) throws PseudoException,SQLException{
		CourseDao.updateCourseInDatabases(course,connections);
		EduDaoBasic.setCache(CacheConfig.course_keyPrefix + course.getCourseId(), CacheConfig.course_expireTime, course);
		CourseRamCache.set(course);
	}
	
	public static Course createCourse(Course course,Connection...connections) throws SQLException{
		Course storedCourse = CourseDao.addCourseToDatabases(course,connections);
		EduDaoBasic.setCache(CacheConfig.course_keyPrefix + storedCourse.getCourseId(), CacheConfig.course_expireTime, storedCourse);
		CourseRamCache.set(storedCourse);
		return storedCourse;
	}	
	
	public static ArrayList<Course> searchCourse(CourseSearchRepresentation sr) throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, PseudoException, SQLException{
		ArrayList<Course> result = new ArrayList<Course>();
		boolean useCache = sr.getUseCache() == 1;
		if (!useCache){
			result = CourseDao.searchCourse(sr);
			return result;
		}
		
		Object obj = EduDaoBasic.getCache(sr.toCacheKey());
		if (obj != null){
			ArrayList<Integer> idList = (ArrayList<Integer>) obj;
			return getCourseByIdList(idList);
		}
		else{
			result = CourseDao.searchCourse(sr);

			ArrayList<Integer> idList = new ArrayList<Integer>();
			for (Course course: result){
				idList.add(course.getCourseId());
				EduDaoBasic.addCache(CacheConfig.course_keyPrefix + course.getCourseId(), CacheConfig.course_expireTime, course);
				CourseRamCache.set(course);
			}
			EduDaoBasic.setCache(sr.toCacheKey(), CacheConfig.courseSearch_expireTime, idList);

			return result;
		}
	}
	
	public static ArrayList<Course> getCourseByIdList(ArrayList<Integer> idList) throws PseudoException, SQLException{
		ArrayList<Course> result = new ArrayList<Course>();
		//get ram cached courses, copy into result
		result.addAll(CourseRamCache.getBulk(idList));
		//remove existing course ids from idList
		for (int i = 0; i < result.size(); i++){
			idList.remove(new Integer(result.get(i).getCourseId()));
		}
		
		//fetch from memcached server
		ArrayList<String> keyList = new ArrayList<String>();
		for (int i = 0; i < idList.size(); i++){
			keyList.add(CacheConfig.course_keyPrefix + idList.get(i));
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (keyList.size() > 0){
			resultMap = EduDaoBasic.getBulkCache(keyList);
		}
		//find if there are missing courses
		ArrayList<Integer> missingIdList = new ArrayList<Integer>();
		for (int i = 0; i < idList.size(); i++){
			Object single = resultMap.get(CacheConfig.course_keyPrefix + idList.get(i));
			if (single == null){
				missingIdList.add(idList.get(i));
			}
			else{
				Course course = (Course) single;
				result.add(course);
				CourseRamCache.set(course);
			}
		}
		
		//fetched from mysql, add to ram cache and memcache
		ArrayList<Course> finalPieces = CourseDao.getCourseByIdList(missingIdList);
		for (Course piece : finalPieces){
			EduDaoBasic.addCache(CacheConfig.course_keyPrefix + piece.getCourseId(), CacheConfig.course_expireTime, piece);
			CourseRamCache.set(piece);
		}
		result.addAll(finalPieces);
		return result;
	}
	
	public static  Course getCourseByReference(String reference) throws PseudoException, SQLException{
		CourseSearchRepresentation sr = new CourseSearchRepresentation();
		sr.setCourseReference(reference);
		ArrayList<Course> courses = CourseDao.searchCourse(sr);
		if (courses.size() == 0){
			throw new CourseNotFoundException();
		}
		else if (courses.size() > 1){
			throw new ValidationException("系统错误：编码重复");
		}
		else{
			return courses.get(0);
		}
	}
	
	public static boolean isReferenceAvailable(String reference) throws PseudoException, SQLException{
		CourseSearchRepresentation sr = new CourseSearchRepresentation();
		sr.setCourseReference(reference);
		return CourseDao.searchCourse(sr).size() == 0;
	}
	
}
