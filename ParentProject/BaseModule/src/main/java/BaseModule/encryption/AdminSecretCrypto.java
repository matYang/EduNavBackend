package BaseModule.encryption;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class AdminSecretCrypto {
	 private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";


    public static boolean validatePassword(String password, String correctHash)throws NoSuchAlgorithmException, InvalidKeySpecException{
        if (password == null){
        	return false;
        }
    	return validatePassword(password.toCharArray(), correctHash);
    }

    private static boolean validatePassword(char[] password, String correctHash)throws NoSuchAlgorithmException, InvalidKeySpecException{
        // Decode the hash into its parameters
        String[] params = correctHash.split(":");
        int iterations = 3000;
        byte[] salt = fromHex(params[0]);
        byte[] hash = fromHex(params[1]);
        // Compute the hash of the provided password, using the same salt, 
        // iteration count, and hash length
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, testHash);
    }

    private static boolean slowEquals(byte[] a, byte[] b){
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }


    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException{
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    private static byte[] fromHex(String hex){
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

}
