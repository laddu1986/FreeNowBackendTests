package com.freenow.api.stepdefs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freenow.api.common.model.post.Comment;
import com.freenow.api.utils.Assertions;
import org.apache.commons.validator.routines.EmailValidator;
import com.freenow.api.common.context.ContextEnums;
import com.freenow.api.common.context.TestContext;
import com.freenow.api.common.model.post.Post;
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

import static com.freenow.api.utils.Assertions.*;
import static com.freenow.api.utils.TestUtils.validateHttpStatus;
import static com.freenow.api.utils.TestUtils.validateResponseSchema;

/*
 * This class contains all the Base API Test Steps
 */
public class CommonSteps {

    public Response res = null; // Response
    public JsonPath jp = null; // JsonPath

    public RestTemplate restTemplate;
    public LogUtils LOGGER;
    public ConfigReader configReader;
    public Assertions assertions ;

    TestContext testContext;

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
                    e.printStackTrace();
                    LOGGER.fail("Error reading Query Param map");
                }

            }
            LOGGER.info(entry.getKey() + ":" + entry.getValue());
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

    @When("^User submits the 'GET' request and stores response$")
    public void user_submits_the_GET_request_and_save_response() {
        LOGGER.info("Retrieving response from.. : /" + testContext.getScenarioContext().getContext(ContextEnums.API_ENDPOINT));
        //res = restTemplate.getResponsebyPath(testContext.getScenarioContext().getContext(ContextEnums.API_ENDPOINT).toString());
        res = restTemplate.getResponse();

        LOGGER.code("Response retrieved as : " + res.asString());

        // save GET response in scenario context
        testContext.getScenarioContext().setResponse(ContextEnums.RESPONSE, res);

    }

    @Then("^Verify response status code is '([^\"]+)'$")
    public void verify_user_validates_status_code(int statusCode) {
        // verify if the HTTP Status received in response was [statusCode]
        validateHttpStatus(res, statusCode );
    }

    @Then("^Verify GET Users schema and fields$")
    public void validate_received_get_user_response() {
        //get user response from ScenarioContext and match with USER model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        validateResponseSchema(res, "user.json");

        List<User> user = Arrays.asList(res.as(User[].class));
        LOGGER.info("username is:" + user.get(0).getUsername());
        LOGGER.info("name is:" + user.get(0).getName());
    }

    @When("^I store userId for current username$")
    public void store_userId_for_current_user() {
        //get user response from ScenarioContext and match with USER model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        List<User> user = Arrays.asList(res.as(User[].class));

        //save userId for the current user i.e. Samantha
        testContext.getScenarioContext().setContext(ContextEnums.RETRIEVED_USER_ID, user.get(0).getId());

    }

    @Then("^Verify GET Posts schema and fields$")
    public void validate_received_get_posts_response() {
        //get user response from ScenarioContext and match with POST model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        validateResponseSchema(res, "post.json");

        List<Post> listOfPosts = Arrays.asList(res.as(Post[].class));

        LOGGER.info("POST Response " + listOfPosts.size());

    }

    @When("^I store list of available postIds for current username$")
    public void store_list_of_postIds_for_current_username() {
        //get user response from ScenarioContext and match with POST model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        List<Post> listOfPosts = Arrays.asList(res.as(Post[].class));

        Integer[] listOfPostIds = new Integer[listOfPosts.size()];
        int i = 0;
        for (Post singlePost : listOfPosts) {
            listOfPostIds[i] = singlePost.getId();
            i++;
        }

        //save list of postIds for the current user i.e. Samantha
        testContext.getScenarioContext().setContext(ContextEnums.RETRIEVED_POST_IDS_FOR_USER, listOfPostIds);

    }

    @Then("^Verify GET Comment schema and fields$")
    public void validate_received_get_comments_response() {
        //get user response from ScenarioContext and match with POST model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        validateResponseSchema(res, "comment.json");

    }

    @Then("^Verify email format for each retrieved comment$")
    public void validate_email_format_for_each_comment() {
        //get /comments response from ScenarioContext and map it to a List Object
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);

        List<Comment> listOfComments = Arrays.asList(res.as(Comment[].class));
        for (Comment currentComment : listOfComments) {
            LOGGER.info("id is:" + currentComment.getId());
            LOGGER.info("email is:" + currentComment.getemail());
            boolean validEmail = EmailValidator.getInstance().isValid(currentComment.getemail());
            assertTrue(validEmail, "Email field has invalid format : " + currentComment.getemail());
        }
    }

}