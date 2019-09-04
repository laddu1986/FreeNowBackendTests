package com.freenow.api.stepdefs;

import com.freenow.api.common.context.ContextEnums;
import com.freenow.api.common.context.TestContext;
import com.freenow.api.common.model.post.Comment;
import com.freenow.api.common.model.post.Post;
import com.freenow.api.common.model.user.User;
import com.freenow.global.utils.LogUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import io.restassured.response.Response;
import org.apache.commons.validator.routines.EmailValidator;
import java.util.Arrays;
import java.util.List;

import static com.freenow.api.utils.Assertions.*;
import static com.freenow.api.utils.TestUtils.*;

/*
 * This class contains all the Extended Steps used while interacting with /user, /post and /comment endpoints
 */
public class ExtendedTestSteps {

    private Response res = null;        // Response
    private LogUtils LOGGER;
    private TestContext testContext;

    public ExtendedTestSteps(TestContext context) {
        testContext = context;
        LOGGER = testContext.getLogUtils();
    }

    @Then("^Verify GET Users schema and fields$")
    public void validate_received_get_user_response() {
        //get user response from ScenarioContext and match with USER model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        validateResponseSchema(res, "user.json");

        List<User> user = Arrays.asList(res.as(User[].class));
        //verify username searched for is also the one retrieved
        assertEquals(user.get(0).getUsername(), (String) testContext.getScenarioContext().getContext(ContextEnums.ACTUAL_USER_NAME), "Retrieved Username doesn't match the actual searched username");

    }

    @Then("^Verify GET Users returns single user record$")
    public void validate_received_get_user_is_a_single_record() {
        //get user response from ScenarioContext and match with USER model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        validateResponseSchema(res, "user.json");

        List<User> user = Arrays.asList(res.as(User[].class));
        //verify username searched returns only one record
        assertTrue(user.size() == 1, "There are more than one users with same username");

    }

    @Then("^Verify GET Users returns no user record$")
    public void validate_received_get_user_has_no_single_record() {
        //get user response from ScenarioContext and match with USER model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        validateResponseSchema(res, "user.json");

        List<User> user = Arrays.asList(res.as(User[].class));
        //verify username searched returns only one record
        assertTrue(user.size() == 0, "User found with Username format 'username'");

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

    @Then("^Verify GET Comment schema$")
    public void validate_received_get_comments_response() {
        //get user response from ScenarioContext and match with COMMENT model
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);
        validateResponseSchema(res, "comment.json");

    }

    @Then("^Verify email format for each retrieved comment$")
    public void validate_email_format_for_each_comment() {
        //get /comments response from ScenarioContext and map it to a List Object
        res = (Response) testContext.scenarioContext.getResponse(ContextEnums.RESPONSE);

        List<Comment> listOfComments = Arrays.asList(res.as(Comment[].class));
        for (Comment currentComment : listOfComments) {
            boolean validEmail = EmailValidator.getInstance().isValid(currentComment.getemail());
            assertTrue(validEmail, "Email field has invalid format : " + currentComment.getemail());
            LOGGER.info("For postId : " + currentComment.getpostId() + " & commentId " + currentComment.getId() + " email " + currentComment.getemail() + " format is valid.");
        }
    }

}