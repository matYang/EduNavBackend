package BaseModule.exception;

public class PseudoException extends Exception{


	private static final long serialVersionUID = 2620232305997865752L;
	protected String exceptionText;
	

	public PseudoException(){
        super();
        this.exceptionText = "不明错误，请稍后再试";
    }
	
	public PseudoException(String exceptionText){
		super();
		this.exceptionText = exceptionText;
	}
	
	public String getExceptionText(){
		return this.exceptionText;
	}
	
	public int getCode() {
        return 0;
    }
}
