package com.freenow.api.stepdefs.hooks;

import com.freenow.api.common.context.ContextEnums;
import com.freenow.api.common.context.TestContext;
import com.freenow.api.utils.RestTemplate;
import com.freenow.global.utils.ConfigReader;
import com.freenow.global.utils.LogUtils;
import org.apache.commons.lang3.RandomStringUtils;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {

    private static ConfigReader configReader;
    private static RestTemplate restUtils;
    private static LogUtils LOGGER;

    private TestContext testContext;

    public Hooks(TestContext context) {
        testContext = context;
        configReader = testContext.getConfigReader();
        restUtils = testContext.getRestTemplate();
        LOGGER = testContext.getLogUtils();

        LOGGER.info("Setting BASEURI as :" + configReader.getProperty("BASE_URL"));

        testContext.scenarioContext.setContext(ContextEnums.TEST_RUN_TIMESTAMP,
                System.getProperty("TEST_RUN_TIMESTAMP"));
    }

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        String scenarioID = RandomStringUtils.random(5, true, true);
        LOGGER.info("Starting Test : " + scenario.getName() + " with id : " + scenarioID);

        testContext.scenarioContext.setContext(ContextEnums.CURRENT_SCENARIO_ID, scenarioID);
        testContext.scenarioContext.setContext(ContextEnums.CURRENT_SCENARIO_NAME, scenario.getName()); // save in scenario context
    }

    /*
     * This method runs at the last of a test case run and closes current driver
     * instance
     *
     */
    @After(order = 0)
    public void tearDown() {

        // Reset ApiUtils Values
        LOGGER.info("Resetting  RestUtils instance..");
        restUtils.resetRestAssured();

        LOGGER.info("Test Run Complete \n");
    }

    /*
     * This method attaches failure screenshot (if any) to extent Report
     */
    @After(order = 1)
    public void afterScenario(Scenario scenario) {

        if (scenario.isFailed()) {
            LOGGER.error("Test FAILURE!!");
        }
    }

}
