package BaseModule.configurations;

public class EnumConfig {

	public static enum Status{
        activated(0),deactivated(1),deleted(2);
        public int code;
        Status(int code){
            this.code = code;
        }
        private final static Status[] map = Status.values();
        public static Status fromInt(int n){
            return map[n];
        }
    }
	
	public static enum Privilege{
        first(0),business(1),economy(2);
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
		user_registration(0), user_bookingConfirmation(1), user_forgetPassword(2), partner_forgetPassword(3);
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
