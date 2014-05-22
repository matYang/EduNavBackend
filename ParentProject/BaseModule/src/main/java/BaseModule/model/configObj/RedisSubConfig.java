package BaseModule.model.configObj;

public class RedisSubConfig {
	
	public RedisSubConfig(){
		super();
	}
	
	public String keyPrefix;
	public int authCodeLength;
	public long activeThreshold;
	public long expireThreshold;
	public boolean authCodeUpper;

}
