[![CircleCI](https://circleci.com/gh/far11ven/freenow-backend-tests/tree/develop.svg?style=svg)](https://circleci.com/gh/far11ven/freenow-backend-tests/tree/develop)

# freenow-backend-tests
FREENOW - Backend Test Challenge

# Project Summary

This framework uses Java-Cucumber-TestNG with RestAssured for testing

- For Reporting Extent Reports are used and for Log4J is used for logging 
- ```"com.freenow.api.testrunner"``` contains all test runners for running this project
- ```"com.freenow.api.stepdefs"``` contains all the step definitions
- ```"com.freenow.global.utils"``` contains utility functions used in project
- ```"features/"``` contains all the Test Scenario features written in Gherkin
- ```"output/<env>/"``` contains all the Test Reports related resources
- ```"src/test/resources/runner.config"``` contains all the TestRunner configurations required when running from IDE
- ```"src/test/resources/<env>/env.config"``` contains all the environment specific configuration settings
- ```"src/test/resources/schemas"``` contains all Json Schemas for User, Post and Comment




## How To Run :

 **Method#1** - Running Via IDE
 
 **Pre-requisite:**
 ```runner.config``` file should have following properties:
 ```ENV	= [test]```
 ```MODE = [api]```
 
 - Goto ```src/test/java > com.freenow.api.testrunner```
 - Right click on ```"CucumberTestRunnerReporter.java"``` > ```Run As``` > ```TestNG Test```
 
 
 **Method#2** - Running Via Command Line
 - Open ```"Command Prompt"```
 - Goto ```Project directory```
 - type following command : 
 
 		```> mvn install```
 		```> mvn test -Denv="test" -Dmode="api" -Dcucumber.options="features/*" -Dcucumber.options="--tags @api" ```
 			
 			**-Denv** examples "test" or "dev"
 			**-Dmode** can take value "api"
 			**-Dcucumber.options="./features/**** specifies path to features folder where all .feature files are stored
 			**-Dcucumber.options="--tags @api"** specify "@api" to run all test features or a particular tag like "@e2e" to run single feature
 		
## Run Report: 
Test Reports can be found at path ```\output\<env>\Test_Report_final.html``` (Automatically created after first run) 

![alt text](https://raw.githubusercontent.com/far11ven/freenow-backend-tests/develop/src/test/resources/images/Screenshot_TestReport.png)

## Run Logs: 

Logs are stored under respective date folder under "output/<env>/logs" directory path ```\output\<env>\logs\TestLog_<Timestamp>``` (Automatically created after first run)

### Successful Run Logs: 
![alt text](https://raw.githubusercontent.com/far11ven/freenow-backend-tests/develop/src/test/resources/images/Screenshot_TestLogs_pass.PNG)

### Failure Run Logs: 
![alt text](https://raw.githubusercontent.com/far11ven/freenow-backend-tests/develop/src/test/resources/images/Screenshot_TestLogs_fail.PNG)

**CircleCI Dashboard:**

CircleCI Dashboard Url: https://circleci.com/gh/far11ven/freenow-backend-tests

![alt text](https://raw.githubusercontent.com/far11ven/freenow-backend-tests/develop/src/test/resources/images/Screenshot_CircleCI.png)

