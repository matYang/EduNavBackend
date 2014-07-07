package BaseModule.configurations;

import BaseModule.interfaces.PseudoEnum;

public final class EnumConfig {
	

	public static enum AccountStatus implements PseudoEnum{
        activated(0),deactivated(1),deleted(2);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        AccountStatus(int code){
            this.code = code;
        }
        private final static AccountStatus[] map = AccountStatus.values();
        public static AccountStatus fromInt(int n){
            return map[n];
        }
    }
	
	public static enum CreditStatus implements PseudoEnum{
        usable(0),expired(1),used(2);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        CreditStatus(int code){
            this.code = code;
        }
        private final static CreditStatus[] map = CreditStatus.values();
        public static CreditStatus fromInt(int n){
            return map[n];
        }
    }
	
	public static enum CouponStatus implements PseudoEnum{
        usable(0),expired(1),used(2), inactive(3);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        CouponStatus(int code){
            this.code = code;
        }
        private final static CouponStatus[] map = CouponStatus.values();
        public static CouponStatus fromInt(int n){
            return map[n];
        }
    }
	
	
	public static enum CouponOrigin implements PseudoEnum{
        registration(0), invitation(1), admin(2);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        CouponOrigin(int code){
            this.code = code;
        }
        private final static CouponOrigin[] map = CouponOrigin.values();
        public static CouponOrigin fromInt(int n){
            return map[n];
        }
    }
	
	public static enum TransactionType implements PseudoEnum{
        cashback(0),deposit(1),withdraw(2),invitation(3);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        TransactionType(int code){
            this.code = code;
        }
        private final static TransactionType[] map = TransactionType.values();
        public static TransactionType fromInt(int n){
            return map[n];
        }
    }
	
	public static enum BookingStatus implements PseudoEnum{
        awaiting(0),confirmed(1),cancelled(2),failed(3),delivered(4), noShow(5),late(6), registered(7), paid(8), noPay(9), started(10), refunded(11), succeeded(12), consolidated(13);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        BookingStatus(int code){
            this.code = code;
        }
        private final static BookingStatus[] map = BookingStatus.values();
        public static BookingStatus fromInt(int n){
            return map[n];
        }
    }
	
	public static enum BookingType implements PseudoEnum{
		offline(0),online(1);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        BookingType(int code){
            this.code = code;
        }
        private final static BookingType[] map = BookingType.values();
        public static BookingType fromInt(int n){
            return map[n];
        }
	}
		
	public static enum ServiceFeeStatus implements PseudoEnum{
		shouldCharge(0),hasCharged(1),refundCharge(2), noCharge(3), consolidated(4), naive(5);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        ServiceFeeStatus(int code){
            this.code = code;
        }
        private final static ServiceFeeStatus[] map = ServiceFeeStatus.values();
        public static ServiceFeeStatus fromInt(int n){
            return map[n];
        }
	}

	public static enum CommissionStatus implements PseudoEnum{
		shouldCharge(0),hasCharged(1),refundCharge(2), noCharge(3), consolidated(4), naive(5);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        CommissionStatus(int code){
            this.code = code;
        }
        private final static CommissionStatus[] map = CommissionStatus.values();
        public static CommissionStatus fromInt(int n){
            return map[n];
        }
	}
	
	public static enum Privilege implements PseudoEnum{
        root(0),mamagement(1),routine(2);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        Privilege(int code){
            this.code = code;
        }
        private final static Privilege[] map = Privilege.values();
        public static Privilege fromInt(int n){
            return map[n];
        }
    }
	
	public static enum SMSEvent implements PseudoEnum{
		user_cellVerification(0), user_changePassword(1), user_forgetPassword(2), partner_forgetPassword(3), partner_changePassword(4), user_bookingAwaiting(5), user_bookingConfirmed(6), user_bookingFailed(7), user_register(8), user_invitee(9), user_inviter(10), user_inviterConsolidation(11);
		public int code;
		@Override
	    public int getCode() {
	        return code;
	    }
		SMSEvent(int code){
			this.code = code;
		}
		private final static SMSEvent[] map = SMSEvent.values();
		public static SMSEvent fromInt(int n){
			return map[n];
		}
	}
	
	public static enum CourseStatus implements PseudoEnum{
		openEnroll(0),deactivated(1),consolidated(2);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        CourseStatus(int code){
            this.code = code;
        }
        private final static CourseStatus[] map = CourseStatus.values();
        public static CourseStatus fromInt(int n){
            return map[n];
        }
    }	
	
	public static enum PartnerQualification implements PseudoEnum{
		verified(0),unverified(1);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        PartnerQualification(int code){
            this.code = code;
        }
        private final static PartnerQualification[] map = PartnerQualification.values();
        public static PartnerQualification fromInt(int n){
            return map[n];
        }
    }
	
	public static enum Visibility implements PseudoEnum{
		invisible(0), visible(1);
		public int code;
        @Override
        public int getCode() {
            return code;
        }
        Visibility(int code){
            this.code = code;
        }
        private final static Visibility[] map = Visibility.values();
        public static Visibility fromInt(int n){
            return map[n];
        }
	}
	
	public static enum ReflectiveOp implements PseudoEnum{
		TOJSON(0),STOREJSON(1),STOREKVPS(2), INITIALIZE(3);
        public int code;
        @Override
        public int getCode() {
            return code;
        }
        ReflectiveOp(int code){
            this.code = code;
        }
        private final static ReflectiveOp[] map = ReflectiveOp.values();
        public static ReflectiveOp fromInt(int n){
            return map[n];
        }
	}

}
