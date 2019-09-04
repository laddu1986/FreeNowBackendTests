@api @e2e
Feature: E2E - Positive Scenario
  @api1 @positive
  Scenario: Searching a user By 'Username' and validating Email format on all that user post's comments
    Given As a user I want to execute 'USERS_BASE_PATH' endpoint
    When I set query params as
    | username    | Samantha |
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Users returns single user record
    And Verify GET Users schema and fields
    When I store userId for current username
    And User executes 'POSTS_BASE_PATH' endpoint
    And I set 'userId' as 'RETRIEVED_USER_ID' in query params
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Posts schema and fields
    When I store list of available postIds for current username
    And User executes 'COMMENTS_BASE_PATH' endpoint
    When I set list of 'postId' as 'RETRIEVED_POST_IDS_FOR_USER' in query params
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Comment schema
    And Verify email format for each retrieved comment


