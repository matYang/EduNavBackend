package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.eduDAO.CouponDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.eduDAO.UserDao;
import BaseModule.model.Coupon;


public class CouponCleaner extends CouponDao{

	public static void clean() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;	
		Connection transientConnection = null;
		
		String ct = DateUtility.toSQLDateTime(DateUtility.getCurTimeInstance());
		String query = "SELECT * FROM CouponDao where (status = ? or status = ?) and expireTime < ?";
		Coupon coupon = null;		
		try{
			conn = EduDaoBasic.getConnection();	
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, CouponStatus.inactive.code);
			stmt.setInt(2, CouponStatus.usable.code);
			stmt.setString(3, ct);
			rs = stmt.executeQuery();
			
			transientConnection = EduDaoBasic.getConnection();
			transientConnection.setAutoCommit(false);
			while(rs.next()){
				try{
					coupon = createCouponByResultSet(rs);
					
					//lock user; now user, booking, coupon or credit can begin
					UserDao.selectUserForUpdate(coupon.getUserId(), transientConnection);
					
					//fetch a fresh copy of coupon, guard during concurrency, though protection against high concurrency is limited
					coupon = CouponDao.getCouponByCouponId(coupon.getCouponId(), transientConnection);
					
					//double check preconditions
					if ( (coupon.getStatus() == CouponStatus.inactive || coupon.getStatus() == CouponStatus.usable)  && coupon.getExpireTime().compareTo(DateUtility.getCurTimeInstance()) < 0){
						//set coupon as expired
						coupon.setStatus(CouponStatus.expired);
						CouponDao.updateCouponInDatabases(coupon,transientConnection);	
						
						//update uer's account balance
						UserDao.updateUserBCC(0, 0, -coupon.getAmount(), coupon.getUserId(), transientConnection);
					}
					
					//commit changes, end transaction/release lock on that user
					transientConnection.commit();
				} catch (Exception e){
					transientConnection.rollback();
					DebugLog.d(e);
				}
			}
			transientConnection.setAutoCommit(true);
		}catch(Exception e){
			DebugLog.d(e);
			if (transientConnection != null){
				try {
					transientConnection.rollback();
				} catch (SQLException e1) {
					DebugLog.d(e1);
				}
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			EduDaoBasic.closeResources(transientConnection, null, null, true);
		}
	}
}
