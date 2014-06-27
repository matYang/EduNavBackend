package BaseModule.clean.cleanScheduler;

import BaseModule.clean.cleanTasks.BookingCleaner;
import BaseModule.clean.cleanTasks.CouponCleaner;
import BaseModule.clean.cleanTasks.CourseCleaner;
import BaseModule.clean.cleanTasks.CreditCleaner;
import BaseModule.clean.cleanTasks.SessionCleaner;
import BaseModule.clean.cleanTasks.UserCleaner;


public class Clean{

	public void cleanSchedules(){
		SessionCleaner.clean();
		CreditCleaner.clean();
		CouponCleaner.clean();
		CourseCleaner.clean();		
		UserCleaner.clean();
		BookingCleaner.clean();
	}


}
