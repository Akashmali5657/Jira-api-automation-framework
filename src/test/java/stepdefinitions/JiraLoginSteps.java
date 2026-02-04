package stepdefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.ConfigReader;

import java.util.Properties;

import static io.restassured.RestAssured.given;

public class JiraLoginSteps {

    Response response;
    Properties prop = ConfigReader.getProperties();

    String baseUrl = prop.getProperty("baseUrl");
    String email = prop.getProperty("Email");
    String apiToken = prop.getProperty("API_Token");

    // ---------------- GIVEN ----------------
    @Given("user has Jira login payload")
    public void user_has_jira_login_payload() {
        RestAssured.baseURI = baseUrl;
    }

    // ---------------- WHEN ----------------
    @When("user calls Jira login API with get method")
    public void user_calls_jira_login_api_with_get_method() {

        response =
                given()
                        .auth().preemptive()
                        .basic(email, apiToken)
                        .header("Content-Type", "application/json")
                        .when()
                        .get("/rest/api/3/myself");

        System.out.println(response.asString());
    }

    // ---------------- THEN ----------------
    @Then("status code should be {int}")
    public void status_code_should_be(Integer expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode.intValue());
    }

    // ---------------- AND ----------------
    @Then("response should contain authentication token")
    public void response_should_contain_authentication_token() {

        String accountId = response.jsonPath().getString("accountId");
        Assert.assertNotNull(accountId);
        System.out.println("Login Successful. Account ID: " + accountId);
    }

    // ---------------- INVALID LOGIN ----------------
    @Given("user has Jira login payload with invalid credentials")
    public void user_has_jira_login_payload_with_invalid_credentials() {

        response =
                given()
                        .auth().preemptive()
                        .basic(email, "invalidToken123")
                        .header("Content-Type", "application/json")
                        .when()
                        .get("/rest/api/3/myself");
        System.out.println(response.asString());
    }

    @Then("error message should be displayed")
    public void error_message_should_be_displayed() {
        Assert.assertEquals(response.getStatusCode(), 401);
        System.out.println("Unauthorized access - invalid credentials");
    }

    @When("user calls Jira login API with get method using invalid token")
    public void userCallsJiraLoginAPIWithGetMethodUsingInvalidToken() {
        response =
                given()
                        .auth().preemptive()
                        .basic(email, apiToken + "Invalid-token")
                        .header("Content-Type", "application/json")
                        .when()
                        .get("/rest/api/3/myself");

        System.out.println(response.asString());
    }
}