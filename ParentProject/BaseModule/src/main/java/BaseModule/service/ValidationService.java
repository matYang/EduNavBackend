package BaseModule.service;

import BaseModule.exception.validation.ValidationException;
import BaseModule.model.*;

public class ValidationService {
	
	//TODO
	public static boolean validateName(String name){
		return true;
	}
	
	public static boolean validatePhone(String cellNum){
		return true;
	}
	
	public static boolean validatePassword(String password){
		return true;
	}
	
	public static boolean validateUser(User user) throws ValidationException{
		return true;
	}
	
	public static boolean validatePartner(Partner partner) throws ValidationException{
		return true;
	}
	
	public static boolean validateCourse(Course course) throws ValidationException{
		return true;
	}
	
	public static boolean validateBooking(Booking booking) throws ValidationException{
		return true;
	}

}
