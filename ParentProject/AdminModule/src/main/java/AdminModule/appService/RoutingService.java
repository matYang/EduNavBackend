package AdminModule.appService;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import AdminModule.resources.admin.AdminAccountChangeInfoResource;
import AdminModule.resources.admin.AdminAccountIdResource;
import AdminModule.resources.admin.AdminAccountResource;
import AdminModule.resources.admin.auth.AdminAccountLogin;
import AdminModule.resources.admin.auth.AdminAccountLogout;
import AdminModule.resources.admin.auth.AdminSessionRedirect;
import AdminModule.resources.booking.BookingIdResource;
import AdminModule.resources.booking.GetBookings;
import AdminModule.resources.partner.GetPartners;
import AdminModule.resources.user.GetUsers;
import AdminModule.resources.user.UpdateUserResource;
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
		
		
		/** -------------------- APIs for admin module ------------------ **/
		String adminServicePrefix = "/admin";

		String SessionRedirectPrefix = "/findSession";
		//	API for session redirection upon non-session pages: /a-api/v1.0/admin/findSession
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + SessionRedirectPrefix, AdminSessionRedirect.class);
		// 	API for AdminAccount get/post : /a-api/v1.0/admin
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix, AdminAccountResource.class);
		//  API for AdminAccount put : /a-api/v1.0/admin/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + "/{id}", AdminAccountIdResource.class); 
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + "/{id}", AdminAccountChangeInfoResource.class);
		String loginPrefix = "/login";
		//  API for AdminAccount login : /a-api/v1.0/admin/login
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + loginPrefix, AdminAccountLogin.class);
		String logoutPrefix = "/logout";
		//  API for AdminAccount logout : /a-api/v1.0/admin/logout
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + logoutPrefix, AdminAccountLogout.class);
		
		/** -------------------- APIs for partner module ------------------ **/
		String partnerServicePrefix = "/partner";
		
		//  API for getting  partners : /a-api/v1.0/admin/partner
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + partnerServicePrefix, GetPartners.class);
		
		/** -------------------- APIs for user module ------------------ **/
		String userServicePrefix = "/user";
		
		//  API for admin to query users : /a-api/v1.0/admin/user
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + userServicePrefix, GetUsers.class);
		//  API for admin to update user : /a-api/v1.0/admin/user/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + userServicePrefix, UpdateUserResource.class);
		
		/** -------------------- APIs for booking module ------------------ **/
		String bookingServicePrefix = "/booking";
		
		//  API for admin to get/update booking : /a-api/v1.0/admin/booking/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + bookingServicePrefix + "/{id}", BookingIdResource.class); 
		//  API for admin to query bookings : /a-api/v1.0/admin/booking
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + bookingServicePrefix, GetBookings.class);
		
		return router;
	}
}
