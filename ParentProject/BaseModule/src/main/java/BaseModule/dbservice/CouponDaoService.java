package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Coupon;
import BaseModule.model.User;
import BaseModule.model.representation.CouponSearchRepresentation;

public class CouponDaoService {

	public static Coupon createCoupon(Coupon c,Connection...connections) throws SQLException{
		return CouponDao.addCouponToDatabases(c,connections);
	}
	
	public static void updateCoupon(Coupon c,Connection...connections) throws PseudoException, SQLException{
		CouponDao.updateCouponInDatabases(c,connections);
	}
	
	public static Coupon getCouponByCouponId(int couponId,Connection...connections) throws SQLException, PseudoException{
		return CouponDao.getCouponByCouponId(couponId,connections);
	}
	
	public static ArrayList<Coupon> getCouponByUserId(int userId) throws SQLException{
		CouponSearchRepresentation coup_sr = new CouponSearchRepresentation();
		coup_sr.setUserId(userId);
		return searchCoupon(coup_sr);
	}
	
	public static Coupon addCouponToUser(Coupon c) throws SQLException, PseudoException{
		Connection conn = null;
		try{
			conn =  EduDaoBasic.getConnection();
			conn.setAutoCommit(false);

			c = createCoupon(c, conn);
			UserDao.updateUserBCC(0, 0, c.getAmount(), c.getUserId(), conn);
			
			conn.commit();
			conn.setAutoCommit(true);
			return c;
		} finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}
	}
	
	
	public static void updateCouponToUser(Coupon c, int previousAmount, CouponStatus previousStatus) throws SQLException, PseudoException{
		Connection conn = null;
		try{
			conn =  EduDaoBasic.getConnection();
			conn.setAutoCommit(false);

			if (previousStatus == CouponStatus.usable || c.getStatus() == CouponStatus.inactive){
				if (c.getStatus() == CouponStatus.usable || c.getStatus() == CouponStatus.inactive){
					if (c.getAmount()-previousAmount != 0){
						UserDao.updateUserBCC(0, 0, c.getAmount()-previousAmount, c.getUserId(), conn);
					}
				}
				else if (c.getStatus() == CouponStatus.used || c.getStatus() == CouponStatus.expired){
					UserDao.updateUserBCC(0, 0, -previousAmount, c.getUserId(), conn);
				}
			}
			else if (previousStatus == CouponStatus.used || previousStatus == CouponStatus.expired){
				if (c.getStatus() == CouponStatus.usable || c.getStatus() == CouponStatus.inactive){
					UserDao.updateUserBCC(0, 0, c.getAmount(), c.getUserId(), conn);
				}
				else if (c.getStatus() == CouponStatus.used || c.getStatus() == CouponStatus.expired){
					//do nothing to use's coupon balance
				}
			}
			else{
				throw new ValidationException("未能识别消费券更新状态");
			}
			updateCoupon(c, conn);
			
			conn.commit();
			conn.setAutoCommit(true);
		} finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}
	}

	
	public static ArrayList<Coupon> searchCoupon(CouponSearchRepresentation coup_sr) throws SQLException{
		return CouponDao.searchCoupon(coup_sr);
	}
}
