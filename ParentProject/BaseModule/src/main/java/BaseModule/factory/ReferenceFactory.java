package BaseModule.factory;

import org.apache.commons.lang3.RandomStringUtils;

import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.dbservice.PartnerDaoService;

public class ReferenceFactory {
	
	public static final int partnerReferenceLength = 8;
	public static final int courseReferenceLength = 8;
	public static final int bookingReferenceLength = 10;
	public static final int adminReferenceLength = 8;
	
	
	public static String generatePartnerReference(){
		String ref = RandomStringUtils.randomAlphanumeric(partnerReferenceLength).toUpperCase();
		while (!PartnerDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(partnerReferenceLength).toUpperCase();
		}
		return ref;
	}
	
	public static String generateCourseReference(){
		String ref = RandomStringUtils.randomAlphanumeric(courseReferenceLength).toUpperCase();
		while (!CourseDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(courseReferenceLength).toUpperCase();
		}
		return ref;
	}
	
	public static String generateBookingReference(){
		String ref = RandomStringUtils.randomAlphanumeric(bookingReferenceLength);
		while (BookingDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(bookingReferenceLength);
		}
		return ref;
	}
	
	public static String generateAdminReference(){
		String ref = RandomStringUtils.randomAlphanumeric(adminReferenceLength).toUpperCase();
		while (AdminAccountDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(adminReferenceLength).toUpperCase();
		}
		return ref;
	}
	
}
