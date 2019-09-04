package com.freenow.api.testrunner;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
		features = "@target/rerun.txt",	 //Cucumber picks the failed scenarios from this file
		glue = { "com.freenow.api.stepdefs" },
		plugin = {"com.cucumber.listener.ExtentCucumberFormatter:", "rerun:target/rerun.txt" }
		)

public class FailedTestsRunner extends AbstractTestNGCucumberTests {

}