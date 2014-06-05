package BaseModule.exception.notFound;

import BaseModule.exception.PseudoException;

public class BookingNotFoundException extends PseudoException{

private static final long serialVersionUID = 1L;
	
	protected String exceptionType = "BookingNotFound";

	public BookingNotFoundException(){
        super("对不起，您要找的预定不存在");
    }
	
	public BookingNotFoundException(String exceptionText){
        super(exceptionText);
    }

	@Override
    public int getCode() {
        return 5;
    }
}
