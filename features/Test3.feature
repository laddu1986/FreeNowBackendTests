@api @user
Feature: /post endpoint validations

  @api3.1 @positive
  Scenario: Searching all post present on /post endpoint  and validate response
    Given The user wants to execute 'POSTS_BASE_PATH' endpoint
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Posts schema and fields

  @api3.2 @negative
  Scenario: Searching a post By invalid 'postId' format returns no results
    Given The user wants to execute 'POSTS_BASE_PATH' endpoint
    When User sets query params as
      | userId    | 1232 |
    And User submits the 'GET' request and stores response
    Then Verify response status code is '200'
    And Verify GET Post returns no post record





