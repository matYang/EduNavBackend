package BaseModule.encryptionTest;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import BaseModule.encryption.SimpleMD5Hash;
import BaseModule.encryption.SessionCrypto;

public class EncryptionTest {

	@Test
	public void ImgCryptoTest(){
		String testImgName = "83_--3498as2-re";
		
		String encryptedString = null;
		try {
			encryptedString = SimpleMD5Hash.encrypt(testImgName);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		System.out.println(encryptedString);
		assertTrue(!encryptedString.equals(testImgName));
	}
	
	@Test
	public void SessionCryptoTest(){
		String testSessionKey = "fds$@#tfsdu8YUFG3t+_1~j9";
		String encryptedString = "";
		String decryptedString = "";

		
		try {
			//taking approximately 3.3ms each iteration
			//taking approximately 1.7ms for single decryption, important here as decryption is required for all cookie checking
			for (int i = 0; i < 10000; i++){
				encryptedString = SessionCrypto.encrypt(testSessionKey);
				decryptedString = SessionCrypto.decrypt(encryptedString);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(encryptedString);
		assertTrue(!encryptedString.equals(""));
		assertTrue(!decryptedString.equals(""));
		assertTrue(testSessionKey.equals(decryptedString));
	}
}
