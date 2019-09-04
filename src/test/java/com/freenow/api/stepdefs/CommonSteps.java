package com.freenow.api.stepdefs;

import java.util.HashMap;
import java.util.Map;

import com.freenow.api.common.context.ContextEnums;
import com.freenow.api.common.context.TestContext;
import com.freenow.api.utils.RestTemplate;
import com.freenow.global.utils.ConfigReader;
import com.freenow.global.utils.LogUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.freenow.api.utils.TestUtils.*;

/*
 * This class contains all the Common API Test Steps
 */
public class CommonSteps {

    private Response res = null;         // Response

    private RestTemplate restTemplate;
    private LogUtils LOGGER;
    private ConfigReader configReader;
    private TestContext testContext;

    public CommonSteps(TestContext context) {
        testContext = context;
        LOGGER = testContext.getLogUtils();
        restTemplate = testContext.getRestTemplate();
        configReader = testContext.getConfigReader();
    }

    @When("^User executes '([^\"]+)' endpoint$")
    @Given("^As a user I want to execute '([^\"]+)' endpoint$")
    public void user_wants_to_execute_an_endpoint(String endpointName) {

        // Setup Base Path
        restTemplate.resetBasePath();
        restTemplate.setBasePath(configReader.getProperty(endpointName));
        LOGGER.info("Setting BASEPATH as :" + configReader.getProperty(endpointName));
        // used for ignoring ssl
        restTemplate.relaxedHTTPSValidation();

        // save API_ENDPOINT in scenario context
        testContext.getScenarioContext().setContext(ContextEnums.API_ENDPOINT, configReader.getProperty(endpointName));

    }

    @When("^I set '([^\"]+)' as '([^\"]+)' in query params$")
    public void i_set_queryparam_as(String queryParamName, String queryParamValue) {
        LOGGER.info("Setting query params " + queryParamName + " as.." + queryParamValue);
        Map<String, String> postsRequestQueryParams = new HashMap<>();
        postsRequestQueryParams.put(queryParamName, String.valueOf(testContext.getScenarioContext().getContext(queryParamValue)));
        restTemplate.setRequestQueryParams(postsRequestQueryParams);

    }

    @When("^I set query params as$")
    public void user_sets_query_params(Map<String, String> queryParams) {
        LOGGER.info("Setting query params for the request..");

        Map<String, String> newParams = queryParams;
        for (Map.Entry<String, String> entry : newParams.entrySet()) {

            if (entry.getKey().equals("username")) {

                // save Actual USERNAME to be serched in scenario context
                testContext.getScenarioContext().setContext(ContextEnums.ACTUAL_USER_NAME, entry.getValue());

            }
            LOGGER.info( entry.getKey() + " as : " + entry.getValue());
        }

        restTemplate.setRequestQueryParams(newParams);

    }

    @When("^I set list of '([^\"]+)' as '([^\"]+)' in query params$")
    public void user_sets_array_of_query_params(String queryParamKey, String queryParamsValueList) {
        LOGGER.info("Setting query params for the request..");
        restTemplate.resetRequestQueryParams();
        Integer[] listOfPostIds = (Integer[]) testContext.getScenarioContext().getContext(ContextEnums.RETRIEVED_POST_IDS_FOR_USER);
        for (int i = 0; i < listOfPostIds.length; i++) {
            restTemplate.setRequestQueryParams(queryParamKey, Integer.toString(listOfPostIds[i]));
        }

    }

    @When("^I set headers as$")
    public void user_sets_headers(Map<String, String> headers) {
        LOGGER.info("Setting headers DATA..");
        restTemplate.setContentType(ContentType.JSON);
        restTemplate.setRequestHeader(headers);
    }

    @When("^User submits the 'GET' request and stores response$")
    public void user_submits_the_GET_request_and_save_response() {
        LOGGER.info("Retrieving response from.. : /" + testContext.getScenarioContext().getContext(ContextEnums.API_ENDPOINT));
        res = restTemplate.getResponse();

        LOGGER.code("Response stored as : " + res.asString());

        // save GET response in scenario context
        testContext.getScenarioContext().setResponse(ContextEnums.RESPONSE, res);

    }

    @Then("^Verify response status code is '([^\"]+)'$")
    public void verify_user_validates_status_code(int statusCode) {
        // verify if the HTTP Status received in response was [statusCode]
        validateHttpStatus(res, statusCode );
    }

}