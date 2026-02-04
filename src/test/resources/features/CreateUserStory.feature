#Feature: Create user story in Jira
#
#  As a user
#  I want to create user story into Jira using API
#  and then I want to pull that user story into current sprint
#
#  Scenario: Create User Story in Backlog
#    Given user prepare payload for create user story
#    When user calls create user story API with post http method
#    Then status code should be 200
#    And response should contain user story ID
#
