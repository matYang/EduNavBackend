package BaseModule.eduDAOTest;
import org.junit.Test;
import BaseModule.eduDAO.EduDaoBasic;

public class ClearDatabasesTest {

	@Test
	public void test() {
		EduDaoBasic.clearAllDatabase();
	}

}
