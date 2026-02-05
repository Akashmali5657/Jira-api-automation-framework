Feature: Create user story in Jira

  As a user
  I want to create user story into Jira using API
  and then I want to pull that user story into current sprint

  Scenario: Create a new user story successfully
    Given user has create issue payload
    When user calls create issue API with POST method
    Then status code should be as 201
    And response should contain issue key

  Scenario: Create user story with missing mandatory field
    Given user has create issue payload with missing summary
    When user calls create issue API with POST method
    Then status code should be as 400
    And error message must be displayed
