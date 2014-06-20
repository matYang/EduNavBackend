package BaseModule.cleanerTest;

import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import BaseModule.clean.cleanTasks.UserCleaner;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.configurations.EnumConfig.TransactionType;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.TransactionDao;
import BaseModule.eduDAO.UserDao;
import BaseModule.generator.ReferenceGenerator;
import BaseModule.model.Coupon;
import BaseModule.model.Credit;
import BaseModule.model.Transaction;
import BaseModule.model.User;

public class UserCleanerTest {

	@Test
	public void test(){
		EduDaoBasic.clearAllDatabase();
		try{
			int userNum = 20;
			for(int i=0;i<userNum;i++){				
				String name = "userName " + i;
				String phone = "1234567890" + i;
				String password = "userPassword " + i;
				AccountStatus status = AccountStatus.fromInt(i%3);
				String email = "userEmail " + i;
				String accountNum = ReferenceGenerator.generateUserAccountNumber();				
				String invitationalCode = ReferenceGenerator.generateUserInvitationalCode();
				User user = new User(phone, password, "", invitationalCode, accountNum, status);
				user.setName(name);
				user.setEmail(email);				
				UserDao.addUserToDatabase(user);			
			}
			
			// Transactions		
			// 1,3,4,5,7,8,9,11,12,13,15,16,17,19,20 should be 2000
			// 2,6,10,14,18 should be -2000
			int transactionNum = 20;
			for(int i=1;i<=transactionNum;i++){
				User user = UserDao.getUserById(i);						
				int amount = 2000;
				Transaction transaction = new Transaction(user.getUserId(),i,amount,TransactionType.fromInt(i%4));
				try {
					TransactionDao.addTransactionToDatabases(transaction);									
					if(i==1){
						UserDao.updateUserBCC(999999999, 0, 0, user.getUserId());
					}else if(i==2){
						UserDao.updateUserBCC(-999999999, 0, 0, user.getUserId());
					}else if(i%2==0){
						UserDao.updateUserBCC(amount, 0, 0, user.getUserId());
					}else{
						UserDao.updateUserBCC(-amount, 0, 0, user.getUserId());
					}					
				} catch (SQLException e) {				
					e.printStackTrace();
				}
			}
			//Coupons
			// 1,2,5,6,9,10,13,14,17,18 should be 0
			// 3,4,7,8,11,12,15,16,19,20 should be 50
			int couponNum = 20;			
			Calendar expireTime = DateUtility.getCurTimeInstance();
			for(int i=1;i<=couponNum;i++){				
				int userId = i;
				int amount = 50;
				expireTime.add(Calendar.MINUTE, i);
				Coupon c = new Coupon(userId, amount);				
				c.setExpireTime(expireTime);
				c.setStatus(CouponStatus.fromInt(i%4));
				try {
					CouponDao.addCouponToDatabases(c);
					if(i==1){
						UserDao.updateUserBCC(0, 0, 999999999, userId);
					}else if(i==2){
						UserDao.updateUserBCC(0, 0, -999999999, userId);
					}else if(i%2==0){
						UserDao.updateUserBCC(0, 0, amount, userId);
					}else{
						UserDao.updateUserBCC(0, 0, -amount, userId);
					}					
										
				} catch (SQLException e) {			
					e.printStackTrace();
				}

			}
			//Credit
			//1,2,4,5,7,8,10,11,13,14,16,17,19,20 should be 0
			//3,6,9,12,15,18 should be 50
			int creditNum = 20;
			int amount = 50;
			expireTime = DateUtility.getCurTimeInstance();
			for(int i=1;i<=creditNum;i++){
				int bookingId = i;
				int userId = i;
				amount = 50;
				expireTime.add(Calendar.MINUTE, i);		
				Credit c = new Credit(bookingId,userId,amount);
				c.setExpireTime(expireTime);
				c.setStatus(CreditStatus.fromInt(i%3));

				try {
					CreditDao.addCreditToDatabases(c);
					if(i==1){
						UserDao.updateUserBCC(0,999999999, 0, userId);
					}else if(i==2){
						UserDao.updateUserBCC(0,-999999999, 0, userId);
					}else if(i%2==0){
						UserDao.updateUserBCC(0, amount, 0, userId);
					}else{
						UserDao.updateUserBCC(0, -amount, 0, userId);
					}	
					
				} catch (SQLException e) {			
					e.printStackTrace();
				}
			}	
		}catch(Exception e){
			DebugLog.d(e);
		}
		
		UserCleaner.clean();
		
		
	}
}
