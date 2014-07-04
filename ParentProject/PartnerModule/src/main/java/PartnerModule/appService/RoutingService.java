package PartnerModule.appService;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;
import PartnerModule.resources.course.CourseIdResource;
import PartnerModule.resources.course.CourseResource;
import PartnerModule.resources.partner.PartnerIdResource;
import PartnerModule.resources.partner.PartnerPostClassPhotoResource;
import PartnerModule.resources.partner.PartnerPostTeacherResource;
import PartnerModule.resources.partner.PartnerUpdateClassPhotoResource;
import PartnerModule.resources.partner.PartnerUpdateTeacherResource;
import PartnerModule.resources.partner.auth.PartnerChangePassword;
import PartnerModule.resources.partner.auth.PartnerForgetPassword;
import PartnerModule.resources.partner.auth.PartnerLogin;
import PartnerModule.resources.partner.auth.PartnerLogout;
import PartnerModule.resources.partner.auth.PartnerSessionRedirect;

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
		
		
		/** -------------------- apis for partner module ------------------ **/
		String partnerServicePrefix = "/partner";

		String SessionRedirectPrefix = "/findSession";
		//	p-api for session redirection upon non-session pages: /p-api/v1.0/partner/findSession
		router.attach(ServerConfig.partnerApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + SessionRedirectPrefix, PartnerSessionRedirect.class);
		String LoginPrefix = "/login";
		//	p-api for partner login: /p-api/v1.0/partner/login
		router.attach(ServerConfig.partnerApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + LoginPrefix, PartnerLogin.class);
		String LogoutPrefix = "/logout";
		//	p-api for partner logout: /p-api/v1.0/partner/logout
		router.attach(ServerConfig.partnerApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + LogoutPrefix, PartnerLogout.class);
		String PartnerPrefix = "/partner";
		// 	p-api for partner get/post : /p-api/v1.0/partner/partner/:id
		router.attach(ServerConfig.partnerApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + PartnerPrefix + "/{id}", PartnerIdResource.class);
		
		String ChangePasswordPrefix = "/changePassword";
		//	p-api for partner logout: /p-api/v1.0/partner/changePassword
		router.attach(ServerConfig.partnerApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + ChangePasswordPrefix + "/{id}", PartnerChangePassword.class);
		String ForgetPasswordPrefix = "/forgetPassword";
		//	p-api for partner logout: /p-api/v1.0/partner/forgetPassword
		router.attach(ServerConfig.partnerApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + ForgetPasswordPrefix, PartnerForgetPassword.class);
		
		String PostClassPhotoPrefix = "/postClassPhoto";
		//  API for partner to post class photo images : /a-api/v1.0/partner/postClassPhoto/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + PostClassPhotoPrefix + "/{id}", PartnerPostClassPhotoResource.class);
		String UpdateClassPhotoPrefix = "/updateClassPhoto";
		//  API for partner to update class photo images : /a-api/v1.0/partner/updateClassPhoto/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + UpdateClassPhotoPrefix + "/{id}", PartnerUpdateClassPhotoResource.class);
		
		String PostTeacherPrefix = "/postTeacher";
		//  API for partner to post teachers : /a-api/v1.0/partner/postTeacher/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + PostTeacherPrefix + "/{id}", PartnerPostTeacherResource.class);
		String UpdateTeacherPrefix = "/updateTeacher";
		//  API for partner to update teachers : /a-api/v1.0/partner/updateTeacher/:id
		router.attach(ServerConfig.adminApplicationPrefix + ServerConfig.versionPrefix + partnerServicePrefix + UpdateTeacherPrefix + "/{id}", PartnerUpdateTeacherResource.class);
		
		
		
		/** -------------------- apis for partner module ------------------ **/
		String courseServicePrefix = "/course";
		
		String CoursePrefix = "/course";
		//  p-api for partner to create course : /p-api/v1.0/course/course
		router.attach(ServerConfig.partnerApplicationPrefix + ServerConfig.versionPrefix + courseServicePrefix + CoursePrefix, CourseResource.class);
		//  p-api for partner to update course: /p-api/v1.0/course/course/:id
		router.attach(ServerConfig.partnerApplicationPrefix + ServerConfig.versionPrefix + courseServicePrefix + CoursePrefix + "/{id}", CourseIdResource.class);
		return router;
	}

}