package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.common.DateUtility;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.encryptionException.PasswordHashingException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Coupon;
import BaseModule.model.User;
import BaseModule.model.representation.UserSearchRepresentation;

public class UserDaoService {

	public static ArrayList<User> getAllUsers() throws SQLException{
		return searchUser(new UserSearchRepresentation());
	}

	public static User getUserById(int id,Connection...connections) throws PseudoException, SQLException{
		return UserDao.getUserById(id,connections);
	}

	public static User getUserByPhone(String phone) throws UserNotFoundException, SQLException{
		UserSearchRepresentation u_sr = new UserSearchRepresentation();
		u_sr.setPhone(phone);
		ArrayList<User> users = searchUser(u_sr);
		if (users.size() == 0){
			throw new UserNotFoundException();
		}
		return users.get(0);
	}

	public static User createUser(User user) throws PseudoException,SQLException{
		//initialize coupons on registration
		Connection conn = EduDaoBasic.getSQLConnection();
		try{
			//pre incr, stored when user is added saves 1 query
			user.incCoupon(50);
			user = UserDao.addUserToDatabase(user,conn);
			
			Coupon coupon = new Coupon(user.getUserId(), 50);
			coupon = CouponDaoService.createCoupon(coupon,conn);
			
			ArrayList<Coupon> coupons = new ArrayList<Coupon>();
			coupons.add(coupon);
			user.setCouponList(coupons);
		} finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}
		return user;
	}

	public static void updateUser(User user,Connection...connections) throws PseudoException,SQLException{
		UserDao.updateUserInDatabases(user,connections);
	}

	public static boolean isCellPhoneAvailable(String phone) throws SQLException{
		UserSearchRepresentation u_sr = new UserSearchRepresentation();
		u_sr.setPhone(phone);
		ArrayList<User> users = searchUser(u_sr);
		return users.size() == 0;
	}

	public static User authenticateUser(String phone, String password) throws PseudoException, SQLException{ 
		Connection conn = EduDaoBasic.getSQLConnection();
		User user = null;
		try{
			user = UserDao.authenticateUser(phone, password,conn);
			user.setLastLogin(DateUtility.getCurTimeInstance());
			UserDao.updateUserInDatabases(user,conn);
		} finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}
		return user;
	}

	public static void changePassword(int userId, String oldPassword, String newPassword) throws PseudoException,SQLException{
		UserDao.changeUserPassword(userId, oldPassword, newPassword);
	}

	public static void recoverPassword(String phone, String newPassword) throws PseudoException,SQLException{
		UserDao.recoverUserPassword(phone, newPassword);
	}

	public static void updatePhone(int userId, String phone) throws PseudoException,SQLException{
		Connection conn = EduDaoBasic.getSQLConnection();
		try{
			User user = UserDao.getUserById(userId);
			user.setPhone(phone);
			UserDao.updateUserInDatabases(user);
		}finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}
	}

	public static ArrayList<User> searchUser(UserSearchRepresentation sr) throws SQLException {
		return UserDao.searchUser(sr);
	}	

}
