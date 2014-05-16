package BaseModule.configurations;

public class DatabaseConfig {

	public static final String jdbcUri = ServerConfig.configurationMap.get("jdbcUri");
	public static final String redisUri = ServerConfig.configurationMap.get("redisUri");

	
	//redis related
	public static final String redisSeperator = "+";
	public static final String redisSeperatorRegex = "\\+";

	
	
	/*sql*/	
	public static final String sqlPass = ServerConfig.configurationMap.get("sqlPass");
	public static final String sqlUser = ServerConfig.configurationMap.get("sqlUser");
	
}
