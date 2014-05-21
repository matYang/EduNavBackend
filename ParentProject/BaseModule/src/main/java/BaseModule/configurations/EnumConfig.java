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
}
