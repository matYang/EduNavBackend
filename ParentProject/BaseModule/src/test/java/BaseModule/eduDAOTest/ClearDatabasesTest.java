package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import org.junit.Test;

import BaseModule.eduDAO.EduDaoBasic;

public class ClearDatabasesTest {

	@Test
	public void test() {
		EduDaoBasic.clearBothDatabase();
	}

}
