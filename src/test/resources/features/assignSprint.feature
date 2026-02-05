Feature: Assign sprint to user story

  Scenario: Assign user story to sprint
    Given user create user story and take issue key
    And user has sprint id
    When user calls assign sprint API
    Then three status code should be 204

  Scenario: update priority and label of user story
    Given user create user story and take issue key
    And user has sprint id
    When user calls assign sprint API
    And update priority and label of user story
