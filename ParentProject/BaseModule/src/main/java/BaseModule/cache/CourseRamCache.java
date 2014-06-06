package BaseModule.cache;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.model.Course;

public class CourseRamCache {
	
	private static final ConcurrentHashMap<Integer, CourseCachePair> courseRamCache = new ConcurrentHashMap<Integer, CourseCachePair>();
	private static final long courseRamCacheExpireTime = 600000l;   //expire in 10 min
	private static final int courseRamCacheMax = 1000;		//maximum 10000 cached course
	private static final int courseRamCacheLimit = 1500;	//dangerous level indicating something has clearly been done wrong and strong action needs to be taken to protect ram
	
	private class CourseCachePair{
		private Course course;
		private long timeStamp;
		
		public CourseCachePair(Course course, long timeStamp){
			this.course = course;
			this.timeStamp = timeStamp;
		}
		
		public Course getCourse(){
			return this.course;
		}
		
		public long getTimeStamp(){
			return this.timeStamp;
		}
		
	}
	
	
	public synchronized static void set(Course course){
		try{
			long curTime = DateUtility.getCurTime();
			Course cloned = course.deepCopy();
			CourseCachePair cachePair = new CourseRamCache().new CourseCachePair(cloned, curTime);
			
			courseRamCache.put(course.getCourseId(), cachePair);
			
			int size = courseRamCache.size();
			//if cache exceeds max, randomly remove an entry
			if (size > courseRamCacheMax){
				Object[] keyArr = courseRamCache.keySet().toArray();
				Random random = new Random(curTime);
				
				//randomly remove a key from key array
				int removableKey = (int) keyArr[random.nextInt(keyArr.length)];
				courseRamCache.remove(removableKey);
			}
			//if cache exceeds limit, cache memory management has failed, clear all cache to avoid catastrophic memory overflow, log the occurrence
			if (size > courseRamCacheLimit){
				courseRamCache.clear();
				DebugLog.d("[ERROR] Course Ram Cache memory limit hit");
			}
		}
		catch (Exception e){
			DebugLog.d(e);
		}
		
	}
	
	public static Course get(int courseId){
		CourseCachePair cachePair = courseRamCache.get(courseId);
		//does not exist
		if (cachePair == null){
			return null;
		}
		//expired
		if (DateUtility.getCurTime() - cachePair.getTimeStamp() > courseRamCacheExpireTime){
			courseRamCache.remove(courseId);
			return null;
		}
		return cachePair.getCourse();
	}
	
	
	public static ArrayList<Course> getBulk(ArrayList<Integer> courseIds){
		ArrayList<Course> courses = new ArrayList<Course>();
		for (int courseId : courseIds){
			Course course = get(courseId);
			if (course != null){
				courses.add(course);
			}
		}
		return courses;
	}
	
	public static void clear(){
		courseRamCache.clear();
	}
	
	
}
