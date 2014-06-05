package BaseModule.exception.notFound;

import BaseModule.exception.PseudoException;

public class CreditNotFoundException extends PseudoException{

private static final long serialVersionUID = 1L;
	
	protected String exceptionType = "CreditNotFound";

	public CreditNotFoundException(){
        super("对不起，您要找的积分不存在");
    }
	
	public CreditNotFoundException(String exceptionText){
        super(exceptionText);
    }

	@Override
    public int getCode() {
        return 8;
    }
}
