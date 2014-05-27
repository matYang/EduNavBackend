package BaseModule.configurations;

public class EnumConfig {

	public static enum AccountStatus{
        activated(0),deactivated(1),deleted(2);
        public int code;
        AccountStatus(int code){
            this.code = code;
        }
        private final static AccountStatus[] map = AccountStatus.values();
        public static AccountStatus fromInt(int n){
            return map[n];
        }
    }
	
	public static enum CreditStatus{
        usable(0),expired(1),used(2),awaiting(3);
        public int code;
        CreditStatus(int code){
            this.code = code;
        }
        private final static CreditStatus[] map = CreditStatus.values();
        public static CreditStatus fromInt(int n){
            return map[n];
        }
    }
	
	public static enum CouponStatus{
        usable(0),expired(1),used(2);
        public int code;
        CouponStatus(int code){
            this.code = code;
        }
        private final static CouponStatus[] map = CouponStatus.values();
        public static CouponStatus fromInt(int n){
            return map[n];
        }
    }
	
	public static enum TransactionType{
        coupon(0),deposit(1),withdraw(2);
        public int code;
        TransactionType(int code){
            this.code = code;
        }
        private final static TransactionType[] map = TransactionType.values();
        public static TransactionType fromInt(int n){
            return map[n];
        }
    }
	
	public static enum Privilege{
        root(0),mamagement(1),routine(2);
        public int code;
        Privilege(int code){
            this.code = code;
        }
        private final static Privilege[] map = Privilege.values();
        public static Privilege fromInt(int n){
            return map[n];
        }
    }
	
	public static enum SMSEvent{
		user_cellVerification(0), user_changePassword(1), user_forgetPassword(2), partner_forgetPassword(3), partner_changePassword(4);
		public int code;
		SMSEvent(int code){
			this.code = code;
		}
		private final static SMSEvent[] map = SMSEvent.values();
		public static SMSEvent fromInt(int n){
			return map[n];
		}
	}
	
	public static enum ClassModel{
        smallclass(0),medianclass(1),bigclass(2);
        public int code;
        ClassModel(int code){
            this.code = code;
        }
        private final static ClassModel[] map = ClassModel.values();
        public static ClassModel fromInt(int n){
            return map[n];
        }
    }	
	
	public static enum PartnerQualification{
       verified(0),unverified(1);
        public int code;
        PartnerQualification(int code){
            this.code = code;
        }
        private final static PartnerQualification[] map = PartnerQualification.values();
        public static PartnerQualification fromInt(int n){
            return map[n];
        }
    }	
	
	public static enum TeachingMaterialType{
	       self(0),pub(1);
	        public int code;
	        TeachingMaterialType(int code){
	            this.code = code;
	        }
	        private final static TeachingMaterialType[] map = TeachingMaterialType.values();
	        public static TeachingMaterialType fromInt(int n){
	            return map[n];
	        }
	    }	
}
