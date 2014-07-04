package BaseModule.model;

import java.io.Serializable;
import java.util.Calendar;

import org.json.JSONObject;

import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Visibility;
import BaseModule.interfaces.PseudoModel;
import BaseModule.service.ModelReflectiveService;

public class Teacher implements PseudoModel, Serializable{

	private static final long serialVersionUID = 9l;
	
	private long teacherId;
	private int partnerId;
	private int popularity;
	
	private String imgUrl;
	private String name;
	private String intro;
	
	private Visibility visibility;
	private Calendar creationTime;

	/* for sql */
	public Teacher(long teacherId, int partnerId, String imgUrl, String name, String intro, Calendar creationTime) {
		super();
		this.teacherId = teacherId;
		this.partnerId = partnerId;
		this.imgUrl = imgUrl;
		this.name = name;
		this.intro = intro;
		this.creationTime = creationTime;
	}
	
	public Teacher(int partnerId, String imgUrl, String name, String intro) {
		super();
		this.teacherId = -1;
		this.partnerId = partnerId;
		this.imgUrl = imgUrl;
		this.name = name;
		this.intro = intro;
		this.creationTime = DateUtility.getCurTimeInstance();
	}
	
	public Teacher() {
		super();
		this.teacherId = -1;
		this.partnerId = -1;
		this.imgUrl = "";
		this.name = "";
		this.intro = "";
		this.creationTime = DateUtility.getCurTimeInstance();
	}
	
	public long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		Teacher other = (Teacher) obj;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (partnerId != other.partnerId)
			return false;
		if (teacherId != other.teacherId)
			return false;
		return true;
	}

}
