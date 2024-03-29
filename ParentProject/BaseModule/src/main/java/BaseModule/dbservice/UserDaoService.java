package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Coupon;
import BaseModule.model.User;
import BaseModule.model.representation.UserSearchRepresentation;
import BaseModule.service.SMSService;

public class UserDaoService {
	
	private static final int registrationCouponAmount = 100;
	private static final int invitationCouponAmount = 20;

	public static User getUserById(int id,Connection...connections) throws PseudoException, SQLException{
		return UserDao.getUserById(id,connections);
	}	
	
	public static void updateUserBCC(int balance,int credit,int coupon,int userId,Connection...connections) throws PseudoException, SQLException{
		UserDao.updateUserBCC(balance, credit, coupon, userId, connections);
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
	
	public static User getAndLock(int userId,Connection...connections) throws UserNotFoundException, SQLException{
		return UserDao.getAndLock(userId, connections);
	}
	
	public static User createUser(User user) throws PseudoException,SQLException{
		//initialize coupons on registration
		Connection conn = null;
		boolean ok = false;
		User inviter = null;
		User invitee = null;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			
			ArrayList<Coupon> coupons = new ArrayList<Coupon>();
			//pre incr, stored when user is added saves 1 query
			user.incCoupon(registrationCouponAmount);
			user = UserDao.addUserToDatabase(user,conn);
			
			//lock user
			UserDao.getAndLock(user.getUserId(), conn);
			
			Coupon coupon = new Coupon(user.getUserId(), registrationCouponAmount);
			coupon.setOrigin(CouponOrigin.registration);
			coupon = CouponDaoService.createCoupon(coupon,conn);
			coupons.add(coupon);
			
			if (user.getAppliedInvitationalCode() != null && user.getAppliedInvitationalCode().length() != 0){
				invitee = user;
				inviter  = getUserByInvitationalCode(user.getAppliedInvitationalCode());

				Coupon coupon_invitee = new Coupon(invitee.getUserId(), invitationCouponAmount);
				coupon_invitee.setOrigin(CouponOrigin.invitation);
				coupon_invitee.setStatus(CouponStatus.inactive);
				invitee.incCoupon(invitationCouponAmount);
				UserDao.updateUserBCC(0, 0, invitationCouponAmount, invitee.getUserId(), conn);
				coupon_invitee = CouponDaoService.createCoupon(coupon_invitee,conn);
				coupons.add(coupon_invitee);
				
				//lock inviter
				inviter = UserDao.getAndLock(inviter.getUserId(), conn);
				
				Coupon coupon_inviter = new Coupon(inviter.getUserId(), invitationCouponAmount);
				coupon_inviter.setOrigin(CouponOrigin.invitation);
				coupon_inviter.setStatus(CouponStatus.inactive);
				inviter.incCoupon(invitationCouponAmount);
				UserDao.updateUserBCC(0, 0, invitationCouponAmount, inviter.getUserId(), conn);
				coupon_inviter = CouponDaoService.createCoupon(coupon_inviter,conn);
				
			}
			user.setCouponList(coupons);
			ok = true;			
		} finally{
			if (EduDaoBasic.handleCommitFinally(conn, ok, true)){
				if (invitee != null){
					SMSService.sendUserRegistraterSMS(invitee.getPhone(), registrationCouponAmount);
					//only notify user when everything has absolutely gone right
					if (inviter != null){
						SMSService.sendInviteeSMS(invitee.getPhone(), invitationCouponAmount);
						SMSService.sendInviterSMS(inviter.getPhone(), invitationCouponAmount);
					}
				}
			}
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
		Connection conn = null;
		User user = null;
		boolean ok = false;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			//in this case Dao layers takes care of locking
			user = UserDao.authenticateUser(phone, password,conn);			
			ok = true;
		} finally{
			EduDaoBasic.handleCommitFinally(conn, ok, true);
		}
		return user;
	}

	public static void changePassword(int userId, String oldPassword, String newPassword) throws PseudoException,SQLException{
		Connection conn = null;
		boolean ok = false;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			//in this case Dao layers takes care of locking
			UserDao.changeUserPassword(userId, oldPassword, newPassword, conn);
			ok = true;
		}finally{
			EduDaoBasic.handleCommitFinally(conn, ok, true);
		}
	}

	public static void recoverPassword(String phone, String newPassword) throws PseudoException,SQLException{
		UserDao.recoverUserPassword(phone, newPassword);
	}

	public static void updatePhone(int userId, String phone) throws PseudoException,SQLException{
		Connection conn = null;
		boolean ok = false;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			User user = UserDao.getAndLock(userId, conn);
			user.setPhone(phone);
			UserDao.updateUserInDatabases(user);
			ok = true;
		}finally{
			EduDaoBasic.handleCommitFinally(conn, ok, true);
		}
	}

	public static ArrayList<User> searchUser(UserSearchRepresentation sr) throws SQLException {
		return UserDao.searchUser(sr);
	}
	
	public static User getUserByInvitationalCode(String code) throws SQLException, PseudoException{
		UserSearchRepresentation sr = new UserSearchRepresentation();
		sr.setInvitationalCode(code);
		ArrayList<User> users =  UserDao.searchUser(sr);
		if (users.size() == 0){
			throw new UserNotFoundException();
		}
		else if (users.size() > 1){
			throw new ValidationException("系统错误：编码重复");
		}
		else{
			return users.get(0);
		}
	}
	
	public static boolean isInvitationalCodeAvaialble(String code) throws SQLException{
		UserSearchRepresentation sr = new UserSearchRepresentation();
		sr.setInvitationalCode(code);
		return UserDao.searchUser(sr).size() == 0;
	}
	
	public static boolean isAccountnumberAvailable(String accountNumber) throws SQLException{
		UserSearchRepresentation sr = new UserSearchRepresentation();
		sr.setAccountNumber(accountNumber);
		return UserDao.searchUser(sr).size() == 0;
	}

}
