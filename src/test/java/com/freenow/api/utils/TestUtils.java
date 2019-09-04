package com.freenow.api.utils;

import static com.freenow.api.utils.Assertions.assertEquals;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import java.io.FileNotFoundException;
import java.util.Arrays;

import com.freenow.global.utils.FileUtils;
import com.freenow.global.utils.LogUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import io.restassured.response.Response;

/*
 * This is class contains all the helper methods required by ApiTestSuite class
 * @author Kushal Bhalaik
 */

public class TestUtils {

	private static final LogUtils LOGGER = LogUtils.getInstance(TestUtils.class);

	//This method verifies the http response status code
	public static void validateHttpStatus(Response res, int statusCode) {

		try {
			assertEquals(	res.getStatusCode(), statusCode, "Response Http Status code is as not as expected");
			LOGGER.info("Http Response Status code is as expected : " + statusCode);
		} catch (AssertionError e) {

			LOGGER.fail("API Response Http Status expected was [" + res.getStatusCode() + "] and actual is [" + statusCode +"]");
		}
	}

	//This method validates a received response against expected schema
	public static void validateResponseSchema(Response res, String schemaName) {

		try {
			String schema = FileUtils.readFromFile("./src/test/resources/schemas/" + schemaName);
			res.then().assertThat().body(matchesJsonSchema(schema));
			LOGGER.info("Response Schema Validation is PASS");

		} catch (FileNotFoundException e) {
			LOGGER.fail("Couldn't find [" +  schemaName + "] schema in src/test/resources/schemas folder");

		}catch (AssertionError ex) {
			ex.printStackTrace();
			LOGGER.fail("Response schema validation failed!! for [" + schemaName + "]. " + Joiner.on("\n").join(Iterables.limit(Arrays.asList(ex.getStackTrace()), 10)));  //stores only 10 lines from error stacktrace in log file
		}
	}

}
