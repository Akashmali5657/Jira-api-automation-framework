package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.ConfigReader;
import utils.ExtentRequestAndResponse;

import java.util.Properties;

import static io.restassured.RestAssured.given;

public class CreateUserStoryInBacklog {

    Response response;
    Properties prop = ConfigReader.getProperties();

    String baseUrl = prop.getProperty("baseUrl");
    String apiToken = prop.getProperty("API_Token_base64");
    String projectKey = prop.getProperty("projectKey");
    String issueType = prop.getProperty("issueType");

    String requestBody = "{\n" +
            "  \"fields\": {\n" +
            "    \"project\": { \"key\": \"" + projectKey + "\" },\n" +
            "    \"summary\": \"create API automation for create issue\",\n" +
            "    \"issuetype\": { \"name\": \"Story\" },\n" +
            "    \"priority\": { \"name\": \"High\" }\n" +
            "  }\n" +
            "}";

    @Given("user has create issue payload")
    public void user_has_create_issue_payload() {
        RestAssured.baseURI = baseUrl;
    }

    @When("user calls create issue API with POST method")
    public void user_calls_create_issue_api_with_post_method() {

        response =
                given()
                        .header("Authorization", apiToken)
                        .header("Content-Type", "application/json")
                        .body(requestBody)
                        .when()
                        .post("/rest/api/3/issue");

        ExtentRequestAndResponse.logRequestAndResponse("POST", requestBody,response);

    }

    @Then("response should contain issue key")
    public String response_should_contain_issue_key() {

        String issueKey = response.jsonPath().getString("key");
        Assert.assertNotNull(issueKey);

        System.out.println("Created Issue Key : " + issueKey);
        return issueKey;
    }

    // ---------------- THEN ----------------
    @Then("status code should be as {int}")
    public void status_code_should_be(Integer expectedStatusCode) {
        System.out.println("response.getStatusCode() : "+ response.getStatusCode());
        System.out.println("expectedStatusCode.intValue() : "+ expectedStatusCode);
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode.intValue(), "Matched status code");
    }

    @Given("user has create issue payload with missing summary")
    public void user_has_create_issue_payload_with_missing_summary() {

        RestAssured.baseURI = baseUrl;

        requestBody = "{\n" +
                "  \"fields\": {\n" +
                "    \"project\": { \"key\": \"" + projectKey + "\" },\n" +
                "    \"issuetype\": { \"name\": \"" + issueType + "\" }\n" +
                "  }\n" +
                "}";
    }

    @And("error message must be displayed")
    public void errorMessageMustBeDisplayed() {
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("errors.summary"),"You must specify a summary of the issue.", "message summary :");
    }
}
