package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import BaseModule.common.DateUtility;
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

	public static ArrayList<Coupon> getCouponByUserId(int userId,Connection...connections) throws SQLException{
		CouponSearchRepresentation coup_sr = new CouponSearchRepresentation();
		coup_sr.setUserId(userId);
		return searchCoupon(coup_sr,connections);
	}

	public static Coupon addCouponToUser(Coupon c,Connection...connections) throws SQLException, PseudoException{
		boolean ok = false;
		Connection conn = null;				
		try{
			conn =  EduDaoBasic.getConnection(connections);				
			conn.setAutoCommit(false);			
			UserDaoService.getAndLock(c.getUserId(), conn);
			
			c = createCoupon(c, conn);			
			UserDaoService.updateUserBCC(0, 0, c.getAmount(), c.getUserId(), conn);	
			
			ok = true;
		} finally{
			EduDaoBasic.handleCommitFinally(conn, ok, EduDaoBasic.shouldConnectionClose(connections));
		}
		return c;
	}

	public static String getCouponRecord(int userId,int cashback,Connection...connections) throws ValidationException,SQLException, PseudoException{
		Connection conn = null;		
		Boolean ok = false;		
		String couponRecord = "";		

		try{
			conn = EduDaoBasic.getConnection(connections);			
			conn.setAutoCommit(false);

			User user = UserDaoService.getAndLock(userId, conn);
			
			ArrayList<Coupon> couponList = CouponDaoService.getCouponByUserId(userId, conn);
			if(couponList.size()==0){
				return couponRecord;			
			}
			//Sort by expire time
			Collections.sort(couponList, DateUtility.couponExpireComparator);
			
			int amount = 0;
			ArrayList<Coupon> validationList = new ArrayList<Coupon>();
			for (Coupon c : couponList){
				String ct = DateUtility.toSQLDateTime(DateUtility.getCurTimeInstance());
				String expireTime = DateUtility.toSQLDateTime(c.getExpireTime());

				//Check if usable and inactive amounts sum up to equal user's coupon balance
				if((c.getStatus() == CouponStatus.usable || c.getStatus() == CouponStatus.inactive) && ct.compareTo(expireTime) < 0){	
					amount += c.getAmount();
					//but can only use usable coupons
					if (c.getStatus() == CouponStatus.usable){
						validationList.add(c);
					}
				}	
			}
			
			if(amount != user.getCoupon()){				
				throw new ValidationException("账户信息合计出错！ 需要管理员处理");
			}			
			
			amount = 0;
			ArrayList<Coupon> usedlist = new ArrayList<Coupon>();
			for (int i = 0; i < validationList.size() && amount < cashback; i++){
				amount += validationList.get(i).getAmount();
				usedlist.add(validationList.get(i));
			}

			
			if(validationList.size()==0 || usedlist.size()==0){				
				return couponRecord;
			}
			
			//update coupons
			for(int i = 0; i < usedlist.size()-1; i++){		
				couponRecord += usedlist.get(i).getCouponId() + "_" + usedlist.get(i).getAmount() + "-";
				usedlist.get(i).setAmount(0);	
				usedlist.get(i).setStatus(CouponStatus.used);				
			}
			
			if(amount > cashback){				
				int newAmount = amount-cashback;
				couponRecord += usedlist.get(usedlist.size()-1).getCouponId() + "_" + (usedlist.get(usedlist.size()-1).getAmount()-newAmount);				
				usedlist.get(usedlist.size()-1).setAmount(newAmount);
			}else{
				if (usedlist.size() > 0){
					couponRecord += usedlist.get(usedlist.size()-1).getCouponId() + "_" + usedlist.get(usedlist.size()-1).getAmount();
					usedlist.get(usedlist.size()-1).setAmount(0);
					usedlist.get(usedlist.size()-1).setStatus(CouponStatus.used);		
				}
			}

			//update coupons and user account
			for(int i =0 ; i < usedlist.size() ; i++){
				updateCoupon(usedlist.get(i),conn);
			}
			UserDaoService.updateUserBCC(0, 0, amount >= cashback ? -cashback : -amount, userId, conn);

			ok = true;
		}finally{
			EduDaoBasic.handleCommitFinally(conn, ok, EduDaoBasic.shouldConnectionClose(connections));
		}
		return couponRecord;
	}

	public static void updateCouponToUser(Coupon c, int previousAmount, CouponStatus previousStatus,Connection...connections) throws SQLException, PseudoException{
		boolean ok = false;
		Connection conn = null;
		try{
			conn =  EduDaoBasic.getConnection(connections);
			conn.setAutoCommit(false);
			UserDao.getAndLock(c.getUserId(), conn);
			
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

			//must always always leave ok to the last line
			ok = true;
		} finally{
			EduDaoBasic.handleCommitFinally(conn, ok,EduDaoBasic.shouldConnectionClose(connections));
		}
	}		
	
	public static ArrayList<Coupon> searchCoupon(CouponSearchRepresentation coup_sr,Connection...connections) throws SQLException{
		return CouponDao.searchCoupon(coup_sr,connections);
	}
}
