package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.eduDAO.CreditDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.model.Credit;

public class CreditCleaner extends CreditDao{

	public static void clean() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		Connection transientConnection = null;
					
		try{
			conn = EduDaoBasic.getConnection();		
			String ct = DateUtility.toSQLDateTime(DateUtility.getCurTimeInstance());
			String query = "SELECT * FROM CreditDao where (status = ? and expireTime < ?)";
			Credit credit = null;
			
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, CreditStatus.usable.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();
			
			//use a new transient 
			transientConnection = EduDaoBasic.getConnection();
			transientConnection.setAutoCommit(false);
			while(rs.next()){
				try{
					credit = createCreditByResultSet(rs);
					
					//lock the user, once user is locked, any related booking, coupon and credit operations are guaranteed to be atomic
					UserDao.selectUserForUpdate(credit.getUserId(), transientConnection);
					
					//fetch a fresh new copy of the credit, since user is now locked, operations can be considered atomic
					credit = CreditDao.getCreditByCreditId(credit.getCreditId(), transientConnection);
					
					//double check before proceeding, though protection against high concurrency is limited
					if (credit.getStatus() == CreditStatus.usable && credit.getExpireTime().compareTo(DateUtility.getCurTimeInstance()) < 0){
						//set credit as expired in database
						credit.setStatus(CreditStatus.expired);
						CreditDao.updateCreditInDatabases(credit,transientConnection);
						
						//update user's credit balance
						UserDao.updateUserBCC(0, -credit.getAmount(), 0, credit.getUserId(), transientConnection);
					}
					
					//commit mark end of transaction, release lock on user, mark the end of any operations on user, booking, credit, coupon
					transientConnection.commit();
				} catch (Exception e){
					transientConnection.rollback();
					DebugLog.d(e);
				}
			}
			transientConnection.setAutoCommit(true);
		} catch(Exception e){
			DebugLog.d(e);
			if (transientConnection != null){
				try {
					transientConnection.rollback();
				} catch (SQLException e1) {
					DebugLog.d(e1);
				}
			}
		} finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			EduDaoBasic.closeResources(transientConnection, null, null, true);
		}
	}

}
