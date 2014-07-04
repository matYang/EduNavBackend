package BaseModule.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

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
	public Teacher(long teacherId, int partnerId, int popularity,
			String imgUrl, String name, String intro, Visibility visibility,
			Calendar creationTime) {
		super();
		this.teacherId = teacherId;
		this.partnerId = partnerId;
		this.popularity = popularity;
		this.imgUrl = imgUrl;
		this.name = name;
		this.intro = intro;
		this.visibility = visibility;
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
		this.visibility = Visibility.visible;
		this.popularity = 1;
	}
	
	public Teacher() {
		super();
		this.teacherId = -1;
		this.partnerId = -1;
		this.imgUrl = "";
		this.name = "";
		this.intro = "";
		this.creationTime = DateUtility.getCurTimeInstance();
		this.visibility = Visibility.visible;
		this.popularity = 1;
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

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
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
	
	public void loadFromMap(Map<String, String> kvps) throws Exception{
		ModelReflectiveService.storeKvps(this, kvps);
	}
	
	public void storeJSON(JSONObject jsonModel) throws Exception{
		ModelReflectiveService.storeJSON(this, jsonModel);
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
		if (popularity != other.popularity)
			return false;
		if (teacherId != other.teacherId)
			return false;
		if (visibility != other.visibility)
			return false;
		return true;
	}
	

}
