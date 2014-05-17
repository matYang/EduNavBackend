package AdminModule.appService;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import AdminModule.resources.admin.auth.AdminSessionRedirect;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;

public class RoutingService extends Application{
	
	public RoutingService(){
		super();
	}
	
	public RoutingService(Context context) {
		super(context);
	}
	
	public synchronized Restlet createInboundRoot(){
		DebugLog.d("initiaing router::RoutingService");
		Router router = new Router(getContext());
		
		
		/** -------------------- APIs for user module ------------------ **/
		String adminServicePrefix = "/admin";

		String SessionRedirectPrefix = "/findSession";
		//	API for session redirection upon non-session pages: /p-api/v1.0/admin/findSession
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + SessionRedirectPrefix, AdminSessionRedirect.class);
		
		
		return router;
	}
}
