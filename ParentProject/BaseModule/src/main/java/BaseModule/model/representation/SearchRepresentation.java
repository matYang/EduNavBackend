package BaseModule.model.representation;

import java.util.Calendar;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoRepresentation;

public class SearchRepresentation implements PseudoModel, PseudoRepresentation{
	
	//used for broad search
	private String cat;
	private String subCat;
	
	private String city;
	private String district;
	
	private Calendar startTime;
	private Calendar finishTime;
	
	private String institutionName;
	private AccountStatus statis;
	
	private int startPrice;
	private int finishPrice;
	
	private String courseReference;
	private String partnerReference;
	
	private int courseId;
	private int partnerId;
	private int userId;
	private Calendar creationTime;
	
	
	public SearchRepresentation(String srStr){
		
		
		
	}

	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deSerialize(String serializedRepresentation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

}
