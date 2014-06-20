package BaseModule.model.configObj;

public class RedisAuthenticationObj {
	
	public RedisAuthenticationObj(){
		super();
	}
	
	public String keyPrefix;
	public int authCodeLength;
	public long activeThreshold;
	public long expireThreshold;
	public boolean authCodeUpper;

}
