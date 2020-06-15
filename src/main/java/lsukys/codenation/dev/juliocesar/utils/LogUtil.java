package lsukys.codenation.dev.juliocesar.utils;

import java.io.File;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

	/**
	 * Cria um logger
	 * @param <T>
	 * @param class1
	 * @return
	 */
	public static <T> Logger getLogger(Class<T> class1) {
		LogUtil logUtil = new LogUtil();
		System.setProperty("java.util.logging.config.file", logUtil.getFileFromResources("log4j.properties").getPath());
		Logger logger = LoggerFactory.getLogger(class1);
		return logger;

	}
	
	/**
	 * Recupera o arquivo de log dos resources.
	 * @param fileName
	 * @return
	 */
    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }
    }

}
