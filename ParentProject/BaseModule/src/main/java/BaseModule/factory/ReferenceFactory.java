package BaseModule.factory;

import org.apache.commons.lang3.RandomStringUtils;

public class ReferenceFactory {
	
	public static final int partnerReferenceLength = 8;
	public static final int courseReferenceLength = 8;
	public static final int bookingReferenceLength = 10;
	public static final int adminReferenceLength = 10;
	
	
	public static String generatePartnerReference(){
		String ref = RandomStringUtils.randomAlphanumeric(partnerReferenceLength);
		return ref;
	}
	
	public static String generateCourseReference(){
		String ref = RandomStringUtils.randomAlphanumeric(courseReferenceLength);
		return ref;
	}
	
	public static String generateBookingReference(){
		String ref = RandomStringUtils.randomAlphanumeric(bookingReferenceLength);
		return ref;
	}
	
	public static String generateAdminReference(){
		String ref = RandomStringUtils.randomAlphanumeric(adminReferenceLength);
		return ref;
	}
	
}
