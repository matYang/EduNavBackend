package BaseModule.exception.notFound;

import BaseModule.exception.PseudoException;

public class PartnerNotFoundException extends PseudoException{
	
	private static final long serialVersionUID = -1501312138467329562L;

	
	public PartnerNotFoundException(){
        super("对不起，您要找的机构不存在");
    }
	
	public PartnerNotFoundException(String exceptionText){
        super(exceptionText);
    }

	@Override
    public int getCode() {
        return 2;
    }
}
