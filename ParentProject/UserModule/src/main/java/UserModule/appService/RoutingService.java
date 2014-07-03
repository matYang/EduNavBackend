package UserModule.appService;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import UserModule.resources.alipay.AlipayIdResource;
import UserModule.resources.alipay.AlipayResource;
import UserModule.resources.booking.BookingChangeStatusResource;
import UserModule.resources.booking.BookingResource;
import UserModule.resources.coupon.CouponIdResource;
import UserModule.resources.general.*;
import UserModule.resources.user.UserChangeInfoResource;
import UserModule.resources.user.UserIdResource;
import UserModule.resources.user.UserResource;
import UserModule.resources.user.auth.UserCellVerification;
import UserModule.resources.user.auth.UserChangeCellPhone;
import UserModule.resources.user.auth.UserChangePassword;
import UserModule.resources.user.auth.UserForgetPassword;
import UserModule.resources.user.auth.UserLogin;
import UserModule.resources.user.auth.UserLogout;
import UserModule.resources.user.auth.UserSessionRedirect;

import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;

public final class RoutingService extends Application {
	
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
		//	API for direct user get/post operations: /api/v1.0/user/user
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + UserPrefix, UserResource.class);
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + UserPrefix + "/{id}", UserIdResource.class);
		String UserInfoPrefix = "/info";
		//  API for user update : /api/v1.0/user/info/:id
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + UserInfoPrefix + "/{id}", UserChangeInfoResource.class);
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
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + ForgetPasswordPrefix, UserForgetPassword.class);
		String ChangeCellPhonePrefix = "/changePhone";
		//	API for user change cellphone: /api/v1.0/user/changePhone
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + userServicePrefix + ChangeCellPhonePrefix + "/{id}", UserChangeCellPhone.class);
		
		
		/** -------------------- APIs for general module ------------------ **/
		String generalServicePrefix = "/general";

		String getLocationPrefix = "/location";
		//	API for getting locations: /api/v1.0/general/location
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + generalServicePrefix + getLocationPrefix, GetLocations.class);
		String getCategoriesPrefix = "/category";
		//	API for getting categories: /api/v1.0/general/category
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + generalServicePrefix + getCategoriesPrefix , GetCategories.class);
		String getCoursesPrefix = "/course";
		//	API for getting courses: /api/v1.0/general/course
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + generalServicePrefix + getCoursesPrefix, GetCourses.class);
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + generalServicePrefix + getCoursesPrefix + "/{id}", GetCourseDetail.class);
		String getCourseByIdListPrefix = "/courseByIdList";
		//	API for getting courses: /api/v1.0/general/courseByIdList
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + generalServicePrefix + getCourseByIdListPrefix, GetCourseByIdList.class);

		/** -------------------- APIs for booking module ------------------ **/
		String bookingServicePrefix = "/booking";
		String BookingPrefix = "/booking";
		//	API for booking get/post : /api/v1.0/booking/booking
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + bookingServicePrefix + BookingPrefix, BookingResource.class);
		//	API for booking get/put : /api/v1.0/booking/booking/:id
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + bookingServicePrefix + BookingPrefix + "/{id}", BookingChangeStatusResource.class);
		String ChangeStatusPrefix = "/changeStatus";
		//  API for booking change status: /api/v1.0/booking/changeStatus/:id
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + bookingServicePrefix + ChangeStatusPrefix + "{id}", BookingChangeStatusResource.class);
		
		
		/** -------------------- APIs for coupon module ------------------ **/
		String couponServicePrefix = "/coupon";
		String CouponPrefix =  "/coupon";
		//	API for coupon activation : /api/v1.0/coupon/coupon/:id
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + couponServicePrefix + CouponPrefix + "/{id}", CouponIdResource.class);
				
		/** -------------------- APIs for alipay module -------------------**/
		String alipayServicePrefix = "/alipay";
		String alipayNotifyUrlPrefix = "/alipay_notifyUrl";
		String alipayReturnUrlPrefix = "/alipay_returnUrl";
		//  API for ZFB post processing : /api/v1.0/alipay/alipay_notifyUrl
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + alipayServicePrefix + alipayNotifyUrlPrefix, AlipayResource.class);
		//  API for ZFB get processing: /api/v1.0/ailpay/alipay_returnUrl
		router.attach(ServerConfig.userApplicationPrefix + ServerConfig.versionPrefix + alipayServicePrefix + alipayReturnUrlPrefix, AlipayIdResource.class);
		
		return router;
	}

}
