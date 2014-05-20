package AdminModule.resources.partner;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import AdminModule.resources.AdminPseudoResource;
import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.AccountStatus;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.factory.JSONFactory;
import BaseModule.model.Partner;
import BaseModule.service.ValidationService;

public class GetPartners extends AdminPseudoResource{

//	protected Partner validatePartnerJSON(Representation entity) throws ValidationException{
//		JSONObject jsonPartner = null;
//		Partner partner = null;
//		
//		try {
//			jsonPartner = (new JsonRepresentation(entity)).getJsonObject();
//			
//			
//			String phone = jsonPartner.getString("phone");
//			String name = jsonPartner.getString("name");
//			String instName = jsonPartner.getString("instName");
//			String licence = jsonPartner.getString("licence");
//			String organizationNum = jsonPartner.getString("organizationNum");
//			String reference = jsonPartner.getString("reference");
//			AccountStatus status = AccountStatus.fromInt(jsonPartner.getInt("status"));
//			String password = jsonPartner.getString("password");
//			String confirmPassword = jsonPartner.getString("confirmPassword");			
//			
//			partner = new Partner(name,instName,licence, organizationNum,reference,password, phone, status);
//			if (!ValidationService.isNameValid(name)){
//				throw new ValidationException("姓名格式不正确");
//			}
//			if (!ValidationService.isCellNumValid(phone)){
//				throw new ValidationException("手机号码格式不正确");
//			}
//			if (!ValidationService.isPasswordValid(password)){
//				throw new ValidationException("密码格式不正确");
//			}
//			if (!password.equals(confirmPassword)){
//				throw new ValidationException("两次输入密码不相符");
//			}			
//
//		} catch (JSONException | IOException e) {
//			throw new ValidationException("无效数据格式");
//		}
//		return partner;
//	}
	
	
	@Get
	public Representation getAllPartners(){

		ArrayList<Partner> allPartner = PartnerDaoService.getAllPartners();
		JSONArray jsonArray = JSONFactory.toJSON(allPartner);
		
		Representation result = new JsonRepresentation(jsonArray);

		this.addCORSHeader();
		return result;
	}
	

//	@Post
//	public Representation createUser(Representation entity) {
//		
//		JSONObject newJsonPartner = new JSONObject();
//		Partner creationFeedBack = null;
//		
//		try{
//			this.checkEntity(entity);
//			Partner newPartner = validatePartnerJSON(entity);
//			creationFeedBack = PartnerDaoService.createPartner(newPartner);			
//			
//			//first close authentication as it is registration, then open brand new authentication
//			this.closeAuthentication();
//			this.openAuthentication(creationFeedBack.getPartnerId());
//
//			DebugLog.d("@Post::resources::createPartner: available: " + creationFeedBack.getPhone() + " id: " +  creationFeedBack.getPartnerId());
//			newJsonPartner = JSONFactory.toJSON(creationFeedBack);
//
//		} catch(PseudoException e){
//			this.addCORSHeader();
//			return this.doPseudoException(e);
//		} catch (Exception e){
//			this.addCORSHeader();
//			return this.doException(e);
//		}
//
//		Representation result = new JsonRepresentation(newJsonPartner);
//		
//		this.addCORSHeader(); 
//		return result;
//	}
}
