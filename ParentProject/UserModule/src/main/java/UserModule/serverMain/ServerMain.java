package UserModule.serverMain;

import java.util.Map;

import net.spy.memcached.internal.OperationFuture;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;
import BaseModule.eduDAO.EduDaoBasic;
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
		server.getContext().getParameters().add("maxThreads", "512");

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
		String ac_key = null;
		String ac_ivy = null;
		for (String arg : args){
			if (arg.indexOf("acKey-") == 0)
				ac_key =  arg.split("-")[1];
			if (arg.indexOf("acIvy-") == 0)
				ac_ivy =  arg.split("-")[1];
		}
		
		Map<String, String> configureMap = ServerConfig.configurationMap;
		configureMap.put(ServerConfig.MAP_MODULE_KEY, ServerConfig.MAP_MODULE_USER);
		configureMap.put("sqlMaxConnection","50");
		ServerConfig.acDecode(ac_key, ac_ivy);
		System.out.println("System started under module: " + configureMap.get(ServerConfig.MAP_MODULE_KEY) + " with max sql connection: " + configureMap.get("sqlMaxConnection"));
		
		OperationFuture<Boolean> result = EduDaoBasic.setCache("test", 60, "testing connection");
		System.out.println("Result: " + result.get());
		
		try {
			ServerMain.getInstance().init(args);
			ServerMain.getInstance().start();
			DebugLog.d("Excuting");
			DebugLog.b_d("Start Logging Behaviors");
		} catch (Exception e) {
			DebugLog.d(e);
		}
	}
	
}
