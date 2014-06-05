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
import BaseModule.exception.notFound.CouponNotFoundException;
import BaseModule.exception.notFound.UserNotFoundException;
import BaseModule.model.Coupon;


public class CouponCleaner extends CouponDao{

	public static void clean() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = EduDaoBasic.getSQLConnection();		
		String ct = DateUtility.toSQLDateTime(DateUtility.getCurTimeInstance());
		String query = "SELECT * FROM CouponDao where status = ? and expireTime < ?";
		Coupon c = null;		
		try{
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, CouponStatus.usable.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();
			while(rs.next()){
				c = createCouponByResultSet(rs);
				c.setStatus(CouponStatus.expired);
				CouponDao.updateCouponInDatabases(c,conn);	
				UserDao.updateUserBCC(0, 0, -c.getAmount(), c.getUserId(), conn);
			}
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}catch(CouponNotFoundException e){
			e.printStackTrace();
			DebugLog.d(e);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			DebugLog.d(e);
		}
		EduDaoBasic.closeResources(conn, stmt, null, true);
	}

}
