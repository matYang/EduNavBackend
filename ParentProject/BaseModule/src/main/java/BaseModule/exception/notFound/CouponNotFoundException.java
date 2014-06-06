package BaseModule.exception.notFound;

import BaseModule.exception.PseudoException;

public class CouponNotFoundException extends PseudoException{

private static final long serialVersionUID = 1L;
	
	protected String exceptionType = "CouponNotFound";

	public CouponNotFoundException(){
        super("对不起，您要找的优惠券不存在");
    }
	
	public CouponNotFoundException(String exceptionText){
        super(exceptionText);
    }

	@Override
    public int getCode() {
        return 7;
    }
}
