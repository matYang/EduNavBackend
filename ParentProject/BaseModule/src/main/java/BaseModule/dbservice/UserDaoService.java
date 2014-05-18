package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.eduDAO.UserDao;
import BaseModule.exception.AuthenticationException;
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

	public static User createUser(User user) throws ValidationException{
		return UserDao.addUserToDatabase(user);
	}

	public static void updateUser(User user) throws ValidationException{
		UserDao.updateUserInDatabases(user);
	}

	public static boolean isCellPhoneAvailable(String phone){
		User user = null;
		try{
			user = UserDao.getUserByPhone(phone);
		}catch(UserNotFoundException ex){
			return true;
		}

		return false;
	}
	
	public static User authenticateUser(String phone, String password) throws AuthenticationException{ 
		//TODO return user if authenticated, return null if not 
		
		throw new AuthenticationException("手机号码或密码输入错误");
		//return null;
	}
	
	public static void changePassword(int userId, String oldPassword, String newPassword) throws AuthenticationException{
		//TODO change users password under DAO layer
		throw new AuthenticationException("密码输入错误");
	}

}
