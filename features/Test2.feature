@api @user
Feature: /user endpoint validations

  @api2.1 @positive
  Scenario: Searching all users present on /user endpoint and validate response
    Given The user wants to execute 'USERS_BASE_PATH' endpoint
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Users schema and fields

  @api2.2 @positive
  Scenario: Searching a user By 'Username' returns single record  and validate response
    Given The user wants to execute 'USERS_BASE_PATH' endpoint
    When User sets query params as
    | username    | Samantha |
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Users returns single user record
    And Verify GET Users schema and fields

  @api2.3 @negative
  Scenario: Searching a user By 'username' format returns no results when invalid case of username is provided
    Given The user wants to execute 'USERS_BASE_PATH' endpoint
    When User sets query params as
      | username    | samantha |
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Users returns no user record




