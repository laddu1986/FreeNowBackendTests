package com.freenow.api.utils;

import static com.freenow.api.utils.Assertions.assertEquals;
import static com.freenow.api.utils.Assertions.assertTrue;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import com.freenow.api.common.model.comment.Comment;
import com.freenow.global.utils.FileUtils;
import com.freenow.global.utils.LogUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import io.restassured.response.Response;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * This is class contains all the helper methods required by ApiTestSuite class
 * @author Kushal Bhalaik
 */

public class TestUtils {

	private static final LogUtils LOGGER = LogUtils.getInstance(TestUtils.class);

	//This method verifies the http response status code
	public static void validateHttpStatus(Response res, int statusCode) {

		try {
			assertEquals(	res.getStatusCode(), statusCode, "Response Http Status code is as not as expected");
		} catch (AssertionError e) {

			LOGGER.fail("API Response Http Status expected was [" + res.getStatusCode() + "] and actual is [" + statusCode +"]");
		}
	}

	//This method validates a received response against expected schema
	public static void validateResponseSchema(Response res, String schemaName) {

		try {
			String schema = FileUtils.readFromFile("./src/test/resources/schemas/" + schemaName);
			res.then().assertThat().body(matchesJsonSchema(schema));
			LOGGER.pass("Response Schema Validation is PASS");

		} catch (FileNotFoundException e) {
			LOGGER.fail("Couldn't find [" +  schemaName + "] schema in src/test/resources/schemas folder");

		}catch (AssertionError ex) {
			ex.printStackTrace();
			LOGGER.fail("Response schema validation failed!! for [" + schemaName + "]. " + Joiner.on("\n").join(Iterables.limit(Arrays.asList(ex.getStackTrace()), 10)));  //stores only 10 lines from error stacktrace in log file
		}
	}

	//This method validates email format of all the email attributes in a list of comments
	public static void validateEmailFormat(List<Comment> commentList) {

		for (Comment currentComment : commentList) {
			boolean validEmail = EmailValidator.getInstance().isValid(currentComment.getemail());
			assertTrue(validEmail, "For postId : " + currentComment.getpostId() + " & commentId : " + currentComment.getId() + " email " + currentComment.getemail() + " format is not valid.");
			LOGGER.pass("For postId : " + currentComment.getpostId() + " & commentId " + currentComment.getId() + " email " + currentComment.getemail() + " format is valid.");
		}

	}
}
