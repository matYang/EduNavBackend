package BaseModule.model.representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.CouponOrigin;
import BaseModule.configurations.EnumConfig.CouponStatus;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;
import BaseModule.service.RepresentationReflectiveService;

public class CouponSearchRepresentation implements PseudoModel, PseudoRepresentation {

	private long couponId;
	private int bookingId;

	private int userId;
	private int startAmount;
	private int finishAmount;
	private int startOriginalAmount;
	private int finishOriginalAmount;
	private Calendar creationTime;
	private Calendar expireTime;
	private CouponStatus status;
	private CouponOrigin origin;


	public CouponSearchRepresentation() {
		this.couponId = -1l;
		this.bookingId = -1;
		this.userId = -1;
		this.startAmount = -1;
		this.finishAmount = -1;
		this.startOriginalAmount = -1;
		this.finishOriginalAmount = -1;
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
	public String serialize() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ValidationException {
		return RepresentationReflectiveService.serialize(this);
	}

	@Override
	public boolean isEmpty() throws Exception {
		return RepresentationReflectiveService.isEmpty(this);
	}

	@Override
	public JSONObject toJSON() throws ValidationException {
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

	public int getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(int startAmount) {
		this.startAmount = startAmount;
	}

	public int getFinishAmount() {
		return finishAmount;
	}

	public void setFinishAmount(int finishAmount) {
		this.finishAmount = finishAmount;
	}
	
	public int getStartOriginalAmount() {
		return startOriginalAmount;
	}

	public void setStartOriginalAmount(int startOriginalAmount) {
		this.startOriginalAmount = startOriginalAmount;
	}

	public int getFinishOriginalAmount() {
		return finishOriginalAmount;
	}

	public void setFinishOriginalAmount(int finishOriginalAmount) {
		this.finishOriginalAmount = finishOriginalAmount;
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
				+ ", bookingId=" + bookingId + ", userId=" + userId
				+ ", startAmount=" + startAmount + ", finishAmount="
				+ finishAmount + ", startOriginalAmount=" + startOriginalAmount
				+ ", finishOriginalAmount=" + finishOriginalAmount
				+ ", creationTime=" + creationTime + ", expireTime="
				+ expireTime + ", status=" + status + ", origin=" + origin
				+ "]";
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
		if(this.getStartAmount() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "amount >= ? ";
		}
		if(this.getFinishAmount() >= 0){
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
		if(this.getStartOriginalAmount() != -1){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "originalAmount >= ? ";
		}
		if(this.getFinishOriginalAmount() != -1){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "originalAmount <= ? ";
		}
		return query;
	}	

}
