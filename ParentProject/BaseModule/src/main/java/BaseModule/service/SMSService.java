package BaseModule.service;

import BaseModule.asyncRelayExecutor.ExecutorProvider;
import BaseModule.asyncTask.SMSTask;
import BaseModule.configurations.EnumConfig.SMSEvent;

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
