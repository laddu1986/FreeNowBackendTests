package com.freenow.api.common.context;

import com.freenow.api.utils.RestTemplate;
import com.freenow.global.utils.ConfigReader;
import com.freenow.global.utils.LogUtils;

/**
 * Using PicoContainer to share state between stepdefs in a scenario
 *
 * @author Kushal Bhalaik
 *
 */
public class TestContext {
	public ScenarioContext scenarioContext;
	private static ConfigReader configReader;
	private LogUtils logUtils;
	private RestTemplate restTemplate;
	
	public TestContext() {
		scenarioContext = new ScenarioContext();
		logUtils = LogUtils.getInstance(TestContext.class);
		configReader = ConfigReader.getInstance();
		restTemplate = RestTemplate.getInstance();
	}

	public ScenarioContext getScenarioContext() {
		return scenarioContext;
	}
	
	public ConfigReader getConfigReader() {
		return configReader;
	}

	public LogUtils getLogUtils() {
		return logUtils;
	}
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

}
