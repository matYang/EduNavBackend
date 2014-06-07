package PartnerModule.resources.partner.auth;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import BaseModule.common.DebugLog;
import BaseModule.dbservice.PartnerDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Partner;
import BaseModule.service.EncodingService;
import BaseModule.service.SMSService;
import BaseModule.service.ValidationService;
import PartnerModule.resources.PartnerPseudoResource;
import PartnerModule.service.PartnerForgotPasswordDaoService;

public class PartnerForgetPassword extends PartnerPseudoResource{
	private final String apiId = PartnerForgetPassword.class.getSimpleName();
	
	
	@Get
	public Representation forgetPassword(){
        
		try{
			String cellNum = this.getQueryVal("phone");
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), cellNum);
			
			if (ValidationService.validatePhone(cellNum)){
				
				if (!PartnerDaoService.isCellPhoneAvailable(cellNum)){
					String authCode = PartnerForgotPasswordDaoService.openSession(cellNum);
					SMSService.sendPartnerForgetPasswordSMS(cellNum, authCode);
					setStatus(Status.SUCCESS_OK);
				}
				else{
					throw new ValidationException("手机号码尚未被注册");
				}
			}
			else{
				throw new ValidationException("手机号码格式不正确");
			}
			
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
	
	protected String[] validateForgetPasswordJSON(JSONObject jsonPair) throws ValidationException{
		String[] newPair = new String[2];
		
		try{
			
			String cellNum = EncodingService.decodeURI(jsonPair.getString("phone"));
			String newPassword = EncodingService.decodeURI(jsonPair.getString("newPassword"));
			String confirmNewPassword = EncodingService.decodeURI(jsonPair.getString("confirmNewPassword"));
			String authCode = EncodingService.decodeURI(jsonPair.getString("authCode"));

			if (!ValidationService.validatePhone(cellNum)){
				throw new ValidationException("电话号码格式不正确");
			}
			if (!ValidationService.validatePassword(newPassword)){
				throw new ValidationException("新码格式不正确");
			}
			if (!newPassword.equals(confirmNewPassword )){
				throw new ValidationException("两次输入密码不相符");
			}
			if (!PartnerForgotPasswordDaoService.valdiateSession(cellNum, authCode)){
				throw new ValidationException("手机验证码错误");
			}
			newPair[0] = cellNum;
			newPair[1] = newPassword;
			
		} catch (JSONException | IOException e){
			throw new ValidationException("无效数据格式");
		}
		
		return newPair;
	}
	
	@Post
	public Representation findPassword(Representation entity){
		String[] newPair = new String[2];
		String quickResponseText = "";
		
		try {
			this.checkEntity(entity);
			JSONObject jsonPair =  this.getJSONObj(entity);
			newPair = validateForgetPasswordJSON(jsonPair);

			PartnerDaoService.recoverPassword(newPair[0], newPair[1]);
			PartnerForgotPasswordDaoService.closeSession(newPair[0]);
			
			//open new authentication, log every other client out
			Partner partner = PartnerDaoService.getPartnerByPhone(newPair[0]);
			this.closeAuthentication();
			this.openAuthentication(partner.getPartnerId());

			setStatus(Status.SUCCESS_OK);
			quickResponseText = "密码修改成功";
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, partner.getPartnerId(), this.getUserAgent(), "<Password Classified>");
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
