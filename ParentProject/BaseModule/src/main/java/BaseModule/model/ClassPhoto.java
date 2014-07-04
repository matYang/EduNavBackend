package BaseModule.model;

import java.io.Serializable;
import java.util.Calendar;
import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Visibility;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;

public class ClassPhoto implements PseudoModel, Serializable{

	private static final long serialVersionUID = 10l;
	
	private long classPhotoId;
	private int partnerId;
	
	private String imgUrl;
	private String title;
	private String description;
	
	private Visibility visibility;
	private Calendar creationTime;
	
	
	public ClassPhoto(long classPhotoId, int partnerId, String imgUrl, String title, String description, Calendar creationTime,Visibility visibility) {
		super();
		this.classPhotoId = classPhotoId;
		this.partnerId = partnerId;
		this.imgUrl = imgUrl;
		this.title = title;
		this.description = description;
		this.creationTime = creationTime;
		this.visibility = visibility;
	}
	

	public ClassPhoto(int partnerId, String imgUrl, String title, String description) {
		super();
		this.classPhotoId = -1;
		this.partnerId = partnerId;
		this.imgUrl = imgUrl;
		this.title = title;
		this.description = description;
		this.creationTime = DateUtility.getCurTimeInstance();
		this.visibility = Visibility.visible;
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

	public void setClassPhotoId(long classPhotoId) {
		this.classPhotoId = classPhotoId;
	}
	
	public Calendar getCreationTime() {
		return creationTime;
	}

	public Visibility getVisibility() {
		return visibility;
	}


	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
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
		if (visibility != other.visibility)
			return false;
		return true;
	}


}
