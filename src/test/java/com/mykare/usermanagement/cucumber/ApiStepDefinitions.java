package com.mykare.usermanagement.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mykare.usermanagement.dto.UserResponse;
import com.mykare.usermanagement.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;
import com.mykare.usermanagement.model.Role;
import com.mykare.usermanagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiStepDefinitions {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<String, Object> requestBody;
    private WebTestClient.ResponseSpec response;
    private String username;
    private String password;

    @Before
    public void setup() {
        userRepository.deleteAll();
    }

    @Given("I have a new user with name {string} and email {string} and password {string} and gender {string}")
    public void i_have_a_new_user(String name, String email, String password, String gender) {
        requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("gender", gender);
    }

    @Given("I have valid credentials email {string} and password {string}")
    public void i_have_valid_credentials(String email, String password) {
        requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
    }

    @When("I send a POST request to {string}")
    public void i_send_a_post_request(String endpoint) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        response = webTestClient.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(requestBody))
                .exchange();
    }


    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedContent) {
        response.expectBody(String.class).consumeWith(result -> {
            assert result.getResponseBody() != null;
            assert result.getResponseBody().contains(expectedContent);
        });
    }

    private String createBasicAuthHeader(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    @Given("I am authenticated with basic credentials for user {string} and password {string}")
    public void i_am_authenticated_with_basic_credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // THIS IS THE CORRECTED STEP DEFINITION
    @Given("a regular user with email {string} and role {string} exists in the system")
    public void a_regular_user_with_email_and_role_exists_in_the_system(String email, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("defaultPassword"));
        user.setName("Regular User");
        user.setGender("UNKNOWN");
        user.setRole(Role.valueOf(role.toUpperCase()));
        userRepository.save(user);
    }

    @When("I send a GET request to {string} with basic authentication")
    public void i_send_a_get_request_with_basic_authentication(String endpoint) {
        String basicAuthHeader = createBasicAuthHeader(username, password);
        response = webTestClient.get()
                .uri(endpoint)
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                .exchange();
    }

    @When("I send a PUT request to {string} with basic authentication")
    public void i_send_a_put_request_with_basic_authentication(String endpoint) throws JsonProcessingException {
        String basicAuthHeader = createBasicAuthHeader(username, password);
        ObjectMapper objectMapper = new ObjectMapper();
        response = webTestClient.put()
                .uri(endpoint)
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(requestBody))
                .exchange();
    }

    @When("I send a DELETE request to {string} with basic authentication")
    public void i_send_a_delete_request_with_basic_authentication(String endpoint) {
        String basicAuthHeader = createBasicAuthHeader(username, password);
        response = webTestClient.delete()
                .uri(endpoint)
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                .exchange();
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer expectedStatus) {
        response.expectStatus().isEqualTo(expectedStatus);
    }

    @Then("the response body should be a list of users")
    public void the_response_body_should_be_a_list_of_users() {
        response.expectBodyList(UserResponse.class);
    }

    @Then("the list should contain a user with email {string}")
    public void the_list_should_contain_a_user_with_email(String expectedEmail) {
        response.expectBodyList(UserResponse.class)
                .value(users -> {
                    if (users == null || users.isEmpty()) {
                        throw new AssertionError("Response body is null or empty, expected a list of users.");
                    }
                    boolean found = users.stream()
                            .anyMatch(user -> user.getEmail().equals(expectedEmail));
                    if (!found) {
                        throw new AssertionError("Did not find a user with email: " + expectedEmail);
                    }
                });
    }

    @Then("the response should contain {string}: {string}")
    public void the_response_should_contain_key_value(String key, String value) {
        response.expectBody()
                .jsonPath("$." + key)
                .isEqualTo(value);
    }

    @Given("a user with email {string} and password {string} and role {string} exists")
    public void a_user_with_email_and_password_and_role_exists(String email, String password, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.valueOf(role.toUpperCase()));
        userRepository.save(user);
    }
}