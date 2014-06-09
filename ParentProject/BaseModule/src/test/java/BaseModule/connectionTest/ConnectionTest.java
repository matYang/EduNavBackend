package BaseModule.connectionTest;

import java.sql.Connection;
import java.util.ArrayList;

import org.junit.Test;

import BaseModule.eduDAO.EduDaoBasic;

public class ConnectionTest {

	@Test
	public void testConnections(){
		ArrayList<Connection> clist = new ArrayList<Connection>();
		int connectionNum = 150;
		for(int i = 0; i < connectionNum; i ++){
			clist.add(EduDaoBasic.getConnection());
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		
		for(int i = 0; i < connectionNum; i ++){
			EduDaoBasic.closeResources(clist.get(i), null, null, true);
		}
	}
}

