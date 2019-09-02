package com.freenow.api.common.context;


import java.util.HashMap;
import java.util.Map;

import io.restassured.response.Response;

public class ScenarioContext {

	private Map<String, Object> scenarioContext;

	public ScenarioContext() {
		scenarioContext = new HashMap<>();
	}

	public void setContext(ContextEnums key, Object value) {
		scenarioContext.put(key.toString(), value);
	}

	public Object getContext(ContextEnums key) {
		return scenarioContext.get(key.toString());
	}
	
	public void setResponse(ContextEnums key, Response value) {
		scenarioContext.put(key.toString(), value);
	}

	public Object getResponse(ContextEnums key) {
		return scenarioContext.get(key.toString());
	}

	public Boolean isContains(ContextEnums key) {
		return scenarioContext.containsKey(key.toString());
	}
	

}
