package com.freenow.api.stepdefs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.freenow.api.common.context.ContextEnums;
import com.freenow.api.common.context.TestContext;
import com.freenow.api.common.model.user.User;
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
	public void user_wants_to_execute_rules_endpoint(String endpointName) {
		restTemplate.setBaseURI(configReader.getProperty("BASE_URL"));
		LOGGER.info("Setting BASEURL as :" + configReader.getProperty("BASE_URL"));
		// Setup Base Path
		restTemplate.setBasePath(configReader.getProperty(endpointName));
		LOGGER.info("Setting BASEPATH as :" + configReader.getProperty(endpointName));
		// used for ignoring ssl
		restTemplate.relaxedHTTPSValidation();

		// save API_ENDPOINT in scenario context
		testContext.getScenarioContext().setContext(ContextEnums.API_ENDPOINT, configReader.getProperty(endpointName));

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
		LOGGER.info("GETTING DATA.. FROM.." + testContext.getScenarioContext().getContext(ContextEnums.API_ENDPOINT));
		//res = restTemplate.getResponsebyPath(testContext.getScenarioContext().getContext(ContextEnums.API_ENDPOINT).toString());
		res = restTemplate.getResponse();

		LOGGER.info("GET Response " + res.asString());

		// save GET response in scenario context
		testContext.getScenarioContext().setResponse(ContextEnums.RESPONSE, res);

	}

	@When("^User submits the 'POST' request$")
	public void user_submits_the_POST_request() {
		LOGGER.info("POSTING DATA..");
		res = restTemplate
				.getPOSTResponsebyPath(testContext.getScenarioContext().getContext(ContextEnums.API_ENDPOINT).toString());

		LOGGER.info("POST Response " + res.asString());

		// save POST response in scenario context
		testContext.getScenarioContext().setResponse(ContextEnums.RESPONSE, res);

	}

	@Then("^Verify response status code is '([^\"]+)'$")
	public void verify_user_validates_status_code(int statusCode) {
		jp = restTemplate.getJsonPath(res);

		// verify if the HTTP Status received in response was [statusCode]


	}

	@Then("^Verify GET Users schema and fields$")
	public void validate_received_get_user_response() {
		res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
		List<User> user = Arrays.asList(res.as(User[].class));
		LOGGER.info("username is:" + user.get(0).getUsername());
		LOGGER.info("name is:" + user.get(0).getName());

	}


}