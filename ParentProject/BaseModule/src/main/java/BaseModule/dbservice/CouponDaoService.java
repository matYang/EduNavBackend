package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.CouponDao;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.model.Coupon;
import BaseModule.model.representation.CouponSearchRepresentation;

public class CouponDaoService {

	public static Coupon createCoupon(Coupon c,Connection...connections) throws SQLException{
		return CouponDao.addCouponToDatabases(c,connections);
	}
	
	public static void updateCoupon(Coupon c,Connection...connections) throws CouponNotFoundException, SQLException{
		CouponDao.updateCouponInDatabases(c,connections);
	}
	
	public static Coupon getCouponByCouponId(int couponId,Connection...connections){
		return CouponDao.getCouponByCouponId(couponId,connections);
	}
	
	public static ArrayList<Coupon> getCouponByUserId(int userId){
		CouponSearchRepresentation coup_sr = new CouponSearchRepresentation();
		coup_sr.setUserId(userId);
		return searchCoupon(coup_sr);
	}

	
	public static ArrayList<Coupon> searchCoupon(CouponSearchRepresentation coup_sr){
		return null;
	}
}
