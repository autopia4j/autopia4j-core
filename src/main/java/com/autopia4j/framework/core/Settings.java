package com.autopia4j.framework.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autopia4j.framework.utils.FrameworkException;
import com.autopia4j.framework.utils.Util;


/**
 * Singleton class that encapsulates the user settings specified in the properties file of the framework
 * @author vj
 */
public class Settings {
	private static final Logger logger = LoggerFactory.getLogger(Settings.class);
	private static Properties properties = loadFromPropertiesFile();
	
	private Settings() {
		// To prevent external instantiation of this class
	}
	
	/**
	 * Function to return the singleton instance of the {@link Properties} object
	 * @return Instance of the {@link Properties} object
	 */
	public static Properties getInstance() {
		return properties;
	}
	
	private static Properties loadFromPropertiesFile() {
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		
		if(frameworkParameters.getBasePath() == null) {
			logger.error("FrameworkParameters.basePath is not set!");
			throw new FrameworkException("FrameworkParameters.basePath is not set!");
		}
		
		Properties properties = new Properties();
		String configFileFolder = frameworkParameters.getBasePath() +
									Util.getFileSeparator() + "src" +
									Util.getFileSeparator() + "test" +
									Util.getFileSeparator() + "resources" +
									Util.getFileSeparator();
		
		File configFile = new File(configFileFolder + "config.custom.properties");
		if(configFile.exists()) {
			logger.info("Reading config settings from config.custom.properties");
		} else {
			logger.info("Reading config settings from config.default.properties");
			configFile = new File(configFileFolder + "config.default.properties");
		}
		
		try {
			properties.load(new FileInputStream(configFile));
		} catch (IOException e) {
			logger.error("Exception while loading the config settings file", e);
			throw new FrameworkException("IOException while loading the config settings file");
		}
		
		return properties;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}