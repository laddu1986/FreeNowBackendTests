@api
Feature: GET /user validations
  @api1 @positive
  Scenario: Searching a user By 'Username' returns single record
    Given As a user I want to execute 'USERS_BASE_PATH' endpoint
    When I set query params as
    | username    | Samantha |
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Users returns single user record
    And Verify GET Users schema and fields

  @api1 @negative
  Scenario: Searching a user By 'username' format returns no results
    Given As a user I want to execute 'USERS_BASE_PATH' endpoint
    When I set query params as
      | username    | samantha |
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Users returns no user record




