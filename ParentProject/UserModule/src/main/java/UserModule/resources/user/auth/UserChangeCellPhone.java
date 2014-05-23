package UserModule.resources.user.auth;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.User;
import BaseModule.service.EncodingService;
import BaseModule.service.SMSService;
import BaseModule.service.ValidationService;
import UserModule.resources.UserPseudoResource;
import UserModule.service.UserCellVerificationDaoService;

public class UserChangeCellPhone extends UserPseudoResource{
	
	@Get
	public Representation smsVerification() {
		
		try{
			String oldPhone = this.getQueryVal("oldPhone");
			String newPhone = this.getQueryVal("newPhone");
			if (!ValidationService.validatePhone(oldPhone)){
				throw new ValidationException("旧手机号码格式不正确");
			}
			if (!ValidationService.validatePhone(newPhone)){
				throw new ValidationException("新手机号码格式不正确");
			}
			if (!UserDaoService.isCellPhoneAvailable(newPhone)){
				throw new ValidationException("手机号码已经被注册");
			}
			int userId = this.validateAuthentication();
			User user = UserDaoService.getUserById(userId);
			if (!user.getPhone().equals(oldPhone)){
				throw new ValidationException("旧手机号码不符合账户绑定的手机号码");
			}
			
			String authCode_old = UserCellVerificationDaoService.openSession(oldPhone);
			SMSService.sendUserCellVerificationSMS(oldPhone, authCode_old);
			String authCode_new = UserCellVerificationDaoService.openSession(newPhone);
			SMSService.sendUserCellVerificationSMS(oldPhone, authCode_new);
			
			setStatus(Status.SUCCESS_OK);

		} catch(PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
		} catch (Exception e){
			this.addCORSHeader();
			return this.doException(e);
		}

		Representation result = new JsonRepresentation(new JSONObject());
		this.addCORSHeader(); 
		return result;
	}
	
	protected String[] validateChangeCellPhoneJSON(int userId, Representation entity) throws ValidationException{
		JSONObject jsonPhones = null;
		String[] phones = new String[2];
		
		try{
			jsonPhones = (new JsonRepresentation(entity)).getJsonObject();
			
			String oldPhone = EncodingService.decodeURI(jsonPhones.getString("oldPhone"));
			String newPhone = EncodingService.decodeURI(jsonPhones.getString("newPhone"));
			String authCode_old = EncodingService.decodeURI(jsonPhones.getString("authCode_old"));
			String authCode_new = EncodingService.decodeURI(jsonPhones.getString("authCode_new"));

			if (!ValidationService.validatePhone(oldPhone)){
				throw new ValidationException("旧手机号码格式不正确");
			}
			if (!ValidationService.validatePhone(newPhone)){
				throw new ValidationException("新手机号码格式不正确");
			}

			if (!UserCellVerificationDaoService.valdiateSession(oldPhone, authCode_old)){
				throw new ValidationException("旧手机号码验证码错误");
			}
			if (!UserCellVerificationDaoService.valdiateSession(newPhone, authCode_new)){
				throw new ValidationException("新手机号码验证码错误");
			}
			if (!UserDaoService.isCellPhoneAvailable(newPhone)){
				throw new ValidationException("手机号码已经被注册");
			}
			phones[0] = oldPhone;
			phones[1] = newPhone;
			
		} catch (JSONException | IOException e){
			throw new ValidationException("无效数据格式");
		}
		
		return phones;
	}
	

	@Put
	public Representation changePassword(Representation entity) {
		String[] phones = new String[2];
		String quickResponseText = "";
		
		try {
			this.checkEntity(entity);
			int userId = this.validateAuthentication();

			phones = validateChangeCellPhoneJSON(userId, entity);
			User user = UserDaoService.getUserById(userId);
			if (!user.getPhone().equals(phones[0])){
				throw new ValidationException("旧手机号码不符合账户绑定的手机号码");
			}
			
			UserDaoService.updatePhone(userId, phones[1]);
			UserCellVerificationDaoService.closeSession(phones[0]);
			UserCellVerificationDaoService.closeSession(phones[1]);
			
			//open new authentication, log every other client out
			this.closeAuthentication();
			this.openAuthentication(userId);

			setStatus(Status.SUCCESS_OK);
			quickResponseText = "手机号码修改成功";

		} catch (PseudoException e){
			this.addCORSHeader();
			return this.doPseudoException(e);
        } catch (Exception e){
        	return this.doException(e);
		}
		
		this.addCORSHeader();
		return this.quickRespond(quickResponseText);

	}
}
