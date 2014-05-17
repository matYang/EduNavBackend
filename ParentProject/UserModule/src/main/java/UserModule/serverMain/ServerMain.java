package UserModule.serverMain;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;

import BaseModule.common.DebugLog;
import BaseModule.staticDataService.SystemDataInit;
import UserModule.appService.CleanService;
import UserModule.appService.RoutingService;

public class ServerMain {

	//private static Log log = LogFactory.getLog(ServiceMain.class);

	private static ServerMain me;

	private Component component;

	public void init(String[] arguments) {

	}

	/**
	 * Start the Thread, accept incoming connections
	 * 
	 * Use this entry point to start with embedded HTTP Server
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		component = new Component();

		// Add a new HTTP server listening on port

		Server server = component.getServers().add(Protocol.HTTP, 8015);
		server.getContext().getParameters().add("maxThreads", "256");

		// Attach the sample application
		RoutingService routingService = new RoutingService();

		component.getDefaultHost().attach(routingService);

		// Start the component.
		//log.info("ready to start");
		DebugLog.d("ready to start");
		component.start();

	}

	/**
	 * Stops RESTlet application
	 */
//	public void stop() {
//		component.getDefaultHost().detach(component.getApplication());
//	}

	public static ServerMain getInstance() {
		if (me == null) {
			me = new ServerMain();
		}
		
		return me;
	}
	


	public static void main(String... args) throws Exception {
		DebugLog.initializeLogger();
		SystemDataInit.init();
		try {
			ServerMain.getInstance().init(args);
			ServerMain.getInstance().start();
			DebugLog.d("Excuting");
		} catch (Exception e) {
			DebugLog.d(e);
		}
		Thread thread = new CleanService();
		thread.start();
	}
}