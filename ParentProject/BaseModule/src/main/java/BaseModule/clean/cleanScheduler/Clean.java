package BaseModule.clean.cleanScheduler;

import java.util.Calendar;
import java.util.Date;

import BaseModule.clean.cleanTasks.CouponCleaner;
import BaseModule.clean.cleanTasks.CourseCleaner;
import BaseModule.clean.cleanTasks.CreditCleaner;
import BaseModule.clean.cleanTasks.SessionCleaner;
import BaseModule.common.DateUtility;


public class Clean{

	public static Calendar dateToCalendar(Date date){ 
		Calendar calendar = DateUtility.getCurTimeInstance();
		calendar.setTime(date);
		return calendar;
	}

	
	public void cleanSchedules(){
		SessionCleaner.clean();
		CreditCleaner.clean();
		CouponCleaner.clean();
		CourseCleaner.cleanCourse();
		CourseCleaner.cleanCourseRelatedBooking();
	}


}
