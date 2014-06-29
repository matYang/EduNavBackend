package BaseModule.validationTest;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import BaseModule.service.EncodingService;

public class EncodingTest {

	@Test
	public void test() throws UnsupportedEncodingException {
		String stringToBeEncoded = " + @";
		String encoded = EncodingService.encodeURI(stringToBeEncoded);
		
		System.out.println(encoded);
		
		String decoded = EncodingService.decodeURI(encoded);
		
		assertTrue(stringToBeEncoded.equals(decoded));
	}

}
