package BaseModule.model.dataObj;

import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;

public class BookingStatusObj {
	public BookingStatusObj(){
		super();
		bookingStatus = null;
		serviceFeeStatus = null;
		commissionStatus = null;
	}
	
	public BookingStatus bookingStatus;
	public ServiceFeeStatus serviceFeeStatus;
	public CommissionStatus commissionStatus;
}
