package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.common.DateUtility;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.AuthenticationException;
import BaseModule.exception.user.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.User;
import BaseModule.model.representation.UserSearchRepresentation;

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
	
	public static User authenticateUser(String phone, String password) throws AuthenticationException, ValidationException{ 
		User user = UserDao.authenticateUser(phone, password);
		user.setLastLogin(DateUtility.getCurTimeInstance());
		UserDao.updateUserInDatabases(user);
		return user;
	}
	
	public static void changePassword(int userId, String oldPassword, String newPassword) throws AuthenticationException{
		UserDao.changeUserPassword(userId, oldPassword, newPassword);
	}
	
	public static void recoverPassword(String phone, String newPassword) throws AuthenticationException{
		UserDao.recoverUserPassword(phone, newPassword);
	}
	
	public static void updatePhone(int userId, String phone) throws UserNotFoundException, ValidationException{
		User user = UserDao.getUserById(userId);
		user.setPhone(phone);
		UserDao.updateUserInDatabases(user);
	}

	public static ArrayList<User> searchUser(UserSearchRepresentation sr) {
		return UserDao.searchUser(sr);
	}

}
