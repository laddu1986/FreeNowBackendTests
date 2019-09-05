package com.freenow.api.utils;

import com.freenow.global.utils.LogUtils;
import org.testng.Assert;

/**
 * This class provides custom Assertions (A wrapper around TestNG Assertions with Logging on failure)
*/
public class Assertions {
	private static LogUtils LOGGER = LogUtils.getInstance(Assertions.class);
	public static void assertTrue(boolean condition, String message) {

		try {
			Assert.assertTrue(condition, message);

		} catch (AssertionError e) {
			LOGGER.error(message + " : " + e);
			Assert.fail(e.getMessage());

		}

	}

	public static void assertFalse(boolean condition, String message) {

		try {
			Assert.assertFalse(condition, message);

		} catch (AssertionError e) {

			LOGGER.error(message + " : " + e);
			Assert.fail(e.getMessage());

		}

	}

	public static void assertEquals(String actual, String expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (AssertionError e) {

			LOGGER.error(message + " : " + e);
			Assert.fail(e.getMessage());
		}
	}

	public static void assertEquals(int actual, int expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (AssertionError e) {

			LOGGER.error(message + " : " + e);
			Assert.fail(e.getMessage());
		}
	}

}