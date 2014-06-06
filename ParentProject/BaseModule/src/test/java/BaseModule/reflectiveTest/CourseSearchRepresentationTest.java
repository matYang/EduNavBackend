package BaseModule.reflectiveTest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.exception.PseudoException;
import BaseModule.model.representation.CourseSearchRepresentation;

public class CourseSearchRepresentationTest {

	@Test
	public void test() throws IllegalArgumentException, IllegalAccessException, PseudoException, UnsupportedEncodingException {
		Map<String, String> kvps= new HashMap<String, String>();
		
		kvps.put("courseId", "9");
		kvps.put("district", "白下区");
		kvps.put("city", "南京");
		kvps.put("category", "英语");
		kvps.put("subCategory", "托福");
		kvps.put("partnerReference", "GA3tt90_--+0jlkfhkl990jiof56DFHK83");
		kvps.put("startTime", "2014-05-15");
		kvps.put("startPrice", null);
		kvps.put("finishPrice", "1000");
		kvps.put("creationTime", "2014-05-1");
		kvps.put("finishTime", "2018-09-1");
		
		CourseSearchRepresentation c_sr = new CourseSearchRepresentation();
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (int i = 0; i < 100000; i++){
			c_sr.storeKvps(kvps);
			c_sr.getKeySet();
			c_sr.toJSON();
			c_sr.serialize();
		}
		System.out.println("300000 refelction finished at: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
	}
	
	@Test
	public void testConcurrent_a() throws IllegalArgumentException, IllegalAccessException, PseudoException, UnsupportedEncodingException {
		Map<String, String> kvps= new HashMap<String, String>();
		
		kvps.put("courseId", "9");
		kvps.put("district", "白下区");
		kvps.put("city", "南京");
		kvps.put("category", "英语");
		kvps.put("subCategory", "托福");
		kvps.put("partnerReference", "GA3tt90_--+0jlkfhkl990jiof56DFHK83");
		kvps.put("startTime", "2014-05-15");
		kvps.put("startPrice", "90");
		kvps.put("finishPrice", "1000");
		kvps.put("creationTime", "2014-05-1");
		kvps.put("finishTime", "2018-09-1");
		
		CourseSearchRepresentation c_sr = new CourseSearchRepresentation();
		c_sr.storeKvps(kvps);
		
		System.out.println(c_sr.toString());
		System.out.println(c_sr.getKeySet());
		System.out.println(c_sr.toJSON());
		System.out.println(c_sr.serialize());
		System.out.println(c_sr.serialize().length());
	}
	

}
