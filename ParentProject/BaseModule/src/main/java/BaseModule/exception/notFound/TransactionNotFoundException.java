package BaseModule.exception.notFound;

import BaseModule.exception.PseudoException;

public class TransactionNotFoundException extends PseudoException{

	private static final long serialVersionUID = 7210353179905786104L;

	
	public TransactionNotFoundException(){
        super("对不起，您要找的交易单不存在");
    }
	
	public TransactionNotFoundException(String exceptionText){
        super(exceptionText);
    }

	@Override
    public int getCode() {
        return 6;
    }
}
