package BaseModule.encryptionTest;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.junit.Test;

import BaseModule.configurations.ServerConfig;
import BaseModule.encryption.AccessControlCrypto;

public class AccessControlEncryptionTest {

	@Test
	public void test() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		String key = ServerConfig.ac_key;
		String ivy = ServerConfig.ac_ivy;
		String plainText = "123456789";
		
		String encrypted = AccessControlCrypto.encrypt(plainText, key, ivy);
		String decrypted = AccessControlCrypto.decrypt(encrypted, key, ivy);
		
		
		System.out.println(encrypted);
		System.out.println(decrypted);
		
		assertTrue(plainText.equals(decrypted));
	}

}
