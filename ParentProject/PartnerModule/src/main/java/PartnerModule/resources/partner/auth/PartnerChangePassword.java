package PartnerModule.resources.partner.auth;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Partner;
import BaseModule.service.SMSService;
import BaseModule.service.ValidationService;
import PartnerModule.resources.PartnerPseudoResource;
import PartnerModule.service.PartnerChangePasswordVerificationDaoService;

public class PartnerChangePassword extends PartnerPseudoResource {
	
	
	@Get
	public Representation changePasswordVerification() {
		
		try{
			int partnerId = this.validateAuthentication();
			
			String authCode = PartnerChangePasswordVerificationDaoService.openSession(partnerId);
			Partner partner = PartnerDaoService.getPartnerById(partnerId);
			
			SMSService.sendPartnerChangePasswordSMS(partner.getPhone(), authCode);
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
	

	protected String[] validateForgetPasswordJSON(int partnerId, Representation entity) throws ValidationException{
		JSONObject jsonPasswords = null;
		String[] passwords = new String[2];
		
		try{
			jsonPasswords = (new JsonRepresentation(entity)).getJsonObject();
			
			String oldPassword = jsonPasswords.getString("oldPassword");
			String newPassword = jsonPasswords.getString("newPassword");
			String confirmNewPassword = jsonPasswords.getString("confirmNewPassword");
			String authCode = jsonPasswords.getString("authCode");

			if (!ValidationService.validatePassword(oldPassword)){
				throw new ValidationException("旧密码格式不正确");
			}
			if (!ValidationService.validatePassword(newPassword)){
				throw new ValidationException("新码格式不正确");
			}
			if (!newPassword.equals(confirmNewPassword )){
				throw new ValidationException("两次输入密码不相符");
			}
			if (!PartnerChangePasswordVerificationDaoService.valdiateSession(partnerId, authCode)){
				throw new ValidationException("手机验证码错误");
			}
			passwords[0] = oldPassword;
			passwords[1] = newPassword;
			
		} catch (JSONException | IOException e){
			throw new ValidationException("无效数据格式");
		}
		
		return passwords;
	}
	

	@Put
	public Representation changePassword(Representation entity) {
		String[] passwords = new String[2];
		String quickResponseText = "";
		
		try {
			this.checkEntity(entity);
			int partnerId = this.validateAuthentication();

			passwords = validateForgetPasswordJSON(partnerId, entity);

			PartnerDaoService.changePassword(partnerId, passwords[0], passwords[1]);
			PartnerChangePasswordVerificationDaoService.closeSession(partnerId);
			
			//open new authentication, log every other client out
			this.closeAuthentication();
			this.openAuthentication(partnerId);

			setStatus(Status.SUCCESS_OK);
			quickResponseText = "密码修改成功";

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
