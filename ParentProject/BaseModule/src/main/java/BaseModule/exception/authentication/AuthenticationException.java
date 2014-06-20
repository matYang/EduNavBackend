package BaseModule.exception.authentication;

import BaseModule.exception.PseudoException;

public class AuthenticationException extends PseudoException{

	private static final long serialVersionUID = 5818432084515444759L;

	public AuthenticationException(){
		super("对不起，账户验证失败，请您重新登录");
	}

	public AuthenticationException(String exceptionText){
		super(exceptionText);
	}

	@Override
	public int getCode() {
		return 17;
	}
}
