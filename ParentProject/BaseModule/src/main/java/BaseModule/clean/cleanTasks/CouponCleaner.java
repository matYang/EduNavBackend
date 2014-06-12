package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		Coupon c = null;		
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
					c = createCouponByResultSet(rs);
					c.setStatus(CouponStatus.expired);
					CouponDao.updateCouponInDatabases(c,transientConnection);	
					UserDao.updateUserBCC(0, 0, -c.getAmount(), c.getUserId(), transientConnection);
					
					transientConnection.commit();
				} catch (Exception e){
					transientConnection.rollback();
					DebugLog.d(e);
				}
			}
			transientConnection.setAutoCommit(true);
		}catch(Exception e){
			DebugLog.d(e);
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			EduDaoBasic.closeResources(transientConnection, null, null, true);
		}
		
	}

}
