package BaseModule.cleanerTest;

import java.sql.SQLException;
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
import BaseModule.factory.ReferenceFactory;
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
			for(int i=0;i<=userNum;i++){				
				String name = "userName " + i;
				String phone = "1234567890" + i;
				String password = "userPassword " + i;
				AccountStatus status = AccountStatus.fromInt(i%3);
				String email = "userEmail " + i;
				String accountNum = ReferenceFactory.generateUserAccountNumber();				
				String invitationalCode = ReferenceFactory.generateUserInvitationalCode();
				User user = new User(phone, password, "", invitationalCode, accountNum, status);
				user.setName(name);
				user.setEmail(email);				
				UserDao.addUserToDatabase(user);			
			}
			
			// Transactions			
			int transactionNum = 20;
			for(int i=1;i<=transactionNum;i++){
				User user = UserDao.getUserById(i);						
				int amount = 2000;
				Transaction transaction = new Transaction(user.getUserId(),i,amount,TransactionType.fromInt(i%4));
				try {
					TransactionDao.addTransactionToDatabases(transaction);
					if(transaction.getTransactionType().code == TransactionType.withdraw.code){
						amount = -amount;
					}					
					UserDao.updateUserBCC(amount, 0, 0, user.getUserId());
				} catch (SQLException e) {				
					e.printStackTrace();
				}
			}
			//Coupon
			int couponNum = 20;			
			Calendar expireTime = DateUtility.getCurTimeInstance();
			for(int i=1;i<=couponNum;i++){
				int bookingId = i;
				int userId = i;
				int amount = 50;
				expireTime.add(Calendar.MINUTE, i);
				Coupon c = new Coupon(userId, amount);
				c.setBookingId(bookingId);
				c.setExpireTime(expireTime);
				c.setStatus(CouponStatus.fromInt(i%4));
				try {
					CouponDao.addCouponToDatabases(c);
					if(c.getStatus().code == CouponStatus.inactive.code ||
							c.getStatus().code == CouponStatus.usable.code){
						UserDao.updateUserBCC(0, 0, amount, userId);
					}					
										
				} catch (SQLException e) {			
					e.printStackTrace();
				}

			}
			//Credit
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
					if(c.getStatus().code == CreditStatus.usable.code){
						UserDao.updateUserBCC(0, amount, 0, userId);
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
