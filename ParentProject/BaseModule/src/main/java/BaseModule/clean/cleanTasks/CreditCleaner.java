package BaseModule.clean.cleanTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
			String query = "SELECT * FROM CreditDao where ((status = ? and expireTime < ?)";
			Credit c = null;
			
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, CreditStatus.usable.code);
			stmt.setString(2, ct);
			rs = stmt.executeQuery();
			
			transientConnection = EduDaoBasic.getConnection();
			transientConnection.setAutoCommit(false);
			while(rs.next()){
				try{
					c = createCreditByResultSet(rs);
					c.setStatus(CreditStatus.expired);
					
					UserDao.updateUserBCC(0, -c.getAmount(), 0, c.getUserId(), transientConnection);
					CreditDao.updateCreditInDatabases(c,transientConnection);
					
					transientConnection.commit();
				} catch (Exception e){
					transientConnection.rollback();
					DebugLog.d(e);
				}
			}
			transientConnection.setAutoCommit(true);
		} catch(Exception e){
			DebugLog.d(e);
		} finally{
			EduDaoBasic.closeResources(conn, stmt, rs, true);
			EduDaoBasic.closeResources(transientConnection, null, null, true);
		}
	}


}
