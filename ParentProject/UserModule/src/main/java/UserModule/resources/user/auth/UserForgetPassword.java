package UserModule.resources.user.auth;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import UserModule.resources.UserPseudoResource;
import UserModule.service.UserForgotPasswordDaoService;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.authentication.AuthenticationException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.User;
import BaseModule.service.EncodingService;
import BaseModule.service.SMSService;
import BaseModule.service.ValidationService;

public final class UserForgetPassword extends UserPseudoResource{
	private final String apiId = UserForgetPassword.class.getSimpleName();
	
	@Get
	public Representation forgetPassword(){
        
		try{
			try{
				this.validateAuthentication();
				throw new ValidationException("请刷新页面或先登出之前的账户");
			} catch (AuthenticationException e){
				//not logged in, proceed
			}
			
			
			String cellNum = this.getQueryVal("phone");
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), cellNum);
			
			if (ValidationService.validatePhone(cellNum)){
				
				if (!UserDaoService.isCellPhoneAvailable(cellNum)){
					String authCode = UserForgotPasswordDaoService.openSession(cellNum);
					SMSService.sendUserForgetPasswordSMS(cellNum, authCode);
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
			if (!UserForgotPasswordDaoService.valdiateSession(cellNum, authCode)){
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
			JSONObject jsonPair = this.getJSONObj(entity);
			newPair = validateForgetPasswordJSON(jsonPair);

			UserDaoService.recoverPassword(newPair[0], newPair[1]);
			UserForgotPasswordDaoService.closeSession(newPair[0]);
			
			//open new authentication, log every other client out
			User user = UserDaoService.getUserByPhone(newPair[0]);
			this.closeAuthentication();
			this.openAuthentication(user.getUserId());

			setStatus(Status.SUCCESS_OK);
			quickResponseText = "密码修改成功";
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, user.getUserId(), this.getUserAgent(), "<Password Classified>");
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
