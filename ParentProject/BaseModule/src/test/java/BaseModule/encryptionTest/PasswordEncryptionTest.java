package BaseModule.encryptionTest;

import static org.junit.Assert.*;

import org.junit.Test;

import BaseModule.common.DateUtility;
import BaseModule.encryption.AdminCrypto;
import BaseModule.encryption.PasswordCrypto;

public class PasswordEncryptionTest {

	//@Test
	public void test() {
		try{
            // Print out 10 hashes
            for(int i = 0; i < 10; i++)
                System.out.println(PasswordCrypto.createHash("p\r\nassw0Rd!"));

            // Test password validation
            boolean failure = false;
            System.out.println("Running tests...System time: " + DateUtility.castToAPIFormat(DateUtility.getCurTimeInstance()));
            for(int i = 0; i < 10000; i++){
                String password = ""+i;
                String hash = PasswordCrypto.createHash(password);
                String secondHash = PasswordCrypto.createHash(password);
                if(hash.equals(secondHash)) {
                    System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
                    failure = true;
                }
                String wrongPassword = ""+(i+1);
                if(PasswordCrypto.validatePassword(wrongPassword, hash)) {
                    System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
                    failure = true;
                }
                if(!PasswordCrypto.validatePassword(password, hash)) {
                    System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
                    failure = true;
                }
            }
            if(failure)
                System.out.println("TESTS FAILED!");
            else
                System.out.println("TESTS PASSED!");
            	System.out.println("System time: " + DateUtility.castToAPIFormat(DateUtility.getCurTimeInstance()));
        } catch(Exception ex) {
            System.out.println("ERROR: " + ex);
        }
	}
	
}
