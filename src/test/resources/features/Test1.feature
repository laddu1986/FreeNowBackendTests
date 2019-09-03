@api
Feature: Searching for user
  I want to use this template for my feature file

  @api1
  Scenario: Searching for a valid user
    Given As a user I want to execute 'USERS_BASE_PATH' endpoint
    When I set query params as
    | username    | Samantha |
    And User submits the 'GET' request
    Then Verify response status code is '200'
    And Verify GET Users schema and fields
    Given As a user I want to execute 'POSTS_BASE_PATH' endpoint
    When I set query params as
      | username    | Samantha |
    And User submits the 'GET' request
    Then Verify response status code is '200'
    And Verify GET Users schema and fields


