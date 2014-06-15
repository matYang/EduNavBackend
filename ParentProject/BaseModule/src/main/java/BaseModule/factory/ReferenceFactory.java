package BaseModule.factory;

import java.sql.SQLException;

import org.apache.commons.lang3.RandomStringUtils;

import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;

public class ReferenceFactory {
	
	private static final int partnerReferenceLength = 8;
	private static final int courseReferenceLength = 8;
	private static final int bookingReferenceLength = 10;
	private static final int adminReferenceLength = 8;
	private static final int userInvitionalCodeLength = 8;
	
	
	public static String generatePartnerReference() throws SQLException{
		String ref = RandomStringUtils.randomAlphanumeric(partnerReferenceLength).toUpperCase();
		while (!PartnerDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(partnerReferenceLength).toUpperCase();
		}
		return ref;
	}
	
	public static String generateCourseReference() throws SQLException, PseudoException{
		String ref = RandomStringUtils.randomAlphanumeric(courseReferenceLength).toUpperCase();
		while (!CourseDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(courseReferenceLength).toUpperCase();
		}
		return ref;
	}
	
	public static String generateBookingReference() throws SQLException, PseudoException{
		String ref = RandomStringUtils.randomAlphanumeric(bookingReferenceLength);
		while (!BookingDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(bookingReferenceLength);
		}
		return ref;
	}
	
	public static String generateAdminReference() throws SQLException{
		String ref = RandomStringUtils.randomAlphanumeric(adminReferenceLength).toUpperCase();
		while (!AdminAccountDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(adminReferenceLength).toUpperCase();
		}
		return ref;
	}
	
	
	public static String generateUserInvitationalCode() throws SQLException{
		String code = RandomStringUtils.randomAlphanumeric(userInvitionalCodeLength).toUpperCase();
		while (!UserDaoService.isInvitationalCodeAvaialble(code)){
			code = RandomStringUtils.randomAlphanumeric(userInvitionalCodeLength).toUpperCase();
		}
		return code;
	}
	
	public static String generateUserAccountNumber() throws SQLException{
		return null;
	}
	
}
