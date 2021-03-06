package com.freenow.api.stepdefs;

import com.freenow.api.common.context.ContextEnums;
import com.freenow.api.common.context.TestContext;
import com.freenow.api.common.model.comment.Comment;
import com.freenow.api.common.model.post.Post;
import com.freenow.api.common.model.user.User;
import com.freenow.api.utils.RestTemplate;
import com.freenow.global.utils.LogUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;

import static com.freenow.api.utils.Assertions.*;
import static com.freenow.api.utils.TestUtils.*;

/**
 * This class contains all the Extended Steps used while interacting with /user, /post and /comment endpoints
 */
public class ExtendedTestSteps {

    private Response res = null;        // Response
    private LogUtils LOGGER;
    private TestContext testContext;

    private RestTemplate restTemplate;
    private CommonSteps commonSteps;


    public ExtendedTestSteps(TestContext context) {
        testContext = context;
        LOGGER = testContext.getLogUtils();
        restTemplate = testContext.getRestTemplate();
        commonSteps = new CommonSteps(testContext);
    }

    @Then("^Verify GET Users schema and fields$")
    public void validate_received_get_user_response() {
        //get user response from ScenarioContext and match with USER model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        validateResponseSchema(res, "user.json");

        List<User> listOfUsers = Arrays.asList(res.as(User[].class));
   
        for(User currentUser : listOfUsers){
            assertTrue(currentUser.getId() !=null,"User Id is missing from the /user response");
            assertTrue(currentUser.getUsername()!=null,"Username is missing from the /user response");
            assertTrue(currentUser.getName()!=null,"User's name is missing from the /user response");
            assertTrue(currentUser.getPhone()!=null,"User phone no is missing from the /user response");
            assertTrue(currentUser.getCompany()!=null,"User company is missing from the /user response");
            assertTrue(currentUser.getWebsite()!=null,"User website is missing from the /user response");
            assertTrue(currentUser.getAddress() !=null,"User Address is missing from the /user response");
            assertTrue(currentUser.getEmail() !=null,"User Email is missing from the /user response");
        }

        LOGGER.pass("Successfully verified GET Users schema and fields");
    }

    @Then("^Verify GET Users returns single user record$")
    public void validate_received_get_user_is_a_single_record() {
        //get user response from ScenarioContext and match with USER model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        validateResponseSchema(res, "user.json");

        List<User> listOfUsers = Arrays.asList(res.as(User[].class));
        //verify username searched returns only one record
        assertTrue(listOfUsers.size() == 1, "There are more than one users with same username");
        //verify username searched for is also the one retrieved
        assertEquals(listOfUsers.get(0).getUsername(), (String) testContext.getScenarioContext().getContext(ContextEnums.ACTUAL_USER_NAME), "Retrieved Username doesn't match the actual searched username");

        LOGGER.pass("Validated GET /users?username=somename returns only 1 record");

    }

    @Then("^Verify GET Users returns no user record$")
    public void validate_received_get_user_has_no_record() {
        //get user response from ScenarioContext
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);

        List<User> user = Arrays.asList(res.as(User[].class));
        //verify username searched returns only one record
        assertTrue(user.size() == 0, "User found with Username format 'username'");

    }

    @Then("^Verify GET Post returns no post record$")
    public void validate_received_get_post_has_no_record() {
        //get user response from ScenarioContext
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);

        List<Post> post = Arrays.asList(res.as(Post[].class));
        //verify username searched returns only one record
        assertTrue(post.size() == 0, "Post found with invalid postId ");

    }

    @When("^Store userId for current username$")
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

        for(Post currentPost : listOfPosts){
            assertTrue(currentPost.getId() !=null,"Post Id is missing from the /post response");
            assertTrue(currentPost.getBody() !=null,"Post Body is missing from the /post response");
            assertTrue(currentPost.getTitle() !=null,"Post Title is missing from the /post response");
            assertTrue(currentPost.getUserId() !=null,"Post UserId is missing from the /post response");
        }

        LOGGER.pass("Successfully verified GET Posts schema and fields");

    }

    @When("^Store list of available postIds for current username$")
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

    @Then("^Verify GET Comments schema and fields$")
    public void validate_received_get_comments_response() {
        //get user response from ScenarioContext and match with COMMENT model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        validateResponseSchema(res, "comment.json");

        LOGGER.pass("Validated GET comments response schema");
        List<Comment> listOfComments = Arrays.asList(res.as(Comment[].class));

        for(Comment currentComment : listOfComments){
            assertTrue(currentComment.getId() !=null,"Comment Id is missing from the /comment response");
            assertTrue(currentComment.getemail()!=null,"Comment's associated email is missing from the /comment response");
            assertTrue(currentComment.getpostId()!=null,"Comment's linked PostId is missing from the /comment response");
            assertTrue(currentComment.getname()!=null,"Comment name is missing from the /comment response");
            assertTrue(currentComment.getBody()!=null,"Comment content is missing from the /comment response");

        }

        LOGGER.pass("Successfully verified GET Comments schema and fields");
    }

    @Then("^Verify email format for each retrieved comment$")
    public void validate_email_format_for_each_comment() {
        //get /comments response from ScenarioContext and map it to a List of Comments Object
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        List<Comment> listOfComments = Arrays.asList(res.as(Comment[].class));

        //validate email formats for all comments of all posts of userId retrieved from /comments?postId=i&postId=i+1&..postId=i+n
        validateEmailFormat(listOfComments);
        LOGGER.pass("Validated all posts comments for user");
    }

    @When("^I iterate through individual '([^\"]+)' comments from '([^\"]+)' and validate email format$")
    public void iterate_through_individual_postid_comments_and_validate_email_format(String queryParamKey, String queryParamsValueList) {

        Integer[] listOfPostIds = (Integer[]) testContext.getScenarioContext().getContext(ContextEnums.valueOf(queryParamsValueList));

        //updates different parameter types upon "collision"
        restTemplate.setRequestSpecificationParamConfig();

        for (int i = 0; i < listOfPostIds.length; i++) {

            //set postId = i in queryparams
            LOGGER.info("Setting query params " +  queryParamKey + " as : " + listOfPostIds[i]);
            restTemplate.setRequestQueryParams(queryParamKey, Integer.toString(listOfPostIds[i]));
            //execute GET request on /comments?postId=i
            commonSteps.user_submits_the_GET_request_and_save_response();

            //get /comments?postId=i response from ScenarioContext and map it to a List Object
            res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
            List<Comment> listOfPostComments = Arrays.asList(res.as(Comment[].class));

            //validate email formats for comments retrieved from /comments?postId=i
            validateEmailFormat(listOfPostComments);
            LOGGER.pass("Validated all Post comments..");

        }

    }

}