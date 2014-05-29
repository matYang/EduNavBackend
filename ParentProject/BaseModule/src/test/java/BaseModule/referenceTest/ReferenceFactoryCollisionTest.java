package BaseModule.referenceTest;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class ReferenceFactoryCollisionTest {

	public String genRef(){
		return RandomStringUtils.randomAlphanumeric(8).toUpperCase();
	}
	
	@Test
	public void test() {
		Map<String, String> collisionMap = new HashMap<String, String>();
		int collisionCounter = 0;
		for (int i = 0; i < 1000000; i++){
			String ref = genRef();
			if (collisionMap.containsKey(ref)){
				i--;
			}
			else{
				collisionMap.put(ref, "");
			}
		}
		
		for (int i = 0; i < 1000000000; i++){
			String ref = genRef();
			if (collisionMap.containsKey(ref)){
				collisionCounter++;
			}
		}
		
		System.out.println("Collision test finished, 1 billion 8-digit refs generated, total collision with 1 million previous generated references is: " + collisionCounter);
	}

}
