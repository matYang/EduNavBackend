package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import BaseModule.configurations.EnumConfig.Status;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
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
		Status status = Status.activated;
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
		Status status = Status.activated;
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
		Status status2 = Status.activated;
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
		Status status = Status.activated;
		User user = new User(name, phone, password,status);
		
		user = UserDao.addUserToDatabase(user);
		user.setName("Matt");
		user.setPassword("sdfewf");
		User user2 = UserDao.addUserToDatabase(user);
		if(user2.getName().equals("Matt")&&user2.getPassword().equals("sdfewf")){
			//Passed;
		}else fail();
	}
	
}
