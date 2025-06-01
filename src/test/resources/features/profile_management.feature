#Feature: Profile Management
#  As a user of the Profile Service API
#  I want to be able to manage user profiles
#  So that I can maintain up-to-date profile information
#
#  Scenario: Full Profile Lifecycle
#    Given the profile service is running
#    And I have the following skills available in the system:
#      | skillId | name    | category   | proficiencyLevel |
#      | 101     | Gherkin | Testing    | 9                |
#      | 102     | Java    | Language   | 8                |
#      | 103     | Spring  | Framework  | 7                |
#    And I have a new profile DTO with name "Cuke User", bio "BDD Enthusiast", location "Feature City", role "QA" and skill "Gherkin" with ID 101 and proficiency 9
#
#    When I send a POST request to "/api/profiles" with the new profile DTO
#    Then the HTTP response status should be 201
#    And the response should contain the created profile with name "Cuke User"
#    And the created profile should have 1 skill named "Gherkin" with proficiency 9
#    And I store the profileId from the response as "currentProfileId"
#
#    Given I have an updated profile DTO for profileId "currentProfileId" with name "Cuke User Updated", bio "Still a BDD Enthusiast", location "Feature City Updated", role "Senior QA"
#    And the updated DTO includes skill "Gherkin" with ID 101 and proficiency 10
#    And the updated DTO includes skill "Java" with ID 102 and proficiency 8
#    When I send a PUT request to "/api/profiles/{currentProfileId}" with the updated profile DTO
#    Then the HTTP response status should be 200
#    And the response should contain the updated profile with name "Cuke User Updated"
#    And the updated profile should have bio "Still a BDD Enthusiast"
#    And the updated profile should have 2 skills
#
#    When I send a GET request to "/api/profiles/{currentProfileId}"
#    Then the HTTP response status should be 200
#    And the response should contain the profile with name "Cuke User Updated"
#    And the profile should have 2 skills including "Java" with proficiency 8
#
#    When I send a DELETE request to "/api/profiles/{currentProfileId}"
#    Then the HTTP response status should be 204
#
#    When I send a GET request to "/api/profiles/{currentProfileId}"
#    Then the HTTP response status should be 404
