package BaseModule.configurations;

public class DatabaseConfig {

	public static final String jdbcUri = ServerConfig.configurationMap.get("jdbcUri");
	public static final String redisUri = ServerConfig.configurationMap.get("redisUri");

	
	//redis related
	public static final String redisSeperator = "+";
	public static final String redisSeperatorRegex = "\\+";
	public static final long session_updateThreshold = 259200000l;		//3 days
	public static final long session_expireThreshold = 604800000l;		//7 days
	public static final long emailActivation_expireThreshold = 604800000l;		//7 days
	public static final long forgetPassword_expireThreshold = 604800000l;		//7 days
	
	
	/*sql*/	
	public static final String UserSRDeparture = "UserSRDeparture";
	public static final String UserSRArrival = "UserSRArrival";
	public static final String DatabasesDeparture = "DatabasesDeparture";
	public static final String DatabasesArrival = "DatabasesArrival";
	public static final String sqlPass = ServerConfig.configurationMap.get("sqlPass");
	public static final String sqlUser = ServerConfig.configurationMap.get("sqlUser");
	
}
