package BaseModule.exception;

public class AuthenticationException extends PseudoException{
	private static final long serialVersionUID = 1L;

	protected String exceptionType = "GeneralValidationException";

	public AuthenticationException(){
		super("验证失败");
	}

	public AuthenticationException(String exceptionText){
		super(exceptionText);
	}

	@Override
	public int getCode() {
		return 17;
	}
}
