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
import AdminModule.resources.booking.BookingChangeStatusResource;
import AdminModule.resources.booking.BookingIdResource;
import AdminModule.resources.booking.BookingResource;
import AdminModule.resources.coupon.CouponIdResource;
import AdminModule.resources.coupon.CouponResource;
import AdminModule.resources.course.CourseIdResource;
import AdminModule.resources.course.CourseResource;
import AdminModule.resources.credit.CreditResource;
import AdminModule.resources.modelLoader.MemcachedBenchMarkResource;
import AdminModule.resources.modelLoader.ModelLoaderResource;
import AdminModule.resources.partner.PartnerIdResource;
import AdminModule.resources.partner.PartnerResource;
import AdminModule.resources.transaction.TransactionResource;
import AdminModule.resources.user.UserResource;
import AdminModule.resources.user.UserIdResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;

public final class RoutingService extends Application{
	
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
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + logoutPrefix + "/{id}", AdminAccountLogout.class);
		String changePassword = "/changePassword";
		//  API for AdminAccount logout : /a-api/v1.0/admin/changePassword
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + adminServicePrefix + changePassword + "/{id}", AdminChangePassword.class);
		
		
		/** -------------------- APIs for partner module ------------------ **/
		String partnerServicePrefix = "/partner";
		
		String PartnerPrefix = "/partner";
		//  API for admin to create partner : /a-api/v1.0/partner/partner
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + PartnerPrefix, PartnerResource.class);
		//  API for admin to update partner : /a-api/v1.0/partner/partner/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + PartnerPrefix + "/{id}", PartnerIdResource.class);
		
		
		
		/** -------------------- APIs for user module ------------------ **/
		String userServicePrefix = "/user";
		
		String UserPrefix = "/user";
		//  API for admin to query users : /a-api/v1.0/user/user
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + UserPrefix, UserResource.class);
		//  API for admin to update user : /a-api/v1.0/user/user/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + UserPrefix + "/{id}", UserIdResource.class);
		
		
		
		/** -------------------- APIs for booking module ------------------ **/
		String bookingServicePrefix = "/booking";
		
		String BookingPrefix = "/booking";
		//  API for admin to query bookings : /a-api/v1.0/booking/booking
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + bookingServicePrefix + BookingPrefix, BookingResource.class);
		//  API for admin to get/update booking : /a-api/v1.0/booking/booking/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + bookingServicePrefix + BookingPrefix + "/{id}", BookingIdResource.class); 
		String ChangeStatusPrefix = "/changeStatus";
		//  API for booking change status: /a-api/v1.0/booking/changeStatus/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + bookingServicePrefix + ChangeStatusPrefix + "{id}", BookingChangeStatusResource.class);
		String ChangeStatusSupervisorCallPrefix = "/changeStatusSupervisor";
		//  API for booking change status with supervisor rights: /a-api/v1.0/booking/changeStatusSupervisor/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + bookingServicePrefix + ChangeStatusSupervisorCallPrefix  + "{id}", BookingChangeStatusResource.class);
		
		
		/** -------------------- APIs for course module ------------------ **/
		String courseServicePrefix = "/course";
		
		String CoursePrefix = "/course";
		//  API for admin to create course : /a-api/v1.0/course/course
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + courseServicePrefix + CoursePrefix, CourseResource.class);
		//  API for admin to update course : /a-api/v1.0/course/course/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + courseServicePrefix + CoursePrefix + "/{id}", CourseIdResource.class);
		
		
		/** -------------------- APIs for coupon module ------------------ **/
		String couponServicePrefix = "/coupon";
		
		String CouponPrefix = "/coupon";
		//  API for admin to search/create coupon : /a-api/v1.0/coupon/coupon
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + couponServicePrefix + CouponPrefix, CouponResource.class);
		//  API for admin to update coupon : /a-api/v1.0/coupon/coupon/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + couponServicePrefix + CouponPrefix + "/{id}", CouponIdResource.class);
		
		
		
		/** -------------------- APIs for credit module ------------------ **/
		String creditServicePrefix = "/credit";
		
		String CreditPrefix = "/credit";
		//  API for admin to search for credit : /a-api/v1.0/credit/credit
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + creditServicePrefix + CreditPrefix, CreditResource.class);
		
		
		
		/** -------------------- APIs for transaction module ------------------ **/
		String transactionServicePrefix = "/transaction";
		
		String TransactionPrefix = "/transaction";
		//  API for admin to search for transaction : /a-api/v1.0/transaction/transaction
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + transactionServicePrefix + TransactionPrefix, TransactionResource.class);

		
		
		/** -------------------- API for ModelLoader module ------------------ **/
		String testServicePrefix = "/test";
		
		String ModelLoaderPrefix = "/modelLoader";
		//  API for model to load : /a-api/v1.0/test/modelLoader
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + testServicePrefix + ModelLoaderPrefix, ModelLoaderResource.class);
		String MemcachedBenchMarkPrefix = "/memcached";
		//	API for making a memcached bench mark: /a-api/v1.0/test/memcached
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + testServicePrefix + MemcachedBenchMarkPrefix, MemcachedBenchMarkResource.class);
		
		
		
		return router;
	}
}
