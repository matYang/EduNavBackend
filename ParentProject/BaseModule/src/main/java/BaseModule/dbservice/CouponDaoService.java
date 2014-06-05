package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.CouponDao;
import BaseModule.exception.coupon.CouponNotFoundException;
import BaseModule.model.Coupon;

public class CouponDaoService {

	public static Coupon createCoupon(Coupon c) throws SQLException{
		return CouponDao.addCouponToDatabases(c);
	}
	
	public static void updateCoupon(Coupon c,Connection...connections) throws CouponNotFoundException, SQLException{
		CouponDao.updateCouponInDatabases(c,connections);
	}
	
	public static ArrayList<Coupon> getCouponByUserId(int userId){
		return CouponDao.getCouponByUserId(userId);
	}
	
	public static Coupon getCouponByCouponId(int couponId){
		return CouponDao.getCouponByCouponId(couponId);
	}
}
