package BaseModule.asyncRelayExecutor;

import BaseModule.interfaces.PseudoAsyncTask;


public class RelayTaskExecutableWrapper implements Runnable{
	
	private final PseudoAsyncTask task;
	
	public RelayTaskExecutableWrapper(final PseudoAsyncTask task){
		this.task = task;
	}
		 
	public void run(){
		this.task.execute();
	}

}
