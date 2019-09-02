package com.freenow.api.stepdefs;

import java.util.List;
import java.util.Map;

import com.freenow.api.common.context.ContextEnums;
import com.freenow.api.common.context.TestContext;
import com.freenow.api.utils.RestTemplate;
import com.freenow.global.utils.ConfigReader;
import com.freenow.global.utils.LogUtils;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/*
 * This class contains all the Base API Test Steps
 */
public class CommonSteps {

	public Response res = null; // Response
	public JsonPath jp = null; // JsonPath

	public RestTemplate restTemplate;
	public LogUtils LOGGER;
	public ConfigReader configReader;

	TestContext testContext;

	public CommonSteps(TestContext context) {
		testContext = context;
		LOGGER = testContext.getLogUtils();
		restTemplate = testContext.getRestTemplate();
		configReader = testContext.getConfigReader();
	}

	@Given("^As a user I want to execute '([^\"]+)' endpoint$")
	public void user_wants_to_execute_rules_endpoint(String endpoint) {
		restTemplate.setBaseURI(configReader.getProperty("BASE_URL"));
		LOGGER.info("Setting BASEURL as :" + configReader.getProperty("BASE_URL"));
		// Setup Base Path
		restTemplate.setBasePath(configReader.getProperty("BASE_PATH"));
		LOGGER.info("Setting BASEPATH as :" + configReader.getProperty("BASE_PATH"));
		// used for ignoring ssl
		restTemplate.relaxedHTTPSValidation();

		// save API_ENDPOINT in context
		testContext.scenarioContext.setContext(ContextEnums.API_ENDPOINT, endpoint);
		LOGGER.info("Setting API_ENDPOINT as :" + endpoint);

		LOGGER.info("endpoint" + endpoint);

	}

	@When("^I set query params as$")
	public void user_sets_query_params(Map<String, String> queryParams) {
		LOGGER.info("Setting query params DATA as..");
		Map<String, String> newParams = queryParams;
		for (Map.Entry<String, String> entry : newParams.entrySet()) {

			if (entry.getValue().contains(",")) {
				String currentParam = entry.getValue();
				currentParam = currentParam.replace(" ", "");
				String keyValues[] = currentParam.split(",");
				String queryString = "";
				for (int i = 0; i < keyValues.length; i++) {

					if (i == 0) {
						queryString = keyValues[i];
					} else {
						queryString = queryString + "&" + entry.getKey() + "=" + keyValues[i];
					}
				}
				try {
					newParams.put(entry.getKey(), queryString);
				} catch (UnsupportedOperationException e) {
				}

			}
			LOGGER.info(entry.getKey() + ":" + entry.getValue());
		}

		restTemplate.setRequestQueryParams(newParams);

	}


	@When("^I set path params as$")
	public void user_sets_path_params(DataTable pathParams) {
		List<List<String>> pathData = pathParams.raw();

		LOGGER.info("Setting path params DATA.." + pathData.get(0).get(0));
		restTemplate.setRequestPathParams(pathData.get(0).get(0), pathData.get(0).get(1));

	}


	@When("^I set headers as$")
	public void user_sets_headers(Map<String, String> headers) {
		LOGGER.info("Setting headers DATA..");
		restTemplate.setRequestHeader(headers);
		restTemplate.setContentType(ContentType.JSON);
	}


	@When("^User submits the 'GET' request$")
	public void user_submits_the_GET_request() {
		LOGGER.info("GETTING DATA..");
		res = restTemplate.getResponsebyPath(testContext.scenarioContext.getContext(ContextEnums.API_ENDPOINT).toString());

		LOGGER.info("GET Response " + res.asString());

		// save response
		testContext.scenarioContext.setResponse(ContextEnums.RESPONSE, res);

	}

	@When("^User submits the 'POST' request$")
	public void user_submits_the_POST_request() {
		LOGGER.info("POSTING DATA..");
		res = restTemplate
				.getPOSTResponsebyPath(testContext.scenarioContext.getContext(ContextEnums.API_ENDPOINT).toString());

		LOGGER.info("POST Response " + res.asString());

		// save response
		testContext.scenarioContext.setResponse(ContextEnums.RESPONSE, res);

	}



}