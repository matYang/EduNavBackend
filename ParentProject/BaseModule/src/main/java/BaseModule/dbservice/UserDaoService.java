package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Coupon;
import BaseModule.model.User;
import BaseModule.model.representation.UserSearchRepresentation;

public class UserDaoService {
	
	private static final int registrationCouponAmount = 50;
	private static final int invitationCouponAmount = 50;

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
		Connection conn = null;
		try{
			conn = EduDaoBasic.getConnection();
			conn.setAutoCommit(false);
			
			ArrayList<Coupon> coupons = new ArrayList<Coupon>();
			//pre incr, stored when user is added saves 1 query
			user.incCoupon(registrationCouponAmount);
			user = UserDao.addUserToDatabase(user,conn);

			Coupon coupon = new Coupon(user.getUserId(), registrationCouponAmount);
			coupon.setOrigin(CouponOrigin.registration);
			coupon = CouponDaoService.createCoupon(coupon,conn);
			coupons.add(coupon);
			
			if (user.getAppliedInvitationalCode() != null || user.getAppliedInvitationalCode().length() != 0){
				User invitee = user;
				User inviter = null;
				ArrayList<User> inviters = getUserByInvitationalCode(user.getAppliedInvitationalCode());
				if (inviters.size() < 0){
					throw new ValidationException("对不起，邀请人不存在");
				}
				else if (inviters.size() > 1){
					throw new ValidationException("邀请码出现错误，请您联系客服人员");
				}
				else{
					inviter = inviters.get(0);
				}
				Coupon coupon_invitee = new Coupon(invitee.getUserId(), invitationCouponAmount);
				coupon_invitee.setOrigin(CouponOrigin.invitation);
				invitee.incCoupon(invitationCouponAmount);
				UserDao.updateUserBCC(0, 0, invitationCouponAmount, invitee.getUserId(), conn);
				coupon_invitee = CouponDaoService.createCoupon(coupon_invitee,conn);
				coupons.add(coupon_invitee);
				
				Coupon coupon_inviter = new Coupon(inviter.getUserId(), invitationCouponAmount);
				coupon_inviter.setOrigin(CouponOrigin.invitation);
				inviter.incCoupon(invitationCouponAmount);
				UserDao.updateUserBCC(0, 0, invitationCouponAmount, inviter.getUserId(), conn);
				coupon_inviter = CouponDaoService.createCoupon(coupon_inviter,conn);
			}
			user.setCouponList(coupons);
		} finally{
			conn.commit();
			conn.setAutoCommit(true);
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
		Connection conn = EduDaoBasic.getConnection();
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
		Connection conn = EduDaoBasic.getConnection();
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
	
	public static ArrayList<User> getUserByInvitationalCode(String code) throws SQLException{
		UserSearchRepresentation sr = new UserSearchRepresentation();
		sr.setInvitationalCode(code);
		return searchUser(sr);
	}
	
	public static boolean isInvitationalCodeAvaialble(String code) throws SQLException{
		return getUserByInvitationalCode(code).size() == 0;
	}

}
