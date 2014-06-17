package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.model.Coupon;
import BaseModule.model.Credit;
import BaseModule.model.Transaction;
import BaseModule.model.User;

public class UserCleaner extends UserDao{

	private static boolean validTransactionList(int expectedAmount,ArrayList<Transaction> tlist){
		int amount = 0;
		for(int i=0;i<tlist.size();i++){
			if(tlist.get(i).getTransactionType().code == TransactionType.withdraw.code){
				amount -= tlist.get(i).getTransactionAmount();
			}else{
				amount += tlist.get(i).getTransactionAmount();
			}
		}
		
		return amount == expectedAmount;
	}
	
	private static boolean validCouponList(int expectedAmount, ArrayList<Coupon> clist){
		int amount = 0;
		for(int i=0;i<clist.size();i++){
			if(clist.get(i).getStatus().code == CouponStatus.inactive.code ||
					clist.get(i).getStatus().code == CouponStatus.usable.code){
				amount += clist.get(i).getAmount();
			}
		}
		return amount == expectedAmount;
	}
	
	private static boolean validCreditList(int expectedAmount, ArrayList<Credit> clist){
		int amount = 0;
		for(int i=0;i<clist.size();i++){
			if(clist.get(i).getStatus().code == CreditStatus.usable.code){
				amount += clist.get(i).getAmount();
			}
		}
		return amount == expectedAmount;
	}
	
	public static void clean(){	
		Connection conn = null;
		ArrayList<User> ulist = new ArrayList<User>();		
		conn = EduDaoBasic.getConnection();
		try {
			ulist = UserDao.getAllUsers(conn);
			for(int i=0;i<ulist.size();i++){
				try{
					conn.setAutoCommit(false);
					UserDao.selectUserForUpdate(ulist.get(i).getUserId(), conn);
					
					boolean validTransactionList = validTransactionList(ulist.get(i).getBalance(),ulist.get(i).getTransactionList());
					boolean validCreditList = validCouponList(ulist.get(i).getCoupon(),ulist.get(i).getCouponList());
					boolean validCouponList = validCreditList(ulist.get(i).getCredit(),ulist.get(i).getCreditList());
					
					if(!validTransactionList || !validCreditList || !validCouponList){
						String bstr = validTransactionList ? "Pass" : "Failed";
						String crestr = validCreditList ? "Pass" : "Failed";
						String coustr = validCouponList ? "Pass" : "Failed";
						System.out.println("user: " + ulist.get(i).getUserId() + " balance account: " + bstr + " credit account: " + crestr + " coupon account: " + coustr);
						System.out.println("user: " + ulist.get(i).getUserId() + " balance account: " + ulist.get(i).getBalance() + " coupon account: " + ulist.get(i).getCoupon() + " credit account: " + ulist.get(i).getCredit());
						
						DebugLog.c_d(ulist.get(i).getUserId(), validTransactionList, validCreditList, validCouponList);
					}	
					
					conn.commit();
					conn.setAutoCommit(true);
				}
				catch (SQLException | UserNotFoundException e){
					conn.rollback();
					DebugLog.d(e);
				}
			}
		} catch (SQLException e) {
			DebugLog.d(e);
		} finally{
			EduDaoBasic.closeResources(conn, null, null, true);
		}
			
		
	}
}
