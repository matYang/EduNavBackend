package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import BaseModule.common.DateUtility;
import BaseModule.concurrentTest.CocurrentCreatingTest;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.PseudoException;
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.exception.notFound.UserNotFoundException;
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
	public static void updateCoupons(ArrayList<Coupon>clist, Connection...connections) throws CouponNotFoundException, SQLException{
		CouponDao.updateCouponsInDatabases(clist, connections);
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
			UserDaoService.selectUserForUpdate(c.getUserId(), conn);
			
			c = createCoupon(c, conn);			
			UserDaoService.updateUserBCC(0, 0, c.getAmount(), c.getUserId(), conn);	
			UserDaoService.selectUserForUpdate(c.getUserId(), conn);
			//For ConcurrentCreation
			CocurrentCreatingTest.en(c.getAmount());
			CocurrentCreatingTest.incc();			
			ok = true;

		} finally{
			EduDaoBasic.handleCommitFinally(conn, ok, EduDaoBasic.shouldConnectionClose(connections));
		}
		return c;
	}

	public static String getCouponRecord(int userId,int cashback,Connection...connections) throws ValidationException,SQLException, PseudoException{
		Connection conn = null;		
		Boolean ok = false;		
		ArrayList<Coupon> clist = new ArrayList<Coupon>();
		ArrayList<Coupon> vlist = new ArrayList<Coupon>();
		ArrayList<Coupon> usedlist = new ArrayList<Coupon>();	
		Coupon c = null;		
		User user = null;			
		int amount = 0;
		int i = 0;
		int totalCoupon = 0;
		String couponRecord = "";		

		try{
			conn = EduDaoBasic.getConnection(connections);			
			conn.setAutoCommit(false);

			user = UserDaoService.selectUserForUpdate(userId, conn);
			
			if(user == null){				
				/*      Used By ConcurrentCreatingTest              */
				CocurrentCreatingTest.inc();
				throw new UserNotFoundException("订单用户不存在");
			}
			totalCoupon = user.getCoupon();
			
			clist = CouponDaoService.getCouponByUserId(userId, conn);

			//System.out.println("clist num: " + clist.size());
			if(clist.size()==0){
				return couponRecord;			
			}
			//Sort
			Collections.sort(clist, DateUtility.couponExpireComparator);
			
			while(i < clist.size()){
				c = clist.get(i);
				String ct = DateUtility.toSQLDateTime(DateUtility.getCurTimeInstance());
				String expireTime = DateUtility.toSQLDateTime(c.getExpireTime());
				//Check if usable
				if(c.getStatus().code == CouponStatus.usable.code && ct.compareTo(expireTime) < 0){	
					amount += c.getAmount();
					vlist.add(c);
				}				
				i++;
			}
			
			if(amount != totalCoupon){
				CocurrentCreatingTest.inc();
				throw new ValidationException("账户出错！ 需要管理员处理");
			}			
			
			amount = 0;
			i = 0;
			
			while(i < vlist.size() && amount < cashback){
				amount += vlist.get(i).getAmount();
				usedlist.add(vlist.get(i));
				i++;
			}
			
			if(vlist.size()==0 || usedlist.size()==0){
				CocurrentCreatingTest.inc();
				return couponRecord;
			}
			
			//update coupons
			for(int k=0;k<usedlist.size()-1;k++){		
				couponRecord += usedlist.get(k).getCouponId() + "_" + usedlist.get(k).getAmount() + "-";
				usedlist.get(k).setStatus(CouponStatus.used);				
			}

			if(amount > cashback){				
				int newAmount = amount-cashback;
				couponRecord += usedlist.get(usedlist.size()-1).getCouponId() + "_" + (usedlist.get(usedlist.size()-1).getAmount()-newAmount) + "-";				
				usedlist.get(usedlist.size()-1).setAmount(newAmount);
			}else{
				if (usedlist.size() > 0){
					couponRecord += usedlist.get(usedlist.size()-1).getCouponId() + "_" + usedlist.get(usedlist.size()-1).getAmount() + "-";
					usedlist.get(usedlist.size()-1).setStatus(CouponStatus.used);		
				}
			}

			//update coupons in database
			updateCoupons(usedlist,conn);		

			//System.out.println("amount: "  + amount + " totalCoupon: " +totalCoupon);
			//update user in database
			if(amount >= cashback){			
				UserDaoService.updateUserBCC(0, 0, -cashback, userId, conn);
				/*      Used By ConcurrentCreatingTest              */
				CocurrentCreatingTest.dn(cashback);				
				//System.out.println("user account - " + cashback);
			}else{				
				UserDaoService.updateUserBCC(0, 0, -amount, userId, conn);
				/*      Used By ConcurrentCreatingTest              */
				CocurrentCreatingTest.dn(amount);			
				//System.out.println("user account - " + amount);
			}

			ok = true;
		}finally{
			//EduDaoBasic.handleCommitFinally(conn, ok,EduDaoBasic.shouldConnectionClose(connections));

		}
		return couponRecord;
	}

	public static void updateCouponToUser(Coupon c, int previousAmount, CouponStatus previousStatus,Connection...connections) throws SQLException, PseudoException{
		boolean ok = false;
		Connection conn = null;
		try{
			conn =  EduDaoBasic.getConnection(connections);
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
