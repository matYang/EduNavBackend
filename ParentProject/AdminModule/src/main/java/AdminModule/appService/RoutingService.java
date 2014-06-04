package AdminModule.appService;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import AdminModule.resources.admin.AdminAccountIdResource;
import AdminModule.resources.admin.AdminAccountResource;
import AdminModule.resources.admin.auth.AdminAccountLogin;
import AdminModule.resources.admin.auth.AdminAccountLogout;
import AdminModule.resources.admin.auth.AdminChangePassword;
import AdminModule.resources.admin.auth.AdminSessionRedirect;
import AdminModule.resources.booking.BookingIdResource;
import AdminModule.resources.booking.BookingResource;
import AdminModule.resources.course.CourseIdResource;
import AdminModule.resources.course.CourseResource;
import AdminModule.resources.modelLoader.MemcachedBenchMarkResource;
import AdminModule.resources.modelLoader.ModelLoaderResource;
import AdminModule.resources.partner.PartnerIdResource;
import AdminModule.resources.partner.PartnerResource;
import AdminModule.resources.user.UserResource;
import AdminModule.resources.user.UserIdResource;
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
		String adminPrefix = "/admin";
		
		String SessionRedirectPrefix = "/findSession";
		//	API for session redirection upon non-session pages: /a-api/v1.0/admin/findSession
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + SessionRedirectPrefix, AdminSessionRedirect.class);
		// 	API for AdminAccount get/post : /a-api/v1.0/admin/admin
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + adminPrefix, AdminAccountResource.class);
		//  API for AdminAccount put : /a-api/v1.0/admin/admin/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + adminPrefix + "/{id}", AdminAccountIdResource.class); 
		String loginPrefix = "/login";
		//  API for AdminAccount login : /a-api/v1.0/admin/login
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + loginPrefix, AdminAccountLogin.class);
		String logoutPrefix = "/logout";
		//  API for AdminAccount logout : /a-api/v1.0/admin/logout
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + logoutPrefix, AdminAccountLogout.class);
		String changePassword = "/changePassword";
		//  API for AdminAccount logout : /a-api/v1.0/admin/changePassword
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + changePassword, AdminChangePassword.class);
		
		/** -------------------- APIs for partner module ------------------ **/
		String partnerServicePrefix = "/partner";
		

		//  API for admin to create partner : /a-api/v1.0/admin/partner
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + partnerServicePrefix, PartnerResource.class);
		//  API for admin to update partner : /a-api/v1.0/admin/partner/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + partnerServicePrefix + "/{id}", PartnerIdResource.class);
		
		/** -------------------- APIs for user module ------------------ **/
		String userServicePrefix = "/user";
		
		//  API for admin to query users : /a-api/v1.0/admin/user
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + userServicePrefix, UserResource.class);
		//  API for admin to update user : /a-api/v1.0/admin/user/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + userServicePrefix + "/{id}", UserIdResource.class);
		
		/** -------------------- APIs for booking module ------------------ **/
		String bookingServicePrefix = "/booking";
		
		//  API for admin to get/update booking : /a-api/v1.0/admin/booking/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + bookingServicePrefix + "/{id}", BookingIdResource.class); 
		//  API for admin to query bookings : /a-api/v1.0/admin/booking
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + bookingServicePrefix, BookingResource.class);
		
		
		/** -------------------- APIs for course module ------------------ **/
		String courseServicePrefix = "/course";
		
		//  API for admin to create course : /a-api/v1.0/admin/course
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + courseServicePrefix, CourseResource.class);
		//  API for admin to get course : /a-api/v1.0/admin/course/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + courseServicePrefix + "/{id}", CourseIdResource.class);
		
		/** -------------------- API for ModelLoader module ------------------ **/
		String modelLoaderPrefix = "/modelLoader";
		//  API for model to load : /a-api/v1.0/admin/modelLoader
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + modelLoaderPrefix, ModelLoaderResource.class);
		String memcachedBenchMarkPrefix = "/memcached";
		//	API for making a memcached bench mark: /a-api/v1.0/admin/modelLoader
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + memcachedBenchMarkPrefix, MemcachedBenchMarkResource.class);
		
		
		
		
		
		return router;
	}
}
