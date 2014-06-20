package BaseModule.asyncRelayExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import BaseModule.asyncTask.SMSTask;
import BaseModule.interfaces.PseudoAsyncTask;


public class ExecutorProvider {
	private static final int threadPool_max_sms = 50;
	private static final ExecutorService smsExecutor = Executors.newFixedThreadPool(threadPool_max_sms);
	
	public static void executeRelay (final PseudoAsyncTask task){
		RelayTaskExecutableWrapper executableTask = new RelayTaskExecutableWrapper(task);
		if (task instanceof SMSTask){
			smsExecutor.submit(executableTask);
		}
		
	}

}
