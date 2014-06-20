package BaseModule.service;

import BaseModule.asyncRelayExecutor.ExecutorProvider;
import BaseModule.asyncTask.SMSTask;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.SMSEvent;
import BaseModule.model.Booking;

public final class SMSService {
	
	//user sms
	public static void sendUserCellVerificationSMS(final String cellNum, final String authCode){
		SMSTask task = new SMSTask(SMSEvent.user_cellVerification, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendUserForgetPasswordSMS(final String cellNum, final String authCode){
		SMSTask task = new SMSTask(SMSEvent.user_forgetPassword, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendUserChangePasswordSMS(final String cellNum, final String authCode){
		SMSTask task = new SMSTask(SMSEvent.user_changePassword, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendBookingFailedSMS(final Booking booking){
		SMSTask sms = new SMSTask(SMSEvent.user_bookingFailed, booking.getPhone(), booking.getCourse().getCourseName());
		ExecutorProvider.executeRelay(sms);
	}
	
	public static void sendBookingConfirmedSMS(final Booking booking){
		SMSTask sms = new SMSTask(SMSEvent.user_bookingConfirmed, booking.getPhone(), booking.getCourse().getCourseName(), DateUtility.castToDateFormat(booking.getScheduledTime()));
		ExecutorProvider.executeRelay(sms);
	}
	
	public static void sendInviteeSMS(final String cellNum, final String payload){
		SMSTask sms = new SMSTask(SMSEvent.user_invitee, cellNum, payload);
		ExecutorProvider.executeRelay(sms);
	}
	
	public static void sendInviterSMS(final String cellNum, final String payload){
		SMSTask sms = new SMSTask(SMSEvent.user_inviter, cellNum, payload);
		ExecutorProvider.executeRelay(sms);
	}
	
	public static void sendInviterConsolidationSMS(final String cellNum, final String payload){
		SMSTask sms = new SMSTask(SMSEvent.user_inviterConsolidation, cellNum, payload);
		ExecutorProvider.executeRelay(sms);
	}
	
	
	
	//partner sms
	public static void sendPartnerForgetPasswordSMS(final String cellNum, final String authCode){
		SMSTask task = new SMSTask(SMSEvent.partner_forgetPassword, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendPartnerChangePasswordSMS(final String cellNum, final String authCode){
		SMSTask task = new SMSTask(SMSEvent.partner_changePassword, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}

}
