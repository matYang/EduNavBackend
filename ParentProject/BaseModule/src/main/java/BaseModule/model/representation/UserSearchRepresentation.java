package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.exception.PseudoException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

public class UserSearchRepresentation implements PseudoModel, PseudoRepresentation {
	
	private int userId;
	private int balance;
	private int coupon;
	private int credit;
	private String name;
	private String phone;
	private AccountStatus status;
	private Calendar creationTime;
	
	public UserSearchRepresentation(){
		this.userId = -1;
		this.balance = -1;
		this.coupon = -1;
		this.credit = -1;
		this.name = null;
		this.phone = null;
		this.status = null;
		this.creationTime = null;
	}
	
	@Override
	public ArrayList<String> getKeySet() {
		return RepresentationReflectiveService.getKeySet(this);
	}

	@Override
	public void storeKvps(Map<String, String> kvps) throws IllegalArgumentException, IllegalAccessException, PseudoException, UnsupportedEncodingException {
		RepresentationReflectiveService.storeKvps(this, kvps);
	}
	
	@Override
	public String serialize() throws IllegalArgumentException, IllegalAccessException {
		return RepresentationReflectiveService.serialize(this);
	}
	
	@Override
	public boolean isEmpty() throws Exception {
		return RepresentationReflectiveService.isEmpty(this);
	}

	@Override
	public JSONObject toJSON() {
		return RepresentationReflectiveService.toJSON(this);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int amount) {
		this.balance = amount;
	}

	public int getCoupon() {
		return coupon;
	}

	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int score) {
		this.credit = score;
	}

	@Override
	public String toString() {
		return "UserSearchRepresentation [userId=" + userId + ", name=" + name
				+ ", phone=" + phone + ", status=" + status + ", creationTime="
				+ creationTime + ", balance=" + balance + ", coupon=" + coupon + ", credit=" + credit + "]";
	}
	
	

}
