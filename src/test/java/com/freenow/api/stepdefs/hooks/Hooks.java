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

/**
 * This class provides before & after hooks for cucumber
 */
public class Hooks {

    private static ConfigReader configReader;
    private static RestTemplate restTemplate;
    private static LogUtils LOGGER;

    private TestContext testContext;

    public Hooks(TestContext context) {
        testContext = context;
        configReader = testContext.getConfigReader();
        restTemplate = testContext.getRestTemplate();
        LOGGER = testContext.getLogUtils();

        //Stores Current Run Timestamp
        testContext.scenarioContext.setContext(ContextEnums.TEST_RUN_TIMESTAMP,
                System.getProperty("TEST_RUN_TIMESTAMP"));
    }

    /*
     * This method runs before any steps for a scenario
     *
     */
    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        String scenarioID = RandomStringUtils.random(5, true, true);
        LOGGER.start("Starting Test : " + scenario.getName());

        testContext.scenarioContext.setContext(ContextEnums.CURRENT_SCENARIO_ID, scenarioID);
        testContext.scenarioContext.setContext(ContextEnums.CURRENT_SCENARIO_NAME, scenario.getName()); // save in scenario context

        restTemplate.setBaseURI(configReader.getProperty("BASE_URL"));
        LOGGER.info("Setting BASEURL as :" + configReader.getProperty("BASE_URL"));
    }

    /*
     * This method runs at the last of a test case run and resets RestTemplate instance
     *
     */
    @After(order = 0)
    public void tearDown() {

        // Reset ApiUtils Values
        LOGGER.info("Resetting RestTemplate instance..");
        restTemplate.resetRestAssured();

        LOGGER.info("Test Run Complete \n");
    }

    /*
     * This method checks if a scenario failed at any point
     */
    @After(order = 1)
    public void afterScenario(Scenario scenario) {

        if (scenario.isFailed()) {
            LOGGER.error("Test FAILURE!!");
        }
    }

}
