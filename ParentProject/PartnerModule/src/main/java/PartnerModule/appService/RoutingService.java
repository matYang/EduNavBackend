package PartnerModule.appService;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;
import PartnerModule.resources.partner.PartnerChangeInfoResource;
import PartnerModule.resources.partner.PartnerIdResource;
import PartnerModule.resources.partner.auth.PartnerLogin;
import PartnerModule.resources.partner.auth.PartnerLogout;
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
		
		
		/** -------------------- APIs for partner module ------------------ **/
		String partnerServicePrefix = "/partner";

		String SessionRedirectPrefix = "/findSession";
		//	API for session redirection upon non-session pages: /p-api/v1.0/partner/findSession
		router.attach(ServerConfig.partnerApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + SessionRedirectPrefix, PartnerSessionRedirect.class);
		String LoginPrefix = "/login";
		//	API for partner login: /api/v1.0/partner/login
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + LoginPrefix, PartnerLogin.class);
		String LogoutPrefix = "/logout";
		//	API for partner logout: /api/v1.0/partner/logout
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + LogoutPrefix + "/{id}", PartnerLogout.class);
		// 	API for partner get/post : /api/v1.0/partner/:id
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + "/{id}", PartnerIdResource.class);
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + "/{id}", PartnerChangeInfoResource.class);
		return router;
	}

}