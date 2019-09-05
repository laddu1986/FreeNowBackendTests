@api
Feature: GET /comments validations
  @api1 @positive
  Scenario: Searching a user By 'Username' returns single record
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
    Then I iterate through individual 'postId' comments from 'RETRIEVED_POST_IDS_FOR_USER' and validate email format





