Feature: Jira Login API

  As a user
  I want to login into Jira using API
  So that I can generate authentication token and access Jira APIs


  Scenario: Successful Jira Login
    Given user has Jira login payload
    When user calls Jira login API with get method
    Then status code should be 200
    And response should contain authentication token

  Scenario: Login with invalid credentials
    Given user has Jira login payload
    When user calls Jira login API with get method using invalid token
    Then status code should be 401
    And error message should be displayed
