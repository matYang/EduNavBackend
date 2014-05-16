package BaseModule.clean.cleanScheduler;
import java.util.Calendar;
import java.util.Date;

import BaseModule.clean.cleanTasks.BookingCleaner;
import BaseModule.clean.cleanTasks.CourseCleaner;
import BaseModule.clean.cleanTasks.SessionCleaner;
import BaseModule.common.DateUtility;


public class Clean{

	public static Calendar dateToCalendar(Date date){ 
		Calendar calendar = DateUtility.getCurTimeInstance();
		calendar.setTime(date);
		return calendar;
	}

	
	public void cleanSchedules(){
		BookingCleaner.clean();
		CourseCleaner.clean();
		SessionCleaner.clean();
	}


}
