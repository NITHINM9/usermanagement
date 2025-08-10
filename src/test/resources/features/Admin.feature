Feature: Admin API
  As an admin user, I want to manage users
  So that I can perform administrative tasks like viewing, deleting, and updating user roles.

  Background:
    Given a user with email "admin@example.com" and password "Admin@123" and role "ADMIN" exists
    And a regular user with email "testuser@example.com" and role "USER" exists in the system
    Given I am authenticated with basic credentials for user "admin@example.com" and password "Admin@123"

  Scenario: Admin successfully retrieves all users
    When I send a GET request to "/admin/users" with basic authentication
    Then the response status should be 200
    And the response body should be a list of users

  Scenario: Admin successfully retrieves a user by email
    When I send a GET request to "/admin/users/email/testuser@example.com" with basic authentication
    Then the response status should be 200
    And the response should contain "email": "testuser@example.com"

  Scenario: Unauthorized access to admin endpoint
    Given I am authenticated with basic credentials for user "user@example.com" and password "User@123"
    When I send a GET request to "/admin/users" with basic authentication
    Then the response status should be 401