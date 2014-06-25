package BaseModule.model.dataObj;

public class RedisAccessControlObj {
	public RedisAccessControlObj(){
		super();
	}
	
	public String keyPrefix;
	public int lockCount;
	public long lockThreshold;

}
