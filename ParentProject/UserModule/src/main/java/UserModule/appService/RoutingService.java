package UserModule.appService;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import UserModule.resources.*;
import UserModule.resources.general.*;
import UserModule.resources.user.UserIdResource;
import UserModule.resources.user.UserResource;
import UserModule.resources.user.auth.UserCellVerification;
import UserModule.resources.user.auth.UserChangePassword;
import UserModule.resources.user.auth.UserForgetPassword;
import UserModule.resources.user.auth.UserLogin;
import UserModule.resources.user.auth.UserLogout;
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
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + SMSVerificationPrefix, UserCellVerification.class);
		
		String UserPrefix = "/user";
		//	API for direct user crud operations: /api/v1.0/user/user
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + UserPrefix, UserResource.class);
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + UserPrefix + "/{id}", UserIdResource.class);
		
		String LoginPrefix = "/login";
		//	API for user login: /api/v1.0/user/login
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + LoginPrefix, UserLogin.class);
		String LogoutPrefix = "/logout";
		//	API for user logout: /api/v1.0/user/logout
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + LogoutPrefix + "/{id}", UserLogout.class);
		
		String ChangePasswordPrefix = "/changePassword";
		//	API for user logout: /api/v1.0/user/changePassword
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + ChangePasswordPrefix + "/{id}", UserChangePassword.class);
		String ForgetPasswordPrefix = "/forgetPassword";
		//	API for user logout: /api/v1.0/user/forgetPassword
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + ForgetPasswordPrefix + "/{id}", UserForgetPassword.class);

		
		
		/** -------------------- APIs for general module ------------------ **/
		String generalServicePrefix = "/general";

		String getLocationPrefix = "/location";
		//	API for getting locations: /api/v1.0/general/location
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + generalServicePrefix + getLocationPrefix, GetLocations.class);
		String getCategoriesPrefix = "/category";
		//	API for getting categories: /api/v1.0/general/category
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + generalServicePrefix + getCategoriesPrefix , GetCategories.class);
		String getPartnersPrefix = "/partner";
		//	API for getting partners: /api/v1.0/general/partner
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + generalServicePrefix + getPartnersPrefix, GetPartnerNames.class);
		String getCoursesPrefix = "/course";
		//	API for getting courses: /api/v1.0/general/course
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + generalServicePrefix + getCoursesPrefix, GetCourses.class);
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + generalServicePrefix + getCoursesPrefix + "/{id}", GetCourseDetail.class);

		
		
		return router;
	}

}
