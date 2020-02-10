@api @comment
Feature: /comments endpoints validations

  @api4.1 @positive
  Scenario: Searching all comments present on /comment endpoint  and validate response
    Given The user wants to execute 'COMMENTS_BASE_PATH' endpoint
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Comments schema and fields

  @api4.2 @positive
  Scenario: Searching individual posts for 'Username' and validating comment email format
    Given The user wants to execute 'USERS_BASE_PATH' endpoint
    When User sets query params as
      | username    | Samantha |
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Users returns single user record
    And Verify GET Users schema and fields
    When Store userId for current username
    And User executes 'POSTS_BASE_PATH' endpoint
    And User sets 'userId' as 'RETRIEVED_USER_ID' in query params
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Posts schema and fields
    When Store list of available postIds for current username
    And User executes 'COMMENTS_BASE_PATH' endpoint
    Then I iterate through individual 'postId' comments from 'RETRIEVED_POST_IDS_FOR_USER' and validate email format





