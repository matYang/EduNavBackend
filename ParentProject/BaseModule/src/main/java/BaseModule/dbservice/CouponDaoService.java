package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.CouponDao;
import BaseModule.exception.PseudoException;
import BaseModule.model.Coupon;
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

	
	public static ArrayList<Coupon> searchCoupon(CouponSearchRepresentation coup_sr) throws SQLException{
		return CouponDao.searchCoupon(coup_sr);
	}
}
