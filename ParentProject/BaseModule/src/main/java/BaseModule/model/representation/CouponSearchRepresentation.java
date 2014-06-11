package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.exception.PseudoException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

public class CouponSearchRepresentation implements PseudoModel, PseudoRepresentation {

	private long couponId;
	private int bookingId;

	private int userId;
	private int startPrice;
	private int finishPrice;
	private Calendar creationTime;
	private Calendar expireTime;
	private CouponStatus status;
	private CouponOrigin origin;


	public CouponSearchRepresentation() {
		this.couponId = -1l;
		this.bookingId = -1;
		this.userId = -1;
		this.startPrice = -1;
		this.finishPrice = -1;
		this.creationTime = null;
		this.expireTime = null;
		this.status = null;
		this.origin = null;
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
	public String serialize() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
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

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
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

	public int getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(int startPrice) {
		this.startPrice = startPrice;
	}

	public int getFinishPrice() {
		return finishPrice;
	}

	public void setFinishPrice(int finishPrice) {
		this.finishPrice = finishPrice;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
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

	@Override
	public String toString() {
		return "CouponSearchRepresentation [couponId=" + couponId
				+ ", bookingId=" + bookingId + ", userId=" + userId + ", startPrice="
				+ startPrice + ", finishPrice=" + finishPrice + ", origin=" + origin
				+ ", creationTime=" + creationTime + ", expireTime="
				+ expireTime + ", status=" + status + "]";
	}

	public String getSearchQuery(){
		String query = "SELECT * from CouponDao ";
		boolean start = false;

		/* Note:Make sure the order following is the same as that in Dao */

		if(this.getCouponId() > 0){
			query += "where ";
			start = true;

			query += "couponId = ? ";
		}
		if(this.getBookingId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "bookingId = ? ";
		}
		if(this.getStartPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "amount >= ? ";
		}
		if(this.getFinishPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "amount <= ? ";
		}
		if(this.getUserId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "userId = ? ";
		}
		if(this.getCreationTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime = ? ";
		}
		if(this.getExpireTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "expireTime = ? ";
		}
		if(this.getStatus() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "status = ? ";
		}
		if(this.getOrigin() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "couponOrigin = ? ";
		}
		return query;
	}	

}
