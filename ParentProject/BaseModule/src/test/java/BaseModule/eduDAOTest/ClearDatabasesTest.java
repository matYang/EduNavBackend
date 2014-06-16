package BaseModule.eduDAOTest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.junit.Test;

import BaseModule.configurations.ServerConfig;
import BaseModule.dbservice.UserDaoService;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.encryption.AccessControlCrypto;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.model.User;

public class ClearDatabasesTest {

	@Test
	public void test() {
		EduDaoBasic.clearAllDatabase();
	}


}
