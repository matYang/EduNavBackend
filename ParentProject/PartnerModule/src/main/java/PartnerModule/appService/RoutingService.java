package PartnerModule.appService;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;



import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;
import PartnerModule.resources.partner.auth.PartnerSessionRedirect;

public class RoutingService extends Application {
	
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
		String partnerServicePrefix = "/partner";

		String SessionRedirectPrefix = "/findSession";
		//	API for session redirection upon non-session pages: /p-api/v1.0/partner/findSession
		router.attach(ServerConfig.partnerApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + SessionRedirectPrefix, PartnerSessionRedirect.class);
		
		
		return router;
	}

}