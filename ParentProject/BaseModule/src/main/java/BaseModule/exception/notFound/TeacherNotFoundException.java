package BaseModule.exception.notFound;

import BaseModule.exception.PseudoException;

public class TeacherNotFoundException extends PseudoException{

private static final long serialVersionUID = 1356275365147457580L;

	
	public TeacherNotFoundException(){
        super("对不起，您要找的教师不存在");
    }
	
	public TeacherNotFoundException(String exceptionText){
        super(exceptionText);
    }

	@Override
    public int getCode() {
        return 10;
    }
}
