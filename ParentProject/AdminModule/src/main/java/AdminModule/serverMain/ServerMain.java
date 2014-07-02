package AdminModule.serverMain;

import java.util.Map;

import net.spy.memcached.internal.OperationFuture;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;
import AdminModule.appService.CleanService;
import AdminModule.appService.RoutingService;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ParamConfig;
import BaseModule.configurations.ServerConfig;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.staticDataService.SystemDataInit;


public class ServerMain {

	//private static Log log = LogFactory.getLog(ServiceMain.class);

	private static int portNumber = 8017;

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

		Server server = component.getServers().add(Protocol.HTTP, portNumber);
		server.getContext().getParameters().add("maxThreads", "64");

		// Attach the sample application
		RoutingService routingService = new RoutingService();

		component.getDefaultHost().attach(routingService);

		// Start the component.
		//log.info("ready to start");
		DebugLog.d("ready to start");
		component.start();

	}

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
		String env = null;
		for (String arg : args){
			if (arg.indexOf("acKey-") == 0)
				ac_key =  arg.split("-")[1];
			if (arg.indexOf("acIvy-") == 0)
				ac_ivy =  arg.split("-")[1];
			if (arg.indexOf("env-") == 0)
				env =  arg.split("-")[1];
		}
		
		ParamConfig.MODULE = ParamConfig.MAP_MODULE_ADMIN;
		ParamConfig.SQLMAX = "4";
		ParamConfig.ACKEY = ac_key;
		ParamConfig.ACIVY = ac_ivy;
		ParamConfig.ENV = env;
		
		if (env.equals(ParamConfig.ENV_TEST)){
			portNumber = 8026;
		}
		System.out.println("System started under module: " + ServerConfig.configurationMap.get(ParamConfig.MAP_MODULE_KEY) + " with max sql connection: " + ServerConfig.configurationMap.get("sqlMaxConnection") + " on port: " + portNumber);
		
		SystemDataInit.init();	
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
		Thread thread = new CleanService();
		thread.start();
	}
}
