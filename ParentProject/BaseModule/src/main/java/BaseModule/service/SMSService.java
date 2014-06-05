package BaseModule.service;

import BaseModule.asyncRelayExecutor.ExecutorProvider;
import BaseModule.asyncTask.SMSTask;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.SMSEvent;
import BaseModule.model.Booking;

public class SMSService {
	
	//user sms
	public static void sendUserCellVerificationSMS(String cellNum, String authCode){
		SMSTask task = new SMSTask(SMSEvent.user_cellVerification, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendUserForgetPasswordSMS(String cellNum, String authCode){
		SMSTask task = new SMSTask(SMSEvent.user_forgetPassword, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendUserChangePasswordSMS(String cellNum, String authCode){
		SMSTask task = new SMSTask(SMSEvent.user_changePassword, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendBookingFailedSMS(Booking booking){
		SMSTask sms = new SMSTask(SMSEvent.user_bookingFailed, booking.getPhone(), booking.getCourse().getCourseName());
		ExecutorProvider.executeRelay(sms);
	}
	
	public static void sendBookingConfirmedSMS(Booking booking){
		SMSTask sms = new SMSTask(SMSEvent.user_bookingConfirmed, booking.getPhone(), booking.getCourse().getCourseName(), DateUtility.castToReadableString(booking.getScheduledTime()));
		ExecutorProvider.executeRelay(sms);
	}
	
	
	//partner sms
	public static void sendPartnerForgetPasswordSMS(String cellNum, String authCode){
		SMSTask task = new SMSTask(SMSEvent.partner_forgetPassword, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendPartnerChangePasswordSMS(String cellNum, String authCode){
		SMSTask task = new SMSTask(SMSEvent.partner_changePassword, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}

}
