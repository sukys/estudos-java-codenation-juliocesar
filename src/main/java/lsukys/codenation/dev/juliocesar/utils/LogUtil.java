package lsukys.codenation.dev.juliocesar.utils;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lsukys.codenation.dev.juliocesar.App;

public class LogUtil {

	/**
	 * Configura o Debugger com level "Trace"
	 * 
	 * @return
	 */
	public static Properties configure() {
		return configure("TRACE");
	}

	/**
	 * Configura o Debugger
	 * 
	 * @param level "WARN", "INFO", "DEBUG"
	 * @return
	 */
	public static Properties configure(String level) {
		Properties prop = new Properties();
		try {
			prop.load(App.class.getResourceAsStream("/log4j.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		prop.setProperty("log4j.rootLogger", level);
		prop.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.stdout.Target", "System.out");
		prop.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d{yy/MM/dd HH:mm:ss} %p %c{2}: %m%n");
		return prop;

	}

	public static <T> Logger getLogger(Class<T> class1) {
		configure();
		Logger logger = LoggerFactory.getLogger(class1);
		return logger;

	}

}
