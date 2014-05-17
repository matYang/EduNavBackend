package UserModule.appService;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import UserModule.resources.*;
import UserModule.resources.user.UserIdResource;
import UserModule.resources.user.UserResource;
import UserModule.resources.user.UserSMSVerification;
import UserModule.resources.user.auth.UserSessionRedirect;

import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;

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
		String userServicePrefix = "/user";

		String SessionRedirectPrefix = "/findSession";
		//	API for session redirection upon non-session pages: /api/v1.0/user/findSession
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + SessionRedirectPrefix, UserSessionRedirect.class);
		
		String SMSVerificationPrefix = "/smsVerification";
		//	API for sms verification: /api/v1.0/user/smsVerification
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + SMSVerificationPrefix, UserSMSVerification.class);
		
		String UserPrefix = "/user";
		//	API for direct user crud operations: /api/v1.0/user/user
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + UserPrefix, UserResource.class);
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + UserPrefix + "/{id}", UserIdResource.class);
		
		
		
		
		
		return router;
	}

}
