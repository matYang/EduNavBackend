package BaseModule.dbservice;

import java.util.ArrayList;

import BaseModule.eduDAO.CouponDao;
import BaseModule.exception.coupon.CouponNotFoundException;
import BaseModule.model.Coupon;

public class CouponDaoService {

	public static Coupon createCoupon(Coupon c){
		return CouponDao.addCouponToDatabases(c);
	}
	
	public static void updateCoupon(Coupon c) throws CouponNotFoundException{
		CouponDao.updateCouponInDatabases(c);
	}
	
	public static ArrayList<Coupon> getCouponByUserId(int userId){
		return CouponDao.getCouponByUserId(userId);
	}
	
	public static Coupon getCouponByCouponId(int couponId){
		return CouponDao.getCouponByCouponId(couponId);
	}
}
