Feature: Authentication API

  Scenario: Successful user registration
    Given I have a new user with name "M Nithin" and email "manikkannithin@gmail.com" and password "Password@123" and gender "MALE"
    When I send a POST request to "/auth/register"
    Then the response status should be 200
    And the response should contain "User registered successfully"

  Scenario: Registration fails due to duplicate email
    Given a user with email "manikkannithin@gmail.com" and password "Password@123" and role "USER" exists
    And I have a new user with name "M Nithin" and email "manikkannithin@gmail.com" and password "Password@123" and gender "MALE"
    When I send a POST request to "/auth/register"
    Then the response status should be 400
    And the response should contain "Registration failed: Email already registered"

  Scenario: Successful login
    Given a user with email "manikkannithin@gmail.com" and password "Password@123" and role "USER" exists
    And I have valid credentials email "manikkannithin@gmail.com" and password "Password@123"
    When I send a POST request to "/auth/login"
    Then the response status should be 200
    And the response should contain "Login successful"

  Scenario: Login fails due to wrong password
    Given a user with email "manikkannithin@gmail.com" and password "Password@123" and role "USER" exists
    And I have valid credentials email "manikkannithin@gmail.com" and password "Password123@"
    When I send a POST request to "/auth/login"
    Then the response status should be 401
    And the response should contain "Invalid credentials"

