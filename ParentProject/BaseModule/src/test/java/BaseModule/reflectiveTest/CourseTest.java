package BaseModule.reflectiveTest;

import org.json.JSONObject;
import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.model.Course;

public class CourseTest {

	@Test
	public void test() throws Exception {
		JSONObject json = new JSONObject();
		
		json.put("courseId", 1);
		json.put("creationTime", "1993-05-17 15:26:45");
		json.put("city", "苏州");
		json.put("district", "苏州工业园区");
		json.put("location", "啦啦啦啦啦啦");
		json.put("category", "帅哥");
		json.put("startTime", "2009-10-31 15:26:45");
		json.put("partnerIntro", "我也不知道说这么，貌似这里可以很长的样子，就先这样吧");
		json.put("price", 50);
		json.put("reference", "7HFJ68HF");
		json.put("instName", "马修杨");
		json.put("wholeName", "还是马修杨");
		json.put("quiz", "考试考到死");
		json.put("questionBank", "生死题库-三年高考两年幸福-滑大泡妹手册");
		json.put("questionbankIntro", "真的是瞎掰的呢");
		json.put("seatsTotal", 9);
		json.put("finishTime", "2080-05-17 15:26:45");
		json.put("seatsLeft", 2);
		json.put("studyDaysNote", "好好学习天天向上");
		json.put("courseName", "人生哲学");
		json.put("partnerQualification", 1);
		json.put("classModel", 0);
		json.put("status", 0);
		json.put("teachingMaterialFee", "好贵。。");
		json.put("price", 50);
		json.put("phone", "18662241356");


		
		Course course = null;
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < 100000; i++){
			course = Course.getInstance();
			course.storeJSON(json);
		}
		System.out.println("100000 refelction finished at: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		System.out.println(course.toJSON().toString());
		

	}

}
