package UserModule.resources.user.auth;

import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import UserModule.resources.UserPseudoResource;
import UserModule.service.UserCellVerificationDaoService;
import BaseModule.common.DebugLog;
import BaseModule.dbservice.UserDaoService;
import BaseModule.exception.PseudoException;
import BaseModule.exception.validation.ValidationException;
import BaseModule.service.EncodingService;
import BaseModule.service.SMSService;
import BaseModule.service.ValidationService;

public final class UserCellVerification extends UserPseudoResource{
	private final String apiId = UserCellVerification.class.getSimpleName();
	
	@Get
	public Representation smsVerification() {
		
		try{
			String cellNum = this.getQueryVal("phone");
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_get, -1, this.getUserAgent(), cellNum);
			
			if (ValidationService.validatePhone(cellNum)){
				
				if (UserDaoService.isCellPhoneAvailable(cellNum)){
					String authCode = UserCellVerificationDaoService.openSession(cellNum);
					SMSService.sendUserCellVerificationSMS(cellNum, authCode);
					setStatus(Status.SUCCESS_OK);
				}
				else{
					throw new ValidationException("手机号码已经被注册");
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
	
	
	@Post
	public Representation smsVerify(Representation entity) {
		
		try{
			JSONObject jsonPair = this.getJSONObj(entity);
			String cellNum = EncodingService.decodeURI(jsonPair.getString("phone"));
			String authCode = EncodingService.decodeURI(jsonPair.getString("authCode"));
			
			DebugLog.b_d(this.moduleId, this.apiId, this.reqId_post, -1, this.getUserAgent(), jsonPair.toString());
			
			if (ValidationService.validatePhone(cellNum)){
				boolean verified = UserCellVerificationDaoService.valdiateSession(cellNum, authCode);
				if (verified){
					setStatus(Status.SUCCESS_OK);
				}
				else{
					setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
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
	

}
