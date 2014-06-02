package BaseModule.eduDAOTest;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;


import net.spy.memcached.internal.OperationFuture;

import org.junit.Test;

import BaseModule.eduDAO.EduDaoBasic;

public class CacheTest {

	@Test
	public void test() throws InterruptedException, ExecutionException {
		OperationFuture<Boolean> future = EduDaoBasic.setCache("lol", "dasdas");
		future.get();
		future = EduDaoBasic.setCache("a2a", "lalala");
		Thread.sleep(10);
		assertTrue("dasdas".equals(EduDaoBasic.getCache("lol")));
		assertTrue("lalala".equals(EduDaoBasic.getCache("a2a")));
		future = EduDaoBasic.deleteCache("a2a");
		future.get();
		assertTrue(EduDaoBasic.getCache("a2a") == null);
	}

}
