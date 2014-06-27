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
import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;


public class Coupon implements PseudoModel, Serializable{
	
	private static final long serialVersionUID = 7L;

	public static final long expireThreshould = 15811200000l;
	
	private long couponId;	
	private int userId;
	private int amount;
	private int originalAmount;
	private Calendar creationTime;
	private Calendar expireTime;
	private CouponStatus status;
	private CouponOrigin origin;
	
	//SQL Construction
	public Coupon(long couponId, int userId,
			int amount, Calendar creationTime, Calendar expireTime,
			CouponStatus status,CouponOrigin origin,int originalAmount) {
		super();
		this.couponId = couponId;		
		this.userId = userId;
		this.amount = amount;
		this.creationTime = creationTime;
		this.expireTime = expireTime;
		this.status = status;
		this.origin = origin;
		this.originalAmount = originalAmount;
	}

	//normal construction
	public Coupon(int userId, int amount){
		super();
		this.couponId = -1;		
		this.userId = userId;
		this.amount = amount;
		this.originalAmount = amount;
		this.expireTime = DateUtility.getTimeFromLong(DateUtility.getCurTime() + expireThreshould);
		this.status = CouponStatus.usable;
		this.origin = CouponOrigin.registration;
		this.creationTime = DateUtility.getCurTimeInstance();
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
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

	public CouponStatus getStatus() {
		return status;
	}

	public void setStatus(CouponStatus status) {
		this.status = status;
	}

	public CouponOrigin getOrigin() {
		return origin;
	}


	public void setOrigin(CouponOrigin origin) {
		this.origin = origin;
	}


	public int getOriginalAmount() {
		return originalAmount;
	}


	public Calendar getCreationTime() {
		return creationTime;
	}

	public Coupon deepCopy() throws IOException, ClassNotFoundException{
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(this);
        oos.close();
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Coupon clone = (Coupon) ois.readObject();
        
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
		Coupon other = (Coupon) obj;
		if (amount != other.amount)
			return false;
		if (couponId != other.couponId)
			return false;
		if (origin != other.origin)
			return false;
		if (originalAmount != other.originalAmount)
			return false;
		if (status != other.status)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
	
}
