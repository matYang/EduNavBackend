package BaseModule.exception.notFound;

import BaseModule.exception.PseudoException;

public class UserNotFoundException extends PseudoException {
	
	private static final long serialVersionUID = -7250491721825908242L;

	public UserNotFoundException(){
        super("对不起，您要找的用户不存在");
    }
	
	public UserNotFoundException(String exceptionText){
        super(exceptionText);
    }

	@Override
    public int getCode() {
        return 1;
    }
	
}
