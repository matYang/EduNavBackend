package BaseModule.reflectiveTest;

import static org.junit.Assert.*;

import org.junit.Test;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.model.Coupon;
import BaseModule.model.Credit;
import BaseModule.model.User;

public class ToJSONTest {

	@Test
	public void testCoupon() throws Exception {
		Coupon coupon = new Coupon(1, 100);
		System.out.println(coupon.toJSON().toString());
	}
	
	@Test
	public void testUser() throws Exception {
		String name = "Harry";
		String phone = "12345612312";
		String password = "36krfinal";
		AccountStatus status = AccountStatus.activated;
		String email = "xiongchuhanplace@hotmail.com";
		String accountNum = "1";
		User user = new User(phone, password, "", "",accountNum,status);
		user.setName(name);
		user.setEmail(email);
		user.getCouponList().add(new Coupon(1,2));
		user.getCouponList().add(new Coupon(2,3));
		user.getCouponList().add(new Coupon(99,200));
		user.getCouponList().add(new Coupon(1312,2123));
		
		user.getCreditList().add(new Credit(10,100,10));
		user.getCreditList().add(new Credit(9,15,999));
		
		System.out.println(user.toJSON());
	}

}
