package BaseModule.encodingTest;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import BaseModule.service.EncodingService;

public class EncodingServiceTest {

	@Test
	public void test() throws UnsupportedEncodingException {
		String special = "https://www.aishangke.cn/test?param=我是一条中文字符&date=1993-05-17 22:22:22";
		String encoded = EncodingService.encodeURI(special);
		String decoded = EncodingService.decodeURI(special);
		assertTrue(decoded.equals(special));
		assertFalse(encoded.equals(special));
		
		encoded = special;
		for (int i = 0; i < 100; i++){
			encoded = EncodingService.encodeURI(encoded);
		}
		assertFalse(encoded.equals(special));
		for (int i = 0; i < 100; i++){
			encoded = EncodingService.decodeURI(encoded);
		}
		
		assertTrue(encoded.equals(special));
	}
	
	@Test
	public void testa() throws UnsupportedEncodingException {
		String special = "https%3A%2F%2Fwww.aishangke.cn%2Ftest%3Fparam%3D%E6%88%91%E6%98%AF%E4%B8%80%E6%9D%A1%E4%B8%AD%E6%96%87%E5%AD%97%E7%AC%A6%26date%3D1993-05-17+22%3A22%3A22";
		String decoded = EncodingService.decodeURI(special);
		System.out.println("Decoded special: " + decoded);
		for (int i = 0; i < 100; i++){
			decoded = EncodingService.decodeURI(decoded);
		}
		assertFalse(decoded.equals(special));
		assertTrue(EncodingService.decodeURI(special).equals(decoded));
		assertTrue(EncodingService.encodeURI(decoded).equals(special));
	}

}
