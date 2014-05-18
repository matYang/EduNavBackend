package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.user.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.User;

public class UserDaoTest {

	@Test
	public void testCreate(){
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(name, phone, password,status);

		try{
			UserDao.addUserToDatabase(user);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testGet() throws ValidationException, UserNotFoundException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(name, phone, password,status);

		user = UserDao.addUserToDatabase(user);

		User test = UserDao.getUserById(user.getUserId());

		if(user.equals(test)){
			//Passed;
		}else{
			fail();
		}
		test = UserDao.getUserByPhone(user.getPhone());
		if(user.equals(test)){
			//Passed;
		}else{
			fail();
		}

		String name2 = "Matt";
		String phone2 = "1324234234";
		String password2 = "36krl";
		AccountStatus status2 = AccountStatus.activated;
		User user2 = new User(name2, phone2, password2,status2);
		UserDao.addUserToDatabase(user2);

		ArrayList<User> ulist = new ArrayList<User>();
		ulist = UserDao.getAllUsers();
		if(ulist.size()==2 && ulist.get(0).equals(user) && ulist.get(1).equals(user2)){
			//Passed;
		}else fail();
	}

	@Test
	public void testUpdate() throws ValidationException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(name, phone, password,status);

		user = UserDao.addUserToDatabase(user);
		user.setName("Matt");
		user.setPhone("324234324");
		User user2 = UserDao.addUserToDatabase(user);
		if(user2.getName().equals("Matt")&&user2.getPhone().equals("324234324")){
			//Passed;
		}else fail();
	}

	@Test
	public void testUpdateUserPassword() throws ValidationException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(name, phone, password,status);		
		user.setName("Matt");		
		user = UserDao.addUserToDatabase(user);

		try{
			UserDao.changeUserPassword(user.getUserId(), "36krfinal", "xcf");
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}

		boolean fail = false;

		try{
			UserDao.changeUserPassword(user.getUserId(), "xcf", "3432fdsf");
		}catch(AuthenticationException e){
			fail = true;
		}
		catch(Exception e){
			e.printStackTrace();
			fail();
		}

		if(fail) fail();

		fail = true;
		try{
			UserDao.changeUserPassword(user.getUserId(), "xcf", "");
		}catch(AuthenticationException e){
			fail = false;
		}
		catch(Exception e){			
			fail();
		}

		if(fail) fail();

		fail = true;
		try{
			UserDao.changeUserPassword(2, "xcf", "sdf3");
		}catch(AuthenticationException e){
			fail = false;
		}
		catch(Exception e){			
			fail();
		}

		if(fail) fail();

	}

	@Test
	public void testAuthUser() throws ValidationException{
		EduDaoBasic.clearBothDatabase();
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		User user = new User(name, phone, password,status);		
		user.setName("Matt");		
		user = UserDao.addUserToDatabase(user);
		User test = null;
		try {
			test = UserDao.authenticateUser(phone, password);
		} catch (AuthenticationException e) {
			fail();
		}
		if(test==null) fail();
		
		test = null;
		try {
			test = UserDao.authenticateUser("12345612311", password);
		} catch (AuthenticationException e) {
			//Passed;
		}
		
		if(test != null) fail();
		
		test = null;
		try {
			test = UserDao.authenticateUser(phone, "36krfinai");
		} catch (AuthenticationException e) {
			//Passed;
		}
		
		if(test != null) fail();

	}

}
