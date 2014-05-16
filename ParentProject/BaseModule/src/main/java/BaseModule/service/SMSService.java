package BaseModule.service;

import BaseModule.asyncRelayExecutor.ExecutorProvider;
import BaseModule.asyncTask.SMSTask;
import BaseModule.configurations.EnumConfig.SMSEvent;

public class SMSService {
	
	//user sms
	public static void sendUserCellVerificationSMS(String cellNum, String authCode){
		SMSTask task = new SMSTask(SMSEvent.user_registration, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendUserForgetPasswordSMS(String cellNum, String authCode){
		SMSTask task = new SMSTask(SMSEvent.user_forgetPassword, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendUserBookingConfirmationSMS(String cellNum, String authCode){
		SMSTask task = new SMSTask(SMSEvent.user_bookingConfirmation, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}
	
	
	
	//partner sms
	public static void sendPartnerForgetPasswordSMS(String cellNum, String authCode){
		SMSTask task = new SMSTask(SMSEvent.partner_forgetPassword, cellNum, authCode);
		ExecutorProvider.executeRelay(task);
	}

}
