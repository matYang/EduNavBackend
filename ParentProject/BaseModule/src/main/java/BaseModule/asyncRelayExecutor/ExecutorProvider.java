package BaseModule.asyncRelayExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import BaseModule.asyncTask.SMSTask;
import BaseModule.interfaces.PseudoAsyncTask;


public class ExecutorProvider {
	private static final int threadPool_max_sms = 50;
	private static final ExecutorService smsExecutor = Executors.newFixedThreadPool(threadPool_max_sms);
	
	public static Future<Boolean> executeRelay (final PseudoAsyncTask task){
		if (task instanceof SMSTask){
			return smsExecutor.submit(new Callable<Boolean>() {
		         public Boolean call() {
		             return task.execute();
		         }});
		}
		
		return null;
	}

}
