Feature: Update Jira User Story Status

  Scenario: Move user story to Done
    Given user create user story and it has issue key
    When user fetches transitions for issue
    And user updates issue status to Done
    Then status code should be 204 .
