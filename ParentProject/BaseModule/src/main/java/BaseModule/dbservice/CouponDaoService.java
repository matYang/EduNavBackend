package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.CouponDao;
import BaseModule.exception.coupon.CouponNotFoundException;
import BaseModule.model.Coupon;

public class CouponDaoService {

	public static Coupon createCoupon(Coupon c,Connection...connections) throws SQLException{
		return CouponDao.addCouponToDatabases(c,connections);
	}
	
	public static void updateCoupon(Coupon c,Connection...connections) throws CouponNotFoundException, SQLException{
		CouponDao.updateCouponInDatabases(c,connections);
	}
	
	public static ArrayList<Coupon> getCouponByUserId(int userId){
		return CouponDao.getCouponByUserId(userId);
	}
	
	public static Coupon getCouponByCouponId(int couponId,Connection...connections){
		return CouponDao.getCouponByCouponId(couponId,connections);
	}
}
