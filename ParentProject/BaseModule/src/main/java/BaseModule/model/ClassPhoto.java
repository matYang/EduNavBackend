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
	private String title;
	private String description;
	
	private Calendar creationTime;
	
	
	public ClassPhoto(long classPhotoId, int partnerId, String imgUrl, String title, String description, Calendar creationTime) {
		super();
		this.classPhotoId = classPhotoId;
		this.partnerId = partnerId;
		this.imgUrl = imgUrl;
		this.title = title;
		this.description = description;
		this.creationTime = creationTime;
	}
	

	public ClassPhoto(int partnerId, String imgUrl, String title, String description) {
		super();
		this.classPhotoId = -1;
		this.partnerId = partnerId;
		this.imgUrl = imgUrl;
		this.title = title;
		this.description = description;
		this.creationTime = DateUtility.getCurTimeInstance();
	}
	
	public ClassPhoto() {
		super();
		this.classPhotoId = -1;
		this.partnerId = -1;
		this.imgUrl = "";
		this.title = "";
		this.description = "";
		this.creationTime = DateUtility.getCurTimeInstance();
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getClassPhotoId() {
		return classPhotoId;
	}

	public Calendar getCreationTime() {
		return creationTime;
	}

	@Override
	public JSONObject toJSON() throws Exception {
		return ModelReflectiveService.toJSON(this);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (classPhotoId ^ (classPhotoId >>> 32));
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((imgUrl == null) ? 0 : imgUrl.hashCode());
		result = prime * result + partnerId;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
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
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (imgUrl == null) {
			if (other.imgUrl != null)
				return false;
		} else if (!imgUrl.equals(other.imgUrl))
			return false;
		if (partnerId != other.partnerId)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	


}
