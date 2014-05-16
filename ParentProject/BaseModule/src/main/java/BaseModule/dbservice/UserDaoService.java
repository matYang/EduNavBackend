package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.eduDAO.UserDao;
import BaseModule.exception.user.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.User;

public class UserDaoService {

	public static ArrayList<User> getAllUsers(){
		return UserDao.getAllUsers();
	}
	
	public static User getUserById(int id) throws UserNotFoundException{
		return UserDao.getUserById(id);
	}
	
	public static User getUserByPhone(String phone) throws UserNotFoundException{
		return UserDao.getUserByPhone(phone);
	}
	
	public static void updateUser(User user) throws ValidationException{
		UserDao.updateUserInDatabases(user);
	}
}
