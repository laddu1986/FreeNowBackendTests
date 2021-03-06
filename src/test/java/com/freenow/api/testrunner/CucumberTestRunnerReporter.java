package com.freenow.api.testrunner;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.cucumber.listener.ExtentProperties;
import com.cucumber.listener.Reporter;
import com.freenow.global.utils.ConfigReader;
import com.freenow.global.utils.ExtentHelper;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;


/**
 * This class provides Main TestRunner for all the features
 */
@CucumberOptions(features = {"features"},
        glue = {"com.freenow.api.stepdefs"},
        plugin = {"com.cucumber.listener.ExtentCucumberFormatter:", "rerun:target/rerun.txt"},
        tags = {"@api"},
        monochrome = true
)

public class CucumberTestRunnerReporter extends AbstractTestNGCucumberTests {

    private static ConfigReader configReader = ConfigReader.getInstance();

    /*
     *  This method initializes the Extent Reports
     */
    @BeforeClass
    public static void reporterSetup() {

        String currTestRunTimestamp = Long.toString(new Date().getTime());
        System.setProperty("TEST_RUN_TIMESTAMP", currTestRunTimestamp); // Save current Run Timestamp

        ExtentProperties extentProperties = ExtentProperties.INSTANCE;

        if (null != System.getProperty("env")) {
            extentProperties.setReportPath(
                    "output/" + System.getProperty("env") + "/Test_Report.html");
        } else {
            extentProperties.setReportPath(
                    "output/" + configReader.getRunnerConfigProperty("ENV") + "/Test_Report.html");
        }

    }

    /*
     *  This method finalizes the Extent Reports
     */
    @AfterClass
    public static void reporterTeardown() throws UnknownHostException {
        Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
        Reporter.setSystemInfo("Test User", System.getProperty("user.name"));
        Reporter.setSystemInfo("Host Machine", InetAddress.getLocalHost().getHostName());
        Reporter.setSystemInfo("Operating System Type", System.getProperty("os.name"));
        Reporter.setSystemInfo("Web App Name", "https://jsonplaceholder.typicode.com");

        if (null != System.getProperty("mode")) {
            Reporter.setSystemInfo("Initiator", System.getProperty("mode"));
        } else {
            Reporter.setSystemInfo("Initiator", configReader.getRunnerConfigProperty("MODE"));
        }

        Reporter.setTestRunnerOutput("<pre><h4> Test Run Logs : " + "</h4></pre>");

        try {
            Reporter.setTestRunnerOutput(ExtentHelper.getFinalLogs());

        } catch (IOException e) {

            Reporter.setTestRunnerOutput(
                    "<span style=\"color:red;\"> There was an error attaching logs to this report. Please check following directory for logs : <pre>/output/"
                            + System.getProperty("env") + "/logs/ </pre></span>");
            e.printStackTrace();
        }

        ExtentHelper.createAssetsDirectory();
        Reporter.getExtentReport().flush();
        ExtentHelper.replaceEmbeddedLinks("Runner"); // local resources for circle ci archive

    }

}
