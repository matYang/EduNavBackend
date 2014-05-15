package AdminModule.exception;

import BaseModule.exception.PseudoException;

public class AdminAccountNotFoundException extends PseudoException{

private static final long serialVersionUID = 1L;
	
	protected String exceptionType = "AdminAccountNotFound";

	public AdminAccountNotFoundException(){
        super("对不起，您要找的管理员不存在");
    }
	
	public AdminAccountNotFoundException(String exceptionText){
        super(exceptionText);
    }

	@Override
    public int getCode() {
        return 4;
    }
}
