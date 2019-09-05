@api @user
Feature: /user endpoint validations

  @api2 @positive
  Scenario: Searching all users present on /user endpoint
    Given As a user I want to execute 'USERS_BASE_PATH' endpoint
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Users schema and fields

  @api2 @positive
  Scenario: Searching a user By 'Username' returns single record
    Given As a user I want to execute 'USERS_BASE_PATH' endpoint
    When I set query params as
    | username    | Samantha |
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Users returns single user record
    And Verify GET Users schema and fields

  @api2 @negative
  Scenario: Searching a user By 'username' format returns no results when invalid case of username is provided
    Given As a user I want to execute 'USERS_BASE_PATH' endpoint
    When I set query params as
      | username    | samantha |
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Users returns no user record




