package BaseModule.reflectiveTest;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.exception.PseudoException;
import BaseModule.model.Course;

public class CourseTest {

	@Test
	public void test() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, PseudoException, ParseException {
		Map<String, String> kvps= new HashMap<String, String>();
		
		kvps.put("courseId", "1");
		kvps.put("creationTime", "1993-05-17 15:26:45");
		kvps.put("city", "苏州");
		kvps.put("district", "苏州工业园区");
		kvps.put("location", "啦啦啦啦啦啦");
		kvps.put("category", "帅哥");
		kvps.put("startTime", "2009-10-31 15:26:45");
		kvps.put("partnerIntro", "我也不知道说这么，貌似这里可以很长的样子，就先这样吧");
		kvps.put("price", "50");
		kvps.put("reference", "7HFJ68HF");
		kvps.put("teacherImgUrl", "http://stackoverflow.com/search?q=java+convert+array");
		kvps.put("instName", "马修杨");
		kvps.put("wholeName", "还是马修杨");
		kvps.put("quiz", "考试考到死");
		kvps.put("questionBank", "生死题库-三年高考两年幸福-滑大泡妹手册");
		kvps.put("questionbankIntro", "真的是瞎掰的呢");
		kvps.put("seatsTotal", "9");
		kvps.put("finishTime", "2080-05-17 15:26:45");
		kvps.put("seatsLeft", "2");
		kvps.put("studyDays", "1-3-5-6-7");
		kvps.put("studyDaysNote", "好好学习天天向上");
		kvps.put("courseName", "人生哲学");
		kvps.put("partnerQualification", "1");
		kvps.put("classModel", "0");
		kvps.put("status", "0");
		kvps.put("teachingMaterialFee", "false");
		kvps.put("url-classImg1", "fasdf");
		kvps.put("price", "50");
		kvps.put("url-classImg2", "adfdas");
		kvps.put("url-classImg3", "fdasf");
		kvps.put("phone", "18662241356");
		kvps.put("url-classImg4", "adsfas");
		kvps.put("url-classImg5", "asdf");

		
		Course course = null;
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < 100000; i++){
			course = new Course();
			course.loadFromMap(kvps);
		}
		System.out.println("100000 refelction finished at: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		//System.out.println(course.toJSON());
		

	}

}
