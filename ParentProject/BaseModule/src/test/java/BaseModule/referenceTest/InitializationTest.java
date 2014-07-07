package BaseModule.referenceTest;

import static org.junit.Assert.*;

import org.junit.Test;

import BaseModule.model.AdminAccount;
import BaseModule.model.Booking;
import BaseModule.model.ClassPhoto;
import BaseModule.model.Coupon;
import BaseModule.model.Course;
import BaseModule.model.Credit;
import BaseModule.model.Partner;
import BaseModule.model.Teacher;
import BaseModule.model.Transaction;
import BaseModule.model.User;

public class InitializationTest {

	@Test
	public void test() throws Exception {
		Teacher teacher = Teacher.getInstance();
		ClassPhoto classPhoto = ClassPhoto.getInstance();
		User user = User.getInstance();
		Partner partner = Partner.getInstance();
		Course course = Course.getInstance();
		Credit credit = Credit.getInstance();
		Coupon coupon = Coupon.getInstance();
		Booking booking = Booking.getInstance();
		Transaction transaction = Transaction.getInstance();
		AdminAccount account = AdminAccount.getInstance();
		
		System.out.println(teacher.toJSON().toString());
		System.out.println(classPhoto.toJSON().toString());
		System.out.println(user.toJSON().toString());
		System.out.println(partner.toJSON().toString());
		System.out.println(course.toJSON().toString());
		System.out.println(credit.toJSON().toString());
		System.out.println(coupon.toJSON().toString());
		System.out.println(booking.toJSON().toString());
		System.out.println(transaction.toJSON().toString());
		System.out.println(account.toJSON().toString());
	}

}
