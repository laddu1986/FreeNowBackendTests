# freenow-backend-tests
FREENOW - Backend Test Challenge

# Project Summary

This framework uses Java-Cucumber-TestNG along with RestAssured

- For Reporting Extent Reports are used and for logging Log4J
- ```"runner.config"``` contains all the TestRunner configurations when running from IDE Environment
- ```"<env>/env.config"``` contains all the environment configuration settings
- ```"com.freenow.global.utils"``` contains utility functions used in project
- ```"com.freenow.api.stepdefs"``` contains all the step definitions



## How To Run:

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
 		```> mvn test -Denv="test" -Dcucumber.options="./src/test/resources/features/*" -Dcucumber.options="--tags @web" -Dmode="api" ```
 			
 			**-Denv** examples "test" or "dev"
 			**-Dqweb** can take values "api"
 			**-Dcucumber.options="./src/test/resources/features/*** specifies path to features folder where all .feature files are stored
 			**-Dcucumber.options="--tags @all"** specify "@all" to run all tests or a particular tag like "@m1u1" to run single test
 		
## Logs: 

Logs are stored under respective date folder under "logs" directory path ```\output\<env>\logs\TestLog_<Timestamp>``` (Automatically created after first run)

## Reports: 
Reports can be found at path ```\output\<env>\Test_Report_final.html``` (Automatically created after first run) and screenshots at ```\output\<env>\screenshots\```
