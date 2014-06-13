package BaseModule.concurrentTest;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.model.User;

public class ConcurrentBookingCouponTest {

	@Test
	public void test() throws PseudoException, SQLException {
		EduDaoBasic.clearAllDatabase();				
		String name = "Harry";
		String userphone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhan@hotmail.com";			
		User user = new User(userphone, password, "", "",status);
		user.setName(name);
		user.setEmail(email);		
		UserDao.addUserToDatabase(user);
		
		
	}

}
