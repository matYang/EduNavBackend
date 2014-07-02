package BaseModule.generator;

import java.sql.SQLException;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import BaseModule.common.DateUtility;
import BaseModule.dbservice.AdminAccountDaoService;
import BaseModule.dbservice.BookingDaoService;
import BaseModule.dbservice.CourseDaoService;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;

public class ReferenceGenerator {
	
	private static final int partnerReferenceLength = 8;
	private static final int courseReferenceLength = 8;
	private static final int bookingReferenceLength = 10;
	private static final int adminReferenceLength = 8;
	private static final int userInvitionalCodeLength = 8;
	private static final int userAccountNumberLength = 19;
	private static final long userAccountNumberEpoch = 1402835376803l;
	
	
	public synchronized static String generatePartnerReference() throws SQLException{
		String ref = RandomStringUtils.randomAlphanumeric(partnerReferenceLength).toUpperCase();
		while (!PartnerDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(partnerReferenceLength).toUpperCase();
		}
		return ref;
	}
	
	public synchronized static String generateCourseReference() throws SQLException, PseudoException{
		String ref = RandomStringUtils.randomAlphanumeric(courseReferenceLength).toUpperCase();
		while (!CourseDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(courseReferenceLength).toUpperCase();
		}
		return ref;
	}
	
	public synchronized static String generateBookingReference() throws SQLException, PseudoException{
		String ref = RandomStringUtils.randomAlphanumeric(bookingReferenceLength);
		while (!BookingDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(bookingReferenceLength);
		}
		return ref;
	}
	
	public synchronized static String generateAdminReference() throws SQLException{
		String ref = RandomStringUtils.randomAlphanumeric(adminReferenceLength).toUpperCase();
		while (!AdminAccountDaoService.isReferenceAvailable(ref)){
			ref = RandomStringUtils.randomAlphanumeric(adminReferenceLength).toUpperCase();
		}
		return ref;
	}
	
	
	public synchronized static String generateUserInvitationalCode() throws SQLException{
		String code = RandomStringUtils.randomAlphanumeric(userInvitionalCodeLength).toUpperCase();
		while (!UserDaoService.isInvitationalCodeAvaialble(code)){
			code = RandomStringUtils.randomAlphanumeric(userInvitionalCodeLength).toUpperCase();
		}
		return code;
	}
	
	private synchronized static String generateAccountNumber() throws SQLException{
		String prefix = "6";
		Random random = new Random();
		int randomCount = random.nextInt(100);
		String sufix = (DateUtility.getCurTime() - userAccountNumberEpoch) + "" + randomCount;
		while (sufix.length() < userAccountNumberLength-1){
			sufix = "0" + sufix;
		}
		return prefix + sufix;
	}
	
	public synchronized static String generateUserAccountNumber() throws SQLException{
		String accountNumber = generateAccountNumber();
		while (!UserDaoService.isAccountnumberAvailable(accountNumber)){
			accountNumber = generateAccountNumber();
		}
		return accountNumber;
	}
	
}
