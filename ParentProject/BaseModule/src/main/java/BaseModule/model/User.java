package BaseModule.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;

public class User implements PseudoModel, Serializable{
	
	private static final long serialVersionUID = 1L;

	private int userId;
	private int balance;
	private int coupon;
	private int credit;
	
	private String name;
	private String phone;
	private String password;
	private String email;
	
	private AccountStatus status;	
	
	private String invitationalCode;
	private String appliedInvitationalCode;
	private String accountNumber;
	
	private Calendar creationTime;
	private Calendar lastLogin;
	
	private transient ArrayList<Coupon> couponList = new ArrayList<Coupon>();
	private transient ArrayList<Credit> creditList = new ArrayList<Credit>();
	private transient ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
	
	private User(){}
	public static User getInstance(){
		User user = new User();
		try {
			ModelReflectiveService.initialize(user);
		} catch (Exception e) {
			return null;
		}
		return user;
	}

	//SQL Retrieving
	public User(int userId, String name, String phone, Calendar creationTime,
			Calendar lastLogin, String password, AccountStatus status,int balance,
			int coupon,int credit,String email,String invitationalCode,String appliedInvitationalCode, 
			String accountNumber) {
		super();
		this.userId = userId;		
		this.name = name;
		this.phone = phone;
		this.creationTime = creationTime;
		this.lastLogin = lastLogin;
		this.password = password;
		this.status = status;	
		this.balance = balance;
		this.coupon = coupon;
		this.credit = credit;
		this.email = email;
		this.invitationalCode = invitationalCode;
		this.appliedInvitationalCode = appliedInvitationalCode;
		this.accountNumber = accountNumber;
	}
	
	//Normal Construction
	public User(String phone, String password, String appliedInvitationalCode, String invitationalCode, String accountNumber, AccountStatus status) {
		super();		
		this.balance = 0;
		this.coupon = 0;
		this.credit = 0;
		this.name = "";
		this.phone = phone;
		this.password = password;
		this.status = status;
		this.email = "";
		this.invitationalCode = invitationalCode;
		this.appliedInvitationalCode = appliedInvitationalCode;
		this.accountNumber = accountNumber;
		this.creationTime = DateUtility.getCurTimeInstance();
		if(this.lastLogin==null){
			this.lastLogin = (Calendar) this.creationTime.clone();
		}		
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
	public Calendar getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Calendar lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	public int getBalance() {
		return balance;
	}

	public void incBalance(int amount){
		this.balance += amount;
	}
	
	public void decBalance(int amount){
		this.balance -= amount;
	}

	public int getCoupon() {
		return coupon;
	}
	
	public void incCoupon(int amount){
		this.coupon += amount;
	}

	public void decCoupon(int amount){
		this.coupon -= amount;
	}

	public int getCredit() {
		return credit;
	}

	public void incCredit(int amount){
		this.credit += amount;
	}
	
	public void decCredit(int amount){
		this.credit -= amount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<Coupon> getCouponList() {
		return couponList;
	}

	public void setCouponList(ArrayList<Coupon> couponList) {
		this.couponList = couponList;
	}

	public ArrayList<Credit> getCreditList() {
		return creditList;
	}

	public void setCreditList(ArrayList<Credit> creditList) {
		this.creditList = creditList;
	}

	public ArrayList<Transaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(ArrayList<Transaction> transactionList) {
		this.transactionList = transactionList;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	public String getInvitationalCode() {
		return invitationalCode;
	}

	public void setInvitationalCode(String invitationalCode) {
		this.invitationalCode = invitationalCode;
	}

	public String getAppliedInvitationalCode() {
		return appliedInvitationalCode;
	}

	public void setAppliedInvitationalCode(String appliedInvitationalCode) {
		this.appliedInvitationalCode = appliedInvitationalCode;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public User deepCopy() throws IOException, ClassNotFoundException{
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(this);
        oos.close();
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final User clone = (User) ois.readObject();
        
        return clone;
	}

	public JSONObject toJSON() throws Exception{
		return ModelReflectiveService.toJSON(this);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		User other = (User) obj;
		if (accountNumber == null) {
			if (other.accountNumber != null) {
				return false;
			}
		} else if (!accountNumber.equals(other.accountNumber)) {
			return false;
		}
		if (appliedInvitationalCode == null) {
			if (other.appliedInvitationalCode != null) {
				return false;
			}
		} else if (!appliedInvitationalCode
				.equals(other.appliedInvitationalCode)) {
			return false;
		}
		if (balance != other.balance) {
			return false;
		}
		if (coupon != other.coupon) {
			return false;
		}
		if (credit != other.credit) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (invitationalCode == null) {
			if (other.invitationalCode != null) {
				return false;
			}
		} else if (!invitationalCode.equals(other.invitationalCode)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (phone == null) {
			if (other.phone != null) {
				return false;
			}
		} else if (!phone.equals(other.phone)) {
			return false;
		}
		if (status != other.status) {
			return false;
		}
		if (userId != other.userId) {
			return false;
		}
		return true;
	}
	

}
