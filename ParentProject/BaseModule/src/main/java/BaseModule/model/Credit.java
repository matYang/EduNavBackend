package BaseModule.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import org.json.JSONObject;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.CreditStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;


public class Credit implements PseudoModel, Serializable{
	
	private static final long serialVersionUID = 8L;
	
	public static final long usableThreshould = 31536000000l;
	public static final long expireThreshould = 31536000000l;	//1 year

	private long creditId;
	private int bookingId;
	private int userId;
	private int amount;
	private Calendar creationTime;
	private Calendar expireTime;	
	
	private CreditStatus status;
	
	//SQL Construction
	public Credit(long creditId, int bookingId, int userId, int amount,
			Calendar creationTime, Calendar expireTime, CreditStatus status) {
		super();
		this.creditId = creditId;
		this.bookingId = bookingId;
		this.userId = userId;
		this.amount = amount;
		this.creationTime = creationTime;
		this.expireTime = expireTime;
		this.status = status;
		
	}
	
	//normal construction
	public Credit(int bookingId, int userId, int amount){
		this.creditId = -1;
		this.bookingId = bookingId;
		this.userId = userId;
		this.amount = amount;
		this.creationTime = DateUtility.getCurTimeInstance();
		this.expireTime = DateUtility.getTimeFromLong(DateUtility.getCurTime() + usableThreshould);
		this.status = CreditStatus.usable;		
	}

	public long getCreditId() {
		return creditId;
	}

	public void setCreditId(long creditId) {
		this.creditId = creditId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Calendar getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Calendar expireTime) {
		this.expireTime = expireTime;
	}

	public CreditStatus getStatus() {
		return status;
	}

	public void setStatus(CreditStatus status) {
		this.status = status;
	}


	public Calendar getCreationTime() {
		return creationTime;
	}
	
	public Credit deepCopy() throws IOException, ClassNotFoundException{
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(this);
        oos.close();
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Credit clone = (Credit) ois.readObject();
        
        return clone;
	}
	
	public JSONObject toJSON() throws Exception{
		return ModelReflectiveService.toJSON(this);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credit other = (Credit) obj;
		if (amount != other.amount)
			return false;
		if (bookingId != other.bookingId)
			return false;
		if (creditId != other.creditId)
			return false;
		if (status != other.status)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
	
}
