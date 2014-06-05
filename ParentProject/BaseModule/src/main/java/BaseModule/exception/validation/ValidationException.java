package BaseModule.exception.validation;

import BaseModule.exception.PseudoException;

public class ValidationException extends PseudoException{

	private static final long serialVersionUID = 1L;

	protected String exceptionType = "GeneralValidationException";

	private ValidationException(){
		super("验证失败，请核对内容");
	}

	public ValidationException(String exceptionText){
		super(exceptionText);
	}

	@Override
	public int getCode() {
		return 18;
	}
}
