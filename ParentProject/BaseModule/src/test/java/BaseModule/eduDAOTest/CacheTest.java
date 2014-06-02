package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;


import net.spy.memcached.internal.OperationFuture;

import org.junit.Test;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.model.User;

public class CacheTest {

	@Test
	public void test() throws InterruptedException, ExecutionException {
		OperationFuture<Boolean> future = EduDaoBasic.setCache("lol", 3600, "dasdas");
		future.get();
		future = EduDaoBasic.setCache("a2a", 3600, "lalala");
		Thread.sleep(10);
		assertTrue("dasdas".equals(EduDaoBasic.getCache("lol")));
		assertTrue("lalala".equals(EduDaoBasic.getCache("a2a")));
		future = EduDaoBasic.deleteCache("a2a");
		future.get();
		assertTrue(EduDaoBasic.getCache("a2a") == null);
	}
	
	@Test
	public void testObj() throws Exception{
		User user = new User("matthew", "18662241356", "111111", AccountStatus.activated, "uwse@me.com");
		OperationFuture<Boolean> future = EduDaoBasic.setCache("lol2", 3600, user);
		future.get();
		User user2 = (User) EduDaoBasic.getCache("lol2");
		assertTrue(user.equals(user2));
	}

}
