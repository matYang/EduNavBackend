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
import BaseModule.exception.credit.CreditNotFoundException;
import BaseModule.exception.user.UserNotFoundException;
import BaseModule.model.Credit;

public class CreditCleaner extends CreditDao{

	public static void clean() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = EduDaoBasic.getSQLConnection();		
		String ct = DateUtility.toSQLDateTime(DateUtility.getCurTimeInstance());			
		String query = "SELECT * FROM CreditDao where ((status = ? and expireTime < ?) or (status =? and usableTime <= ?))";
		Credit c = null;
		try{
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, CreditStatus.usable.code);
			stmt.setString(2, ct);
			stmt.setInt(3, CreditStatus.awaiting.code);
			stmt.setString(4, ct);
			rs = stmt.executeQuery();
			while(rs.next()){
				c = createCreditByResultSet(rs);	
				if(c.getStatus().code == CreditStatus.usable.code){
					c.setStatus(CreditStatus.expired);
				}else if(c.getStatus().code == CreditStatus.awaiting.code){
					c.setStatus(CreditStatus.usable);
				}	

				CreditDao.updateCreditInDatabases(c,conn);

				if(c.getStatus().code == CreditStatus.usable.code){
					c.setAmount(-c.getAmount());
					CreditDao.addCreditToUser(c, conn);
				}

			}
		}catch(SQLException e){
			e.printStackTrace();
			DebugLog.d(e);
		}catch(CreditNotFoundException e){
			e.printStackTrace();
			DebugLog.d(e);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			DebugLog.d(e);
		}

		EduDaoBasic.closeResources(conn, stmt, null, true);
	}


}
