package BaseModule.service;

import BaseModule.asyncRelayExecutor.ExecutorProvider;
import BaseModule.asyncTask.SMSTask;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.SMSEvent;
import BaseModule.model.Booking;

public final class SMSService {
	
	//user sms
	public static void sendUserCellVerificationSMS(final String cellNum, final String authCode){
		String payload = "您申请注册爱上课会员的验证码为：" + authCode + "（如非本人操作请忽略）。爱上课，专注课程预订~";
		SMSTask task = new SMSTask(SMSEvent.user_cellVerification, cellNum, payload);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendUserForgetPasswordSMS(final String cellNum, final String authCode){
		String payload = "尊敬的爱会员，您请求更改密码的验证码为：" + authCode + "。请妥善保管~";
		SMSTask task = new SMSTask(SMSEvent.user_forgetPassword, cellNum, payload);
		ExecutorProvider.executeRelay(task);
	}
	
	public static void sendUserChangePasswordSMS(final String cellNum, final String authCode){
		String payload = "尊敬的爱会员，您请求更改密码的验证码为：" + authCode + "。请妥善保管~";
		SMSTask task = new SMSTask(SMSEvent.user_changePassword, cellNum, payload);
		ExecutorProvider.executeRelay(task);
	}
	
	
	public static void sendBookingAwaitingSMS(final Booking booking){
		String payload = "您预订的" + booking.getCourse().getInstName() + "课程订单已提交，我们将尽量于半小时内通知您确认结果,请您耐心等待~";
		SMSTask sms = new SMSTask(SMSEvent.user_bookingAwaiting, booking.getPhone(), payload);
		ExecutorProvider.executeRelay(sms);
	}


	public static void sendBookingConfirmedSMS(final Booking booking){
		String payload = "确认：" + booking.getName() + DateUtility.castToSMSFormat(booking.getScheduledTime()) + "前完成" + booking.getCourse().getInstName()
				+ "报到，" + booking.getCourse().getCourseName() + "独享￥" + booking.getCourse().getPrice() + "(原价￥" + booking.getCourse().getOriginalPrice() + ", 优惠"
						+ (booking.getCourse().getOriginalPrice() - booking.getCourse().getPrice()) + "元)；地址：" + booking.getCourse().getLocation() 
				+ "；订单查询变更取消，请登录官网iShangke.CN" + "；告知机构您的爱上课用户名（注册手机号）才能享受折扣哦~";
		SMSTask sms = new SMSTask(SMSEvent.user_bookingConfirmed, booking.getPhone(), payload);
		ExecutorProvider.executeRelay(sms);
	}
	
	public static void sendBookingFailedSMS(final Booking booking){
		String note = booking.getNote() == null || booking.getNote().length() == 0 ? "满班" : booking.getNote();
		String payload = "很抱歉，您预订的" + booking.getCourse().getInstName() + "课程由于" + note + "被拒绝，请前往官网查看其他课程吧~";
		SMSTask sms = new SMSTask(SMSEvent.user_bookingFailed, booking.getPhone(), payload);
		ExecutorProvider.executeRelay(sms);
	}
	
	public static void sendUserRegistraterSMS(final String cellNum, final int amount){
		String payload = "恭喜您成为爱上课会员，获得" + amount + "元现金抵扣券已到账，快去官网选课吧~";
		SMSTask sms = new SMSTask(SMSEvent.user_register, cellNum, payload);
		ExecutorProvider.executeRelay(sms);
	}
	
	public static void sendInviteeSMS(final String cellNum, final int amount){
		String payload = "您的邀请码验证成功，您和小伙伴额外获得的" + amount + "元现金抵扣券已到账，快去通知其他小伙伴吧~";
		SMSTask sms = new SMSTask(SMSEvent.user_invitee, cellNum, payload);
		ExecutorProvider.executeRelay(sms);
	}
	
	public static void sendInviterSMS(final String cellNum, final int amount){
		String payload = "感谢您邀请好友成为会员，额外获得" + amount + "元现金抵扣券已到账，快去官网选课吧~";
		SMSTask sms = new SMSTask(SMSEvent.user_inviter, cellNum, payload);
		ExecutorProvider.executeRelay(sms);
	}
	
	public static void sendInviterConsolidationSMS(final String cellNum, final int amount){
		String payload = "您邀请的好友已通过爱上课报名课程，您额外获得的" + amount + "元现金已到账，不是券，是现金哦~";
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
