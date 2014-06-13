package BaseModule.eduDAOTest;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import BaseModule.dbservice.UserDaoService;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.model.User;

public class ClearDatabasesTest {

	@Test
	public void test() {
		EduDaoBasic.clearAllDatabase();
	}
	

}
