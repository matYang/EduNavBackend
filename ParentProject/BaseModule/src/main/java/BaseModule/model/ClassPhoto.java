package BaseModule.model;

import java.io.Serializable;
import java.util.Calendar;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;

public class ClassPhoto implements PseudoModel, Serializable{

	private static final long serialVersionUID = 10l;
	
	private long classPhotoId;
	private int partnerId;
	
	private String imgUrl;
	private String intro;
	
	private Calendar creationTime;

	public ClassPhoto(long classPhotoId, int partnerId, String imgUrl, String intro, Calendar creationTime) {
		super();
		this.classPhotoId = classPhotoId;
		this.partnerId = partnerId;
		this.imgUrl = imgUrl;
		this.intro = intro;
		this.creationTime = creationTime;
	}

	public ClassPhoto(int partnerId, String imgUrl, String intro) {
		super();
		this.classPhotoId = -1;
		this.partnerId = partnerId;
		this.imgUrl = imgUrl;
		this.intro = intro;
		this.creationTime = DateUtility.getCurTimeInstance();
	}
	
	public long getClassPhotoId() {
		return classPhotoId;
	}

	public void setClassPhotoId(long classPhotoId) {
		this.classPhotoId = classPhotoId;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public JSONObject toJSON() throws Exception {
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
		ClassPhoto other = (ClassPhoto) obj;
		if (classPhotoId != other.classPhotoId)
			return false;
		if (imgUrl == null) {
			if (other.imgUrl != null)
				return false;
		} else if (!imgUrl.equals(other.imgUrl))
			return false;
		if (intro == null) {
			if (other.intro != null)
				return false;
		} else if (!intro.equals(other.intro))
			return false;
		if (partnerId != other.partnerId)
			return false;
		return true;
	}
	
	


}
