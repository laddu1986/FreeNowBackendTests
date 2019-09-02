package com.freenow.api.testrunner;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;


/* * This class provides Main TestRunner for all the features */
@CucumberOptions( features = { "src/test/resources/features" },
	glue = { "com.freenow.api.stepdefs" },
	plugin = {"com.cucumber.listener.ExtentCucumberFormatter:", "rerun:target/rerun.txt" }, 
	tags = { "@api" }, 
	dryRun = false,
	monochrome = true 
) 

public class CucumberTestRunnerReporter extends AbstractTestNGCucumberTests {

}
