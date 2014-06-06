package BaseModule.common;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import BaseModule.configurations.ServerConfig;

public class DebugLog {

	private static final Logger systemLogger = Logger.getLogger("system.logger");
	private static final Logger behaviorsLogger = Logger.getLogger("behaviors.logger");
	

	public static void initializeLogger(){		
		PropertyConfigurator.configure(ServerConfig.resourcePrefix + "log4j.properties");
	}
	
	public static void d(Exception e){
		//using reflection to get caller name, 500x faster than stack trace
		//if not accessible, do:  right click on project -> configure build path -> remove JRE system library -> add Library -> JRE System Library (Default) -> OK, clean & recompile
		String caller = sun.reflect.Reflection.getCallerClass(2).getName();
		systemLogger.info(caller + " got Exception! ",e);			
		e.printStackTrace();
	}
	
	public static void b_d(Exception e){
		String caller = sun.reflect.Reflection.getCallerClass(2).getName();
		behaviorsLogger.info(caller + " got Exception! ",e);			
		e.printStackTrace();
	}
	

	public static void d(String message){
		log(message);
	}	

	public static void b_d(String message){
		b_log(message);
	}
	
	private static void log(String message){
		systemLogger.info(message);
	}
	
	private static void b_log(String message){
		behaviorsLogger.info(message);
	}
	
	
}
