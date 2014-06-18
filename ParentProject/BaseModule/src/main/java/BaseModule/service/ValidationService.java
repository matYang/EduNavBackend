package BaseModule.service;

import java.util.regex.Pattern;

import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ValidationConfig;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.*;

public class ValidationService {
		
	public static boolean validateName(String userName){
		Pattern NamePattern = Pattern.compile(ValidationConfig.RegexNamePattern);
		Pattern WhiteSpacePattern = Pattern.compile(ValidationConfig.RegexNameWhiteSpacePattern);
		if (userName == null || userName.length() == 0 || userName.length() > ValidationConfig.maxUserNameLength){
			return false;
		}	
		if(WhiteSpacePattern.matcher(userName).matches()){
			userName = userName.replaceAll("\\s+", "");
		}
		if (NamePattern.matcher(userName).matches()){
			return true;
		}
		return false;
	}
	
	public static boolean validatePhone(String phone){
		if (phone == null){
			return false;
		}
		//check if every digit is a number
		for (int i = 0; i < phone.length(); i++) {
			if (Character.isDigit(phone.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean validateEmail(String email){

		Pattern emailPattern = Pattern.compile(ValidationConfig.RegexEmailPattern);
		try{
			if (emailPattern.matcher(email).matches() && email.length()<=ValidationConfig.maxEmailLength) {
				return true;
			}else{
				return false;
			}
		}catch(Exception ex){
			DebugLog.d(ex);
			return false;
		}
	}
	
	public static boolean validatePassword(String password){
		if (password == null || password.length() < ValidationConfig.minPasswordLength || password.length() > ValidationConfig.maxPasswordLength){
			return false;
		}
		
		Pattern PasswordPattern = Pattern.compile(ValidationConfig.RegexPwPattern);
		if(PasswordPattern.matcher(password).matches()){
			return true;
		}
		return false;
	}
	
	public static boolean validateUser(User user) throws ValidationException{
		if (!(user.getName() == null || user.getName().length() == 0 || validateName(user.getName())) || !validatePhone(user.getPhone()) ||
				!validatePassword(user.getPassword()) || user.getBalance() < 0||
				user.getCoupon() < 0 || user.getCredit() < 0 || !(user.getEmail() == null || user.getEmail().length() == 0 || validateEmail(user.getEmail()))){
			throw new ValidationException("用户输入信息不符合规范");			
		}	
		return true;
	}
	
	public static boolean validatePartner(Partner partner) throws ValidationException{
		if(!validateName(partner.getInstName()) || !validatePassword(partner.getPassword()) ||
				!validatePhone(partner.getPhone())){
			throw new ValidationException("合作伙伴输入信息不符合规范");
		}
		if(partner.getLicence() == null || partner.getLicence().length() ==0){
			throw new ValidationException("合作伙伴营业执照不能为空");
		}
		if(partner.getInstName() == null || partner.getInstName().length() == 0){
			throw new ValidationException("合作伙伴机构名字不能为空");
		}
		if(partner.getOrganizationNum() == null || partner.getOrganizationNum().length() == 0){
			throw new ValidationException("合作伙伴机构号不能为空");
		}
		if(partner.getReference() == null || partner.getReference().length() == 0 ){
			throw new ValidationException("合作伙伴信息不完整");
		}
		return true;
	}
	
	public static boolean validateCourse(Course course) throws ValidationException{
		if(course.getPartnerId() <= 0 || course.getClassSize() < 0 || 
				course.getCourseHourNum() < 0 || course.getPrice() < 0 ||
				course.getCourseHourLength() < 0 ||
				!validatePhone(course.getPhone())){
			throw new ValidationException("课程信息不符合规范");
		}		
		if(course.getStartDate() == null || course.getFinishDate() == null ||
				course.getStartTime1() == -1 || course.getFinishTime1() == -1){
			throw new ValidationException("课程开始或完成时间不能为空");
		}
		if((course.getStartTime2() == -1 && course.getFinishTime2() != -1) ||
				(course.getStartTime2() != -1 && course.getFinishTime2() == -1)){
			throw new ValidationException("课程时间安排不合理");
		}
		if(DateUtility.compareday(course.getStartDate(), course.getFinishDate()) >= 0 ||
				(course.getStartTime1() >= course.getFinishTime1()) || 
				(course.getStartTime2() >= course.getFinishTime2() && (course.getStartTime2() != -1 || course.getFinishTime2() != -1)) ||
				(course.getStartTime2() <= course.getFinishTime1() && course.getStartTime2() != -1)){
			throw new ValidationException("课程开始或完成时间不合理");
		}		
		if(course.getStartTime1()>=2400 || course.getFinishTime1()>=2400 ||
				course.getStartTime2()>=2400 || course.getFinishTime2()>=2400){
			throw new ValidationException("课程开始或完成时间不合理");
		}
		if(course.getCategory() == null || course.getCategory().length() == 0 ||
				course.getSubCategory() == null || course.getSubCategory().length() == 0 ||
				course.getSubSubCategory() == null || course.getSubSubCategory().length() == 0 ||
				course.getCourseName() == null || course.getCourseName().length() == 0){
			throw new ValidationException("课程信息不完整");
		}
		return true;
	}
	
	public static boolean validateBooking(Booking booking) throws ValidationException{
		if(booking.getUserId() <= 0 || booking.getCourseId() <= 0 ||
				booking.getPartnerId() <= 0 || booking.getTransactionId() <= 0){
			throw new ValidationException("预定信息不完整");
		}		
		if(booking.getReference() == null || booking.getReference().length() == 0 ||
				booking.getScheduledTime() == null || booking.getName() == null ||
				booking.getName().length() == 0 ){
			throw new ValidationException("预定信息不完整");
		}
		if(booking.getPrice() < 0 || !validatePhone(booking.getPhone()) ||booking.getCashbackAmount()<0){
			throw new ValidationException("预定信息不规范");
		}
		return true;
	}

}
