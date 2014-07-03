package BaseModule.exception.notFound;

import BaseModule.exception.PseudoException;

public class ClassPhotoNotFoundException extends PseudoException{

private static final long serialVersionUID = 1356275365147457580L;

	
	public ClassPhotoNotFoundException(){
        super("对不起，您要找的课程图片不存在");
    }
	
	public ClassPhotoNotFoundException(String exceptionText){
        super(exceptionText);
    }

	@Override
    public int getCode() {
        return 9;
    }
}
