package BaseModule.log4j;

import org.apache.log4j.PropertyConfigurator;

import BaseModule.configurations.ServerConfig;

public class Log4j {
	public static void configure(){				
		PropertyConfigurator.configure("log4j.properties");
	}
}
