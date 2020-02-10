package com.freenow.global.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * This class provides instance of ConfigReader for reading values from "config.properties" & <env>/env.config
*/
public class ConfigReader {

	private FileInputStream fis = null;
	private Properties pro = new Properties();
	private final String runnerConfigFilePath = System.getProperty("user.dir") + "/src/test/resources/runner.config";

	private String envFilePath = null;
	private static ConfigReader configReaderInstance = null;

	private ConfigReader() {

	}

	public static ConfigReader getInstance() {

		if (configReaderInstance == null)
			configReaderInstance = new ConfigReader();

		return configReaderInstance;
	}

	/*
	 * This method provides reading values from "<env>/env.config", takes one
	 * parameter "propName" whose value needs to be returned
	 * 
	 * @returns "propNameValue"
	 */
	public String getProperty(String propName) {
		
		try {
			fis = new FileInputStream(new File(getFilePath()));
		} catch (FileNotFoundException e) {

			e.printStackTrace();

		}

		try {
			pro.load(fis);
		} catch (IOException e) {

			e.printStackTrace();

		}

		return pro.getProperty(propName);
	}

	/*
	 * This method provides reading values from "runner.config", takes one
	 * parameter "propName" whose value needs to be returned
	 */
	public String getRunnerConfigProperty(String propName) {
	
		try {
			fis = new FileInputStream(new File(runnerConfigFilePath));
		} catch (FileNotFoundException e) {

			e.printStackTrace();

		}

		try {
			pro.load(fis);
		} catch (IOException e) {

			e.printStackTrace();
		}

		return pro.getProperty(propName);
	}

	private String getFilePath() {

		if (null != System.getProperty("env")) {
			envFilePath = System.getProperty("user.dir") + "/src/test/resources/environments/"
					+ System.getProperty("env") + "/env.config";
		} else {
			envFilePath = System.getProperty("user.dir") + "/src/test/resources/environments/"
					+ getRunnerConfigProperty("ENV") + "/env.config";
		}

		return envFilePath;
	}

}
